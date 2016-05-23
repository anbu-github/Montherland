package com.dev.montherland.util;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;

import com.dev.montherland.model.Database;

import java.util.ArrayList;
import java.util.List;


public class StaticVariables {

    public static List<Database> database;

    public static String id="";
    public static String email="";
    public static String password="";
    public static Integer cbpos=-1;
    public static Integer count=0;
    public static Integer value=0;
    public static String customerContact;     //create purchase order spinner customerContact
    public static String customerName;     //create purchase order spinner customerContact
    public static String companyName;     //create purchase order spinner customerContact
    public static String customerId;     //id choosen in create order customer spinner
    public static String orderType;     //create purchase order spinner customerContact
    public static String orderTypeId;     //create purchase order spinner customerContact
    public static String deliveryDate;     //create purchase order spinner customerContact
    public static String pickupDate;     //create purchase order spinner customerContact
    public static String prodcutInstr="";     //create purchase order spinner customerContact
    public static String pickedDateTIme="";     //create purchase order spinner customerContact
    public static String deliveryDateTIme="";     //create purchase order spinner customerContact
    public static String deliveryDefultDate="";     //create purchase order spinner customerContact
    public static String pickupDefaultDate="";     //create purchase order spinner customerContact
    public static String address_id="";     //create purchase order spinner customerContact
    public static String addressId="";     //create purchase order spinner customerContact
    public static String status="";     //create purchase order spinner customerContact
    public static String selectAddress="";     //create purchase order spinner customerContact
    public static String delDate="";     //create purchase order spinner customerContact
    public static String deliveryTime="";     //create purchase order spinner customerContact
    public static String pickupTime="";     //create purchase order spinner customerContact
    public static Integer quantityPos=-1;     //create purchase order spinner customerContact
    public static Integer stylePos=-1;     //create purchase order spinner customerContact
    public static Boolean isSelected=false;
    public static Bundle bundle;
    public static String mode="";
    public static String mode1="";
    public static String cus_id="";
    public static String state="";
    public static Boolean isEditable=false;
    public  static   ArrayList<String> addressIdList=new ArrayList<>();


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
    public static ArrayList<String> customerList = new ArrayList<>();

    // Order Details
    public static String order_id = "";

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


    public static void hideKeyboard(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        //Find the currently focused view, so we can grab the correct window token from it.
        View view = activity.getCurrentFocus();
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = new View(activity);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
}
