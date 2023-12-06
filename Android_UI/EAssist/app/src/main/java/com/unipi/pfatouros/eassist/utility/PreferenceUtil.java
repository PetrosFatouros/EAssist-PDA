package com.unipi.pfatouros.eassist.utility;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class PreferenceUtil {

    public static boolean saveToken(String token, Context context){

        // Save jwt to shared preferences

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(Constants.TOKEN_KEY, token);
        editor.apply();
        return true;
    }

    public static String getToken(Context context){

        // Get jwt from shared preferences

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        return preferences.getString(Constants.TOKEN_KEY, null);
    }
}
