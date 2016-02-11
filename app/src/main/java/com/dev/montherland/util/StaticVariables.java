package com.dev.montherland.util;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;

import java.util.ArrayList;

/**
 * Created by pf-05 on 1/27/2016.
 */
public class StaticVariables {
    public static String id="";
    public static String email="";
    public static String password="";
    public static Integer cbpos=0;
    public static Integer count=1;
    public static String customerContact;     //create purchase order spinner customerContact
    public static String customerName;     //create purchase order spinner customerContact
    //create order adapter
    public static ArrayList<String> editQuantityList = new ArrayList<>();
    public static ArrayList<String> garmentTypeList = new ArrayList<>();
    public static ArrayList<String> garmentIdList = new ArrayList<>();


    public static boolean isNetworkConnected(Activity thisActivity) {
        ConnectivityManager cm = (ConnectivityManager) thisActivity.getSystemService(Context.CONNECTIVITY_SERVICE);

        return cm.getActiveNetworkInfo() != null;
    }

}
