package com.jaggu.sitams;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;


public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    UserLoginTask sc;
    EditText roll;
    EditText pass;
    ConnectionDetector cd;
    JSONArray arr;
    SharedPreferences s;
    String ro;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(getSupportActionBar()!=null)
            getSupportActionBar().setElevation(0);
        setContentView(R.layout.activity_login);
        s = PreferenceManager.getDefaultSharedPreferences(this);
        //if already login open sudent class
        String sr = s.getString("roll", "");
        if (!sr.equalsIgnoreCase("")) {
            Intent not = new Intent(getApplicationContext(), mainnav.class);
            startActivity(not);
        } else {
            getlog();
        }

    }

    private void getlog() {
        this.roll = (EditText) findViewById(R.id.email);
        this.pass = (EditText) findViewById(R.id.password);
        Button b = (Button) findViewById(R.id.email_sign_in_button);
        Button b1=(Button)findViewById(R.id.guest);
        if (b != null) {
            b.setOnClickListener(this);
            b1.setOnClickListener(this);
        }
    }

    @SuppressLint("LongLogTag")
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.email_sign_in_button:
                String roll1 = roll.getText().toString();
                String pass1 = pass.getText().toString();
                ro = roll1;
                String URL = "http://117.239.51.142:8008/sitamsapp/appview/login.php" + "?roll=" + roll1 + "&pass=" + pass1;
                Log.i(URL, "1");
                this.cd = new ConnectionDetector(getApplicationContext());

                if (!this.cd.isConnectingToInternet()) {
                    showsnackbar("NO INTERNET");
                    // Toast.makeText(getApplicationContext(), "Please check your internet connection", Toast.LENGTH_LONG).show();
                } else {
                    if (roll1.length() > 0 && pass1.length() > 0) {
                        sc = new UserLoginTask(this, getApplicationContext(), roll1, pass1);
                        sc.execute(URL);
                    } else {
                        showsnackbar("Please enter ROll AND PASS");
                    }
                }
                break;

            case R.id.guest:
                Intent i = new Intent(getApplicationContext(), mainnav.class);
                SharedPreferences s = PreferenceManager.getDefaultSharedPreferences(this);
                SharedPreferences.Editor editor = s.edit();
                editor.putString("roll","Guest");
                editor.apply();
                i.putExtra("name", "sitams");
                i.putExtra("email", "no use for now");
                startActivity(i);
                LoginActivity.this.finish();

        }

    }

    void getval(JSONObject json) {
        JSONObject jc;
        this.arr = json.optJSONArray("login");
        try {
            jc = arr.getJSONObject(0);
            Log.i("login", jc.getString("val"));
            String c = "1";
            String c1 = jc.get("val").toString();
            if (c.equalsIgnoreCase(c1)) {
                SharedPreferences s = PreferenceManager.getDefaultSharedPreferences(this);
                SharedPreferences.Editor editor = s.edit();
                editor.putString("roll", ro);
                editor.apply();
                Intent i = new Intent(getApplicationContext(), mainnav.class);
                i.putExtra("name", s.getString("roll", null));
                i.putExtra("email", "no use for now");
                startActivity(i);
                LoginActivity.this.finish();

            } else {

                //Toast.makeText(getApplicationContext(), "user details invalid", Toast.LENGTH_LONG).show();
                showsnackbar("User Details Invalid");
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    void showsnackbar(String message) {
        Snackbar snackbar;
        RelativeLayout rlayout = (RelativeLayout) findViewById(R.id.rlogin);
        snackbar = Snackbar.make(rlayout, message, Snackbar.LENGTH_LONG);
        View SnackbarView = snackbar.getView();
        SnackbarView.setBackgroundColor(Color.parseColor("#FB0404"));
        snackbar.show();
    }



    public class UserLoginTask extends AsyncTask<String, Void, String> {

        Activity a;
        String jsonResult;
        ProgressDialog pd;
        Context c;
        Exception e;
        String roll, pass;

        public UserLoginTask(Activity a, Context context, String roll, String pass) {
            this.a = a;
            this.c = context;
            this.roll = roll;
            this.pass = pass;
        }

        @Override
        protected void onPreExecute() {
            pd = ProgressDialog.show(a, null, "Connecting...", true);
            pd.setCancelable(false);
            pd.show();
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            pd.dismiss();
            if (e != null) {
                Toast.makeText(a, "" + e, Toast.LENGTH_LONG).show();
            } else {
                try {
                    getval(new JSONObject(result));
                } catch (JSONException e1) {
                    e1.printStackTrace();
                }
            }

        }


        @Override
        protected String doInBackground(String... arg0) {


            try {
                HttpParams hp = new BasicHttpParams();
                HttpConnectionParams.setConnectionTimeout(hp, 20 * 1000);
                HttpConnectionParams.setSoTimeout(hp, 30 * 1000);
                HttpClient client = new DefaultHttpClient();
                // Create URL string
                // Create Request to server and get response
                HttpGet httppost = new HttpGet();
                httppost.setURI(new URI(arg0[0]));
                //response = client.execute(httppost);
                /*HttpClient httpclient=new DefaultHttpClient(hp);
                HttpGet httppost=new HttpGet(url1);*/
                HttpResponse response = client.execute(httppost);
                jsonResult = InputStreamToString(response.getEntity().getContent()).toString();
            } catch (ClientProtocolException e) {
                this.e = e;
                Log.i("error", "" + e);
                return null;
            } catch (IOException e) {
                this.e = e;
                Log.i("error", "" + e);
                return null;
            } catch (Exception e) {
                this.e = e;
                Log.i("error", "" + e);
                return null;
            }
            return jsonResult;
        }

        private StringBuilder InputStreamToString(InputStream is) {

            String rLine = "";
            StringBuilder answer = new StringBuilder();
            BufferedReader rd = new BufferedReader(new InputStreamReader(is));
            try {
                while ((rLine = rd.readLine()) != null) {
                    answer.append(rLine);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            return answer;
        }

    }

}
