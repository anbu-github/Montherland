package com.dev.montherland;

import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ExpandableListView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.dev.montherland.adapter.AddressCreateAdapter;
import com.dev.montherland.adapter.CreateOrderAdapter;
import com.dev.montherland.adapter.OrderConfirmDetailsAdapter;
import com.dev.montherland.parsers.Response_JSONParser;
import com.dev.montherland.util.PDialog;
import com.dev.montherland.util.StaticVariables;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.HashMap;
import java.util.Map;

public class OrderConfirmDetails extends AppCompatActivity {
    String address1, address2, address3, city, state,address_id,zipcode;
    TextView tv_address1, tv_address2, tv_address3, tv_city, tv_state,total_item,customer_contact;
    Activity thisActivity = this;
    com.dev.montherland.adapter.ExpandableListView listview;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_confirm_details);

        tv_address1 = (TextView) findViewById(R.id.add1);
        tv_address2 = (TextView) findViewById(R.id.add2);
        tv_address3 = (TextView) findViewById(R.id.add3);
        tv_state = (TextView) findViewById(R.id.state);
        tv_city = (TextView) findViewById(R.id.city);
        customer_contact = (TextView) findViewById(R.id.customer_contact);
        total_item = (TextView) findViewById(R.id.total_item_no);
        listview = (com.dev.montherland.adapter.ExpandableListView) findViewById(R.id.listView);

        try {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Order Details");
            address1 = getIntent().getExtras().getString("address1");
            address2 = getIntent().getExtras().getString("address2");
            address3 = getIntent().getExtras().getString("address3");
            city = getIntent().getExtras().getString("city");
            state = getIntent().getExtras().getString("state");
            address_id = getIntent().getExtras().getString("address_id");
            zipcode = getIntent().getExtras().getString("zipcode");
            //        Log.v("address1",address1);


            tv_address1.setText(address1);
            tv_address2.setText(address2);
            tv_address3.setText(address3);
            customer_contact.setText(StaticVariables.customerName);
            tv_state.setText(state);
            tv_city.setText(zipcode);


        } catch (Exception e) {
            e.printStackTrace();
        }


      listview.setAdapter(new OrderConfirmDetailsAdapter(thisActivity));
        Log.v("no of item", StaticVariables.garmentTypeList.size()+"");
        total_item.setText(StaticVariables.garmentTypeList.size()+"");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu items for use in the action barenu
        getMenuInflater().inflate(R.menu.menu_next, menu);
        MenuItem item = menu.findItem(R.id.next_button);
        item.setTitle("Confirm");
        return true;
    }

    public void confirmOrderRequest() {

        Gson gson = new Gson();
        String jsonGarmentId=gson.toJson(StaticVariables.garmentIdList);
        String jsonQuantity=gson.toJson(StaticVariables.editQuantityList);
        Log.d("garment_type_json", jsonGarmentId);
        Log.d("quantity_json",jsonQuantity);
        Log.d("address_id",address_id);
        Log.d("customer_contact_id",StaticVariables.customerContact);

          PDialog.show(thisActivity);
        StringRequest request = new StringRequest(Request.Method.GET, getResources().getString(R.string.url_motherland) + "purchase_order_submit.php?id=4&email=test@test.com&password=aaa&customer_contact_id="+StaticVariables.customerContact+"&customer_contact_address_id="+address_id+"&garment_type_json="+jsonGarmentId+"&quantity_json="+jsonQuantity,
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response) {
                        PDialog.hide();
                        try {
                            PDialog.hide();
                            JSONArray ar = new JSONArray(response);


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

                Gson gson = new Gson();
                String jsonGarmentId=gson.toJson(StaticVariables.garmentIdList);
                String jsonQuantity=gson.toJson(StaticVariables.garmentTypeList);

                params.put("email", "test@test.com");
                params.put("password", "e48900ace570708079d07244154aa64a");
                params.put("id", "4");
                params.put("customer_contact_address_id", address_id);
                params.put("customer_contact_id", StaticVariables.customerContact);
                params.put("garment_type_json", jsonGarmentId);
                params.put("quantity_json", jsonQuantity);

                Log.d("garment_type_json", jsonGarmentId);
                Log.d("quantity_json",jsonQuantity);
                Log.d("address_id",address_id);
                Log.d("customer_contact_id",StaticVariables.customerContact);

                return params;
            }
        };
        AppController.getInstance().addToRequestQueue(request, "data_receive");
        Log.v("request", request + "");


//        StringRequest request = new StringRequest(Request.Method.GET, getResources().getString(R.string.url_motherland) + "purchase_order_submit.php?id=4&email=test@test.com&" +
//                "password=e48900ace570708079d07244154aa64a&customer_contact_id="+StaticVariables.customerContact+"&customer_contact_address_id="+address_id+"&garment_type_json="+jsonQuantity+"&garment_type_json="+jsonGarmentId+");
//                new Response.Listener<String>()
//                {
//                    @Override
//                    public void onResponse(String response) {
//                        PDialog.hide();
//                        Log.v("response", response + "");
//                        try {
//                            PDialog.hide();
//                            JSONArray ar = new JSONArray(response);
//
//
//                        } catch (JSONException e) {
//                            PDialog.hide();
//                            //Log.d("response", response);
//                            //Log.d("error in json", "l " + e);
//
//                        } catch (Exception e) {
//                            PDialog.hide();
////                            Log.d("json connection", "No internet access" + e);
//                        }
//                    }
//                },
//
//                new Response.ErrorListener() {
//
//                    @Override
//                    public void onErrorResponse(VolleyError arg0) {
//                        PDialog.hide();
//
//                    }
//                }) {
//
//            @Override
//            protected Map<String, String> getParams() {
//
//                Gson gson = new Gson();
//                String jsonGarmentId=gson.toJson(StaticVariables.garmentIdList);
//                String jsonQuantity=gson.toJson(StaticVariables.garmentTypeList);
//
//                Map<String, String> params = new HashMap<>();
//
//                params.put("email", "test@test.com");
//                params.put("password", "e48900ace570708079d07244154aa64a");
//                params.put("id", "4");
//                params.put("customer_contact_address_id", address_id);
//                params.put("customer_contact_id", StaticVariables.customerContact);
//                params.put("garment_type_json", jsonGarmentId);
//                params.put("quantity_json", jsonQuantity);
//
//                Log.d("garment_type_json", jsonGarmentId);
//                Log.d("quantity_json",jsonQuantity);
//                Log.d("address_id",address_id);
//                Log.d("customer_contact_id",StaticVariables.customerContact);
//                return params;
//            }
//        };
//        AppController.getInstance().addToRequestQueue(request, "data_receive");
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
                Toast.makeText(thisActivity, "confirm", Toast.LENGTH_SHORT).show();
                if (StaticVariables.isNetworkConnected(thisActivity)) {
                    confirmOrderRequest();
                }
                else {
                    Toast.makeText(thisActivity,"Please check the network connection",Toast.LENGTH_SHORT).show();
                }



                return true;

            default:
                return true;
        }
    }

    public void onBackPressed() {

        super.onBackPressed();
        overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);

        overridePendingTransition(R.anim.left_right, R.anim.right_left);
    }
}