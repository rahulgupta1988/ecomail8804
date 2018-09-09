package com.sixd.ecomail.Utility;

import android.app.Application;
import android.util.Log;


import com.sixd.ecomail.R;
import com.sixd.ecomail.model.MyObjectBox;

import io.objectbox.BoxStore;

/**
 * Created by Praveen on 03-Apr-18.
 */

public class MyApplication extends Application {

    private static MyApplication mInstance;
    private BoxStore mBoxStore;

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
       mBoxStore= MyObjectBox.builder().androidContext(MyApplication.this).build();
    }


    public static synchronized MyApplication getInstance() {
        return mInstance;
    }

    public void setConnectivityListener(ConnectivityReceiver.ConnectivityReceiverListener listener) {
        ConnectivityReceiver.connectivityReceiverListener = listener;
    }

    public static void MyLogi(String tag,String message){
        if(true)
            Log.i(tag,message);
    }

    public BoxStore getBoxStore() {
        return mBoxStore;
    }


    public int getThemeID(){
        int currentThemeID=0;
        String themeName = SharedPreferencesManager.getThemeName(getApplicationContext());
        if(themeName!=null){

          /*   if(themeName.equals("Ecomail"))
                 currentThemeID= R.style.AppTheme;

             else if(themeName.equals("Doctors Without Borders"))
                 currentThemeID= R.style.AppTheme;

             else if(themeName.equals("Chase Bank"))
                 currentThemeID= R.style.AppTheme;

             else if(themeName.equals("The Nature Conservancy"))
                 currentThemeID= R.style.AppTheme;

             else if(themeName.equals("Bank Of America"))
                 currentThemeID= R.style.BOATheme;

             else if(themeName.equals("Wells Fargo"))
                 currentThemeID= R.style.AppTheme;

             else if(themeName.equals("Citizens Bank"))
                 currentThemeID= R.style.CITIZENSBANKTheme;

             else
                 currentThemeID= R.style.AppTheme;*/

            currentThemeID= R.style.AppTheme;

        }

        else {
            currentThemeID= R.style.AppTheme;
        }

        return currentThemeID;

    }
}
