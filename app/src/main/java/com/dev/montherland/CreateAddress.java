package com.dev.montherland;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.dev.montherland.model.Response_Model;
import com.dev.montherland.parsers.Response_JSONParser;
import com.dev.montherland.util.PDialog;
import com.dev.montherland.util.StaticVariables;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CreateAddress extends Activity {
    EditText email, address1, address2, address3, city, pincode;

    Activity thisActivity = this;
    ArrayList<String> stateList = new ArrayList<>();
    ArrayList<String> stateIdList = new ArrayList<>();
    Spinner stateSpinner;
    ArrayAdapter<String> dataAdapter;
    String data_receive = "string_req_recieve", strAddress1, strAddress2, strAddress3, strPincode, strCity, stateId,strState;
    List<Response_Model> feedlist;
    String action="",addressId;
    String intent_name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_address);
        stateSpinner = (Spinner) findViewById(R.id.state_spin);
        address1 = (EditText) findViewById(R.id.add1);
        address2 = (EditText) findViewById(R.id.add3);
        address3 = (EditText) findViewById(R.id.address_line3);
        city = (EditText) findViewById(R.id.add2);
        pincode = (EditText) findViewById(R.id.pincode);

        intent_name=getIntent().getExtras().getString("intent_name");
        stateList.add("Select");
        stateIdList.add("Select");
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        if (Build.VERSION.SDK_INT > 19) {
            getActionBar().setDisplayHomeAsUpEnabled(true);
            getActionBar().setHomeAsUpIndicator(R.drawable.pf);
        } else {
            getActionBar().setHomeButtonEnabled(true);
            getActionBar().setIcon(R.drawable.pf);
        }

        try {
            if (intent_name.contains("edit_address")||intent_name.contains("customer_edit_address")){

                StaticVariables.hideKeyboard(thisActivity);
                getActionBar().setTitle("Edit Address");

                if (intent_name.contains("edit_address")) {
                    action = "edit_address";
                }

                address1.setText(getIntent().getExtras().getString("address1"));
                address2.setText(getIntent().getExtras().getString("address2"));
                address3.setText(getIntent().getExtras().getString("address3"));
                city.setText(getIntent().getExtras().getString("city"));
                pincode.setText(getIntent().getExtras().getString("zipcode"));
                addressId=getIntent().getExtras().getString("address_id");
                strState=getIntent().getExtras().getString("state");

            }

            else if (intent_name.contains("customer_address")){

                getActionBar().setTitle("Create Address");
                action="create_customer_address";

            }


            dbhelp.DatabaseHelper2 dbhelp = new dbhelp.DatabaseHelper2(thisActivity);
            dbhelp.getReadableDatabase();
            StaticVariables.database = dbhelp.getdatabase();
            dbhelp.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (StaticVariables.isNetworkConnected(thisActivity)) {
            getStateList();
        } else {
            Toast.makeText(thisActivity, getResources().getString(R.string.no_internet_access), Toast.LENGTH_LONG).show();
        }


        dataAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, stateList);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

       stateSpinner.setOnTouchListener(new View.OnTouchListener() {
           @Override
           public boolean onTouch(View v, MotionEvent event) {

               StaticVariables.hideKeyboard(thisActivity);
               return false;
           }
       });

        stateSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                stateId = String.valueOf(stateIdList.get(position));


                View view1 = getCurrentFocus();
                if (view1 != null) {
                    InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

    public void submitAddress() {
        strAddress1 = String.valueOf(address1.getText());
        strAddress2 = String.valueOf(address2.getText());
        strAddress3 = String.valueOf(address3.getText());
        strCity = String.valueOf(city.getText());
        strPincode = String.valueOf(pincode.getText());

        if (strAddress1.equals("")) {
            address1.setError("Please enter the address");
            address1.requestFocus();
        } else if (strCity.equals("")) {
            city.setError("Please enter the city");
            city.requestFocus();
        } else if (strPincode.equals("")) {
            pincode.setError("please enter pincode");
            pincode.requestFocus();
        } else if (strPincode.length() != 6) {
            pincode.setError("please enter valid pincode");
            pincode.requestFocus();
        }
        else if (stateSpinner.getSelectedItem().equals("Select")) {
            Toast.makeText(thisActivity,"please select state",Toast.LENGTH_SHORT).show();
        }
        else {

            if (StaticVariables.isNetworkConnected(thisActivity)) {

                if (action.contains("edit_address")){
                    editAddress();
                }else {
                    createAddress();
                }
            } else {
                Toast.makeText(thisActivity, "Please check the network connection", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void updatedisplay() {
        PDialog.hide();
        if (feedlist != null) {

            for (final Response_Model flower : feedlist) {
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
                        AlertDialog.Builder builder = new AlertDialog.Builder(thisActivity);
                        builder.setCancelable(false)
                                .setTitle("Success")
                                .setMessage("Address successfully saved")
                                .setNegativeButton("ok", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        Intent intent = new Intent(thisActivity, SelectAddress.class);
                                        startActivity(intent);
                                        overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);


                                    }
                                });
                        builder.show();

                }
            }
        } else {
            Toast.makeText(thisActivity, getResources().getString(R.string.error_occurred1), Toast.LENGTH_LONG).show();
        }
    }

    public void editAddress() {
        PDialog.show(thisActivity);
        StringRequest request = new StringRequest(Request.Method.POST, getResources().getString(R.string.url_motherland) + "customer_address_edit.php?",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        PDialog.hide();
//                        Toast.makeText(thisActivity,"address saved",Toast.LENGTH_SHORT).show();
                        AlertDialog.Builder builder = new AlertDialog.Builder(thisActivity);
                        builder.setCancelable(false)
                                .setTitle("Success")
                                .setMessage("Address has saved successfully")
                                .setNegativeButton("ok", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        Intent intent = new Intent(thisActivity, SelectAddress.class);
                                        if (!(StaticVariables.selectAddress.contains("Create order"))) {
                                            intent.putExtra("change_address", "change_address");
                                        }
                                        startActivity(intent);
                                        finish();
                                        overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);


                                    }
                                });
                        builder.show();

                        Log.v("response", response + "");
                        try {
                            PDialog.hide();
                            JSONArray ar = new JSONArray(response);

                            feedlist = Response_JSONParser.parserFeed(response);
                            updatedisplay();


                        } catch (JSONException e) {
                            PDialog.hide();

                        } catch (Exception e) {
                            PDialog.hide();
                        }
                    }
                },

                new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError arg0) {
                        PDialog.hide();

                    }
                }) {

            @Override
            protected Map<String, String> getParams() {

                Map<String, String> params = new HashMap<>();

                params.put("id", StaticVariables.database.get(0).getId());
                params.put("address_id", addressId);
                params.put("password", StaticVariables.database.get(0).getPassword());
                params.put("email", StaticVariables.database.get(0).getEmail());
                params.put("address_line1", strAddress1);
                params.put("address_line2", strAddress2);
                params.put("address_line3", strAddress3);
                params.put("city", strCity);
                params.put("state_id", stateId);
                params.put("zipcode", strPincode);


                Log.v("address_id", addressId);

                return params;
            }
        };
        AppController.getInstance().addToRequestQueue(request, data_receive);
        Log.v("request", request + "");
    }


    public void createAddress() {
        PDialog.show(thisActivity);
        StringRequest request = new StringRequest(Request.Method.POST, getResources().getString(R.string.url_motherland) + "customer_address_create.php?",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        PDialog.hide();
                       // Toast.makeText(thisActivity,"address saved",Toast.LENGTH_SHORT).show();
                        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(thisActivity);
                        builder.setCancelable(false)
                                .setTitle("Success")
                                .setMessage("Address has saved successfully")
                                .setNegativeButton("ok", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {

                                        if (action.contains("create_customer_address")){

                                            Intent intent1 = new Intent(thisActivity, SelectAddress.class);
                                            intent1.putExtra("customer_address", "customer_address");
                                            startActivity(intent1);
                                            finish();
                                            overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);

                                        }
                                        else {
                                            Intent intent = new Intent(thisActivity, SelectAddress.class);
                                            startActivity(intent);
                                            finish();
                                            overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
                                        }



                                    }
                                });
                        builder.show();

                        Log.v("response", response + "");
                        try {
                            PDialog.hide();
                            JSONArray ar = new JSONArray(response);

                            feedlist = Response_JSONParser.parserFeed(response);
                            updatedisplay();

                        } catch (JSONException e) {
                            PDialog.hide();

                        } catch (Exception e) {
                            PDialog.hide();
                        }
                    }
                },

                new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError arg0) {
                        PDialog.hide();

                    }
                }) {

            @Override
            protected Map<String, String> getParams() {

                Map<String, String> params = new HashMap<>();
                params.put("id", StaticVariables.database.get(0).getId());

                params.put("customer_id", StaticVariables.customerId);

                if(action.contains("create_customer_address")){

                }
                else {
                    params.put("customer_contact_id", StaticVariables.customerContact);

                }
                params.put("email", StaticVariables.database.get(0).getEmail());
                params.put("address_lin1", strAddress1);
                params.put("address_lin2", strAddress2);
                params.put("address_lin3", strAddress3);
                params.put("city", strCity);
                params.put("state_id", stateId);
                params.put("zip", strPincode);

                Log.v("zipcode", strPincode);

                return params;
            }
        };
        AppController.getInstance().addToRequestQueue(request, data_receive);
        Log.v("request", request + "");
    }

    public void getStateList() {
        PDialog.show(thisActivity);
        StringRequest request = new StringRequest(Request.Method.POST, getResources().getString(R.string.url_motherland) + "state_list.php?",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        PDialog.hide();
                        Log.v("response", response + "");

                        try {

                            stateList.clear();
                            stateIdList.clear();

                            stateList.add("Select");
                            stateIdList.add("Select");

                            JSONArray ar = new JSONArray(response);

                            for (int i = 0; i < ar.length(); i++) {
                                JSONObject parentObject = ar.getJSONObject(i);

                                stateList.add(parentObject.getString("name"));
                                stateIdList.add(parentObject.getString("id"));

                            }
                            stateSpinner.setAdapter(dataAdapter);

                            if (action.contains("edit_address")){
                             for (int i=0;i<stateList.size();i++) {

                                 if (strState.equals(stateList.get(i))){
                                     stateSpinner.setSelection(i);
                                 }
                             }
                            }

                        } catch (JSONException e) {
                            PDialog.hide();

                        } catch (Exception e) {
                            PDialog.hide();

                        }
                    }
                },

                new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError arg0) {
                        PDialog.hide();

                    }
                }) {


        };
        AppController.getInstance().addToRequestQueue(request, data_receive);
        Log.v("request", request + "");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu items for use in the action barenu
        getMenuInflater().inflate(R.menu.menu_save, menu);

        return true;
    }

    public void onBackPressed() {

         if (StaticVariables.status.contains("Save")){
            Intent intent = new Intent(thisActivity, SelectAddress.class);
            intent.putExtra("change_address","change_address");
            startActivity(intent);
            finish();
            overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
        }
       else if (action.contains("edit_address")){
            Intent intent = new Intent(thisActivity, SelectAddress.class);
            startActivity(intent);
            finish();
            overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
        }
        else if (action.contains("create_customer_address")){

             Intent intent = new Intent(thisActivity, SelectAddress.class);
             intent.putExtra("customer_address", "customer_address");
             startActivity(intent);
             finish();
             overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);

        }
        else {
             super.onBackPressed();
             overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
         }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
              if (StaticVariables.status.contains("Save")){
                    Intent intent = new Intent(thisActivity, SelectAddress.class);
                    intent.putExtra("change_address","change_address");
                    startActivity(intent);
                    finish();
                    overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
                }
            else if (action.contains("edit_address")){
                Intent intent = new Intent(thisActivity, SelectAddress.class);
                startActivity(intent);
                finish();
                overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
            }


              else if (action.contains("create_customer_address")){

                  Intent intent = new Intent(thisActivity, SelectAddress.class);
                  intent.putExtra("customer_address", "customer_address");
                  startActivity(intent);
                  finish();
                  overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);

              }
              else {
                  super.onBackPressed();
                  overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
              }

                return true;
            case R.id.save_button:

                StaticVariables.hideKeyboard(thisActivity);
                submitAddress();
                return true;

            default:
                return true;
        }
    }
}
