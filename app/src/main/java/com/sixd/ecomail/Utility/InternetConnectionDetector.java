package com.sixd.ecomail.Utility;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

/**
 * Created by Praveen on 03-Apr-18.
 */

public class InternetConnectionDetector {

    private Context _context;
    public InternetConnectionDetector(Context context) {
        this._context = context;
    }

    public  boolean isConnectingToInternet() {

        try {
            ConnectivityManager cm = (ConnectivityManager) _context.getSystemService(
                    Context.CONNECTIVITY_SERVICE);
            NetworkInfo netInfo = cm.getActiveNetworkInfo();
            if (netInfo != null && netInfo.isConnectedOrConnecting()) {
                Log.d("available", "Network available:true");
                return true;
            } else {
                Log.d("not available", "Network available:false");
                return false;
            }
        } catch (Exception e) {
            return false;
        }
    }
}
