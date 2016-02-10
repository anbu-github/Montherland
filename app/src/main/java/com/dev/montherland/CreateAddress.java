package com.dev.montherland;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.dev.montherland.model.Response_Model;
import com.dev.montherland.util.PDialog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CreateAddress extends AppCompatActivity {
    EditText contactName,businessName,siteName,mobileNo,email,address1,address2,address3,city,pincode;

    Activity thisActivity = this;
    String data_receive = "string_req_recieve";
    List<Response_Model> response_model;
    ArrayList<String> stateList= new ArrayList<>();;
    ArrayList<String> stateIdList= new ArrayList<>();;
    Spinner stateSpinner;
    ArrayAdapter<String> dataAdapter;
    String strAddress1,strAddress2,strAddress3,strPincode,strState,strStateId,strCity,stateId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_address);
        stateSpinner = (Spinner) findViewById(R.id.state_spin);
        address1 = (EditText) findViewById(R.id.add1);
        address2 = (EditText) findViewById(R.id.add3);
        address3 = (EditText) findViewById(R.id.address_line3);
        city = (EditText) findViewById(R.id.state);
        pincode = (EditText) findViewById(R.id.pincode);
        getStateList();

        dataAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, stateList);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);


        stateSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                 stateId=stateIdList.get(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        try {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void submitAddress(){
        strAddress1=address1.getText().toString();
        strAddress2=address2.getText().toString();
        strAddress3=address3.getText().toString();
        strCity=city.getText().toString();
        strPincode=pincode.getText().toString();


        if (strAddress1.equals("")){
            address1.setError("Please enter the address");
        }
        else if (strCity.equals("")){
            city.setError("Please enter the city");
        }
        else if (strPincode.equals("")){
            pincode.setError("please enter pincode");
        }
        else {
            createAdddress();
        }
    }

    public void createAdddress() {
        PDialog.show(thisActivity);
        StringRequest request = new StringRequest(Request.Method.POST, getResources().getString(R.string.url_motherland) + "customer_address_create.php?",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        PDialog.hide();

                        AlertDialog.Builder builder = new AlertDialog.Builder(thisActivity);
                        builder.setCancelable(false)
                                .setTitle("Success")
                                .setMessage("Address successfully saved")
                                .setNegativeButton("ok", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {

                                        Intent intent = new Intent(thisActivity, SelectAddress.class);
                                        startActivity(intent);
                                        overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);


                                    }
                                });
                        builder.show();
                        Log.v("response", response + "");
                        try {
                            PDialog.hide();
                            JSONArray ar = new JSONArray(response);

                            for (int i = 0; i < ar.length(); i++) {
                                JSONObject parentObject = ar.getJSONObject(i);


                                Log.v("name", parentObject.getString("name"));

                                //Log.d("success", parentObject.getString("success"));
                            }


                        } catch (JSONException e) {
                            PDialog.hide();
                            //Log.d("response", response);
                            //Log.d("error in json", "l " + e);

                        } catch (Exception e) {
                            PDialog.hide();
//                            Log.d("json connection", "No internet access" + e);
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
                params.put("email", "test@test.com");
                params.put("customer_id", "5");
                params.put("id", "4");
                params.put("address_lin1", strAddress1);
                params.put("address_lin2", strAddress2);
                params.put("address_lin3", strAddress3);
                params.put("city", strCity);
                params.put("state_id", stateId);
                params.put("zipcode", strPincode);


                //id=4&email=test@test.com&address_lin1=121westparallel&address_lin2=kartiknager&address_lin3=abcd&city=bangalore&state_id=1&customer_id=5

                //Log.d("params", database.get(0).getId());
                //Log.d("service_id", StaticVariables.service_id);
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
                            PDialog.hide();
                            JSONArray ar = new JSONArray(response);
                           // response_model= Response_JSONParser.parserFeed(response);

                            for (int i = 0; i < ar.length(); i++) {
                                JSONObject parentObject = ar.getJSONObject(i);

                               stateList.add(parentObject.getString("name"));
                               stateIdList.add(parentObject.getString("id"));


                            }
                         stateSpinner.setAdapter(dataAdapter);

                        } catch (JSONException e) {
                            PDialog.hide();
                            //Log.d("response", response);
                            //Log.d("error in json", "l " + e);

                        } catch (Exception e) {
                            PDialog.hide();
//                            Log.d("json connection", "No internet access" + e);
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

                //Log.d("params", database.get(0).getId());
                //Log.d("service_id", StaticVariables.service_id);
                return params;
            }
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

        super.onBackPressed();
        overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);

        overridePendingTransition(R.anim.left_right, R.anim.right_left);
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
            super.onBackPressed();
                overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);

                return true;
            case R.id.save_button:
              //  Toast.makeText(thisActivity, "Save", Toast.LENGTH_SHORT).show();
                submitAddress();
                return true;

            default:
                return true;
        }
    }
}
