package com.jaggu.sitams;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;

import java.net.URI;

/**
 * Created by jaggu on 24-Jun-16.
 *
 */
public class FirebaseInstance extends FirebaseInstanceIdService {

    //used to generate token and send token to sitams server
    @Override
    public void onTokenRefresh() {
        // Get updated InstanceID token. when ever token changed
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        Log.d("token-===", "Refreshed token: " + refreshedToken);

        SharedPreferences s = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        if (s.getString("roll", null) != null) {
            registeronserver(refreshedToken);


            // TODO: Implement this method to send any registration to your app's servers.
        } else {
            //put token to shared if he is not loged in to use later
            SharedPreferences.Editor editor = s.edit();
            editor.putString("token", refreshedToken);
            editor.apply();
        }

    }

    private void registeronserver(String refreshedToken) {
        SharedPreferences s = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        SharedPreferences.Editor editor = s.edit();
        editor.putString("token", refreshedToken);
        editor.apply();
        String URL = "http://117.239.51.142:8008/sitamsapp/appview/register.php?name=" + s.getString("roll", null) + "&email=" + "nouse" + "&regId=" + refreshedToken;

        try {
            //code to send token to sitams server only if he is logged in
            HttpParams hp = new BasicHttpParams();
            HttpConnectionParams.setConnectionTimeout(hp, 20 * 1000);
            HttpConnectionParams.setSoTimeout(hp, 30 * 1000);
            HttpClient client = new DefaultHttpClient();
            HttpGet httppost = new HttpGet();
            httppost.setURI(new URI(URL));
            HttpResponse response = client.execute(httppost);
            if (response.getStatusLine().getStatusCode() == 200) {
                SharedPreferences.Editor editor1 = s.edit();
                editor1.putString("registered", "yes");
                editor1.apply();
                Log.i("registered", "yes");
            }
        } catch (Exception e) {
            Log.i("error at conn", "" + e);
            SharedPreferences.Editor editor2 = s.edit();
            editor2.putString("token", refreshedToken);
            editor2.putString("registered", null);
            editor2.apply();
        }

    }

}
