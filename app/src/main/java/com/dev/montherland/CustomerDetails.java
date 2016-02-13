package com.dev.montherland;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.dev.montherland.adapter.CustomerDetailsAdapter;
import com.dev.montherland.adapter.PurchaseOrderDetailadapter;
import com.dev.montherland.model.Customer_Details_Model;
import com.dev.montherland.parsers.Customer_Details_Parser;
import com.dev.montherland.util.PDialog;
import com.dev.montherland.util.StaticVariables;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CustomerDetails extends AppCompatActivity {
    List<Customer_Details_Model> person;
    TextView name,website,mobile;
    String id,email,password,cus_name,cus_mobile,cus_website,data_receive="data_receive";
    Activity thisActivity=this;
    ArrayList<String> cusList=new ArrayList<>();
    com.dev.montherland.adapter.ExpandableListView listview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_details);

        name=(TextView)findViewById(R.id.add1);
        website=(TextView)findViewById(R.id.website);
        mobile=(TextView)findViewById(R.id.phone);
        getContactlist();
        listview = (com.dev.montherland.adapter.ExpandableListView) findViewById(R.id.listView);

        try {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
           id=getIntent().getExtras().getString("id");

        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    public void getContactlist() {
           PDialog.show(thisActivity);
        StringRequest request = new StringRequest(Request.Method.POST, getResources().getString(R.string.url_motherland) + "customer_details.php?",
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response) {
                        PDialog.hide();
                        Log.v("response", response + "");
                        try {
                            PDialog.hide();
                            JSONObject jobj = new JSONObject(response);

                            String basic_details=jobj.getString("basic_details");
                            String orders=jobj.getString("orders");
                            JSONArray ar = new JSONArray(basic_details);
                            for (int i = 0; i < ar.length(); i++) {
                                JSONObject parentObject = ar.getJSONObject(i);
                                name.setText(parentObject.getString("name"));
                                website.setText(parentObject.getString("website"));
                                mobile.setText(parentObject.getString("phone"));
                                Log.v("contactList",parentObject.getString("name"));
                                //Log.d("success", parentObject.getString("success"));
                            }

                            person= Customer_Details_Parser.parserFeed(orders);
                            listview.setAdapter(new CustomerDetailsAdapter(CustomerDetails.this,person));

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
                params.put("customer_id",id);
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
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                super.onBackPressed();
                overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);

                return true;
            case R.id.next_button:

            default:
                return true;

        }
    }
    public void onBackPressed() {

        super.onBackPressed();
        overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);

    }
}
