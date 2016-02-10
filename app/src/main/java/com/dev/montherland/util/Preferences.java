package com.dev.montherland.util;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by pf-05 on 2/9/2016.
 */
public class Preferences {

    public static final String ISLOGIN = "userlogin";

    public static void putLogin(final Context context, String value) {
        Preferences.putPref(context, ISLOGIN, value);
    }


    public static String getLogin(final Context context) {
        return Preferences.getPref(context, ISLOGIN,null);
    }
    public static String getPref(final Context context, String pref,
                                 String def) {
        SharedPreferences prefs = Preferences.get(context);
        String val = prefs.getString(pref, def);

        if (val == null || val.equals("") || val.equals("null"))
            return def;
        else
            return val;
    }

    public static SharedPreferences get(final Context context) {
        return context.getSharedPreferences("com.mycampus",Context.MODE_PRIVATE);
    }
    public static void putPref(final Context context, String pref,
                               String val) {
        SharedPreferences prefs = Preferences.get(context);
        SharedPreferences.Editor editor = prefs.edit();

        editor.putString(pref, val);
        editor.commit();
    }
}
