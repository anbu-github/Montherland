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
    public static Integer count=0;
    public static Integer value=0;
    public static String customerContact;     //create purchase order spinner customerContact
    public static String customerName;     //create purchase order spinner customerContact
    public static String companyName;     //create purchase order spinner customerContact
    public static String orderType;     //create purchase order spinner customerContact
    public static String orderTypeId;     //create purchase order spinner customerContact
    public static String deliveryDate;     //create purchase order spinner customerContact
    public static String pickupDate;     //create purchase order spinner customerContact
    public static String prodcutInstr="";     //create purchase order spinner customerContact
    public static String pickedDateTIme="";     //create purchase order spinner customerContact
    public static String deliveryDateTIme="";     //create purchase order spinner customerContact
    public static String purchaseOrderType="";     //create purchase order spinner customerContact
    //create order adapter
    public static ArrayList<String> editQuantityList = new ArrayList<>();
    public static ArrayList<String> garmentTypeList = new ArrayList<>();
    public static ArrayList<String> garmentIdList = new ArrayList<>();
    public static ArrayList<String> quantityList = new ArrayList<>();
    public static ArrayList<String> garmentListIds = new ArrayList<>();
    public static ArrayList<String> garmentStyle = new ArrayList<>();
    public static ArrayList<String> garmentWashtype = new ArrayList<>();
    public static ArrayList<String> garmentWashTypeId = new ArrayList<>();
    public static ArrayList<String> garmentInstr = new ArrayList<>();


    public static boolean isNetworkConnected(Activity thisActivity) {
        ConnectivityManager cm = (ConnectivityManager) thisActivity.getSystemService(Context.CONNECTIVITY_SERVICE);

        return cm.getActiveNetworkInfo() != null;
    }

    public  static boolean checkIfNumber(String in) {

        try {
            Integer.parseInt(in);

        } catch (NumberFormatException ex) {
            return false;
        }

        return true;
    }
}
