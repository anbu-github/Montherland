package com.dev.montherland;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
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

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PurchaseOrderDetails extends Activity {

    List<GarmentListModel1> persons;
    List<PurchaseOrderDetailsModel> feedlist;

    StaggeredGridLayoutManager mLayoutManager;
    com.dev.montherland.adapter.ExpandableListView listview;

    TextView cusName,cusCompany,add1,add2,add3,state,city,zipcode,date,pickup_date,status,order_type,instr,order_id;
    Activity thisActivity=this;
    ScrollView scrollView;
    LinearLayout layout;
    ImageView edit_order,edit_instruction,edit_address;
    String menuTitle="";
    LinearLayout order_history_layout;
    String mode="",order_status;
    Date created_date;
    Button cancelOrder,orderReceipt,orderDelivery;
    String orders;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.order_details);

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        if (Build.VERSION.SDK_INT > 19) {
            getActionBar().setDisplayHomeAsUpEnabled(true);
            getActionBar().setHomeAsUpIndicator(R.drawable.pf);
        }
        else {
            getActionBar().setHomeButtonEnabled(true);
            getActionBar().setIcon(R.drawable.pf);
        }

        scrollView=(ScrollView)findViewById(R.id.scrollview);
        scrollView.setFocusable(true);
        cusName=(TextView)findViewById(R.id.dc_number);
        cusCompany=(TextView)findViewById(R.id.item);
        date=(TextView)findViewById(R.id.date);
        add1=(TextView)findViewById(R.id.add1);
        add2=(TextView)findViewById(R.id.state);
        add3=(TextView)findViewById(R.id.add3);
        state=(TextView)findViewById(R.id.add2);
        city=(TextView)findViewById(R.id.city);
        status=(TextView)findViewById(R.id.status);
        pickup_date=(TextView)findViewById(R.id.style);
        order_type=(TextView)findViewById(R.id.order_type);
        instr=(TextView)findViewById(R.id.instr);
        zipcode=(TextView)findViewById(R.id.zipcode);
        orderReceipt=(Button) findViewById(R.id.order_receipt);
        orderDelivery=(Button) findViewById(R.id.order_delivery);
        cancelOrder=(Button) findViewById(R.id.cancel_order);
        edit_order = (ImageView) findViewById(R.id.edit_order);
        edit_address = (ImageView) findViewById(R.id.edit_address);
        order_id = (TextView) findViewById(R.id.order_id);
        edit_instruction = (ImageView) findViewById(R.id.edit_instruction);
        layout=(LinearLayout)findViewById(R.id.layout);
        order_history_layout=(LinearLayout)findViewById(R.id.order_history_layout);


        layout.setVisibility(View.INVISIBLE);
        StaticVariables.mode="";
        StaticVariables.state="";
        try {
            PDialog.show(thisActivity);
            mode=getIntent().getExtras().getString("intent_from");
            StaticVariables.mode=mode;
        }catch (Exception e){
            e.printStackTrace();
        }

        if (StaticVariables.isNetworkConnected(thisActivity)) {

            getOrderDetails();
        } else {
            PDialog.hide();
            Toast.makeText(thisActivity, getResources().getString(R.string.no_internet_access), Toast.LENGTH_LONG).show();
        }


        orderReceipt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent in=new Intent(thisActivity,OrderReceipts.class);
                in.putExtra("orders_list",orders);
                startActivity(in);
                overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);

            }
        });

        orderDelivery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in=new Intent(thisActivity,OrderReceipts.class);
                in.putExtra("orders_list",orders);
                in.putExtra("intent_from","order_delivery");
                startActivity(in);
                overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
            }
        });

        cancelOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (status.getText().equals("Entered")){
                    android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(thisActivity);
                    builder.setCancelable(false)
                            .setTitle("Confirm")
                            .setMessage("Are you sure want to cancel the order?")
                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    orderCancel();
                                }
                            }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    });
                    builder.show();
                }
                else {
                    Intent fbIntent=new Intent(thisActivity,FeedbackActivity.class);
                    startActivity(fbIntent);
                    overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);

                }
            }
        });


        mLayoutManager = new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL);
        mLayoutManager.scrollToPositionWithOffset(0, 20);
        mLayoutManager.setOrientation(mLayoutManager.VERTICAL);
        listview = (com.dev.montherland.adapter.ExpandableListView) findViewById(R.id.listView);
        listview.setFocusable(false);

        try {
            if (getIntent().getExtras().get("order_history").equals("order_history")){
                menuTitle="order_history";
                StaticVariables.mode="order_history";
                getActionBar().setTitle("Order History");
                order_history_layout.setVisibility(View.VISIBLE);
//                StaticVariables.mode="item_history";
                edit_address.setImageResource(R.drawable.order_history);
                edit_address.getLayoutParams().height=60;
                edit_address.getLayoutParams().width=60;
                edit_instruction.getLayoutParams().height=60;
                edit_instruction.getLayoutParams().width=60;
                edit_order.getLayoutParams().height=60;
                edit_order.getLayoutParams().width=60;

                edit_address.requestLayout();
                edit_order.requestLayout();
                edit_instruction.requestLayout();
                edit_instruction.setImageResource(R.drawable.order_history);

                edit_order.setImageResource(R.drawable.order_history);
            }
        }catch (Exception e){

            e.printStackTrace();
        }

        edit_instruction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (menuTitle.equals("order_history")) {

                    Intent in = new Intent(thisActivity, History_Orders_Details.class);
                    StaticVariables.order_id = feedlist.get(0).getId();



                    try {

                        if (mode.contains("customer_order")) {
                            in.putExtra("intent_to", "customer_order");
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    in.putExtra("intent_from", "instr_history");
                    StaticVariables.mode = "instr_history";
                    startActivity(in);
                    finish();
                    overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);

                } else {
                    //       Toast.makeText(thisActivity,"This feature is under construction",Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(thisActivity, InstructionActivity.class);
                    if (mode.contains("customer_order")) {
                        intent.putExtra("intent_from", "customer_order");
                    }
                    intent.putExtra("change_instr", "change instr");
                    intent.putExtra("instr", String.valueOf(instr.getText()));
                    intent.putExtra("order_id", feedlist.get(0).getId());
                    startActivity(intent);
                    overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);

                }
            }
        });

        edit_address.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (menuTitle.equals("order_history")) {
                    Intent in = new Intent(thisActivity, History_Orders_Details.class);
                    StaticVariables.order_id = feedlist.get(0).getId();
                    in.putExtra("intent_from","address_history");
                    try {
                        if (mode.contains("customer_order")) {
                            in.putExtra("intent_to", "customer_order");
                        }
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                    StaticVariables.mode="address_history";
                    startActivity(in);
                    finish();
                    overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);

                } else {
                    StaticVariables.selectAddress = "order details";
                    Intent intent = new Intent(thisActivity, SelectAddress.class);
                    intent.putExtra("change_address", "change address");
                    if (mode.contains("customer_order")){
                        intent.putExtra("intent_from","customer_order");
                    }
                    startActivity(intent);
                    finish();
                    overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
                }
            }
        });

        edit_order.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (menuTitle.equals("order_history")) {

                    Intent in = new Intent(thisActivity, History_Orders_Details.class);
                    StaticVariables.order_id = feedlist.get(0).getId();
                    in.putExtra("intent_from", "order_details");
                    try {
                        if (mode.contains("customer_order")) {
                            in.putExtra("intent_to", "customer_order");
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    StaticVariables.mode = "order_details_history";
                    startActivity(in);
                    finish();
                    overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);

                } else {

                    Intent orderIntent = new Intent(thisActivity, CreatePurchaseOrder.class);
                    Bundle args = new Bundle();
                    //  orderIntent.putExtra("edit_order",ArrayList<PurchaseOrderDetaFilsModel>);
                    args.putSerializable("edit_order", (Serializable) feedlist);
                    orderIntent.putExtra("BUNDLE", args);

                    orderIntent.putExtra("order_details", "order_details");
                    orderIntent.putExtra("order_id", feedlist.get(0).getId());
                    if (mode.contains("customer_order")) {
                        orderIntent.putExtra("intent_from", "customer_order");
                    }
                    startActivity(orderIntent);
                    finish();
                    overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
                }

            }
        });
    }

    public void orderCancel() {
        PDialog.show(thisActivity);
        StringRequest request = new StringRequest(Request.Method.POST, getResources().getString(R.string.url_motherland) + "order_cancel.php?",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        PDialog.hide();
                        Log.v("response", response + "");
                        try {
                            JSONObject ar = new JSONObject(response);

                            if (ar.get("id").equals("Success")){
                                android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(thisActivity);
                                builder.setCancelable(false)
                                        .setTitle("Success")
                                        .setMessage("Order has been cancelled")
                                        .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {

                                                Intent intent = new Intent(thisActivity, NavigataionActivity.class);
                                                intent.putExtra("redirection","Orders");
                                                startActivity(intent);
                                                finish();
                                                overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
                                            }
                                        });
                                builder.show();
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
                params.put("order_id", StaticVariables.order_id);
                params.put("password", StaticVariables.database.get(0).getPassword());
                params.put("id", StaticVariables.database.get(0).getId());
                params.put("status_id", "9");
                return params;
            }
        };
        AppController.getInstance().addToRequestQueue(request, "data_receive");
        Log.v("request", request + "");
    }


    private void update_display(String response) {
        try {
            JSONObject jobj = new JSONObject(response);
            String basic_details=jobj.getString("basic_details");
             orders=jobj.getString("orders");

            feedlist= Purchase_OrderDetails_JSONParser.parserFeed(basic_details);

            if(feedlist != null) {
                cusName.setText(feedlist.get(0).getCustomerContact());
                cusCompany.setText(feedlist.get(0).getCustomerCompany());
                date.setText(feedlist.get(0).getExpected_delivery());
                StaticVariables.deliveryDate = feedlist.get(0).getDate();
                add1.setText(feedlist.get(0).getAddressline1());

                if (feedlist.get(0).getAddressline2().isEmpty()||feedlist.get(0).getAddressline2().equals("")){
                    add2.setVisibility(View.GONE);
                }
                else {
                    add2.setText(feedlist.get(0).getAddressline2());

                }
                if (feedlist.get(0).getAddressline3().isEmpty()||feedlist.get(0).getAddressline3().equals("")){
                    add3.setVisibility(View.GONE);
                }
                else {
                    add3.setText(feedlist.get(0).getAddressline3());
                }

                state.setText(feedlist.get(0).getState());
                city.setText(feedlist.get(0).getCity());
                zipcode.setText(feedlist.get(0).getZipcode());
                status.setText(feedlist.get(0).getStatus());

                order_status=feedlist.get(0).getStatus();


                if (order_status.equals("Entered")){
                    cancelOrder.setVisibility(View.VISIBLE);
                }
                if (order_status.equals("Entered")||order_status.equals("In Progress")){
                  //  cancelOrder.setVisibility(View.VISIBLE);
                    orderDelivery.setVisibility(View.VISIBLE);
                    orderReceipt.setVisibility(View.VISIBLE);
                }

                else if (order_status.equals("Completed")){
                    cancelOrder.setVisibility(View.VISIBLE);
                    cancelOrder.setText("Feedback");

                }
                instr.setText(feedlist.get(0).getInstruction());
                pickup_date.setText(feedlist.get(0).getExpected_pickup());
                order_type.setText(feedlist.get(0).getOrder_type());
                order_id.setText(feedlist.get(0).getId());

                StaticVariables.customerId=feedlist.get(0).getCustomerId();

                StaticVariables.address_id=feedlist.get(0).getAddressid();
                StaticVariables.pickupDefaultDate=feedlist.get(0).getPickupdate();
                StaticVariables.deliveryDefultDate=feedlist.get(0).getDeliverydate();

                try {

                    if (menuTitle.contains("order_history") || getIntent().getExtras().getString("intent_from").contains("customer_order")) {
                        edit_address.setVisibility(View.VISIBLE);
                        edit_instruction.setVisibility(View.VISIBLE);
                        edit_order.setVisibility(View.VISIBLE);

                        cancelOrder.setVisibility(View.GONE);
                        orderDelivery.setVisibility(View.GONE);
                        orderReceipt.setVisibility(View.GONE);
                    }
                }catch (Exception e){
                    edit_address.setVisibility(View.VISIBLE);
                    edit_instruction.setVisibility(View.VISIBLE);
                    edit_order.setVisibility(View.VISIBLE);
                }

                if (feedlist.get(0).getStatus().equals("completed")&&(!menuTitle.contains("order_history"))){
                    edit_address.setVisibility(View.GONE);
                    edit_instruction.setVisibility(View.GONE);
                    edit_order.setVisibility(View.GONE);

                }
                else if (feedlist.get(0).getStatus().equals("Order Cancelled")||feedlist.get(0).getStatus().equals("Completed")){
                    edit_address.setVisibility(View.GONE);
                    edit_instruction.setVisibility(View.GONE);
                    edit_order.setVisibility(View.GONE);

                    StaticVariables.state="order_cancelled";
                }


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
               // PDialog.hide();
                Toast.makeText(PurchaseOrderDetails.this,getResources().getString(R.string.error_occurred1),Toast.LENGTH_LONG).show();
            }


            Calendar deliveryItemCalendar = Calendar.getInstance();
            Calendar deliveryCalendar = Calendar.getInstance();

            DateFormat ddf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            DateFormat ddf1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

            deliveryCalendar.setTime(ddf1.parse(feedlist.get(0).getDeliverydate()));

            created_date=ddf.parse(feedlist.get(0).getCreated_date());

            Date current_date=new Date();
            Log.v("currentdate",current_date.getDate()+"created"+created_date.getDate());


           /* if (created_date.getDate()!=current_date.getDate()){
                edit_address.setVisibility(View.INVISIBLE);
                edit_instruction.setVisibility(View.INVISIBLE);
                edit_order.setVisibility(View.INVISIBLE);
                StaticVariables.isEditable=false;

            }else {
               // Toast.makeText(thisActivity,"greater date",Toast.LENGTH_SHORT).show();
             StaticVariables.isEditable=true;
            }*/

            if (menuTitle.contains("order_history")){
                edit_address.setVisibility(View.VISIBLE);
                edit_instruction.setVisibility(View.VISIBLE);
                edit_order.setVisibility(View.VISIBLE);

            }

            persons= Garment_JSONParser1.parserFeed(orders);

            Integer pos=-1;
            for (int i=0;i<=persons.size();i++){
                try{

                    deliveryItemCalendar.setTime(ddf1.parse(persons.get(i).getExpectedDeliveryDate()));
                    Log.v("dates", deliveryCalendar.getTime() + "and"+deliveryItemCalendar.getTime());

                    if (deliveryCalendar.after(deliveryItemCalendar)){
                        Log.v("status",pos+"");
                    }
                    else {
                        pos=i;
                        deliveryCalendar.setTime(deliveryItemCalendar.getTime());
                        Log.v("status",pos+"i");
                    }

                }
                catch (Exception e){
                    e.printStackTrace();
                }

            }
            StaticVariables.delDate="";
            if (pos!=-1){
                date.setText(persons.get(pos).getExpectedDelivery());
                StaticVariables.delDate=persons.get(pos).getExpectedDeliveryDate();

                Log.v("deliverydate",StaticVariables.delDate);
            }

            if(persons != null) {
                Log.v("garmentper", persons.get(0).getStyleNumber());
               /* if (mode.contains("customer_order")){
                    StaticVariables.mode1="customer_order";
                }*/
                listview.setAdapter(new PurchaseOrderDetailadapter(PurchaseOrderDetails.this, persons));


            } else {
                Toast.makeText(PurchaseOrderDetails.this,getResources().getString(R.string.error_occurred1),Toast.LENGTH_LONG).show();
            }

            // Toast.makeText(PurchaseOrderDetails.this, "Please click on any item which to be edited", Toast.LENGTH_SHORT).show();
            Log.v("garment type", persons.get(0).getGarmentQty());

        } catch (JSONException e) {
           // PDialog.hide();

        } catch (Exception ignored) {
        }



    }


    public void getOrderDetails(){

        StringRequest request = new StringRequest(Request.Method.POST, getResources().getString(R.string.url_motherland) + "master_purchase_order_details.php",
                new Response.Listener<String>()

                {
                    @Override
                    public void onResponse(String response) {

                        Log.v("response", response);
                        update_display(response);
                        layout.setVisibility(View.VISIBLE);

                    }

                },

                new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError arg0) {
                       // PDialog.hide();
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
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu items for use in the action barenu

        if (!(menuTitle.contains("order_history"))){
            getMenuInflater().inflate(R.menu.menu_order_history, menu);

        }

        return true;
    }

    public void goBack(){
         PDialog.hide();
        if (menuTitle.equals("order_history")){
            Intent in=new Intent(thisActivity,PurchaseOrderDetails.class);

            try {
                if (mode.contains("customer_order")) {
                    in.putExtra("intent_from", "customer_order");
                }
            }catch (Exception e){
                e.printStackTrace();
            }
            startActivity(in);
            overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
            finish();

        }else if (mode.contains("customer_order")){
            Intent in = new Intent(thisActivity, Customer_details_orders.class);
            startActivity(in);
            finish();
            overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
        }
        else {
            Intent in = new Intent(thisActivity, NavigataionActivity.class);
            in.putExtra("redirection","Orders");
            startActivity(in);
            finish();
            overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);

        }
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
               goBack();
                return true;
            case R.id.order_history:
                menuTitle="order_history";

                Intent in=new Intent(thisActivity,PurchaseOrderDetails.class);
                in.putExtra("order_history","order_history");
                if (mode.contains("customer_order")){
                    in.putExtra("intent_from","customer_order");
                }
                startActivity(in);
                finish();
                overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);

                return true;
            default:
                return true;
        }
    }

    @Override
    public void onBackPressed() {
      goBack();
    }
}
