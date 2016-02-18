package com.dev.montherland;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.MenuItem;
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

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PurchaseOrderDetails extends AppCompatActivity {

    List<GarmentListModel1> persons;
    List<PurchaseOrderDetailsModel> feedlist;
    private RecyclerView recyclerView;
    StaggeredGridLayoutManager mLayoutManager;
    com.dev.montherland.adapter.ExpandableListView listview;

    String id;
    TextView cusName,cusCompany,add1,add2,add3,state,city,zipcode,date,pickup_date,delivery_date,status,order_type,instr;
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
       // recyclerView = (RecyclerView)findViewById(R.id.rv);
        //recyclerView.setHasFixedSize(true);
        try{
            id=getIntent().getExtras().getString("id");
            order_type.setText(StaticVariables.purchaseOrderType);

        }catch (Exception e){
            e.printStackTrace();
        }

        mLayoutManager = new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL);
        mLayoutManager.setOrientation(mLayoutManager.VERTICAL);
        listview = (com.dev.montherland.adapter.ExpandableListView) findViewById(R.id.listView);

        getJobsList();
    }

    public void getJobsList(){
        //Log.v("jobsheet", "jobsheet");

        PDialog.show(PurchaseOrderDetails.this);
        StringRequest request = new StringRequest(Request.Method.POST, getResources().getString(R.string.url_motherland) + "master_purchase_order_details.php",
                new Response.Listener<String>()

                {
                    @Override
                    public void onResponse(String response) {
                        PDialog.hide();
                        Log.v("response",response);
                        try {
                            //Log.v("response",response);
                            JSONObject jobj = new JSONObject(response);
                            String basic_details=jobj.getString("basic_details");
                            String orders=jobj.getString("orders");

                            feedlist= Purchase_OrderDetails_JSONParser.parserFeed(basic_details);
                            cusName.setText(feedlist.get(0).getCustomerContact());
                            cusCompany.setText(feedlist.get(0).getCustomerCompany());
                            date.setText(feedlist.get(0).getDate());
                            StaticVariables.deliveryDate=feedlist.get(0).getDate();
                            add1.setText(feedlist.get(0).getAddressline1());
                            add2.setText(feedlist.get(0).getAddressline2());
                            add3.setText(feedlist.get(0).getAddressline3());
                            state.setText(feedlist.get(0).getState());
                            city.setText(feedlist.get(0).getCity());
                            zipcode.setText(feedlist.get(0).getZipcode());
                            status.setText(feedlist.get(0).getStatus());
                            instr.setText(feedlist.get(0).getInstruction());
                            pickup_date.setText(feedlist.get(0).getExpected_pickup());

                            Log.v("basic details",feedlist.get(0).getAddressline1());
                            persons= Garment_JSONParser1.parserFeed(orders);

                            Log.v("garmentper", persons.get(0).getStyleNumber());

                            listview.setAdapter(new PurchaseOrderDetailadapter(PurchaseOrderDetails.this,persons));


                           Toast.makeText(PurchaseOrderDetails.this, "Please click on any item which to be edited", Toast.LENGTH_SHORT).show();
                            Log.v("garment type", persons.get(0).getGarmentQty());



                        } catch (JSONException e) {
                            PDialog.hide();

                        } catch (Exception e) {
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

                //JSONArray jsonArrayCatId = new JSONArray(Arrays.asList(categoryId));
                //JSONArray jsonArraySubCatid = new JSONArray(Arrays.asList(subCategoryId));
                Map<String, String> params = new HashMap<>();
                params.put("id", "4");
                params.put("email", "test@test.com");
                params.put("password", "e48900ace570708079d07244154aa64a");
                params.put("order_id",StaticVariables.quantityList.get(StaticVariables.count));


                //Log.v("params", database.get(0).getId() + "");
                Log.v("service_id", StaticVariables.password + "");
                Log.v("service_id", id + "");

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
                overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);

                return true;


            default:
                return true;
        }
    }


}
