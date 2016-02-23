package com.dev.montherland;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PurchaseOrderDetails extends AppCompatActivity {

    List<GarmentListModel1> persons;
    List<PurchaseOrderDetailsModel> feedlist;

    StaggeredGridLayoutManager mLayoutManager;
    com.dev.montherland.adapter.ExpandableListView listview;

    TextView cusName,cusCompany,add1,add2,add3,state,city,zipcode,date,pickup_date,edit_order,status,order_type,instr;
    Activity thisActivity=this;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.order_details);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

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


        edit_order.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent orderIntent=new Intent(thisActivity,CreatePurchaseOrder.class);
                Bundle args = new Bundle();
              //  orderIntent.putExtra("edit_order",ArrayList<PurchaseOrderDetaFilsModel>);
                args.putSerializable("edit_order", (Serializable)feedlist);
                orderIntent.putExtra("BUNDLE",args);
                orderIntent.putExtra("order_details","order_details");
                orderIntent.putExtra("order_id",feedlist.get(0).getId());

                startActivity(orderIntent);

            }
        });


        mLayoutManager = new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL);
        mLayoutManager.setOrientation(mLayoutManager.VERTICAL);
        listview = (com.dev.montherland.adapter.ExpandableListView) findViewById(R.id.listView);

        getOrderDetails();
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
                date.setText(feedlist.get(0).getDate());
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
                Log.v("basic details", feedlist.get(0).getAddressline1());
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

            Toast.makeText(PurchaseOrderDetails.this, "Please click on any item which to be edited", Toast.LENGTH_SHORT).show();
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
