package com.jaggu.sitams;


import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import com.davemorrissey.labs.subscaleview.ImageSource;
import com.davemorrissey.labs.subscaleview.SubsamplingScaleImageView;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;


public class holidays extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(getSupportActionBar()!=null)
            getSupportActionBar().setElevation(0);
        setContentView(R.layout.activity_holidays);
       final SubsamplingScaleImageView im=(SubsamplingScaleImageView) findViewById(R.id.holyy);
        //Picasso.with(getApplicationContext()).load("http://117.239.51.142/sitamsapp/images/listholy.jpg").error(R.drawable.ic_broken_image_black_24dp).into(im);

       final Target  target= new Target() {
        @Override
        public void onBitmapLoaded (final Bitmap bitmap,
                                    final Picasso.LoadedFrom loadedFrom) {
            if (im != null) {
                im.setImage(ImageSource.bitmap(bitmap));
                ProgressBar p=(ProgressBar)findViewById(R.id.pb);
                if (p != null) {
                    p.setVisibility(View.GONE);
                }
            }
        }

        @Override
        public void onBitmapFailed (final Drawable drawable) {
            Log.d("", "Failed");
        }

        @Override
        public void onPrepareLoad (final Drawable drawable) {
            ProgressBar p=(ProgressBar)findViewById(R.id.pb);
            if (p != null) {
                p.setVisibility(View.VISIBLE);
            }


        }
    };
    if (im != null) {
        im.setTag(target);
    }
    Picasso.with(getApplicationContext()).load ("http://117.239.51.142:8008/sitamsapp/images/listholy.jpg").error(R.drawable.ic_broken_image_black_24dp).into(target);


}


}
