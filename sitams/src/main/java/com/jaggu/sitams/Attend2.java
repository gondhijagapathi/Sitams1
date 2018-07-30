package com.jaggu.sitams;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;


import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.HorizontalBarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.utils.ColorTemplate;

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

public class Attend2 extends AppCompatActivity {

    ConnectionDetector cd;
    Serverconnect2 sc;
    JSONArray arr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //set ui for attend class
        if(getSupportActionBar()!=null)
            getSupportActionBar().setElevation(0);
        setContentView(R.layout.activity_attend2);
        //tablelayout is used to show attendance in form of table
        //this methos check if internet is available if yes gets data from server
        getattendance();

    }

    void getattendance() {
        //connectiondetector class is used to find if internet is working
        this.cd = new ConnectionDetector(getApplicationContext());

        if (!this.cd.isConnectingToInternet()) {
            //shows error message at bottom
            CardView c=(CardView)findViewById(R.id.topcard);
            if (c != null) {
                c.setVisibility(View.VISIBLE);
            }
            TextView error=(TextView)findViewById(R.id.error);
            if (error != null) {
                error.setText("Offline mode (No Graph)");
                error.setVisibility(View.VISIBLE);
            }

            offlinedata();

            //Toast.makeText(getApplicationContext(), "Please check your internet connection", Toast.LENGTH_LONG).show();
        } else {
            //get roll number from shared preferences
            SharedPreferences s = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
            //generate url
            String url = "http://117.239.51.142:8008/sitamsapp/appview/attend.php?roll=" + s.getString("roll", null);
            Log.i("refresh", "its running");
            //start class which runns in background and connects the url
            sc = new Serverconnect2(this, getApplicationContext());
            sc.execute(url);
        }

    }
    void offlinedata()
    {
        SharedPreferences s=PreferenceManager.getDefaultSharedPreferences(this);
        if(s.contains("att")&&s.contains("held")&&s.contains("cum")) {
            TextView te1 = (TextView) findViewById(R.id.catt);
            if (te1 != null) {
                //set name of student using name in attance data
                te1.setText("" +s.getString("att",null));
            }
            TextView te2 = (TextView) findViewById(R.id.cheld);
            if (te2 != null) {
                //set name of student using name in attance data
                te2.setText("" +s.getString("held",null));
            }
            TextView te3 = (TextView) findViewById(R.id.cumdtext);
            if (te3 != null) {
                //set name of student using name in attance data
                te3.setText("" + s.getString("cum",null));
            }
        }
    }

    void getval(JSONObject json) {
        //json values used here
        JSONObject jc;
        ArrayList<String> labels=new ArrayList<>();
        ArrayList<BarEntry> entries =new ArrayList<>();
        ArrayList<BarEntry> comentries =new ArrayList<>();
        ArrayList<BarEntry> comentries2 =new ArrayList<>();
        double attended=0.0;
        double held=0.0;
        String name="NOT SET";
        Log.i("hi", "got here");
        //addhead is used add heading of table
        //parse attend data to get more view open attendance link
        this.arr = json.optJSONArray("attend");
        try {
            for (int i = 0; i < arr.length(); i++) {



                jc = arr.getJSONObject(i);
                TextView te = (TextView) findViewById(R.id.attendtext);
                if (te != null) {
                    //set name of student using name in attance data
                    te.setText("Name:- " + jc.get("name").toString());
                    name= jc.getString("name");
                }
                //set data for particular fiedl

                labels.add(jc.get("mon").toString());
                Log.i("book", jc.get("mon").toString());

                held=held+ Integer.parseInt(jc.get("cheld").toString());
                Log.i("book", jc.get("cheld").toString());
                comentries.add(new BarEntry((float) held,i));

                attended=attended+Integer.parseInt(jc.get("catt").toString());
                Log.i("book", jc.get("catt").toString());
                comentries2.add(new BarEntry((float) attended,i));

                Log.i("book", jc.get("per").toString());


                Log.i("book", jc.get("cum").toString());

                Float x=Float.parseFloat(jc.get("per").toString());
                entries.add(new BarEntry(x, i));
            }
            TextView te1 = (TextView) findViewById(R.id.catt);
            Double d=(attended/held);
            d=d*100;
            SharedPreferences s = PreferenceManager.getDefaultSharedPreferences(this);
            SharedPreferences.Editor editor = s.edit();
            editor.putString("att",""+(int)attended);
            editor.putString("held",""+(int)held);
            editor.putString("cum",""+d);
            editor.putString("name",name);
            editor.apply();
            if (te1 != null) {
                //set name of student using name in attance data
                te1.setText("" + (int) attended);
            }
            TextView te2 = (TextView) findViewById(R.id.cheld);
            if (te2 != null) {
                //set name of student using name in attance data
                te2.setText(""+(int)held);
            }
            TextView te3 = (TextView) findViewById(R.id.cumdtext);
            if (te3 != null) {
                //set name of student using name in attance data
                te3.setText(""+d);
            }

            adddata(labels,entries,comentries,comentries2);
        } catch (JSONException e) {
            e.printStackTrace();
            Log.i("hello", "error");
        } catch (Exception e1) {
            e1.printStackTrace();
        }
    }



    void adddata(ArrayList<String> labels,ArrayList<BarEntry> entries,ArrayList<BarEntry> comentries,ArrayList<BarEntry> comentries2)
    {
        BarChart barchart = (BarChart) findViewById(R.id.barchart);
        HorizontalBarChart comchart=(HorizontalBarChart) findViewById(R.id.combarchart);
        BarDataSet dataset=new BarDataSet(entries,"Attendance");
        dataset.setColors(ColorTemplate.PASTEL_COLORS);
        BarData data=new BarData(labels,dataset);
        if (barchart != null) {
            barchart.setData(data);
        }
        if (barchart != null) {
            barchart.animateXY(1000,5000);
        }


        BarDataSet data1=new BarDataSet(comentries,"Held");
        data1.setColors(ColorTemplate.JOYFUL_COLORS);
        BarDataSet data2=new BarDataSet(comentries2,"Attended");
        data2.setColors(ColorTemplate.COLORFUL_COLORS);
        ArrayList<BarDataSet> datahori=new ArrayList<>();
        datahori.add(data1);
        datahori.add(data2);
        BarData total=new BarData(labels,datahori);
        if (comchart != null) {
            comchart.setData(total);
            comchart.animateXY(1000,5000);
        }


    }


    public class Serverconnect2 extends AsyncTask<String, Void, String> {
        Activity a;
        String jsonResult;
        ProgressDialog pd;
        Context c;
        Exception e;

        //parameter contructor
        public Serverconnect2(Activity a, Context context) {
            this.a = a;
            this.c = context;
        }

        @Override
        protected void onPreExecute() {
            //show loading dialog this method starts before url is loaded
            pd = ProgressDialog.show(a, null, "Loading Data..", true);
            pd.setCancelable(false);
            pd.show();
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            //this method executes after url loading finished
            //finish loading dialog
            pd.dismiss();
            //show if its eroor loading eroor
            if (e != null) {
                Toast.makeText(a, "" + e, Toast.LENGTH_LONG).show();
            } else {
                try {
                    //if ok send send result to getval fun
                    getval(new JSONObject(result));
                } catch (JSONException e1) {
                    e1.printStackTrace();
                    //Toast.makeText(a, "some Error or Attendance for ur roll number not uploaded", Toast.LENGTH_LONG).show();
                    CardView c=(CardView)findViewById(R.id.topcard);
                    if (c != null) {
                        c.setVisibility(View.VISIBLE);
                    }
                    TextView error=(TextView)findViewById(R.id.error);
                    if (error != null) {
                        error.setText("Attendance Not Uploaded");
                        error.setVisibility(View.VISIBLE);
                        SharedPreferences s=PreferenceManager.getDefaultSharedPreferences(a);
                        SharedPreferences.Editor editor = s.edit();
                        editor.remove("att");
                        editor.remove("cum");
                        editor.remove("held");
                        editor.apply();
                    }

                }
            }

        }


        @Override
        protected String doInBackground(String... arg0) {

            //main methos that gets data from server
            try {
                HttpParams hp = new BasicHttpParams();
                HttpConnectionParams.setConnectionTimeout(hp, 20 * 1000);
                HttpConnectionParams.setSoTimeout(hp, 30 * 1000);
                HttpClient httpclient = new DefaultHttpClient(hp);
                HttpGet httppost = new HttpGet(arg0[0]);
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
