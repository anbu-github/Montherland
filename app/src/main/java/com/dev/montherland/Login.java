package com.dev.montherland;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.dev.montherland.model.ExistingUser_Model;
import com.dev.montherland.parsers.Existing_User_JSONParser;
import com.dev.montherland.util.MCrypt;
import com.dev.montherland.util.PDialog;
import com.dev.montherland.util.Preferences;
import com.dev.montherland.util.StaticVariables;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class Login extends AppCompatActivity {

    String data_receive = "string_req_recieve";
    TextInputLayout inputLayoutName,inputLayoutPassword;
    EditText username,password;
    String str_username,str_password,id;
    Activity thisActivity=this;
    String islogin;
    private final String DEFAULT = "Login";
    List<ExistingUser_Model> feedslist;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        username=(EditText)findViewById(R.id.input_email);
        password=(EditText)findViewById(R.id.input_password);
        inputLayoutName=(TextInputLayout)findViewById(R.id.input_layout_name);
        inputLayoutPassword=(TextInputLayout)findViewById(R.id.input_layout_password);

        getDataList();
        if (!(savedInstanceState==null)) {
            try {
                String i = savedInstanceState.getString("hello");
                Log.v("hello", i);
            }
            catch (Exception e){
                e.printStackTrace();
            }
        }

    }

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
        outState.putString("hello", "hello");
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
      String i= savedInstanceState.getString("hello");
        Toast.makeText(Login.this, i, Toast.LENGTH_SHORT).show();
    }




    public void getDataList() {

        StringRequest request = new StringRequest(Request.Method.POST, getResources().getString(R.string.url_motherland) + "login.php?",
                new Response.Listener<String>()

                {
                    @Override
                    public void onResponse(String response) {

                        Log.v("response", response + "");
                        try {
                            JSONObject js = new JSONObject(response);
                            String id = js.getString("id");
                            String updated_date = js.getString("updated_date");

                            Log.v("id",id+"");
                            Log.v("updated_date",updated_date+"");

                            //Log.v("Respone", response + "");

                        } catch (JSONException e) {
                            //Log.d("response", response);
                            //Log.d("error in json", "l " + e);

                        } catch (Exception e) {
//                            Log.d("json connection", "No internet access" + e);
                        }
                    }
                },

                new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError arg0) {


                    }
                }) {

            @Override
            protected Map<String, String> getParams() {

                Map<String, String> params = new HashMap<>();
                params.put("id", "1");

                //Log.d("params", database.get(0).getId());
                //Log.d("service_id", StaticVariables.service_id);
                return params;
            }
        };
        AppController.getInstance().addToRequestQueue(request, data_receive);
    }

    public void login(View view){

        SharedPreferences sharedPreferences = getSharedPreferences("Login", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor=sharedPreferences.edit();  //to edit the sharedPreference

        //Adding values to sharedPreference
        editor.putString("islogin", "loggedIn");

        //Commiting the changes in the sharedPreference
        editor.commit();
        if (!validateUsername()){
            return;
        }
        else if (!validatePassword()){
            return;
        }
        else {
            str_username=username.getText().toString();
            str_password=password.getText().toString();
            try {
                MCrypt mcrypt = new MCrypt();
                str_password = MCrypt.bytesToHex(mcrypt.encrypt(str_password));
            } catch (Exception e) {
                e.printStackTrace();
            }

            if (StaticVariables.isNetworkConnected(thisActivity)) {
                loginRequest();
            }
            else {
                Toast.makeText(thisActivity, "Please check the network connection", Toast.LENGTH_SHORT).show();
            }
         /*   Intent in=new Intent(Login.this,HomeActivity.class);
            startActivity(in);*/



        }

    }
    private boolean validateUsername() {
        if (username.getText().toString().trim().isEmpty()) {
            inputLayoutName.setError("Enter username");
            requestFocus(username);
            return false;
        } else {
            inputLayoutName.setErrorEnabled(false);
        }
        return true;
    }

    private boolean validatePassword() {
        if (password.getText().toString().trim().isEmpty()) {
            inputLayoutPassword.setError("Enter password");
            requestFocus(password);
            return false;
        } else {

            inputLayoutPassword.setErrorEnabled(false);
        }


        return true;
    }

    private void requestFocus(View view) {
        if (view.requestFocus()) {
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }


    public void loginRequest() {
        PDialog.show(Login.this);

        StringRequest request = new StringRequest(Request.Method.POST, getResources().getString(R.string.url_motherland) + "login.php?",
                new Response.Listener<String>()

                {
                    @Override
                    public void onResponse(String response) {


                        PDialog.hide();
                        Log.v("response", response + "");
                        feedslist= Existing_User_JSONParser.parserFeed(response);

                        if (response.equals("")){

                            AlertDialog.Builder builder = new AlertDialog.Builder(Login.this);
                            builder.setCancelable(false)
                                    .setTitle("Login result")
                                    .setMessage("Invalid username or password")
                                    .setNegativeButton("Ok", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {

                                        }
                                    })
                                    .show();
                        }
                        try {

                            JSONArray ar = new JSONArray(response);


                            for (int i = 0; i < ar.length(); i++) {
                                JSONObject parentObject = ar.getJSONObject(i);

                                  id=parentObject.getString("id");

                            }
                                Intent in=new Intent(Login.this,NavigataionActivity.class);
                                in.putExtra("id",id);
                                in.putExtra("email",str_username);
                                in.putExtra("password",str_password);

                                startActivity(in);
                               overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
                            Preferences.putLogin(thisActivity, "logged in");
                            finish();




                            //Log.v("Respone", response + "");

                        } catch (JSONException e) {
                            //Log.d("response", response);
                            //Log.d("error in json", "l " + e);

                        } catch (Exception e) {
//                            Log.d("json connection", "No internet access" + e);
                        }
                    }
                },

                new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError arg0) {
                        PDialog.hide();
                       // Toast.makeText(Login.this,"Invalid username or password",Toast.LENGTH_SHORT).show();
                        AlertDialog.Builder builder = new AlertDialog.Builder(Login.this);
                        builder.setCancelable(false)
                                .setTitle("Login result")
                                .setMessage("Invalid username or password")
                                .setNegativeButton("Ok", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {


                                    }
                                })
                                .show();


                    }
                }) {

            @Override
            protected Map<String, String> getParams() {

                Map<String, String> params = new HashMap<>();
                params.put("email", str_username);
                params.put("password", str_password);
                Log.v("encript_password",str_password);

                //Log.d("params", database.get(0).getId());
                //Log.d("service_id", StaticVariables.service_id);
                return params;
            }
        };
        AppController.getInstance().addToRequestQueue(request, data_receive);
    }

    public void updatedisplay() {
        PDialog.hide();
        //Log.d("updatedisplay_out", "updatedisplay_out");
        if (feedslist != null) {

            for (final ExistingUser_Model flower : feedslist) {
                String success = flower.getId();
                switch (success) {
                    case "Error": {
                        AlertDialog.Builder builder = new AlertDialog.Builder(thisActivity);
                        builder.setTitle("Error")
                                .setMessage("Unknown Error")
                                .setNegativeButton("Ok", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.cancel();
                                    }
                                });
                        AlertDialog alert = builder.create();
                        alert.show();
                        break;
                    }

                    default:
                        dbhelp entry = new dbhelp(thisActivity);
                        entry.open();
                        entry.createuser(flower.getId(), flower.getName(), flower.getEmail(), flower.getPassword(), flower.getPhone(), flower.getBusiness_id(), flower.getProfile_id(), flower.getNode_id(), flower.getRole_id(), flower.getPermission_id(), flower.getLevel_id());



                }
            }
        } else {


        }
    }


}
