package com.jaggu.sitams;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
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
import java.util.ArrayList;
import java.util.HashMap;

import me.leolin.shortcutbadger.ShortcutBadger;


public class Notify extends AppCompatActivity implements OnItemClickListener, SwipeRefreshLayout.OnRefreshListener {
    ConnectionDetector cd;
    Sitamsldb controller;
    Serverconnect1 sc;
    int field;
    String url = "http://sitams.url.ph/sitams/default.php";
    SwipeRefreshLayout srl1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getIntent().getExtras();
        field = bundle.getInt("load");
        if(getSupportActionBar()!=null)
        getSupportActionBar().setElevation(0);
        setContentView(R.layout.notify);
        this.srl1 = (SwipeRefreshLayout) findViewById(R.id.srl);
        if (srl1 != null) {
            srl1.setColorSchemeResources(R.color.red, R.color.green, R.color.blue);
            srl1.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                @Override
                public void onRefresh() {
                    Refresh();
                }
            });
        }
        if (field == 1) {
            Log.i("ll", "ll");
            Refresh();
        }
        listnote();
    }


    public void listnote() {
        controller = new Sitamsldb(this);
        ArrayList<HashMap<String, String>> notifyList = controller.gettitles();
        if (notifyList.size() != 0) {
            // Set the User Array list in ListView
            ListAdapter adapter = new SimpleAdapter(this, notifyList, R.layout.adapter, new String[]{
                    "ndate", "ntitle","nbody"}, new int[]{R.id.ndate, R.id.ntitle,R.id.nbody});
            ListView myList = (ListView) findViewById(R.id.notifylist);
            if (myList != null) {
                myList.setAdapter(adapter);
                myList.setOnItemClickListener(this);
            }

        } else {
            Refresh();
        }
    }

    public void onlylist() {
        controller = new Sitamsldb(this);
        ArrayList<HashMap<String, String>> notifyList = controller.gettitles();
        if (notifyList.size() != 0) {
            // Set the User Array list in ListView
            ListAdapter adapter = new SimpleAdapter(this, notifyList, R.layout.adapter, new String[]{
                    "ndate", "ntitle","nbody"}, new int[]{R.id.ndate, R.id.ntitle,R.id.nbody});
            ListView myList = (ListView) findViewById(R.id.notifylist);
            if (myList != null) {
                myList.setAdapter(adapter);
                myList.setOnItemClickListener(this);
            }

        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        menu.getItem(0).setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
        super.onCreateOptionsMenu(menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem paramMenuItem) {
        switch (paramMenuItem.getItemId()) {
            default:
                return super.onOptionsItemSelected(paramMenuItem);
            case R.id.refresh:
                Refresh();
                return true;
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long row) {
        Intent noti = new Intent(getApplicationContext(), NotifyDetail.class);
        noti.putExtra("po", position);
        startActivity(noti);
    }

    private void Refresh() {

        this.cd = new ConnectionDetector(getApplicationContext());
        if (!this.cd.isConnectingToInternet()) {
            Toast.makeText(getApplicationContext(), "Please check your internet connection", Toast.LENGTH_LONG).show();
            srl1.setRefreshing(false);
        } else {
            Log.i("refresh", "its running");
            sc = new Serverconnect1(this, getApplicationContext());
            sc.execute(url);
        }
    }

    @Override
    public void onRefresh() {
        Log.i("ll", "l");
        Refresh();
    }

    public class Serverconnect1 extends AsyncTask<String, Void, String> {
        Activity a;
        String jsonResult;
        ProgressDialog pd;
        HashMap<String, String> queryValues;
        Sitamsldb controller;
        Context c;
        Exception e;

        public Serverconnect1(Activity a, Context context) {
            this.a = a;
            this.c = context;
        }

        @Override
        protected void onPreExecute() {
                /*pd= ProgressDialog.show(a,null,"Loading...",true);
                pd.setCancelable(false);
		    	pd.show();*/
            srl1.setRefreshing(true);
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            //pd.dismiss();
            srl1.setRefreshing(false);
            onlylist();
            ShortcutBadger.applyCount(getApplicationContext(), 0);
            if (e != null) {
                Toast.makeText(a, "" + e, Toast.LENGTH_LONG).show();
            }

        }


        @Override
        protected String doInBackground(String... arg0) {


            try {
                HttpParams hp = new BasicHttpParams();
                HttpConnectionParams.setConnectionTimeout(hp, 20 * 1000);
                HttpConnectionParams.setSoTimeout(hp, 30 * 1000);
                HttpClient httpclient = new DefaultHttpClient(hp);
                HttpGet httppost = new HttpGet("http://117.239.51.142:8008/sitamsapp/appview/show.php");
                HttpResponse response = httpclient.execute(httppost);
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
            onPostExecut();
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

        private void onPostExecut() {
            try {
                controller = new Sitamsldb(a);
                controller.deletetab();
                JSONObject obj = new JSONObject(jsonResult);
                JSONArray arr = obj.optJSONArray("notify");
                for (int i = 0; i < arr.length(); i++) {
                    // Get JSON object
                    JSONObject jc = arr.getJSONObject(i);
                    // DB QueryValues Object to insert into SQLite
                    queryValues = new HashMap<String, String>();
                    // Add userID extracted from Object
                    queryValues.put("nId", jc.get("nId").toString());
                    // Add userName extracted from Object
                    queryValues.put("ntitle", jc.get("ntitle").toString());
                    queryValues.put("nbody", jc.get("nbody").toString());
                    queryValues.put("ndate", jc.get("ndate").toString());
                    queryValues.put("ncol", i + "");
                    // Insert notification into SQLite DB
                    controller.insertnotification(queryValues);
                   // String jaggu = controller.getmaxrow1();
                   // controller.setmax(jaggu);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }


}
		

