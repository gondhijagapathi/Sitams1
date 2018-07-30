package com.jaggu.sitams;


import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.davemorrissey.labs.subscaleview.ImageSource;
import com.davemorrissey.labs.subscaleview.SubsamplingScaleImageView;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import de.hdodenhof.circleimageview.CircleImageView;


public class CourseDetail extends AppCompatActivity {
    int field;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getIntent().getExtras();
        this.field = bundle.getInt("po");
        if(getSupportActionBar()!=null)
            getSupportActionBar().setElevation(0);
        setContentView(R.layout.activity_course_detail);
        go();
    }

    private void go() {
        if (field == 1) {
            setdata(R.string.hastext,0);
        } else if (field == 2) {
            setdata(R.string.csehodtext,1);
        } else if (field == 3) {

            setdata(R.string.ecehodtext,2);

        } else if (field == 4) {
            setdata(R.string.eeehodtext,3);
        } else if (field == 5) {
            setdata(R.string.mechhodtext,4);

        } else if (field == 6) {

            setdata(R.string.civilhodtext,5);

        } else if (field == 7) {
            setdata(R.string.mbahodtext,6);

        } else if (field == 8) {

            setdata(R.string.mcahodtext,7);

        }

    }
    void setdata(int string,int image2)
    {
        String faculty[]={"hasfaculty.JPG","csefaculty.JPG","ecefaculty.JPG","eeefaculty.JPG","mechfaculty.JPG","civilfaculty.JPG","mbafaculty.JPG","mcafaculty.JPG"};
        String hodpics[]={"hashod.jpg","csehod.jpg","ecehod.jpg","eeehod.jpg","mechhod.jpg","civilhod.jpg","mbahod.jpg","mcahod.jpg"};
        CircleImageView imageView = (CircleImageView) findViewById(R.id.imageView6);
        if (imageView != null) {
            Picasso.with(getApplicationContext()).load("http://117.239.51.142:8008/sitamsapp/images/"+hodpics[image2]).error(R.drawable.ic_broken_image_black_24dp).into(imageView);
            // imageView.setImageResource(image1);
        }
        TextView te = (TextView) findViewById(R.id.textView8);
        if (te != null) {
            te.setText(this.getString(string));
        }
        final SubsamplingScaleImageView im = (SubsamplingScaleImageView) findViewById(R.id.imageView7);
        if (im != null) {
            final ProgressBar p=(ProgressBar)findViewById(R.id.p);
            if (p != null) {
                p.setVisibility(View.VISIBLE);
            }


            Target target= new Target() {
                @Override
                public void onBitmapLoaded (final Bitmap bitmap,
                                            final Picasso.LoadedFrom loadedFrom) {
                    if (im != null) {
                        im.setImage(ImageSource.bitmap(bitmap));
                        ProgressBar p=(ProgressBar)findViewById(R.id.p);
                        if (p != null) {
                            p.setVisibility(View.GONE);
                        }
                    }
                }

                @Override
                public void onBitmapFailed (final Drawable drawable) {
                    p.setVisibility(View.GONE);
                    Log.d("", "Failed");
                }

                @Override
                public void onPrepareLoad (final Drawable drawable) {
                    ProgressBar p=(ProgressBar)findViewById(R.id.p);
                    if (p != null) {
                        p.setVisibility(View.VISIBLE);
                    }


                }
            };

            if (im != null) {
                im.setTag(target);
            }
            Picasso.with(getApplicationContext()).load("http://117.239.51.142:8008/sitamsapp/images/"+faculty[image2]).error(R.drawable.ic_broken_image_black_24dp).into(target);
            //imageView1.setImageResource(image2);
        }
    }
}

