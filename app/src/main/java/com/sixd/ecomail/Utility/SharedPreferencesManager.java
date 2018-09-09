package com.sixd.ecomail.Utility;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Praveen on 27-Mar-18.
 */

public class SharedPreferencesManager {
    private static final String SECURITY_SP = "6d-ecomail-app-sp";

    public static void putThemeName(Context context, String themeName) {
        SharedPreferences sp = context.getSharedPreferences(SECURITY_SP,
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(Constant.Key.THEME_NAME, themeName);
        editor.commit();
    }


    public static String getThemeName(Context context) {
        SharedPreferences sp = context.getSharedPreferences(SECURITY_SP, Context.MODE_PRIVATE);
        String sessiontoken=sp.getString(Constant.Key.THEME_NAME, null);
        return sessiontoken;
    }


    public static void putConsumerID(Context context, String consumerId) {
        SharedPreferences sp = context.getSharedPreferences(SECURITY_SP,
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(Constant.Key.CONSUMER_ID, consumerId);
        editor.commit();
    }

    public static String getConsumerID(Context context) {
        SharedPreferences sp = context.getSharedPreferences(SECURITY_SP, Context.MODE_PRIVATE);
        String sessiontoken=sp.getString(Constant.Key.CONSUMER_ID, null);
        return sessiontoken;
    }

    public static void deleteConsumerID(Context context) {
        SharedPreferences sp = context.getSharedPreferences(SECURITY_SP,
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(Constant.Key.CONSUMER_ID, "");
        editor.commit();
    }


}
