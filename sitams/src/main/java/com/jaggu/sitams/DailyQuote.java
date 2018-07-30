package com.jaggu.sitams;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class DailyQuote extends AppCompatActivity {

    String string,authr;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daily_quote);
        if(getSupportActionBar()!=null){
            getSupportActionBar().hide();
        }
        Bundle b=getIntent().getExtras();
        if(b!=null){
            string=b.getString("string","");
            authr=b.getString("author","");
        }
        ImageView close=(ImageView)findViewById(R.id.close);
        final TextView  text=(TextView)findViewById(R.id.text);
        final TextView  author=(TextView)findViewById(R.id.author);

        text.setText(string);
        author.setText(authr);
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
}
