package com.jaggu.sitams;


import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;


public class Aboutdetail extends AppCompatActivity {
    int field;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //get putextra data here
        Bundle bundle = getIntent().getExtras();
        this.field = bundle.getInt("po");
        //set ui for aboutdetail
        if(getSupportActionBar()!=null)
            getSupportActionBar().setElevation(0);
        setContentView(R.layout.activity_aboutdetail);
        //method used to show details based on putextra data
        go();
    }

    void go() {

        //field variable is intialized with put extra data if it is 1,2,3.... we dynamically change images and texts
        if (field == 1) {

            setdata(R.drawable.chairphoto,R.string.chairman);
        } else if (field == 2) {

            setdata(R.drawable.cp,R.string.chairman1);

        } else if (field == 3) {

            setdata(R.drawable.chairphoto,R.string.trust);
        } else if (field == 4) {

            setdata(R.drawable.govm,R.string.govtext);
        }  else if (field == 5) {

            setdata(R.drawable.princephoto,R.string.princpal);

        } else if (field == 6) {
            setdata(R.drawable.aostaff,R.string.aostafftext);
        }

        this.field = 0;
    }
    void setdata(int image1,int string)
    {
        ImageView imageView = (ImageView) findViewById(R.id.imageView4);
        if (imageView != null) {
            imageView.setImageResource(image1);
        }
        TextView te = (TextView) findViewById(R.id.textView5);
        if (te != null) {
            te.setText(this.getString(string));
        }
    }

}
