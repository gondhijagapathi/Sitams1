package com.jaggu.sitams;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;

public class Aboutus2 extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_us);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        if (fab != null) {
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent in=new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel","08572246298",null));
                    startActivity(in);
                }
            });
        }
        but();
    }

    void but() {
        //find button by its id and set on click listiner for all
        Button chair1 = (Button) findViewById(R.id.chair);
        if (chair1 != null) {
            chair1.setOnClickListener(this);
        }
        Button chair2 = (Button) findViewById(R.id.chair2);
        if (chair2 != null) {
            chair2.setOnClickListener(this);
        }
        Button trust = (Button) findViewById(R.id.trust);
        if (trust != null) {
            trust.setOnClickListener(this);
        }
        Button gov = (Button) findViewById(R.id.gov);
        if (gov != null) {
            gov.setOnClickListener(this);
        }
        Button prince = (Button) findViewById(R.id.prince);
        if (prince != null) {
            prince.setOnClickListener(this);
        }
        Button ao = (Button) findViewById(R.id.Ao);
        if (ao != null) {
            ao.setOnClickListener(this);
        }

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.chair:
                //open Aboutdetails and putextra is used to show info based on button
                Intent noti = new Intent(getApplicationContext(), Aboutdetail.class);
                noti.putExtra("po", 1);
                startActivity(noti);
                break;
            case R.id.chair2:
                Intent noti1 = new Intent(getApplicationContext(), Aboutdetail.class);
                noti1.putExtra("po", 2);
                startActivity(noti1);
                break;
            case R.id.trust:
                Intent noti2 = new Intent(getApplicationContext(), Aboutdetail.class);
                noti2.putExtra("po", 3);
                startActivity(noti2);
                break;
            case R.id.gov:
                Intent noti3 = new Intent(getApplicationContext(), Aboutdetail.class);
                noti3.putExtra("po", 4);
                startActivity(noti3);
                break;
            case R.id.prince:
                Intent noti7 = new Intent(getApplicationContext(), Aboutdetail.class);
                noti7.putExtra("po", 5);
                startActivity(noti7);
                break;
            case R.id.Ao:
                Intent noti5 = new Intent(getApplicationContext(), Aboutdetail.class);
                noti5.putExtra("po", 6);
                startActivity(noti5);
                break;
        }
    }
}
