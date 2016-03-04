package com.dev.montherland;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.dev.montherland.util.MCrypt;
import com.dev.montherland.util.PDialog;
import com.dev.montherland.util.StaticVariables;

import java.util.HashMap;
import java.util.Map;


public class Change_Password extends Activity {

    EditText et, et2, et3;
    String password = "", newpassword = "", confirmpassword = "";
    Button b;
    ProgressDialog progress;
    CheckBox ch;
    String user_id = "";
    String user_email = "";
    private String TAG = Change_Password.class.getSimpleName();
    private String tag_string_req = "string_req";
    Activity thisActivity=this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.change_password);

        try {
            if (Build.VERSION.SDK_INT > 19) {
                getActionBar().setDisplayHomeAsUpEnabled(true);
                getActionBar().setHomeAsUpIndicator(R.drawable.pf);
            } else {
                getActionBar().setHomeButtonEnabled(true);
                getActionBar().setIcon(R.drawable.pf);
            }
        }catch (Exception e){
            e.printStackTrace();
        }



        et = (EditText) findViewById(R.id.changepassword_password);
        et2 = (EditText) findViewById(R.id.changepassword_newpassword);
        et3 = (EditText) findViewById(R.id.changepassword_confirmpassword);

        ch = (CheckBox) findViewById(R.id.changepassword_showpassword);
        ch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    et.setTransformationMethod(null);
                    et2.setTransformationMethod(null);
                    et3.setTransformationMethod(null);
                    ch.setText("Hide Password");
                } else {
                    et.setTransformationMethod(new PasswordTransformationMethod());
                    et2.setTransformationMethod(new PasswordTransformationMethod());
                    et3.setTransformationMethod(new PasswordTransformationMethod());
                    ch.setText("Show password");
                }
            }
        });

        b = (Button) findViewById(R.id.changepassword_submit);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                password = et.getText().toString();
                newpassword = et2.getText().toString();
                confirmpassword = et3.getText().toString();
                if (password.equals("") || password.isEmpty() || password.trim().isEmpty()) {
                    et.setError(getResources().getString(R.string.correct_password));
                    Toast.makeText(Change_Password.this, R.string.correct_password, Toast.LENGTH_LONG).show();
                } else if (newpassword.equals("") || newpassword.isEmpty() || newpassword.trim().isEmpty()) {
                    et2.setError(getResources().getString(R.string.password_new));
                    Toast.makeText(Change_Password.this, R.string.password_new, Toast.LENGTH_LONG).show();
                } else if (confirmpassword.equals("") || confirmpassword.isEmpty() || confirmpassword.trim().isEmpty()) {
                    et3.setError(getResources().getString(R.string.correct_confirm_password));
                    Toast.makeText(Change_Password.this, R.string.correct_confirm_password, Toast.LENGTH_LONG).show();
                } else if (password.length() < 6) {
                    et.setError(getResources().getString(R.string.password_limit_correct));
                    Toast.makeText(Change_Password.this, R.string.password_limit_correct, Toast.LENGTH_LONG).show();
                } else if (newpassword.length() < 6) {
                    et2.setError(getResources().getString(R.string.password_new_limit));
                    Toast.makeText(Change_Password.this, R.string.password_new_limit, Toast.LENGTH_LONG).show();
                } else if (confirmpassword.length() < 6) {
                    et3.setError(getResources().getString(R.string.confirm_password_limit_correct));
                    Toast.makeText(Change_Password.this, R.string.confirm_password_limit_correct, Toast.LENGTH_LONG).show();
                } else if (!password.trim().equals(password)) {
                    et.setError(getResources().getString(R.string.password_space));
                    Toast.makeText(Change_Password.this, R.string.password_space, Toast.LENGTH_LONG).show();
                } else if (!newpassword.trim().equals(newpassword)) {
                    et2.setError(getResources().getString(R.string.password_space));
                    Toast.makeText(Change_Password.this, R.string.password_space, Toast.LENGTH_LONG).show();
                } else if (!confirmpassword.trim().equals(confirmpassword)) {
                    et3.setError(getResources().getString(R.string.password_space));
                    Toast.makeText(Change_Password.this, R.string.password_space, Toast.LENGTH_LONG).show();
                } else if (newpassword.equals(confirmpassword)) {
                    if (newpassword.trim().equals(newpassword)) {
                        try {
                            MCrypt mcrypt = new MCrypt();
                            PDialog.show(thisActivity);
                            newpassword = MCrypt.bytesToHex(mcrypt.encrypt(newpassword));
                            password = MCrypt.bytesToHex(mcrypt.encrypt(password));
                            Toast.makeText(Change_Password.this, R.string.password_match, Toast.LENGTH_LONG).show();
                            if (StaticVariables.isNetworkConnected(thisActivity)) {
                                StringRequest request = new StringRequest(Request.Method.POST, getResources().getString(R.string.url_motherland) + "change_password.php",
                                        new Response.Listener<String>() {
                                            @Override
                                            public void onResponse(String s) {
                                                Log.v("response", s);


                                                //Log.d("password response", s);
                                                updatedislay(s);
                                                // PDialog.hide();
                                            }
                                        },

                                        new Response.ErrorListener() {
                                            @Override
                                            public void onErrorResponse(VolleyError volleyError) {
//                                                    VolleyLog.d(TAG, "Error: " + volleyError.getMessage());
                                                PDialog.hide();
                                            }
                                        }) {

                                    @Override
                                    protected Map<String, String> getParams() {
                                        Map<String, String> params = new HashMap<String, String>();
                                        params.put("id", StaticVariables.database.get(0).getId());
                                        params.put("email", StaticVariables.database.get(0).getEmail());
                                        params.put("password", password);
                                        params.put("newpassword", newpassword);
                                        return params;
                                    }

                                };
                                AppController.getInstance().addToRequestQueue(request, tag_string_req);
                            } else {
                                Toast.makeText(Change_Password.this, R.string.password_correct2, Toast.LENGTH_LONG).show();
                            }
                        } catch (Exception e) {
                            Toast.makeText(Change_Password.this, R.string.password_correct2, Toast.LENGTH_LONG).show();
                            //                    Log.d("Exception while password decryption : ",""+e);
                        }

                    } else {
                        Toast.makeText(Change_Password.this, R.string.password_space, Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(Change_Password.this, R.string.password_confirm_password_not_match, Toast.LENGTH_LONG).show();
                }
            }
        });
    }



    public void updatedislay(String res) {
        switch (res) {
            case "password updated":
                dbhelp entry = new dbhelp(Change_Password.this);
                entry.open();
                entry.updatepassword(user_id, newpassword);
                entry.close();
                AlertDialog.Builder builder = new AlertDialog.Builder(Change_Password.this);
                builder.setMessage(getResources().getString(R.string.changepassword_success))
                        .setCancelable(false)
                        .setNeutralButton(getResources().getString(R.string.ok), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                progress.show();
                                Intent intent = new Intent(Change_Password.this, MainActivity.class);
                                intent.putExtra("user_id", user_id);
                                intent.putExtra("email", user_email);
                                Change_Password.this.finish();
                                startActivity(intent);
                                Change_Password.this.overridePendingTransition(R.anim.left_to_right, R.anim.right_to_left);
                            }
                        });
                AlertDialog alert = builder.create();
                alert.show();
                break;
            case "Passwords does not match":
                Toast.makeText(Change_Password.this, R.string.password_online_local_different1, Toast.LENGTH_LONG).show();
                break;
            case "Email not set":
                Toast.makeText(Change_Password.this, R.string.unknownerror, Toast.LENGTH_LONG).show();
                break;
            default:
                android.support.v7.app.AlertDialog.Builder builder1 = new android.support.v7.app.AlertDialog.Builder(new ContextThemeWrapper(thisActivity,R.style.myDialog));
                builder1.setMessage(getResources().getString(R.string.changepassword_success))
                        .setCancelable(false)
                        .setNeutralButton(getResources().getString(R.string.ok), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Intent intent = new Intent(Change_Password.this, NavigataionActivity.class);
                                intent.putExtra("user_id", user_id);
                                intent.putExtra("email", user_email);
                                Change_Password.this.finish();
                                startActivity(intent);
                                Change_Password.this.overridePendingTransition(R.anim.left_to_right, R.anim.right_to_left);
                            }
                        });

                builder1.show();
                break;
        }
    }



    public void onBackPressed() {

        super.onBackPressed();
        finish();
        overridePendingTransition(R.anim.left_right, R.anim.right_left);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                Intent intent = new Intent(Change_Password.this, NavigataionActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.left_right, R.anim.right_left);
                return true;
            default:
                return true;
        }
    }
}

