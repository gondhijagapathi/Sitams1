package com.jaggu.sitams;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class ConnectionDetector {
    private Context _context;

    //param constructor
    public ConnectionDetector(Context paramContext) {
        this._context = paramContext;
    }

    //fun to find internet connetcion
    public boolean isConnectingToInternet() {
        try {
            ConnectivityManager connectivity = (ConnectivityManager) _context.getSystemService(Context.CONNECTIVITY_SERVICE);
            if (connectivity != null) {
                NetworkInfo[] info = connectivity.getAllNetworkInfo();
                if (info != null)
                    for (NetworkInfo anInfo : info)
                        if (anInfo.getState() == NetworkInfo.State.CONNECTED) {
                            return true;
                        }

            }
            return false;
        } catch (Exception localException) {
            localException.printStackTrace();
        }
        return false;
    }
}
