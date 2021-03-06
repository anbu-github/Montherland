package com.dev.montherland;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
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
import com.dev.montherland.util.StaticVariables;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class Login extends Activity {

    String data_receive = "string_req_recieve";

    EditText username, password;
    String str_username, str_password, id;
    Activity thisActivity = this;
    List<ExistingUser_Model> feedslist;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        username = (EditText) findViewById(R.id.input_email);
        password = (EditText) findViewById(R.id.input_password);

    }

    public void login(View view) {
        SharedPreferences sharedPreferences = getSharedPreferences("Login", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();  //to edit the sharedPreference

        //Adding values to sharedPreference
        editor.putString("islogin", "loggedIn");

        //Commiting the changes in the sharedPreference
        editor.apply();
        if (!validateUsername()) {
            Toast.makeText(thisActivity,getResources().getString(R.string.enter_email),Toast.LENGTH_LONG).show();
        } else if (!validatePassword()) {
            Toast.makeText(thisActivity,getResources().getString(R.string.enter_password),Toast.LENGTH_LONG).show();
        } else {
            str_username = username.getText().toString();
            str_password = password.getText().toString();
            try {
                MCrypt mcrypt = new MCrypt();
                str_password = MCrypt.bytesToHex(mcrypt.encrypt(str_password));
            } catch (Exception e) {
                e.printStackTrace();
            }

            if (StaticVariables.isNetworkConnected(thisActivity)) {
                loginRequest();
            } else {
                Toast.makeText(thisActivity, getResources().getString(R.string.no_internet_connection), Toast.LENGTH_SHORT).show();
            }

        }

    }

    private boolean validateUsername() {
        if (username.getText().toString().trim().isEmpty()) {
            username.setError(getResources().getString(R.string.enter_email));
            requestFocus(username);
            return false;
        }
        return true;
    }

    private boolean validatePassword() {
        if (password.getText().toString().trim().isEmpty()) {
            password.setError(getResources().getString(R.string.enter_password));
            requestFocus(password);
            return false;
        }
        return true;
    }

    private void requestFocus(View view) {
        if (view.requestFocus()) {
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }


    public void loginRequest() {
        StaticVariables.hideKeyboard(thisActivity);
        PDialog.show(Login.this);

        StringRequest request = new StringRequest(Request.Method.POST, getResources().getString(R.string.url_motherland) + "login.php",
                new Response.Listener<String>()

                {
                    @Override
                    public void onResponse(String response) {

                        PDialog.hide();
                        Log.v("response", response + "");
                        feedslist = Existing_User_JSONParser.parserFeed(response);
                        updatedisplay();
                        if (response.equals("")) {
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
                    }
                },

                new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError arg0) {
                        PDialog.hide();
                        Toast.makeText(Login.this, getResources().getString(R.string.no_internet_access), Toast.LENGTH_LONG).show();
                    }
                }) {

            @Override
            protected Map<String, String> getParams() {

                Map<String, String> params = new HashMap<>();
                params.put("email", str_username);
                params.put("password", str_password);
                Log.v("encript_password", str_password);
                return params;
            }
        };
        AppController.getInstance().addToRequestQueue(request, data_receive);
    }

    public void updatedisplay() {
        PDialog.hide();
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

                    case "Error Password":{
                        AlertDialog.Builder builder = new AlertDialog.Builder(thisActivity);
                        builder.setTitle("Error")
                                .setMessage("Invalid login credentials")
                                .setNegativeButton("Ok", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        password.setText("");
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
                        entry.createuser(flower.getId(), flower.getName(), flower.getEmail(), flower.getPassword(), flower.getPhone(), flower.getCustomer_id(), flower.getProfile_id(), flower.getRole_id());

                        try {
                            dbhelp.DatabaseHelper2 dbhelp = new dbhelp.DatabaseHelper2(thisActivity);
                            dbhelp.getReadableDatabase();
                            StaticVariables.database = dbhelp.getdatabase();
                            dbhelp.close();
                            Log.d("id database", StaticVariables.database.get(0).getId());
                            Log.d("email database", StaticVariables.database.get(0).getEmail());

                            if (flower.getRole_id().equals("3")){
                                Intent in = new Intent(thisActivity, com.dev.montherland.customers.NavigataionActivity.class);
                                startActivity(in);
                                finish();
                            }else {
                                Intent in = new Intent(thisActivity, NavigataionActivity.class);
                                startActivity(in);
                                finish();
                            }
                        } catch (Exception e) {
                            Log.d("error in database",e+"");
                            e.printStackTrace();
                        }

                }
            }
        } else {
            Toast.makeText(Login.this, getResources().getString(R.string.error_occurred1), Toast.LENGTH_LONG).show();
        }
    }
}
