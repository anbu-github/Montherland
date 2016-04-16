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
import com.dev.montherland.parsers.Response_JSONParser;
import com.dev.montherland.util.PDialog;
import com.dev.montherland.util.StaticVariables;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class EditAddress extends Activity {
    String data_receive = "string_req_recieve", strAddress1, strAddress2, strAddress3, strPincode, strCity, stateId, strState;
    EditText email, address1, address2, address3, city, pincode;
    Activity thisActivity = this;
    ArrayList<String> stateList = new ArrayList<>();
    ArrayList<String> stateIdList = new ArrayList<>();
    Spinner stateSpinner;
    ArrayAdapter<String> dataAdapter;
    String addressId;

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

        StaticVariables.hideKeyboard(thisActivity);
        getActionBar().setTitle("Edit Address");

        address1.setText(getIntent().getExtras().getString("address1"));
        address2.setText(getIntent().getExtras().getString("address2"));
        address3.setText(getIntent().getExtras().getString("address3"));
        city.setText(getIntent().getExtras().getString("city"));
        pincode.setText(getIntent().getExtras().getString("zipcode"));
        addressId = getIntent().getExtras().getString("address_id");
        strState = getIntent().getExtras().getString("state");
        stateId = getIntent().getExtras().getString("stateid");
        addressId = getIntent().getExtras().getString("addressId");

        if (StaticVariables.isNetworkConnected(thisActivity)) {
            getStateList();
        } else {
            Toast.makeText(thisActivity, getResources().getString(R.string.no_internet_connection), Toast.LENGTH_SHORT).show();
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
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

public void save() {
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

    } else if (stateSpinner.getSelectedItem().equals("Select")) {
        Toast.makeText(thisActivity, "please select state", Toast.LENGTH_SHORT).show();
    } else {

        if (StaticVariables.isNetworkConnected(thisActivity)) {

            editAddress();
        } else {
            Toast.makeText(thisActivity, "Please check the network connection", Toast.LENGTH_SHORT).show();
        }

    }
}

    public void editAddress() {
        PDialog.show(thisActivity);
        StringRequest request = new StringRequest(Request.Method.POST, getResources().getString(R.string.url_motherland) + "edit_address.php?",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            PDialog.hide();

                        }
                        catch (Exception e){
                            e.printStackTrace();
                        }
                       // Toast.makeText(thisActivity, "address saved", Toast.LENGTH_SHORT).show();
                        AlertDialog.Builder builder = new AlertDialog.Builder(thisActivity);
                        builder.setCancelable(false)
                                .setTitle("Success")
                                .setMessage("Address has saved successfully")
                                .setNegativeButton("ok", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        Intent intent = new Intent(thisActivity, ViewAddress.class);
                                        if (!(StaticVariables.selectAddress.contains("Create order"))) {
                                            intent.putExtra("change_address", "change_address");
                                        }
                                        startActivity(intent);
                                        finish();
                                        overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);


                                    }
                                });
                        builder.show();

                        Log.v("response", response + "");
                        try {
                            PDialog.hide();
                            JSONArray ar = new JSONArray(response);


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
                params.put("password", StaticVariables.database.get(0).getPassword());
                params.put("email", StaticVariables.database.get(0).getEmail());
                params.put("address1", strAddress1);
                params.put("address2", strAddress2);
                params.put("address3", strAddress3);
                params.put("city", strCity);
                params.put("state", stateId);
                params.put("pincode", strPincode);
                params.put("address_id", addressId);

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


                            for (int i = 0; i < stateList.size(); i++) {

                                if (strState.equals(stateList.get(i))) {
                                    stateSpinner.setSelection(i);
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

    @Override
    public void onBackPressed() {
        Intent intent=new Intent(EditAddress.this,ViewAddress.class);
        startActivity(intent);
        finish();
        overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:

                  Intent intent=new Intent(EditAddress.this,ViewAddress.class);
                   startActivity(intent);
                   finish();
                overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
             return  true;
            case R.id.save_button:

                StaticVariables.hideKeyboard(thisActivity);
                save();
                return true;

            default:
                return true;

        }

    }
}