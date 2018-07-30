package com.jaggu.sitams;


import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //On create is first called when Activity opened
        super.onCreate(savedInstanceState);

        /*code to set no actionbar screen*/
            if (getSupportActionBar() != null) {
                getSupportActionBar().hide();
            }


        //set ui for this class called layout
        setContentView(R.layout.activity_main);

        //set version number to textview dynamically

        TextView t = (TextView) findViewById(R.id.ver);
        if (t != null) {
            t.setText("VERSION " + BuildConfig.VERSION_NAME);
        }
        //open thread to make screen appear for 2 seconds
        new Thread(new Runnable() {
            @Override
            public void run() {

                try {
                    Thread.sleep(2000);
                    SharedPreferences s = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                    if (!s.contains("roll")) {
                        Intent In = new Intent(getApplicationContext(), LoginActivity.class);
                        startActivity(In);

                    } else {
                       /* Intent In = new Intent(context, Student.class);
                        v.getContext().startActivity(In);*/

                        try {
                            Intent i = new Intent(getApplicationContext(), mainnav.class);
                            startActivity(i);
                        } catch (Exception e) {
                            Toast.makeText(getApplicationContext(), "student ==" + e, Toast.LENGTH_LONG).show();
                        }

                    }
                    MainActivity.this.finish();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
}
