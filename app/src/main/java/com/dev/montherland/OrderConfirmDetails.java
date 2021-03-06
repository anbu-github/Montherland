package com.dev.montherland;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.dev.montherland.adapter.OrderConfirmDetailsAdapter;
import com.dev.montherland.model.Database;
import com.dev.montherland.util.PDialog;
import com.dev.montherland.util.StaticVariables;
import com.google.gson.Gson;

import org.json.JSONArray;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OrderConfirmDetails extends Activity {

    String address1, address2, address3, city, state, address_id, zipcode;
    TextView tv_address1, tv_address2, tv_address3, tv_city, tv_state, total_item, customer_contact, tv_zipcode;
    TextView str_instr, company, orderType, delivery, total_no, pickup_date,edit_order;
    Activity thisActivity = this;
    com.dev.montherland.adapter.ExpandableListView listview;

    Calendar pickupCalendar = Calendar.getInstance();
    Calendar deliveryCalendar = Calendar.getInstance();

    DateFormat df = new SimpleDateFormat("dd-MM-yyyy HH-mm");
    DateFormat df1 = new SimpleDateFormat("dd-MM-yyyy HH-mm");
    DateFormat df3 = new SimpleDateFormat("dd-MM-yyyy HH:mm");
    DateFormat df2 = new SimpleDateFormat("dd MMM yyyy hh:mm a");

    String myFormat1 = "yyyy-MM-dd HH:mm:ss";
    SimpleDateFormat sdf1 = new SimpleDateFormat(myFormat1);
    SimpleDateFormat sdf2 = new SimpleDateFormat(myFormat1);
    String dTIme,pTime;
    List<Database> database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.confirm_details);

        tv_address1 = (TextView) findViewById(R.id.add1);
        tv_address2 = (TextView) findViewById(R.id.state);
        tv_address3 = (TextView) findViewById(R.id.add3);
        tv_state = (TextView) findViewById(R.id.add2);
        tv_city = (TextView) findViewById(R.id.city);
        tv_zipcode = (TextView) findViewById(R.id.zipcode);
        str_instr = (TextView) findViewById(R.id.instr);
        company = (TextView) findViewById(R.id.item);
        orderType = (TextView) findViewById(R.id.order_type);
        pickup_date = (TextView) findViewById(R.id.style);
        delivery = (TextView) findViewById(R.id.date);
        total_no = (TextView) findViewById(R.id.total_no);
        total_item = (TextView) findViewById(R.id.total_quantity);
        edit_order = (TextView) findViewById(R.id.edit_order);


        customer_contact = (TextView) findViewById(R.id.dc_number);
        // total_item = (TextView) findViewById(R.id.total_item_no);
        listview = (com.dev.montherland.adapter.ExpandableListView) findViewById(R.id.listView);

        try {
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
            if (Build.VERSION.SDK_INT > 19) {
                getActionBar().setDisplayHomeAsUpEnabled(true);
                getActionBar().setHomeAsUpIndicator(R.drawable.pf);
            } else {
                getActionBar().setHomeButtonEnabled(true);
                getActionBar().setIcon(R.drawable.pf);
            }

            getActionBar().setTitle("Create Order");
        } catch (Exception e) {
            e.printStackTrace();
        }

        address1 = getIntent().getExtras().getString("address1");
        address2 = getIntent().getExtras().getString("address2");
        address3 = getIntent().getExtras().getString("address3");
        city = getIntent().getExtras().getString("city");
        state = getIntent().getExtras().getString("state");
        address_id = getIntent().getExtras().getString("address_id");
        zipcode = getIntent().getExtras().getString("zipcode");

        str_instr.setText(StaticVariables.prodcutInstr);
        tv_address1.setText(address1);
        tv_address2.setText(address2);
        tv_address3.setText(address3);
        customer_contact.setText(StaticVariables.customerName);
        tv_state.setText(state);
        tv_city.setText(city);
        tv_zipcode.setText(zipcode);

        pTime=StaticVariables.pickupDate+" "+StaticVariables.pickupTime;
     //   pickup_date.setText(pTime);



        Log.v("expected_pickup", StaticVariables.pickedDateTIme);

        total_no.setText(String.valueOf(StaticVariables.garmentTypeList.size()));
        company.setText(StaticVariables.companyName);
        orderType.setText(StaticVariables.orderType);

        dTIme=StaticVariables.deliveryDate+" "+StaticVariables.deliveryTime;
        Log.v("dtime",dTIme);
      //  delivery.setText(dTIme);


        try {

            Date pdate = df3.parse(pTime);
            //pickupCalendar.setTime(df.parse(pTime));
            String getPdate=df2.format(pdate);
            pickup_date.setText(getPdate);

            Date ddate=df3.parse(dTIme);
            String getDdate=df2.format(ddate);
            delivery.setText(getDdate);

        }catch (Exception e){
            e.printStackTrace();
        }

