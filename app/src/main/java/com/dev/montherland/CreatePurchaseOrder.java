package com.dev.montherland;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.dev.montherland.adapter.CreateOrderAdapter;
import com.dev.montherland.model.Response_Model;
import com.dev.montherland.parsers.Response_JSONParser;
import com.dev.montherland.util.PDialog;
import com.dev.montherland.util.StaticVariables;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CreatePurchaseOrder extends AppCompatActivity {


    Activity thisActivity = this;
    String data_receive = "string_req_recieve";
    ArrayList<String> customerLIst = new ArrayList<>();
    ArrayList<String> customerIdList = new ArrayList<>();
    ArrayList<String> companyList = new ArrayList<>();
    ArrayList<String> companyIdList = new ArrayList<>();
    ArrayList<String> garmentList = new ArrayList<>();

    Spinner customerList, Companylist, ContactList, GarmentTypes;
    ArrayAdapter<String> dataAdapter, dataAdapter2, dataAdapter3, dataAdapter4;
    List<Response_Model> response_model;
    String companyId, customerId, contactId;
    int count = 1;
    RecyclerView mRecyclerView;
    RecyclerView.LayoutManager mLayoutManager;
    RecyclerView.Adapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_purchase_order);
        customerList = (Spinner) findViewById(R.id.customer_company_list);
        Companylist = (Spinner) findViewById(R.id.customer_list);

        GarmentTypes = (Spinner) findViewById(R.id.garmentType);
        mRecyclerView = (RecyclerView) findViewById(R.id.listView);
        mRecyclerView.setHasFixedSize(true);

        // The number of Columns
        mLayoutManager = new GridLayoutManager(CreatePurchaseOrder.this,1);
        mRecyclerView.setLayoutManager(mLayoutManager);

        try {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        } catch (Exception e) {
            e.printStackTrace();
        }


        getCompanyList();
        getGarmentTypes();

        dataAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, customerLIst);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        dataAdapter2 = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, companyList);
        dataAdapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        dataAdapter4 = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, garmentList);
        dataAdapter4.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);


        customerList.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {


                try {
                    customerId = customerLIst.get(position);
                    StaticVariables.customerContact = position + "";
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        Companylist.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                try {
                    companyId = position + "";
                    customerLIst.clear();
                    getCustomerList();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


    }

    public void addOrder(View view) {
        count++;
        Toast.makeText(thisActivity, "cubj", Toast.LENGTH_SHORT).show();
        mAdapter = new CreateOrderAdapter(thisActivity,
                count,dataAdapter4);
        mRecyclerView.setAdapter(mAdapter);

    }

    public void getCustomerList() {

        // PDialog.show(thisActivity);
        StringRequest request = new StringRequest(Request.Method.POST, getResources().getString(R.string.url_motherland) + "customer_company_list.php?",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        PDialog.hide();
                        Log.v("response", response + "");
                        try {
                            PDialog.hide();
                            JSONArray ar = new JSONArray(response);

                            customerLIst.add("Select");
                            for (int i = 0; i < ar.length(); i++) {

                                JSONObject parentObject = ar.getJSONObject(i);
                                customerLIst.add(parentObject.getString("name"));


                                Log.v("name", parentObject.getString("name"));
                                //Log.d("success", parentObject.getString("success"));
                            }
                            customerList.setAdapter(dataAdapter);


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
                params.put("customer_id", companyId);
                params.put("password", "e48900ace570708079d07244154aa64a");
                params.put("id", "4");

                //Log.d("params", database.get(0).getId());
                //Log.d("service_id", StaticVariables.service_id);
                return params;
            }
        };
        AppController.getInstance().addToRequestQueue(request, data_receive);
        Log.v("request", request + "");
    }

    public void getCompanyList() {
        PDialog.show(thisActivity);
        StringRequest request = new StringRequest(Request.Method.POST, getResources().getString(R.string.url_motherland) + "customer_list.php?",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // PDialog.hide();
                        Log.v("response", response + "");
                        try {
                            //PDialog.hide();
                            JSONArray ar = new JSONArray(response);

                            companyList.add("Select");
                            for (int i = 0; i < ar.length(); i++) {
                                JSONObject parentObject = ar.getJSONObject(i);
                                companyList.add(parentObject.getString("name"));
                                companyIdList.add(parentObject.getString("id"));


                                Log.v("company_name", parentObject.getString("name"));
                                //Log.d("success", parentObject.getString("success"));
                            }
                            Companylist.setAdapter(dataAdapter2);


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
                params.put("password", "e48900ace570708079d07244154aa64a");
                params.put("id", "4");

                //Log.d("params", database.get(0).getId());
                //Log.d("service_id", StaticVariables.service_id);
                return params;
            }
        };
        AppController.getInstance().addToRequestQueue(request, data_receive);
        Log.v("request", request + "");
    }

    public void getGarmentTypes() {
        //   PDialog.show(thisActivity);
        StringRequest request = new StringRequest(Request.Method.POST, getResources().getString(R.string.url_motherland) + "garment_type_list.php?",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        PDialog.hide();
                        Log.v("response", response + "");
                        try {
                            PDialog.hide();
                            JSONArray ar = new JSONArray(response);
                            response_model = Response_JSONParser.parserFeed(response);

                            try {
                                for(Response_Model flower: response_model) {
                                    garmentList.add(flower.getName());
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                            mAdapter = new CreateOrderAdapter(thisActivity,
                                    count,dataAdapter4);
                            mRecyclerView.setAdapter(mAdapter);

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
                params.put("password", "e48900ace570708079d07244154aa64a");
                params.put("id", "4");

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
        getMenuInflater().inflate(R.menu.menu_next, menu);

        return true;
    }

    public void onBackPressed() {

        super.onBackPressed();

        Intent in = new Intent(thisActivity, NavigataionActivity.class);
        startActivity(in);
        finish();
        overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Intent intent = new Intent(thisActivity, NavigataionActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);

                return true;
            case R.id.next_button:
                ArrayList<String> editQuantityList = new ArrayList<>();
                ArrayList<String> editQuantityList1 = new ArrayList<>();
                ArrayList<String> garmentTypeList = new ArrayList<>();
                ArrayList<String> garmentTypeList1 = new ArrayList<>();
                ArrayList<String> garmentIdList = new ArrayList<>();
                ArrayList<String> garmentIdList1 = new ArrayList<>();

                editQuantityList = StaticVariables.editQuantityList;
                garmentTypeList = StaticVariables.garmentTypeList;
                garmentIdList = StaticVariables.garmentIdList;
                for (int i = 0; i <= editQuantityList.size(); i++) {
                    try {

                        if (!(editQuantityList.get(i).equals("TEST"))) {
                            editQuantityList1.add(editQuantityList.get(i));
                        }
                        if (!(garmentTypeList.get(i).equals("TEST"))) {
                            garmentTypeList1.add(garmentTypeList.get(i));
                        }
                        if (!(garmentIdList.get(i).equals("TEST"))) {
                            garmentIdList1.add(garmentIdList.get(i));
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                Log.v("list", editQuantityList1.toString());
                Toast.makeText(thisActivity, "next", Toast.LENGTH_SHORT).show();
                Intent in = new Intent(CreatePurchaseOrder.this, SelectAddress.class);
                StaticVariables.editQuantityList = editQuantityList1;
                StaticVariables.garmentTypeList = garmentTypeList1;
                StaticVariables.garmentIdList = garmentIdList1;
                startActivity(in);
                return true;

            default:
                return true;
        }
    }
}
