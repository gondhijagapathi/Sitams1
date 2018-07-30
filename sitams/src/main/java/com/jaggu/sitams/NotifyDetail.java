package com.jaggu.sitams;


import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;


public class NotifyDetail extends AppCompatActivity {
    int row;
    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getIntent().getExtras();
        this.row = bundle.getInt("po");
        if(getSupportActionBar()!=null)
            getSupportActionBar().setElevation(0);
        setContentView(R.layout.notifydetail);
        settext();
    }

    public void settext() {
        TextView te = (TextView) findViewById(R.id.textview1);
        Sitamsldb controller = new Sitamsldb(this);
        String body = controller.getbody(row);
        if (te != null) {
            te.setText(body);
        }
    }

}
