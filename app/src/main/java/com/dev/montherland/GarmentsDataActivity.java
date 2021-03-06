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
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
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
    ArrayList<String> washTypeList = new ArrayList<>();
    ArrayList<String> garmentTypeList = new ArrayList<>();
    ArrayList<String> itemList = new ArrayList<>();
    ArrayAdapter<String> itemsAdapter;


    Spinner spinner_items;
    List<GarmentListModel> garment_model;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_garments_data);
        mRecyclerView = (RecyclerView) findViewById(R.id.listView);
        spinner_items = (Spinner) findViewById(R.id.spinner_items);
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
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (StaticVariables.isNetworkConnected(thisActivity)) {

            getGarmentTypes();
        } else {
            Toast.makeText(thisActivity, "Please check the network connection", Toast.LENGTH_SHORT).show();
        }


       for (int i=1;i<=20;i++){
           itemList.add(i+"");
       }

        itemsAdapter = new ArrayAdapter<String>(thisActivity,
                android.R.layout.simple_spinner_item, itemList);
        itemsAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinner_items.setAdapter(itemsAdapter);


        spinner_items.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                getWashTypes();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        garment_model = Garment_JSONParer.parserFeed();


    }

    public void getWashTypes() {

        // PDialog.show(thisActivity);
        StringRequest request = new StringRequest(Request.Method.POST, getResources().getString(R.string.url_motherland) + "wash_type_list.php?",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.v("response", response + "");
                        try {
                            PDialog.hide();
                            JSONArray ar = new JSONArray(response);

                            washTypeList.add("Select Wash Type");
                            for (int i = 0; i < ar.length(); i++) {
                                JSONObject parentObject = ar.getJSONObject(i);
                                washTypeList.add(parentObject.getString("name"));
                                garmentWashIdList.add(parentObject.getString("id"));

                                Log.v("name", parentObject.getString("name"));
                                //Log.d("success", parentObject.getString("success"));
                            }

                            PDialog.hide();
                            mAdapter = new CreateOrderAdapter(thisActivity
                                    , garmentTypeList,washTypeList,spinner_items.getSelectedItem().toString(),garment_model);

                            mRecyclerView.setDescendantFocusability(ViewGroup.FOCUS_AFTER_DESCENDANTS);

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
                        //    PDialog.hide();
                            JSONArray ar = new JSONArray(response);

                            garmentTypeList.add("Select Garment Type");
                            for (int i = 0; i < ar.length(); i++) {
                                JSONObject parentObject = ar.getJSONObject(i);
                                garmentTypeList.add(parentObject.getString("name"));

                                Log.v("name", parentObject.getString("name"));
                                //Log.d("success", parentObject.getString("success"));
                            }



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

    public void goNext(){
        ArrayList<String> garment_type = new ArrayList<>();
        ArrayList<String> garment_quantity = new ArrayList<>();
        ArrayList<String> garment_id = new ArrayList<>();
        ArrayList<String> garment_style = new ArrayList<>();
        ArrayList<String> garment_washType = new ArrayList<>();
        ArrayList<String> garment_instr = new ArrayList<>();
        ArrayList<String> garment_washtypeid = new ArrayList<>();

        Boolean goOrnot = false;
        int j = 0;
        if(garment_model != null) {
            for (int i = 0; i < garment_model.size(); i++) {
                if (!garment_model.get(i).getGarmentWashType().equals("Select Wash Type") || !garment_model.get(i).getGarmentStyle().equals("") || !garment_model.get(i).getGarmentQuantity().equals("") || !garment_model.get(i).getGarmentInstr().equals("")) {

                    if (garment_model.get(i).getGarmentType().equals("Select Garment Type")) {
                        goOrnot = false;
                        Toast.makeText(thisActivity, "Please select Garment type of "  , Toast.LENGTH_SHORT).show();

                        break;
                    } else if (garment_model.get(i).getGarmentWashType().equals("Select Wash Type")) {
                        goOrnot = false;
                        Toast.makeText(thisActivity, "Please select wash type of " + garment_model.get(i).getGarmentType(), Toast.LENGTH_SHORT).show();

                        break;
                    } else if (garment_model.get(i).getGarmentStyle().equals("")) {
                        goOrnot = false;
                        Toast.makeText(thisActivity, "Please enter style of " + garment_model.get(i).getGarmentType(), Toast.LENGTH_SHORT).show();
                        break;
                    } else if (garment_model.get(i).getGarmentQuantity().equals("")) {
                        goOrnot = false;
                        StaticVariables.quantityPos=i;
                        Toast.makeText(thisActivity, "Please enter quantity of " + garment_model.get(i).getGarmentType(), Toast.LENGTH_SHORT).show();
                        break;
                    }
                    else if (Integer.parseInt(garment_model.get(i).getGarmentQuantity())==0){
                        goOrnot = false;
                        Toast.makeText(thisActivity,getResources().getString(R.string.enter_valid_number),Toast.LENGTH_LONG).show();
                        break;
                    }
                    else {
                        goOrnot = true;
                        Boolean isValidNo = StaticVariables.checkIfNumber(garment_model.get(i).getGarmentQuantity());
                        Log.v("wash Tylpe", garment_model.get(i).getGarmentWashType());
                        j++;
                        garment_quantity.add(garment_model.get(i).getGarmentQuantity());
                        garment_type.add(garment_model.get(i).getGarmentType());
                        garment_id.add(garment_model.get(i).getGarmentTypeId());
                        garment_style.add(garment_model.get(i).getGarmentStyle());
                        garment_instr.add(garment_model.get(i).getGarmentInstr());
                        garment_washType.add(garment_model.get(i).getGarmentWashType());
                        garment_washtypeid.add(garment_model.get(i).getGarmentWashId());
                    }
                }
//                else {
//                    Toast.makeText(thisActivity,getResources().getString(R.string.enter_product_details),Toast.LENGTH_LONG).show();
//                    goOrnot = false;
//                    break;
//                }
            }
        } else {
            Toast.makeText(thisActivity,getResources().getString(R.string.enter_atleast_one_product),Toast.LENGTH_LONG).show();
        }

        Log.v("quantity no", garment_model.get(0).getGarmentQuantity().toString());
        if(j > 0) {
            if (goOrnot) {
                StaticVariables.address_id="";
                StaticVariables.hideKeyboard(thisActivity);
                Intent in = new Intent(thisActivity, SelectAddress.class);
                StaticVariables.selectAddress = "Create order";
                startActivity(in);
                overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);


                StaticVariables.editQuantityList = garment_quantity;
                StaticVariables.garmentTypeList = garment_type;
                StaticVariables.garmentIdList = garment_id;
                StaticVariables.garmentStyle = garment_style;
                StaticVariables.garmentWashtype = garment_washType;
                StaticVariables.garmentWashTypeId = garment_washtypeid;
                StaticVariables.garmentInstr = garment_instr;
            }
        }
        else {
            Toast.makeText(thisActivity,getResources().getString(R.string.enter_atleast_one_product),Toast.LENGTH_LONG).show();
        }
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
            //   Toast.makeText(thisActivity, "next", Toast.LENGTH_SHORT).show();

                goNext();
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
    public void garmentType(String type, int i, int position) {
        garment_model.get(i).setGarmentType(type);
        garment_model.get(i).setGarmentTypeId(position+"");
        Log.v("garment_item",type+position);
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