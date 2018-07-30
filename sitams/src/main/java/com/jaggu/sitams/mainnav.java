package com.jaggu.sitams;


import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.Html;
import android.util.Log;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Cache;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.firebase.messaging.FirebaseMessaging;
import com.jaggu.sitams.adapter.FeedListAdapter;
import com.jaggu.sitams.app.AppController;
import com.jaggu.sitams.data.FeedItem;

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
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;


public class mainnav extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,SwipeRefreshLayout.OnRefreshListener  {
    private static final String TAG = mainnav.class.getSimpleName();
    private ListView listView;
    private FeedListAdapter listAdapter;
    private List<FeedItem> feedItems;
    private String URL_FEED = "http://117.239.51.142:8008/sitamsapp/newsfeed/show.php";
    SwipeRefreshLayout srl1;

    Context context;
    SharedPreferences s;
    JSONArray arr;
    UserLoginTask sc;
    NavigationView nv;
//check it
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mainnav);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        listView = (ListView) findViewById(R.id.list);

        feedItems = new ArrayList<FeedItem>();

        listAdapter = new FeedListAdapter(this, feedItems);
        listView.setAdapter(listAdapter);

        context=getApplicationContext();
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        nv=navigationView;
        navigationView.setNavigationItemSelectedListener(this);
        View h=navigationView.getHeaderView(0);
        s = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        set(navigationView);

        if(new ConnectionDetector(this).isConnectingToInternet()){
            feedItems.clear();
            getonlinefeed();
        }else {
            getnewsfeed();
        }

        this.srl1 = (SwipeRefreshLayout) findViewById(R.id.content_mainnav);
        if (srl1 != null) {
            srl1.setColorSchemeResources(R.color.red, R.color.green, R.color.blue);
            srl1.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                @Override
                public void onRefresh() {
                    if(!feedItems.isEmpty())
                    feedItems.clear();
                    getonlinefeed();
                }
            });
        }


        //subscibe to fcm topic used to send group of devices single message
        FirebaseMessaging.getInstance().subscribeToTopic("noti");
        if (s.getString("token", null) != null && s.getString("registered", null) == null) {
            String URL = "http://117.239.51.142:8008/sitamsapp/appview/register.php?name=" + s.getString("roll", null) + "&email=" + "nouse" + "&regId=" + s.getString("token", null);
            Log.i("url", URL);
            sc = new UserLoginTask(this, getApplicationContext(), "", "");
            sc.execute(URL);

        }

        try{
            getquote();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void getquote() throws Exception {
        // Instantiate the RequestQueue.
        final SharedPreferences.Editor sss=getSharedPreferences("quote",0).edit();
        final SharedPreferences ss=getSharedPreferences("quote",0);
        RequestQueue queue = Volley.newRequestQueue(this);
        String url ="http://sitams.16mb.com/sitamsapp/newsfeed/show.php";

// Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject j=new JSONObject(response);
                            JSONArray  array=j.getJSONArray("quote");
                            JSONObject data=array.getJSONObject(0);
                            if(!ss.getString("time","").equalsIgnoreCase(data.optString("time"))) {
                                Intent i = new Intent(mainnav.this, DailyQuote.class);
                                i.putExtra("string", Html.fromHtml(data.optString("string", "")).toString());
                                i.putExtra("author", data.optString("author", ""));
                                startActivity(i);
                                sss.putString("time", data.optString("time", ""));
                                sss.apply();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
// Add the request to the RequestQueue.
        queue.add(stringRequest);
    }

    private void getnewsfeed() {
        Cache cache = AppController.getInstance().getRequestQueue().getCache();
        Cache.Entry entry = cache.get(URL_FEED);
        if (entry != null) {
            // fetch the data from cache
            try {
                String data = new String(entry.data, "UTF-8");
                try {
                    parseJsonFeed(new JSONObject(data));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }

        } else {
          getonlinefeed();
        }
        getonlinefeed();

    }


    void getonlinefeed()
    {

        // making fresh volley request and getting json
        JsonObjectRequest jsonReq = new JsonObjectRequest(Request.Method.GET,
                URL_FEED, null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                VolleyLog.d(TAG, "Response: " + response.toString());
                if (response != null) {
                    if(srl1!=null){
                    srl1.setRefreshing(false);
                    }
                    parseJsonFeed(response);
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i("error","error");
                srl1.setRefreshing(false);
                VolleyLog.d(TAG, "Error: " + error.getMessage());
            }
        });

        // Adding request to volley request queue
        AppController.getInstance().addToRequestQueue(jsonReq);
    }



    private void parseJsonFeed(JSONObject response) {
        try {
            JSONArray feedArray = response.getJSONArray("feed");

            for (int i = 0; i < feedArray.length(); i++) {
                JSONObject feedObj = (JSONObject) feedArray.get(i);
                FeedItem item = new FeedItem();
                item.setId(feedObj.getInt("id"));
                item.setName(feedObj.getString("name"));

                // Image might be null sometimes
                String image = feedObj.isNull("image") ? null : feedObj
                        .getString("image");
                item.setImge(image);
                item.setStatus(feedObj.getString("status"));
                Log.i("",feedObj.getString("status"));
                item.setProfilePic(feedObj.getString("profilePic"));
                item.setTimeStamp(feedObj.getString("timeStamp"));

                // url might be null sometimes
                String feedUrl = feedObj.isNull("url") ? null : feedObj
                        .getString("url");
                item.setUrl(feedUrl);

                feedItems.add(item);
            }

            // notify data changes to list adapater
            listAdapter.notifyDataSetChanged();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


   /* void setproimage(NavigationView navigationView)
    {
        if(s.contains("image"))
        {
            Bitmap bitmap = null;
            Bitmap bitmap1 = null;
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), Uri.parse(s.getString("image",null)));
                ImageView imageView = (ImageView) findViewById(R.id.imageViewpro);
                imageView.setImageBitmap(getCircleBitmap(bitmap));
                bitmap1 = MediaStore.Images.Media.getBitmap(getContentResolver(), Uri.parse(s.getString("image",null)));
                View hview=navigationView.getHeaderView(0);
                ImageView img=(ImageView) hview.findViewById(R.id.imageView);
                img.setImageBitmap(getCircleBitmap(bitmap1));


            } catch (IOException e) {
                e.printStackTrace();
            }
            // Log.d(TAG, String.valueOf(bitmap));

        }

    }*/

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_student, menu);
        menu.getItem(0).setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            new AlertDialog.Builder(this).setTitle("Are You Sure?").setMessage("Logout..!!").setPositiveButton("YES", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    SharedPreferences s = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                    SharedPreferences.Editor editor = s.edit();
                    editor.remove("roll");
                    editor.remove("registered");
                    editor.remove("att");
                    editor.remove("cum");
                    editor.remove("held");
                    editor.remove("name");
                    editor.apply();
                    Intent In = new Intent(context, LoginActivity.class);
                    startActivity(In);
                    mainnav.this.finish();
                }
            }).setNegativeButton("NO", null).show();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


     void set(NavigationView navigationView) {
        View hview=navigationView.getHeaderView(0);
        TextView te = (TextView) hview.findViewById(R.id.rollid);
        TextView te1 = (TextView) hview.findViewById(R.id.namee);
        if (te != null) {
            te.setText(s.getString("roll", null));
           te1.setText(s.getString("name", null));
        }
    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.notification) {
            Intent In = new Intent(context, Notify.class);
            In.putExtra("load", 0);
            startActivity(In);
            // Handle the camera action
        } else if (id == R.id.attendance) {
            Intent In = new Intent(context, Attend2.class);
            startActivity(In);

        } else if (id == R.id.result) {
            Intent In = new Intent(context, Results.class);
            startActivity(In);

        } else if (id == R.id.holidays) {
            Intent In = new Intent(context, holidays.class);
            startActivity(In);

        }



        if (id == R.id.nav_aboutus) {
            Intent In = new Intent(context, Aboutus2.class);
            startActivity(In);
            // Handle the camera action
        } else if (id == R.id.nav_courses) {
            Intent In = new Intent(context, Course.class);
            startActivity(In);

        } else if (id == R.id.nav_placements) {
            Intent In = new Intent(context, Placements.class);
            startActivity(In);

        } else if (id == R.id.nav_bus) {
            Intent In = new Intent(context, bus.class);
            startActivity(In);

        } else if (id == R.id.nav_hostel) {
            Intent In = new Intent(context, Hostel.class);
            startActivity(In);

        } else if (id == R.id.nav_gallery) {
            Intent In = new Intent(context, Gallery.class);
            startActivity(In);

        }else if (id == R.id.nav_web) {
            String url = "http://www.sitams.org";
            Intent i = new Intent(Intent.ACTION_VIEW);
            i.setData(Uri.parse(url));
            startActivity(i);

        } else if (id == R.id.nav_send) {
            Intent In = new Intent(Intent.ACTION_SENDTO);
            In.setData(Uri.parse("mailto:sitamsapp@sitams.org"));
            startActivity(In);

        } else if (id == R.id.nav_dev) {
            Intent In = new Intent(context, About.class);
            startActivity(In);

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private Bitmap getCircleBitmap(Bitmap bitmap) {
        final Bitmap output = Bitmap.createBitmap(bitmap.getWidth(),
                bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        final Canvas canvas = new Canvas(output);

        final int color = Color.RED;
        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
        final RectF rectF = new RectF(rect);

        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        canvas.drawOval(rectF, paint);

        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);

        bitmap.recycle();

        return output;
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
                editor.putString("registered", "yes");
                editor.apply();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onRefresh() {

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
            pd = ProgressDialog.show(a, null, "Loading...", true);
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
