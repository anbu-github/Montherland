package com.dev.montherland;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.dev.montherland.adapter.CreateOrderAdapter;
import com.dev.montherland.model.GarmentListModel;
import com.dev.montherland.parsers.Garment_JSONParer;
import com.dev.montherland.util.PDialog;
import com.dev.montherland.util.StaticVariables;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GarmentsDataActivity extends Activity implements CreateOrderAdapter.DataFromAdapterToActivity {

    RecyclerView mRecyclerView;
    RecyclerView.LayoutManager mLayoutManager;
    RecyclerView.Adapter mAdapter;
    Activity thisActivity=this;
    ArrayList<String> garmentWashIdList = new ArrayList<>();
    ArrayList<String> garmentWashTypeList = new ArrayList<>();
    ArrayList<String> washTypeList = new ArrayList<>();
    ArrayList<String> garmentInstrList = new ArrayList<>();
    ArrayList<String> garmentStyleList = new ArrayList<>();
    ArrayList<String> garmentWashTypeIdList = new ArrayList<>();
    ArrayAdapter<String> washTypeAdapter;
    List<GarmentListModel> garment_model;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_garments_data);
        mRecyclerView = (RecyclerView) findViewById(R.id.listView);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new GridLayoutManager(thisActivity, 1);
        mRecyclerView.setLayoutManager(mLayoutManager);

        StaticVariables.status="next";

        try {

            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
            if (Build.VERSION.SDK_INT > 19) {
                getActionBar().setDisplayHomeAsUpEnabled(true);
                getActionBar().setHomeAsUpIndicator(R.drawable.pf);
            } else {
                getActionBar().setHomeButtonEnabled(true);
                getActionBar().setIcon(R.drawable.pf);
            }

            washTypeAdapter = new ArrayAdapter<String>(thisActivity,
                    android.R.layout.simple_spinner_item, washTypeList);
            washTypeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        } catch (Exception e) {
            e.printStackTrace();
        }
        if (StaticVariables.isNetworkConnected(thisActivity)) {
            getGarmentTypes();
        } else {
            Toast.makeText(thisActivity, "Please check the network connection", Toast.LENGTH_SHORT).show();
        }

    }
    public void getWashTypes() {

        // PDialog.show(thisActivity);
        StringRequest request = new StringRequest(Request.Method.POST, getResources().getString(R.string.url_motherland) + "wash_type_list.php?",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        PDialog.hide();
                        Log.v("response", response + "");
                        try {
                            PDialog.hide();
                            JSONArray ar = new JSONArray(response);


                            washTypeList.add("Select wash type");
                            for (int i = 0; i < ar.length(); i++) {
                                JSONObject parentObject = ar.getJSONObject(i);
                                washTypeList.add(parentObject.getString("name"));
                                garmentWashIdList.add(parentObject.getString("id"));

                                Log.v("name", parentObject.getString("name"));
                                //Log.d("success", parentObject.getString("success"));
                            }

                            mAdapter = new CreateOrderAdapter(thisActivity
                                    , garment_model,washTypeAdapter);

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
                params.put("email", StaticVariables.database.get(0).getEmail());
                params.put("password", StaticVariables.database.get(0).getPassword());
                params.put("id", StaticVariables.database.get(0).getId());

                return params;
            }
        };
        AppController.getInstance().addToRequestQueue(request, "data_receive");
        Log.v("request", request + "");
    }
    public void getGarmentTypes() {
           PDialog.show(thisActivity);
        StringRequest request = new StringRequest(Request.Method.POST, getResources().getString(R.string.url_motherland) + "garment_type_list.php?",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                       // PDialog.hide();
                        Log.v("response", response + "");
                        try {
                            PDialog.hide();
                            JSONArray ar = new JSONArray(response);
                            garment_model = Garment_JSONParer.parserFeed(response);

                            Log.v("garment size", garment_model.size() + "");
                            getWashTypes();


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
                params.put("email", StaticVariables.database.get(0).getEmail());
                params.put("password", StaticVariables.database.get(0).getPassword());
                params.put("id", StaticVariables.database.get(0).getId());

                //Log.d("params", database.get(0).getId());
                //Log.d("service_id", StaticVariables.service_id);
                return params;
            }
        };
        AppController.getInstance().addToRequestQueue(request, "data_receive");
        Log.v("request", request + "");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu items for use in the action barenu
        getMenuInflater().inflate(R.menu.menu_next, menu);

        return true;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                super.onBackPressed();
                finish();
                overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);


                return true;
            case R.id.next_button:
                Toast.makeText(thisActivity, "next", Toast.LENGTH_SHORT).show();

                    ArrayList<String> garment_type = new ArrayList<>();
                    ArrayList<String> garment_quantity = new ArrayList<>();
                    ArrayList<String> garment_id = new ArrayList<>();
                    int no = 0;

                    for (int i = 0; i <= garment_model.size(); i++) {

                        try {
                            if (garment_model.get(i).getGarmentQuantity().equals("")||garment_model.get(i).getGarmentStyle().equals("")||garment_model.get(i).getGarmentInstr().equals("")) {

                                no++;

                            }

                            else {

                                Boolean isValidNo = StaticVariables.checkIfNumber(garment_model.get(i).getGarmentQuantity());
                                Log.v("wash Tylpe",garment_model.get(i).getGarmentWashType());

                                 if (!isValidNo) {
                                    Toast.makeText(thisActivity, "Please enter valid number", Toast.LENGTH_SHORT).show();
                                } else {

                                    garment_quantity.add(garment_model.get(i).getGarmentQuantity());
                                    garment_type.add(garment_model.get(i).getGarmentType());
                                    garment_id.add(garment_model.get(i).getGarmentTypeId());
                                    garmentStyleList.add(garment_model.get(i).getGarmentStyle());
                                    garmentWashTypeList.add(garment_model.get(i).getGarmentWashType());
                                    garmentWashTypeIdList.add(garment_model.get(i).getGarmentWashId());
                                    garmentInstrList.add(garment_model.get(i).getGarmentInstr());
                                }

                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    }
                    Log.v("quantity no", garment_model.get(0).getGarmentQuantity().toString());
                if (no==garment_model.size()){
                    Toast.makeText(thisActivity, "Please fill atleast one garment item details", Toast.LENGTH_SHORT).show();

                }

                else if (garment_model.get(0).getGarmentWashType().equals("Select wash type")){
                    Toast.makeText(thisActivity, "Please select wash type", Toast.LENGTH_SHORT).show();

                }

                else {
                    Intent in = new Intent(thisActivity, SelectAddress.class);
                    startActivity(in);

                }

                    StaticVariables.editQuantityList = garment_quantity;
                    StaticVariables.garmentTypeList = garment_type;
                    StaticVariables.garmentIdList = garment_id;
                    StaticVariables.garmentStyle = garmentStyleList;
                    StaticVariables.garmentWashtype = garmentWashTypeList;
                    StaticVariables.garmentWashTypeId = garmentWashTypeIdList;
                    StaticVariables.garmentInstr = garmentInstrList;

                return true;

            default:
                return true;
        }
    }

    @Override
    public void garmentQuantity(String quantity, int i) {
        garment_model.get(i).setGarmentQuantity(quantity);

    }

    @Override
    public void garmentStyle(String style, int i) {
        try
        {
            garment_model.get(i).setGarmentStyle(style);
            Log.v("garmentStyle", style);
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    @Override
    public void washType(String type, int i,int spinPos) {
        try {

            garment_model.get(i).setGarmentWashType(type);
            garment_model.get(i).setGarmentWashId(spinPos + "");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void garmentInstr(String type, int i) {
        garment_model.get(i).setGarmentInstr(type);
    }

}
