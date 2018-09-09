package com.sixd.ecomail.Utility;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Created by Praveen on 03-Apr-18.
 */

public class ConnectivityReceiver extends BroadcastReceiver {

    public static ConnectivityReceiverListener connectivityReceiverListener;

    public ConnectivityReceiver() {
        super();
    }

    @Override
    public void onReceive(final Context context, Intent arg1) {


        Thread waitingThred=new Thread(){

            public void run() {

                try {
                    sleep(1000);

                } catch (InterruptedException e) {

                    e.printStackTrace();
                }
                finally
                {
                    ConnectivityManager cm = (ConnectivityManager) context
                            .getSystemService(Context.CONNECTIVITY_SERVICE);
                    NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
                    boolean isConnected = activeNetwork != null
                            && activeNetwork.isConnectedOrConnecting();

                    if (connectivityReceiverListener != null) {
                        connectivityReceiverListener.onNetworkConnectionChanged(isConnected);
                    }
                }
            }

        };

        waitingThred.start();




    }



    public interface ConnectivityReceiverListener {
        void onNetworkConnectionChanged(boolean isConnected);
    }
}
