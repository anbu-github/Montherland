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
import com.dev.montherland.model.GarmentListModel;
import com.dev.montherland.model.Response_Model;
import com.dev.montherland.parsers.Garment_JSONParer;
import com.dev.montherland.parsers.Response_JSONParser;
import com.dev.montherland.util.PDialog;
import com.dev.montherland.util.StaticVariables;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CreatePurchaseOrder extends AppCompatActivity implements CreateOrderAdapter.DataFromAdapterToActivity {

    Activity thisActivity = this;
    String data_receive = "string_req_recieve";
    ArrayList<String> customerLIst = new ArrayList<>();
    ArrayList<String> customerIdList = new ArrayList<>();
    ArrayList<String> companyList = new ArrayList<>();
    ArrayList<String> companyIdList = new ArrayList<>();
    ArrayList<String> garmentList = new ArrayList<>();

    Spinner customerList, Companylist, ContactList, GarmentTypes;
    ArrayAdapter<String> dataAdapter, dataAdapter2, dataAdapter3, dataAdapter4;
    List<Response_Model> response_model=new ArrayList<>();
    List<GarmentListModel> garment_model;
    String companyId, customerId, contactId,companyName,customerName;

    RecyclerView mRecyclerView;
    RecyclerView.LayoutManager mLayoutManager;
    RecyclerView.Adapter mAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_purchase_order);
        customerList = (Spinner) findViewById(R.id.customer_company_list);
        Companylist = (Spinner) findViewById(R.id.customer_list);

        mRecyclerView = (RecyclerView) findViewById(R.id.listView);
        mRecyclerView.setHasFixedSize(true);

        // The number of Columns
        mLayoutManager = new GridLayoutManager(CreatePurchaseOrder.this,1);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                mAdapter.notifyItemRemoved(0);
                return false;
            }
        });
        try {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (StaticVariables.isNetworkConnected(thisActivity)) {
            getCompanyList();
            getGarmentTypes();
        }
        else {
            Toast.makeText(thisActivity, "Please check the network connection", Toast.LENGTH_SHORT).show();
        }

        dataAdapter = new ArrayAdapter<String>(this,
                R.layout.spinner_layout, customerLIst);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        dataAdapter2 = new ArrayAdapter<String>(this,
                R.layout.spinner_layout, companyList);
        dataAdapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        dataAdapter4 = new ArrayAdapter<String>(this,
                R.layout.spinner_layout, garmentList);
        dataAdapter4.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);


        customerList.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                try {
                    customerId = customerLIst.get(position);
                  //  StaticVariables.customerContact = customerIdList.get(position).toString();
                    customerName=customerList.getSelectedItem().toString();
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
                    companyName=Companylist.getSelectedItem().toString();

                    if (StaticVariables.isNetworkConnected(thisActivity)) {
                        getCustomerList();
                    }
                    else {
                        Toast.makeText(thisActivity, "Please check the network connection", Toast.LENGTH_SHORT).show();
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
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

                            customerLIst.add("Select Contact");
                            for (int i = 0; i < ar.length(); i++) {

                                JSONObject parentObject = ar.getJSONObject(i);
                                customerLIst.add(parentObject.getString("name"));
                                //customerIdList.add(parentObject.getString("id"));
                              StaticVariables.customerContact=parentObject.getString("id");
                                StaticVariables.customerName=parentObject.getString("name");

                                StaticVariables.contactId=parentObject.getString("id");

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
                            companyList.add("Select Company");
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
                            garment_model = Garment_JSONParer.parserFeed(response);

                            Log.v("garment size",garment_model.size()+"");



                            mAdapter = new CreateOrderAdapter(thisActivity
                                    ,garment_model);
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
                Toast.makeText(thisActivity, "next", Toast.LENGTH_SHORT).show();

                if (companyName.equals("Select Company")){

                    Toast.makeText(thisActivity,"Please select company",Toast.LENGTH_SHORT).show();
                }
                else if(customerName.equals("Select Contact")){
                    Toast.makeText(thisActivity,"Please select contact",Toast.LENGTH_SHORT).show();

                }

                else {
                    ArrayList<String>garment_type=new ArrayList<>();
                    ArrayList<String>garment_quantity=new ArrayList<>();
                    ArrayList<String>garment_id=new ArrayList<>();
                    int no=0;


                    for (int i=0;i<=garment_model.size();i++){

                        try{
                            if (garment_model.get(i).getGarmentQuantity().equals("")){

                                no++;
                               // Toast.makeText(thisActivity,"Please enter the quantity of" +garment_model.get(i).getGarmentType(),Toast.LENGTH_SHORT).show();
                            }
                            else {

                                Boolean isValidNo=StaticVariables.checkIfNumber(garment_model.get(i).getGarmentQuantity());

                                if (!isValidNo){
                                    Toast.makeText(thisActivity,"Please enter valid number",Toast.LENGTH_SHORT).show();
                                }
                                else {
                                    garment_quantity.add(garment_model.get(i).getGarmentQuantity());
                                    garment_type.add(garment_model.get(i).getGarmentType());
                                    garment_id.add(garment_model.get(i).getGarmentTypeId());
                                }

                            }
                        }catch (Exception e){
                            e.printStackTrace();
                        }

                    }

                    Log.v("quantity no", garment_model.get(0).getGarmentQuantity().toString());

                    if (no>=garment_model.size()){
                        Toast.makeText(thisActivity,"Please enter at least any one of the garment items",Toast.LENGTH_SHORT).show();
                    }
                    else {
                        Intent in = new Intent(CreatePurchaseOrder.this, SelectAddress.class);
                        startActivity(in);

                    }



                   StaticVariables.editQuantityList = garment_quantity;
                    StaticVariables.garmentTypeList = garment_type;
                    StaticVariables.garmentIdList = garment_id;

                }


                return true;

            default:
                return true;
        }
    }

    @Override
    public void garmentQuantity(String quantity, int i) {
        garment_model.get(i).setGarmentQuantity(quantity);
        Log.v("quantity",quantity+"");
    }
}