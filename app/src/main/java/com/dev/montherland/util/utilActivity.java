package com.dev.montherland.util;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.ParseException;
import android.net.Uri;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DateFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by pf-05 on 3/7/2016.
 */
public class utilActivity extends Activity {

    /************ Method To Print details in Log ************/
    public static void print(String logMsg) {
        Log.i("Shenll Log Info ==> ", logMsg);
    }

    public Activity getActivity(){
        return this;
    }

    public void setStatus(String StatusMessage){
        Toast toast = Toast.makeText(this, StatusMessage, Toast.LENGTH_SHORT);
        toast.show();
    }

    public static ProgressDialog showProgressStatus(Activity sourceActivity, String StatusMessage){
        ProgressDialog progressDialog = ProgressDialog.show(sourceActivity, "", StatusMessage, true);
        return progressDialog;
    }

    public static Date getCalendar(int value){
        Calendar c = Calendar.getInstance();
        c.add(Calendar.DATE, value);
        return c.getTime();

    }

    public static String formattohhmmyyyy(){



        return "";
    }

    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        //int orientation = this.getResources().getConfiguration().orientation;
        //do nothing

    }


    //GET CURRENT WEEK

    public static String[] getCurrentWeek(){

        DateFormat format = new SimpleDateFormat("MM/dd/yyyy");
        Calendar calendar = Calendar.getInstance();
        calendar.setFirstDayOfWeek(Calendar.MONDAY);
        calendar.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);

        String[] days = new String[7];
        for (int i = 0; i < 7; i++)
        {
            days[i] = format.format(calendar.getTime());
            calendar.add(Calendar.DAY_OF_MONTH, 1);
        }

        return days;
    }

    public static String funTimeFormat(String aTime){

        String strTime="";

        String hour = null;
        String minute =  null;
        String second = null;

        try{
            String timearr[] = aTime.split(":");

            hour = timearr[0];
            minute = timearr[1];


            int hours = Integer.parseInt(hour);
            int min  = Integer.parseInt(minute);

            String timeSet = "";
            if (hours > 12) {
                hours -= 12;
                timeSet = "PM";
            } else if (hours == 0) {
                hours += 12;
                timeSet = "AM";
            } else if (hours == 12)
                timeSet = "PM";
            else
                timeSet = "AM";

            // Append in a StringBuilder
            strTime = new StringBuilder().append(pad(hours)).append(':')
                    .append(pad(min)).append(" ").append(timeSet).toString();




        }catch(Exception e){
            e.printStackTrace();
        }

        return strTime;

    }

    public static boolean isNumeric(String str)
    {
        try
        {
            double d = Double.parseDouble(str);
        }
        catch(NumberFormatException nfe)
        {
            return false;
        }
        return true;
    }

    private static String pad(int c) {
        if (c >= 10)
            return String.valueOf(c);
        else
            return "0" + String.valueOf(c);
    }


    public static boolean ismailVaild(String mailaddr) {
        boolean isValid = false;

        //String expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";

        String expression ="^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

        CharSequence inputStr = mailaddr;

        Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(inputStr);
        if (matcher.matches()) {
            isValid = true;
        }
        return isValid;
    }


    public static String hours24to12format(String times){

        String time = null;
        try {

            SimpleDateFormat  inFormat   = new SimpleDateFormat("HH:mm");
            SimpleDateFormat  outFormat  = new SimpleDateFormat("hh:mm aa");
            time = outFormat.format(inFormat.parse(times));

        }catch(Exception e){
            e.printStackTrace();
        }


        return time;

    }

    public static String hours12to24format(String timeampm){

        Date date =null;

    	/* SimpleDateFormat displayFormat = new SimpleDateFormat("HH:mm");
         SimpleDateFormat parseFormat = new SimpleDateFormat("hh:mm aa");
         try {
			date = parseFormat.parse(timeampm);
		} catch (java.text.ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        DateFormat f2 = new SimpleDateFormat("HH:mm");
        String time = f2.format(date); // "23:00"

        //String time = date.toString();*/
        String time = null;
        try {
            SimpleDateFormat inFormat = new SimpleDateFormat("hh:mm aa");
            SimpleDateFormat outFormat = new SimpleDateFormat("HH:mm");
            time = outFormat.format(inFormat.parse(timeampm));

        }catch(Exception e){
            e.printStackTrace();
        }


        return time;
    }


    public static String setTimeFormat(int hours, int mins) {

        String timeSet = "";
        if (hours > 12) {
            hours -= 12;
            timeSet = "PM";
        } else if (hours == 0) {
            hours += 12;
            timeSet = "AM";
        } else if (hours == 12)
            timeSet = "PM";
        else
            timeSet = "AM";


        String minutes = "";
        if (mins < 10)
            minutes = "0" + mins;
        else
            minutes = String.valueOf(mins);

        // Append in a StringBuilder
        String aTime = new StringBuilder().append(hours).append(':')
                .append(minutes).append(" ").append(timeSet).toString();

        return aTime;
    }


    public static  String parseDate(String time,String _inputpattern, String _outputpattern) {
        //time = "2015-02-24";
       /* String inputPattern = "yyyy-MM-dd";
        String outputPattern = "MMM dd, yyyy";*/


        String inputPattern = _inputpattern;
        String outputPattern = _outputpattern;


        SimpleDateFormat inputFormat = new SimpleDateFormat(inputPattern);
        SimpleDateFormat outputFormat = new SimpleDateFormat(outputPattern);

        Date date = null;
        String str = null;

        try {
            try {
                date = inputFormat.parse(time);
                str = outputFormat.format(date);
            } catch (java.text.ParseException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

        } catch (ParseException e) {
            e.printStackTrace();
        }
        return str;
    }


    public static  String parseDateToddMMyyyy(String time) {
        //time = "2015-02-24";
        String inputPattern = "yyyy-MM-dd";
        String outputPattern = "MMM dd, yyyy";
        SimpleDateFormat inputFormat = new SimpleDateFormat(inputPattern);
        SimpleDateFormat outputFormat = new SimpleDateFormat(outputPattern);

        Date date = null;
        String str = null;

        try {
            try {
                date = inputFormat.parse(time);
                str = outputFormat.format(date);
            } catch (java.text.ParseException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

        } catch (ParseException e) {
            e.printStackTrace();
        }
        return str;
    }

    public boolean isTaskTime(String sTartTime){

        boolean iscurrenttasktime = false;

        String nowTime = null;
        Calendar cCalendar = Calendar.getInstance();

        nowTime = new  SimpleDateFormat("HH:mm:ss").format(cCalendar.getTime());

        Log.v("System Time-->", ""+nowTime);

        try {



            String string2 = sTartTime;
            Date beforeDate = null;
            Date inBeforeDate = null;
            try {
                beforeDate = new SimpleDateFormat("HH:mm:ss").parse(string2);
            } catch (java.text.ParseException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            String someRandomTime = nowTime;
            try {
                inBeforeDate = new SimpleDateFormat("HH:mm:ss").parse(someRandomTime);
            } catch (java.text.ParseException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            if (inBeforeDate.before(beforeDate)) {
                //checkes whether the current time is between 14:49:00 and 20:11:13.
                System.out.println("log_tag" + "Time check today True " );

                iscurrenttasktime=true;

            }else{
                System.out.println("log_tag" + "Time check today false " );
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return iscurrenttasktime;

    }


    public static boolean istoday(String strdate){
        boolean check =true;

        Date date = null;

        // 2015-02-26


        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        try
        {
            try {
                date = format.parse(strdate);
            } catch (java.text.ParseException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        catch (ParseException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        Date current = new Date();
        int values = 0;
        if (date != null){

            values  = current.compareTo(date);

        }


        Log.v("Date Value", ""+values);

        if(values<0){
            check=false;
        }



        return check;
    }

    public void hidekeyboard(Activity activity){

        InputMethodManager inputMethodManager = (InputMethodManager)  activity.getSystemService(Activity.INPUT_METHOD_SERVICE);

        inputMethodManager.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), 0);
    }


    public static void showLog(String message) {
        Log.d("Jewel", message);
    }

    // play default notification sound
    public static void playSound(Context context) {
        Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        Ringtone r = RingtoneManager.getRingtone(context, notification);
        r.play();
    }

    // it will help to know phone is connected to internet or not
    // in this case ACCESS_NETWORK_STATE is needed as permission
    public static boolean isInternetOn(Context context) {

        try {
            ConnectivityManager con = (ConnectivityManager) context
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo wifi, mobile;
            wifi = con.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
            mobile = con.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);

            if (wifi.isConnectedOrConnecting() || mobile.isConnectedOrConnecting()) {
                return true;
            }


        } catch (Exception e) {
            // TODO: handle exception
        }
        return false;
    }


    // get app version name
    public static String getAppVersion(Context context) {
        String version = "";
        PackageInfo packageInfo = null;
        try {
            packageInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            version = packageInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return version;
    }

    /**
     * get active gmail account
     * need permission for this
     **/
    public static String getPhoneGmailAcc(Context context) {
        AccountManager accountManager = AccountManager.get(context);
        Account[] accounts = accountManager.getAccountsByType("com.google");
        return accounts.length > 0 ? accounts[0].name.trim().toLowerCase() : "null";

    }

    // save data to sharedPreference
    public static void savePref(Context context, String name, String value) {
        SharedPreferences pref = context.getSharedPreferences("TVGuide", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString(name, value);
        editor.apply();
    }

    // reset data from shared preference
    public static void resetPref(Context context) {
        SharedPreferences pref = context.getSharedPreferences("TVGuide", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.clear();
        editor.apply();
    }

    // get data from shared preference
    public static String getPref(Context context, String name, String defaultValue) {
        SharedPreferences pref = context.getSharedPreferences("TVGuide", Context.MODE_PRIVATE);
        return pref.getString(name, defaultValue);
    }

    // open browser with url
    public static void openBrowser(Context context, String link) {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_VIEW);
        intent.addCategory(Intent.CATEGORY_BROWSABLE);
        intent.setData(Uri.parse(link));
        context.startActivity(intent);
    }

    // get string within limitation
    public static String shortTitle(String title, int limit) {

        String short_title = "";

        if (title != null && title.length() > 0) {
            short_title = title.substring(0, title.length() > limit ? limit + 1 : title.length());
            if (short_title.length() > limit)
                short_title += "...";
        }


        return short_title;
    }

    // show toast
    public static void showMessage(Context context, String message) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }

    // convert time into bangla
    public static String getBDTime(String time) {
        String pre_time = "";
        if (time.length() > 0) {
            String[] spec_time = time.split(":");
            int hour = Integer.valueOf(spec_time[0]);

            if (hour >= 5 && hour < 12) {
                pre_time = "সকাল";
            } else if (hour >= 12 && hour < 15) {
                pre_time = "দুপুর";
            } else if (hour >= 15 && hour < 18) {
                pre_time = "বিকাল";
            } else if (hour >= 18 && hour < 20) {
                pre_time = "সন্ধ্যা";
            } else if ((hour >= 20 && hour <= 24) || (hour >= 0 && hour < 5)) {
                pre_time = "রাত";
            }

           // time = convertTimeAMPM(time);

            time = time.replace("0", "০").replace("1", "১").replace("2", "২")
                    .replace("3", "৩").replace("4", "৪").replace("5", "৫")
                    .replace("6", "৬").replace("7", "৭").replace("8", "৮")
                    .replace("9", "৯")

                    .replace("AM", "").replace("PM", "")
                    .replace("am", "").replace("pm", "")
                    .replace(":", " : ");


        }
        return pre_time + " " + time;
    }

    //convert date into bd
    public static String getBDDate(String date) {

        if (date.length() > 0) {
            date = date.replace("Jan", "জানুয়ারি").replace("Feb", "ফেব্রুয়ারী")
                    .replace("Mar", "মার্চ").replace("Apr", "এপ্রিল")
                    .replace("May", "মে").replace("Jun", "জুন")
                    .replace("Jul", "জুলাই").replace("Aug", "আগস্ট")
                    .replace("Sep", "সেপ্টেমবর").replace("Oct", "অক্টোবর")
                    .replace("Nov", "নভেম্বর").replace("Dec", "ডিসেমবর")

                    .replace("0", "০").replace("1", "১").replace("2", "২")
                    .replace("3", "৩").replace("4", "৪").replace("5", "৫")
                    .replace("6", "৬").replace("7", "৭").replace("8", "৮")
                    .replace("9", "৯");
        }


        return date;
    }


    //convert number into bd
    public static String convertNum(String num) {
        if (num.length() > 0) {
            num = num.replace("0", "০").replace("1", "১").replace("2", "২")
                    .replace("3", "৩").replace("4", "৪").replace("5", "৫")
                    .replace("6", "৬").replace("7", "৭").replace("8", "৮")
                    .replace("9", "৯");
        }


        return num;
    }


    // get android device id
    public static String getDeviceId(Context context) {
        return Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
    }

    /**
     * android device imei ID
     * need permission for this (android.permission.READ_PHONE_STATE)
     */
    public static String getPhoneIMEI(Context context)
    {
        TelephonyManager telephonyMgr = (TelephonyManager)context.getSystemService(Context.TELEPHONY_SERVICE);
        return telephonyMgr.getDeviceId();
    }

    // get date with adding number of day to current
    public static String getDate(int day) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date curentDate = Calendar.getInstance().getTime();
        Calendar c = Calendar.getInstance();
        c.setTime(curentDate);
        c.add(Calendar.DATE, day);
        Date toDay = c.getTime();
        return sdf.format(c.getTime());
    }

    // get day with adding number of day to current
    public static String getDay(int day) {
        SimpleDateFormat sdf = new SimpleDateFormat("EEEE");
        Date curentDate = Calendar.getInstance().getTime();
        Calendar c = Calendar.getInstance();
        c.setTime(curentDate);
        c.add(Calendar.DATE, day);
        Date toDay = c.getTime();
        return sdf.format(c.getTime());
    }


    // get day in bd with adding number of day to current
    public static String getDayBan(int day) {
        String sday = getDay(day);
        if (sday.equalsIgnoreCase("Saturday")) {
            return "শনিবার";
        } else if (sday.equalsIgnoreCase("Sunday")) {
            return "রবিবার";
        } else if (sday.equalsIgnoreCase("Monday")) {
            return "সোমবার";
        } else if (sday.equalsIgnoreCase("Tuesday")) {
            return "মঙ্গলবার";
        } else if (sday.equalsIgnoreCase("Wednesday")) {
            return "বুধবার";
        } else if (sday.equalsIgnoreCase("Thursday")) {
            return "বৃহস্পতিবার";
        } else if (sday.equalsIgnoreCase("Friday")) {
            return "শুক্রবার";
        }

        return "";
    }

    // get current date
    public static String getToday() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String day = sdf.format(new Date());
        return day;
    }

    // get current time
    public static String getTimeNow() {
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
        String time = sdf.format(new Date());
        return time;
    }
/*
    // get time with PM/AM
    public static String convertTimeAMPM(String time) {
        String new_time = "";

        Date date = new Date();
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
            SimpleDateFormat newsdf = new SimpleDateFormat("hh:mm aa");
            date = sdf.parse(time);
            new_time = newsdf.format(date);
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return new_time;
    }*/

    // get device wise pixel
    public static int getPixel(Context context, int pixel) {
        float density = context.getResources().getDisplayMetrics().density;
        int paddingDp = (int) (pixel * density);
        return paddingDp;
    }

    /**
     * getting data as String from resource file
     * file have to be in res/ folder
     */


    /**
     * get string value from json data
     * {"fname":"Abdur","lname":"Rahman"}
     */

    public static String getStringFromJSON(String json,String key){
        try {
            JSONObject jsonObj=new JSONObject(json);
            if(jsonObj.has(key)){
                return jsonObj.getString(key);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return "JSON_PARSE_ERROR";
    }

    // get checksum of a file
    // file have to be in assets folder for this method. Otherwise have to use File instead of String as parameter
    public static String getChecksum(Context context,String fileName){
        MessageDigest md=null;
        InputStream is;
        try {
            md = MessageDigest.getInstance("MD5");
            is = context.getAssets().open(fileName);
            byte[] block = new byte[4096];
            int length;
            while ((length = is.read(block)) > 0) {
                md.update(block, 0, length);
            }
            byte[] byteData=md.digest();

            //convert the byte to hex format
            StringBuffer sb = new StringBuffer();
            for (int i = 0; i < byteData.length; i++) {
                sb.append(Integer.toString((byteData[i] & 0xff) + 0x100, 16).substring(1));
            }
            return sb.toString();

        } catch (NoSuchAlgorithmException | IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }


    /**
     * getting all private fields from a class using JAVA REFRELCTION
     * @param myInstance will be the instance of prefer class
     */


}
