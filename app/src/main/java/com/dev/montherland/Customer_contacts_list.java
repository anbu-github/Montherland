package com.dev.montherland;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.dev.montherland.adapter.CustomerContactAdapter;
import com.dev.montherland.adapter.CustomerDetailsAdapter;
import com.dev.montherland.model.Customer_Contact_Model;
import com.dev.montherland.model.Response_Model;
import com.dev.montherland.parsers.Customer_Contact_JSONParser;
import com.dev.montherland.parsers.Response_JSONParser;
import com.dev.montherland.util.PDialog;
import com.dev.montherland.util.StaticVariables;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Customer_contacts_list extends Activity {

    private RecyclerView recyclerView;
    private List<Customer_Contact_Model> persons;
    StaggeredGridLayoutManager mLayoutManager;
    Activity thisActivity=this;
    String companyId;
    ImageButton btn_add_address;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        recyclerView = (RecyclerView)findViewById(R.id.rv);
        recyclerView.setHasFixedSize(true);
        mLayoutManager=new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL);
        mLayoutManager.setOrientation(mLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(new LinearLayoutManager(thisActivity));

        btn_add_address=(ImageButton)findViewById(R.id.btn_add_address);

        btn_add_address.setVisibility(View.VISIBLE);

        btn_add_address.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

               // Toast.makeText(thisActivity,"This feature is under construction",Toast.LENGTH_SHORT).show();
                Intent intent=new Intent(thisActivity,CreateCustomerContact.class);
                startActivity(intent);
                finish();

            }
        });


        if (StaticVariables.isNetworkConnected(thisActivity)) {
            getCustomerList();        }
        else {
            Toast.makeText(thisActivity, getResources().getString(R.string.no_internet_connection), Toast.LENGTH_LONG).show();

        }

        if (Build.VERSION.SDK_INT > 19) {
            getActionBar().setDisplayHomeAsUpEnabled(true);
            getActionBar().setHomeAsUpIndicator(R.drawable.pf);
        } else {
            getActionBar().setHomeButtonEnabled(true);
            getActionBar().setIcon(R.drawable.pf);
        }

        try {
            companyId=getIntent().getExtras().getString("companyid");
            StaticVariables.customerId=companyId;

            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        }
        catch (Exception e){
            e.printStackTrace();
        }

    }

    public void goBack(){
        Intent in = new Intent(thisActivity, com.dev.montherland.CustomerDetails.class);
        startActivity(in);
        finish();

        overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
               goBack();
                return true;
            case R.id.next_button:

            default:
                return true;

        }
    }
    public void onBackPressed() {

        goBack();

    }

    public void displayData(String id){



    }

    public void getCustomerList() {


        PDialog.show(thisActivity);
        StringRequest request = new StringRequest(Request.Method.POST, getResources().getString(R.string.url_motherland) + "company_contact_list.php?",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                         PDialog.hide();
                        Log.v("response", response + "");
                        try {
                            JSONArray ar = new JSONArray(response);


                            persons= Customer_Contact_JSONParser.parserFeed(response);

                                if (persons.get(0).getId().equals("No Data")) {
                                    Toast.makeText(thisActivity, getResources().getString(R.string.no_contact_found), Toast.LENGTH_LONG).show();
                                }
                                else if (persons.get(0).getId().equals("Error")) {
                                    Toast.makeText(thisActivity, getResources().getString(R.string.error_occurred1), Toast.LENGTH_LONG).show();
                                }
                                else if (persons.get(0).getId().equals("")) {
                                    Toast.makeText(thisActivity, getResources().getString(R.string.error_occurred1), Toast.LENGTH_LONG).show();
                                }
                                else {
                                    recyclerView.setAdapter(new CustomerContactAdapter(persons));

                                }

                        } catch (Exception e) {
                            // PDialog.hide();
                        }
                    }
                },

                new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError arg0) {
                        //  PDialog.hide();

                    }
                }) {

            @Override
            protected Map<String, String> getParams() {

                Map<String, String> params = new HashMap<>();
                params.put("email", StaticVariables.database.get(0).getEmail());
                params.put("customer_id", StaticVariables.customerId);
                params.put("password", StaticVariables.database.get(0).getPassword());
                params.put("id", StaticVariables.database.get(0).getId());
                return params;
            }
        };
        AppController.getInstance().addToRequestQueue(request, "data_receive");
        Log.v("request", request + "");
    }


}
