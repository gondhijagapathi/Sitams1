package com.jaggu.sitams;


import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;


public class About extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //set ui for about class
        if(getSupportActionBar()!=null)
            getSupportActionBar().setElevation(0);
        setContentView(R.layout.about);
    }
}