/*
        Integer total = 0;
        for (int i = 0; i <= StaticVariables.garmentTypeList.size() - 1; i++) {
            total = total + Integer.parseInt(StaticVariables.editQuantityList.get(i));
        }

        total_item.setText(String.valueOf(total));*/

        listview.setAdapter(new OrderConfirmDetailsAdapter(thisActivity));
        Log.v("no of item", StaticVariables.garmentTypeList.size() + "");
        //total_no.setText(StaticVariables.garmentTypeList.size() + "");
    }

    public void save(){
        if (StaticVariables.isNetworkConnected(thisActivity)) {
            confirmOrderRequest();

        } else {
            Toast.makeText(thisActivity, "Please check the network connection", Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu items for use in the action barenu
        getMenuInflater().inflate(R.menu.menu_submit, menu);
        // item.setTitle("Submit");
    //    final Menu m = menu;
    //    final  MenuItem item = menu.findItem(R.id.next_button);
/*
        item.getActionView().setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                m.performIdentifierAction(item.getItemId(), 0);
               // save();
            }
        });*/
        return true;
    }


    public void confirmOrderRequest() {
        PDialog.show(thisActivity);
        //  String url= "http://purplefront.net/motherland_dev/home/purchase_order_submit.php?id=4&email=test@test.com&password=aaa&customer_contact_id=5&customer_contact_address_id=6&garment_type_json="+jsonGarmentId+"&quantity_json="+jsonQuantity+"&wash_type_id_json="+jsonWashTypeId+"&style_number_json="+jsonStyle+"&wash_instructions_type=no&expected_pick_up=2016-02-18 02:15:52&expected_delivery=2016-02-18 02:15:52&order_type_id=2&instructions_json="+jsonInstr+"";
        StringRequest request = new StringRequest(Request.Method.POST, getResources().getString(R.string.url_motherland) + "purchase_order_submit.php",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        PDialog.hide();
                        Log.v("response", response);
                        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(thisActivity);
                        builder.setCancelable(false)
                                .setTitle("Success")
                                .setMessage("Order has been placed successfully")
                                .setNegativeButton("ok", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        StaticVariables.selectAddress="";
                                        Intent intent = new Intent(thisActivity, NavigataionActivity.class);
                                        intent.putExtra("redirection","Orders");
                                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                        startActivity(intent);
                                        finish();
                                        StaticVariables.selectAddress=null;
                                        overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
                                    }
                                });
                        builder.show();


                        try {
                            PDialog.hide();
                            JSONArray ar = new JSONArray(response);


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

                //StaticVariables.customerContact=StaticVariables.contactId;
             try {

                 pickupCalendar.setTime(df.parse(pTime));
             deliveryCalendar.setTime(df1.parse(dTIme));

             StaticVariables.pickedDateTIme=sdf1.format(pickupCalendar.getTime());
             StaticVariables.deliveryDateTIme=sdf2.format(deliveryCalendar.getTime());


            }
             catch (Exception e){
             e.printStackTrace();
             }


                String expected_pickup = StaticVariables.pickedDateTIme;
                String expected_delivery = StaticVariables.deliveryDateTIme;
                String jsonStyle = gson.toJson(StaticVariables.garmentStyle);
                String jsonWashTypeId = gson.toJson(StaticVariables.garmentWashTypeId);
                String jsonGarmentId = gson.toJson(StaticVariables.garmentIdList);
                String jsonQuantity = gson.toJson(StaticVariables.editQuantityList);
                String jsonInstr = gson.toJson(StaticVariables.garmentInstr);

                Log.d("jsonInstr", jsonInstr);
                Log.d("expected_pickup", expected_pickup);
                Log.d("expected_delivery", expected_delivery);
                Log.d("garment_type_json", jsonGarmentId);
                Log.v("quantity_json", jsonQuantity);
                Log.d("jsonStyle", jsonStyle);
                Log.d("jsonWashTypeId", jsonWashTypeId);
                Log.d("contact", StaticVariables.customerContact);
                Log.d("address_id", address_id);


                params.put("id", StaticVariables.database.get(0).getId());
                params.put("email", StaticVariables.database.get(0).getEmail());
                params.put("password", StaticVariables.database.get(0).getPassword());
                params.put("customer_contact_id", StaticVariables.customerContact);
                params.put("customer_contact_address_id", address_id);
                params.put("garment_type_json", jsonGarmentId);
                params.put("quantity_json", jsonQuantity);
                params.put("wash_type_id_json", jsonWashTypeId);
                params.put("style_number_json", jsonStyle);
                params.put("wash_instructions_type", StaticVariables.prodcutInstr);
                params.put("expected_pick_up", expected_pickup);
                params.put("expected_delivery", expected_delivery);
                params.put("order_type_id", StaticVariables.orderTypeId);
                params.put("instructions_json", jsonInstr);

                return params;
            }
        };
        AppController.getInstance().addToRequestQueue(request, "data_receive3");
        Log.v("request", request + "");

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
                    save();


                return true;

            default:
                return true;
        }
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);

    }
}