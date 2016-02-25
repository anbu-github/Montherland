package com.dev.montherland;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.dev.montherland.adapter.AddressCreateAdapter;
import com.dev.montherland.model.Create_Address_Model;
import com.dev.montherland.parsers.Create_Address_JSONParser;
import com.dev.montherland.util.PDialog;
import com.dev.montherland.util.StaticVariables;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SelectAddress extends AppCompatActivity {
    String data_receive = "string_req_recieve";
    private RecyclerView recyclerView;
    private List<Create_Address_Model> persons;
    StaggeredGridLayoutManager mLayoutManager;
    Activity thisActivity = this;
    String menuTitle="Next";
    ArrayList<String> editQuantityList = new ArrayList<>();
    ImageButton ig;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_address);
         ig = (ImageButton) findViewById(R.id.btn_add_address);


        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Create Order");

        if (StaticVariables.isNetworkConnected(thisActivity)) {
            getAddressList();
        }
        else {
            Toast.makeText(thisActivity, "Please check the network connection", Toast.LENGTH_SHORT).show();
        }

        recyclerView = (RecyclerView) findViewById(R.id.rv);
        recyclerView.setHasFixedSize(true);
        mLayoutManager = new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(mLayoutManager);
        try {

            if (!(getIntent().getExtras().getString("change_address")==null)){

                getSupportActionBar().setTitle("Change Address");
                StaticVariables.status="Save";
                ig.setVisibility(View.INVISIBLE);
                //getAddressList();
                menuTitle="Save";

            }





        } catch (Exception e) {
            e.printStackTrace();
        }

        ig.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SelectAddress.this, CreateAddress.class);
                startActivity(intent);
            }
        });
    }

    public void getAddressList() {
        PDialog.show(thisActivity);
        StringRequest request = new StringRequest(Request.Method.POST, getResources().getString(R.string.url_motherland) + "customer_addresses.php?",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        PDialog.hide();
                        Log.v("response", response + "");

                        try {
                            PDialog.hide();
                            JSONArray ar = new JSONArray(response);
                            persons = Create_Address_JSONParser.parserFeed(response);
                            recyclerView.setAdapter(new AddressCreateAdapter(persons,thisActivity));
                            // response_model= Response_JSONParser.parserFeed(response);

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
                params.put("email", StaticVariables.database.get(0).getEmail());
                params.put("password", StaticVariables.database.get(0).getPassword());
               // params.put("customer_id", "1");
                 params.put("customer_id", StaticVariables.customerContact);
                params.put("id", StaticVariables.database.get(0).getId());
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
        MenuItem item = menu.findItem(R.id.next_button);
        item.setTitle(menuTitle);
        return true;
    }

    public void onBackPressed() {
        if (menuTitle.contains("Save")){
            Intent in = new Intent(thisActivity, PurchaseOrderDetails.class);
            startActivity(in);
            finish();
            overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
        }
        else {
            super.onBackPressed();
            finish();
            overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
        }

    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                if (menuTitle.contains("Save")){

                    Intent in = new Intent(thisActivity, PurchaseOrderDetails.class);
                    startActivity(in);
                    finish();
                    overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);

                }else {
                    super.onBackPressed();
                    finish();
                    overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
                }

                return true;
            case R.id.next_button:

                if (StaticVariables.cbpos==-1){

                    Toast.makeText(thisActivity, "Please select any address", Toast.LENGTH_SHORT).show();


                }
                else {
                    new AddressCreateAdapter(thisActivity,persons);
                    overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);

                }

               return true;

            default:
                return true;
        }
    }
}