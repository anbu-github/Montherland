package com.dev.montherland.customers;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.Spanned;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.dev.montherland.*;
import com.dev.montherland.model.Response_Model;
import com.dev.montherland.parsers.Response_JSONParser;
import com.dev.montherland.util.PDialog;
import com.dev.montherland.util.StaticVariables;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Create_Customer extends Activity {

    String name, email, mobile, titleId;
    EditText et_name, et_email, et_mobile;
    Activity thisActivity = this;
    List<Response_Model> person;
    ArrayList<String> titleIdList = new ArrayList<>();
    ArrayList<String> titleList = new ArrayList<>();
    ArrayAdapter<String> dataAdapter;
    List<Response_Model> respones;
    TextView textView11;
    String id="",intent_id,position;
    String menuTitle="Create_Customer",intent_from="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create__customer);

        et_name = (EditText) findViewById(R.id.viewedit_name_edit);
        et_email = (EditText) findViewById(R.id.create_email);
        et_mobile = (EditText) findViewById(R.id.viewedit_phone_edit);
        textView11 = (TextView) findViewById(R.id.contact);



        et_email.setVisibility(View.VISIBLE);
        textView11.setVisibility(View.VISIBLE);

        et_name.setFilters(new InputFilter[]{
                new InputFilter() {
                    @Override
                    public CharSequence filter(CharSequence cs, int start,
                                               int end, Spanned spanned, int dStart, int dEnd) {
                        // TODO Auto-generated method stub
                        if (cs.equals("")) { // for backspace
                            return cs;
                        }
                        if (cs.toString().matches("[a-zA-Z ]+")) {
                            return cs;
                        }
                        return "";
                    }
                }
        });

        try {

            if (getIntent().getExtras().getString("intent_from").equals("edit_customer")) {
                menuTitle = "Edit_Customer";
                getActionBar().setTitle("Edit Customer");
                name=getIntent().getExtras().getString("name");
                email=getIntent().getExtras().getString("email");
                mobile=getIntent().getExtras().getString("phone");
                intent_id=getIntent().getExtras().getString("id");

                et_name.setText(name);
                et_mobile.setText(mobile);
                et_email.setText(email);
                textView11.setVisibility(View.GONE);

            }
        }catch (Exception e){
            e.printStackTrace();
        }


        dataAdapter = new ArrayAdapter<>(this, R.layout.spinner_layout, titleList);

        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);


        if (Build.VERSION.SDK_INT > 19) {
            getActionBar().setDisplayHomeAsUpEnabled(true);
            getActionBar().setHomeAsUpIndicator(R.drawable.pf);
        } else {
            getActionBar().setHomeButtonEnabled(true);
            getActionBar().setIcon(R.drawable.pf);
        }

    }

    public void save() {
        name = et_name.getText().toString();
        email = et_email.getText().toString();
        mobile = et_mobile.getText().toString();

        if (name.equals("") || name.isEmpty()) {

            Toast.makeText(thisActivity, "Enter Customer Name", Toast.LENGTH_SHORT).show();
        } else if (email.equals("") || email.isEmpty()) {
            Toast.makeText(thisActivity, getResources().getString(R.string.correct_website), Toast.LENGTH_SHORT).show();
        }

        else if (mobile.equals("") || mobile.isEmpty()) {
            Toast.makeText(thisActivity, getResources().getString(R.string.correct_mobile), Toast.LENGTH_SHORT).show();

        }

        else {

            if (StaticVariables.isNetworkConnected(thisActivity)) {
                createContact();
            } else {
                Toast.makeText(thisActivity, getResources().getString(R.string.no_internet_connection), Toast.LENGTH_SHORT).show();
            }        }


    }

    void updatedisplay1(String id){

        if(id != null) {

            if(id.equals("Success")) {
                android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(thisActivity);
                builder.setCancelable(false)
                        .setTitle("Success")
                        .setMessage("Customer details has updated successfully")
                        .setNegativeButton("ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Intent intent = new Intent(thisActivity, NavigataionActivity.class);
                                intent.putExtra("redirection","Customers");
                                thisActivity.finish();

                                startActivity(intent);
                                overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
                            }
                        });
                builder.show();

            }

        } else {
            Toast.makeText(thisActivity,getResources().getString(R.string.error_occurred1),Toast.LENGTH_LONG).show();
        }

    }


    public void editContact() {
        name = et_name.getText().toString();
        mobile = et_mobile.getText().toString();
        email=et_email.getText().toString();

        if (name.equals("") || name.isEmpty()) {

            Toast.makeText(thisActivity, getResources().getString(R.string.correct_name), Toast.LENGTH_SHORT).show();
        }
        else if (email.equals("") || email.isEmpty()) {
            Toast.makeText(thisActivity, getResources().getString(R.string.correct_website), Toast.LENGTH_SHORT).show();
        }

        else if (mobile.equals("") || mobile.isEmpty()) {
            Toast.makeText(thisActivity, getResources().getString(R.string.correct_mobile), Toast.LENGTH_SHORT).show();

        } else if (mobile.length() != 10) {
            Toast.makeText(thisActivity, getResources().getString(R.string.correct_limit_contact), Toast.LENGTH_SHORT).show();
        } else {

            PDialog.show(thisActivity);
            StringRequest request = new StringRequest(Request.Method.POST, getResources().getString(R.string.url_motherland) + "customer_edit.php",
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String s) {
                            Log.v("response", s);

                            PDialog.hide();
                            try {
                                JSONObject ar = new JSONObject(s);

                                id = ar.getString("id");
                                updatedisplay1(id);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            //Toast.makeText(thisActivity,s,Toast.LENGTH_LONG).show();

                            //Log.d("response", s);

                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError volleyError) {
                    PDialog.hide();
                    Toast.makeText(thisActivity, getResources().getString(R.string.no_internet_access), Toast.LENGTH_LONG).show();
                    //Toast.makeText(Editprofile.this,volleyError.getMessage(),Toast.LENGTH_LONG).show();
                }
            }) {
                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<>();
                    params.put("email", StaticVariables.database.get(0).getEmail());
                    params.put("id", StaticVariables.database.get(0).getId());
                    params.put("name", name);
                    params.put("password", StaticVariables.database.get(0).getPassword());
                    params.put("phone", StaticVariables.database.get(0).getPhone());
                    params.put("customer_id", StaticVariables.customerId);
                    params.put("website", email);
                    params.put("customer_phone", mobile);

                    Log.v("name_str", name + mobile + StaticVariables.customerId + StaticVariables.customerContact + titleId);
                    return params;
                }
            };
            AppController.getInstance().addToRequestQueue(request, "receive_data");
            Log.v("url", request + "");
        }

    }
    void updatedisplay(String id){

        if(id != null) {

            if(id.equals("Success")) {
                android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(thisActivity);
                builder.setCancelable(false)
                        .setTitle("Success")
                        .setMessage("New customer has saved successfully")
                        .setNegativeButton("ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Intent intent = new Intent(thisActivity,NavigataionActivity.class);
                                intent.putExtra("redirection","Customers");
                                thisActivity.finish();
                                startActivity(intent);
                                overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);                                }
                        });
                builder.show();
            } else if(id.equals("Already Exists")) {
                Toast.makeText(thisActivity,getResources().getString(R.string.already_exist),Toast.LENGTH_LONG).show();
            }

        } else {
            Toast.makeText(thisActivity,getResources().getString(R.string.error_occurred1),Toast.LENGTH_LONG).show();
        }

    }

    private void createContact() {
        PDialog.show(thisActivity);
        StringRequest request = new StringRequest(Request.Method.POST, getResources().getString(R.string.url_motherland) + "customer_create.php",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        Log.v("response", s);
                        PDialog.hide();
                        try {
                            JSONObject ar = new JSONObject(s);

                            id=ar.getString("id");
                        }catch (Exception e){
                            e.printStackTrace();
                        }

                        respones = Response_JSONParser.parserFeed(s);
                        updatedisplay(id);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                PDialog.hide();
                Toast.makeText(thisActivity, getResources().getString(R.string.no_internet_access), Toast.LENGTH_LONG).show();
                //Toast.makeText(Editprofile.this,volleyError.getMessage(),Toast.LENGTH_LONG).show();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("email", StaticVariables.database.get(0).getEmail());
                params.put("id", StaticVariables.database.get(0).getId());
                params.put("name", name);
                params.put("password", StaticVariables.database.get(0).getPassword());
                params.put("phone", mobile);
                params.put("website", email);
                params.put("customer_phone", mobile);




                return params;
            }

        };
        AppController.getInstance().addToRequestQueue(request, "receive_data");
        Log.v("url",request+"");
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu items for use in the action barenu
        getMenuInflater().inflate(R.menu.menu_save, menu);

        return true;
    }

    public void onBack(){
        if (menuTitle.contains("Edit_Customer")){
            Intent in=new Intent(thisActivity,NavigataionActivity.class);
            in.putExtra("name",name);
            in.putExtra("phone",mobile);
            in.putExtra("email", email);
            in.putExtra("id", intent_id);
            in.putExtra("position", position);
            in.putExtra("redirection", "Customers");

            startActivity(in);
            overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
            finish();
        }else {
            super.onBackPressed();
            overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
        }
    }

    @Override
    public void onBackPressed() {
       onBack();

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBack();
                return true;
            case R.id.save_button:

                StaticVariables.hideKeyboard(thisActivity);
                if (menuTitle.contains("Create_Customer")) {
                    save();
                }else {

                  //  Toast.makeText(thisActivity,"This feature is under construction",Toast.LENGTH_SHORT).show();
                    editContact();
                }

                return true;

            default:
                return true;
        }
    }
}
