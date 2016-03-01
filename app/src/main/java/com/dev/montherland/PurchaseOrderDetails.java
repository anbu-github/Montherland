package com.dev.montherland;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.dev.montherland.adapter.PurchaseOrderDetailadapter;
import com.dev.montherland.model.GarmentListModel1;
import com.dev.montherland.model.PurchaseOrderDetailsModel;
import com.dev.montherland.parsers.Garment_JSONParser1;
import com.dev.montherland.parsers.Purchase_OrderDetails_JSONParser;
import com.dev.montherland.util.PDialog;
import com.dev.montherland.util.StaticVariables;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PurchaseOrderDetails extends Activity {

    List<GarmentListModel1> persons;
    List<PurchaseOrderDetailsModel> feedlist;

    StaggeredGridLayoutManager mLayoutManager;
    com.dev.montherland.adapter.ExpandableListView listview;

    TextView cusName,cusCompany,edit_instruction,add1,add2,add3,state,city,zipcode,date,pickup_date,edit_order,status,order_type,instr,edit_address,order_id;
    Activity thisActivity=this;
    String status_id,deliveryDate,pickupdate, customerId,orderType;
    LinearLayout layout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.order_details);

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        if (Build.VERSION.SDK_INT > 19) {
            getActionBar().setDisplayHomeAsUpEnabled(true);
            getActionBar().setHomeAsUpIndicator(R.drawable.pf);
        } else {
            getActionBar().setHomeButtonEnabled(true);
            getActionBar().setIcon(R.drawable.pf);
        }

        cusName=(TextView)findViewById(R.id.quantity);
        cusCompany=(TextView)findViewById(R.id.item);
        date=(TextView)findViewById(R.id.date);
        add1=(TextView)findViewById(R.id.add1);
        add2=(TextView)findViewById(R.id.add2);
        add3=(TextView)findViewById(R.id.add3);
        state=(TextView)findViewById(R.id.state);
        city=(TextView)findViewById(R.id.city);
        status=(TextView)findViewById(R.id.status);
        pickup_date=(TextView)findViewById(R.id.style);
        order_type=(TextView)findViewById(R.id.order_type);
        instr=(TextView)findViewById(R.id.instr);
        zipcode=(TextView)findViewById(R.id.zipcode);
        edit_order = (TextView) findViewById(R.id.edit_order);
        edit_address = (TextView) findViewById(R.id.edit_address);
        order_id = (TextView) findViewById(R.id.order_id);
        edit_instruction = (TextView) findViewById(R.id.edit_instruction);
        layout=(LinearLayout)findViewById(R.id.layout);

        edit_instruction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              Toast.makeText(thisActivity,"This feature is under construction",Toast.LENGTH_SHORT).show();
            }
        });
        edit_address.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(thisActivity,SelectAddress.class);
                intent.putExtra("change_address","change_address");
                startActivity(intent);
                finish();
            }
        });

        edit_order.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent orderIntent=new Intent(thisActivity,CreatePurchaseOrder.class);
                Bundle args = new Bundle();
              //  orderIntent.putExtra("edit_order",ArrayList<PurchaseOrderDetaFilsModel>);
                args.putSerializable("edit_order", (Serializable) feedlist);
                orderIntent.putExtra("BUNDLE", args);
                orderIntent.putExtra("order_details", "order_details");


               /* orderIntent.putExtra("order_type", orderType);
                orderIntent.putExtra("customerId", customerId);
                orderIntent.putExtra("pickup_date", pickupdate);
                orderIntent.putExtra("delivery_date",deliveryDate);
                orderIntent.putExtra("status_id",status_id);*/

                orderIntent.putExtra("order_id",feedlist.get(0).getId());

                startActivity(orderIntent);
                finish();
            }
        });


        mLayoutManager = new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL);
        mLayoutManager.setOrientation(mLayoutManager.VERTICAL);
        listview = (com.dev.montherland.adapter.ExpandableListView) findViewById(R.id.listView);

        if (StaticVariables.isNetworkConnected(thisActivity)) {
            getOrderDetails();
        } else {
            Toast.makeText(thisActivity, getResources().getString(R.string.no_internet_access), Toast.LENGTH_LONG).show();
        }

    }

    public void getEditOrderDetails() {

        PDialog.show(thisActivity);
        StringRequest request = new StringRequest(Request.Method.POST, getResources().getString(R.string.url_motherland) + "edit_order_details_view.php?",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //
                        //
                        // PDialog.hide();
                        Log.v("response", response + "");
                        try {
                            PDialog.hide();
                            JSONArray ar = new JSONArray(response);
                            for (int i = 0; i < ar.length(); i++) {
                                JSONObject parentObject = ar.getJSONObject(i);
                                if (!parentObject.getString("status_id").equals("NO Data")) {
                                    //customerList.setSelection(parentObject.getString("customer_contact_id"));
                                    status_id=parentObject.getString("status_id");
                                    deliveryDate=parentObject.getString("expected_delivery_date");
                                    pickupdate=parentObject.getString("expected_pick_up_date");
                                    orderType=parentObject.getString("order_type_id");
                                    Log.v("order_typeId", (parentObject.getString("order_type_id")));
                                    customerId =parentObject.getString("customer_id");

                                 //   StaticVariables.pickedDateTIme=String.valueOf(pickupDate.getText());
                                   // StaticVariables.deliveryDateTIme=String.valueOf(deliveryDate.getText());

                                }
                            }
                            // customerList.setAdapter(dataAdapter);


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
                params.put("email", StaticVariables.database.get(0).getEmail());
                params.put("id", StaticVariables.database.get(0).getId());
                params.put("password", StaticVariables.database.get(0).getPassword());
                params.put("order_id", feedlist.get(0).getId());
                return params;
            }
        };
        AppController.getInstance().addToRequestQueue(request, "data_receive2");
        Log.v("request", request + "");
    }


    private void update_display(String response) {
        try {
            JSONObject jobj = new JSONObject(response);
            String basic_details=jobj.getString("basic_details");
            String orders=jobj.getString("orders");

            feedlist= Purchase_OrderDetails_JSONParser.parserFeed(basic_details);

            if(feedlist != null) {
                cusName.setText(feedlist.get(0).getCustomerContact());
                cusCompany.setText(feedlist.get(0).getCustomerCompany());
                date.setText(feedlist.get(0).getExpected_delivery());
                StaticVariables.deliveryDate = feedlist.get(0).getDate();
                add1.setText(feedlist.get(0).getAddressline1());
                add2.setText(feedlist.get(0).getAddressline2());
                add3.setText(feedlist.get(0).getAddressline3());
                state.setText(feedlist.get(0).getState());
                city.setText(feedlist.get(0).getCity());
                zipcode.setText(feedlist.get(0).getZipcode());
                status.setText(feedlist.get(0).getStatus());
                instr.setText(feedlist.get(0).getInstruction());
                pickup_date.setText(feedlist.get(0).getExpected_pickup());
                order_type.setText(feedlist.get(0).getOrder_type());
                order_id.setText(feedlist.get(0).getId());

                if (feedlist.get(0).getExpected_delivery().contains("null")){
                    date.setText("");

                }else {
                    date.setText(feedlist.get(0).getExpected_delivery());

                }

                if (feedlist.get(0).getExpected_pickup().contains("null")){
                    pickup_date.setText("");

                }else {
                    pickup_date.setText(feedlist.get(0).getExpected_pickup());

                }

                StaticVariables.customerContact=feedlist.get(0).getCustomerId();
                //StaticVariables.customerContact="5";
                Log.v("customer_id", StaticVariables.customerContact);
            } else {
             Toast.makeText(PurchaseOrderDetails.this,getResources().getString(R.string.error_occurred1),Toast.LENGTH_LONG).show();
            }
            persons= Garment_JSONParser1.parserFeed(orders);

            if(persons != null) {
                Log.v("garmentper", persons.get(0).getStyleNumber());
                listview.setAdapter(new PurchaseOrderDetailadapter(PurchaseOrderDetails.this, persons));
            } else {
                Toast.makeText(PurchaseOrderDetails.this,getResources().getString(R.string.error_occurred1),Toast.LENGTH_LONG).show();
            }

           // Toast.makeText(PurchaseOrderDetails.this, "Please click on any item which to be edited", Toast.LENGTH_SHORT).show();
            Log.v("garment type", persons.get(0).getGarmentQty());



        } catch (JSONException e) {
            PDialog.hide();

        } catch (Exception ignored) {
        }
    }

    public void getOrderDetails(){

        PDialog.show(PurchaseOrderDetails.this);
        StringRequest request = new StringRequest(Request.Method.POST, getResources().getString(R.string.url_motherland) + "master_purchase_order_details.php",
                new Response.Listener<String>()

                {
                    @Override
                    public void onResponse(String response) {
                        PDialog.hide();
                        Log.v("response", response);
                        layout.setVisibility(View.VISIBLE);
                        update_display(response);
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
                params.put("email", StaticVariables.database.get(0).getId());
                params.put("password", StaticVariables.database.get(0).getEmail());
                params.put("order_id", StaticVariables.order_id);
                return params;
            }
        };
        AppController.getInstance().addToRequestQueue(request, "data_receive");
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                super.onBackPressed();
                PurchaseOrderDetails.this.finish();
                overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
                return true;
            default:
                return true;
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        PurchaseOrderDetails.this.finish();
        overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
    }
}
