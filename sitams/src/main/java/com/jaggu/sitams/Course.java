package com.jaggu.sitams;


import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.SoundEffectConstants;
import android.view.View;
import android.widget.Button;


public class Course extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //set  ui for this class
        if(getSupportActionBar()!=null)
            getSupportActionBar().setElevation(0);
        setContentView(R.layout.activity_course);
        but();
    }

    // set onclick listiner for buttons like about class same as course detaail same code used
    void but() {
        Button hs = (Button) findViewById(R.id.HS);
        if (hs != null) {
            hs.setOnClickListener(this);
        }
        Button cse = (Button) findViewById(R.id.CSE);
        if (cse != null) {
            cse.setOnClickListener(this);
        }
        Button ece = (Button) findViewById(R.id.ECE);
        if (ece != null) {
            ece.setOnClickListener(this);
        }
        Button eee = (Button) findViewById(R.id.EEE);
        if (eee != null) {
            eee.setOnClickListener(this);
        }
        Button mech = (Button) findViewById(R.id.MECH);
        if (mech != null) {
            mech.setOnClickListener(this);
        }
        Button civil = (Button) findViewById(R.id.CIVIL);
        if (civil != null) {
            civil.setOnClickListener(this);
        }
        Button mba = (Button) findViewById(R.id.MBA);
        if (mba != null) {
            mba.setOnClickListener(this);
        }
        Button mca = (Button) findViewById(R.id.MCA);
        if (mca != null) {
            mca.setOnClickListener(this);
        }

    }

    @Override
    public void onClick(View v) {
        v.playSoundEffect(SoundEffectConstants.CLICK);
        switch (v.getId()) {

            case R.id.HS:
                Intent noti = new Intent(getApplicationContext(), CourseDetail.class);
                noti.putExtra("po", 1);
                startActivity(noti);
                break;
            case R.id.CSE:
                Intent noti10 = new Intent(getApplicationContext(), CourseDetail.class);
                noti10.putExtra("po", 2);
                startActivity(noti10);
                break;
            case R.id.ECE:
                Intent noti1 = new Intent(getApplicationContext(), CourseDetail.class);
                noti1.putExtra("po", 3);
                startActivity(noti1);
                break;
            case R.id.EEE:
                Intent noti2 = new Intent(getApplicationContext(), CourseDetail.class);
                noti2.putExtra("po", 4);
                startActivity(noti2);
                break;
            case R.id.MECH:
                Intent noti3 = new Intent(getApplicationContext(), CourseDetail.class);
                noti3.putExtra("po", 5);
                startActivity(noti3);
                break;
            case R.id.CIVIL:
                Intent noti4 = new Intent(getApplicationContext(), CourseDetail.class);
                noti4.putExtra("po", 6);
                startActivity(noti4);
                break;
            case R.id.MBA:
                Intent noti5 = new Intent(getApplicationContext(), CourseDetail.class);
                noti5.putExtra("po", 7);
                startActivity(noti5);
                break;
            case R.id.MCA:
                Intent noti6 = new Intent(getApplicationContext(), CourseDetail.class);
                noti6.putExtra("po", 8);
                startActivity(noti6);
                break;
        }


    }


}


