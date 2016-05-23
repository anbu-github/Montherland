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
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.dev.montherland.adapter.CustomerDetailsAdapter;
import com.dev.montherland.model.Customer_Details_Model;
import com.dev.montherland.parsers.Customer_Details_Parser;
import com.dev.montherland.util.PDialog;
import com.dev.montherland.util.StaticVariables;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CustomerDetails extends Activity {

    List<Customer_Details_Model> person;
    TextView name,website,mobile;
    String id,data_receive="data_receive",orders;
    Activity thisActivity=this;
    StaggeredGridLayoutManager mLayoutManager;
    private RecyclerView recyclerView;
    RelativeLayout re_layout;
    LinearLayout edit_customer_layout;
    String companyId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_details);
        PDialog.show(thisActivity);
        recyclerView = (RecyclerView) findViewById(R.id.rv);
        name=(TextView)findViewById(R.id.add1);
        website=(TextView)findViewById(R.id.website);
        mobile=(TextView)findViewById(R.id.phone);
        re_layout=(RelativeLayout) findViewById(R.id.re_layout);
      //  re_layout=(RelativeLayout)findViewById(R.id.edit_customer_layout);
        re_layout.setVisibility(View.INVISIBLE);


        recyclerView.setHasFixedSize(true);
        mLayoutManager = new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL);
        mLayoutManager.setOrientation(mLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(new LinearLayoutManager(thisActivity));
        try {
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
            if (Build.VERSION.SDK_INT > 19) {
                getActionBar().setDisplayHomeAsUpEnabled(true);
                getActionBar().setHomeAsUpIndicator(R.drawable.pf);
            } else {
                getActionBar().setHomeButtonEnabled(true);
                getActionBar().setIcon(R.drawable.pf);
            }
            id=getIntent().getExtras().getString("id");
        }
        catch (Exception e){
            e.printStackTrace();
        }

        if (StaticVariables.isNetworkConnected(thisActivity)) {

            getContactlist();
        }else {
            Toast.makeText(thisActivity,getResources().getString(R.string.no_internet_connection),Toast.LENGTH_SHORT).show();
        }

    }

    public void orders(View view){
        Intent in=new Intent(thisActivity,Customer_details_orders.class);
        in.putExtra("orders",orders);
        startActivity(in);
        finish();
        overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
    }

    public void contacts(View view){
        Intent in=new Intent(thisActivity,Customer_contacts_list.class);
        in.putExtra("companyid",companyId);
        startActivity(in);
        finish();
        overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
    }

    public void customerAddress(View view){
        StaticVariables.selectAddress="";
        Intent in=new Intent(thisActivity,SelectAddress.class);
        in.putExtra("customer_address","customer_address");
        startActivity(in);
        overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);}

    public void edit_contact(View view){
        Intent in=new Intent(thisActivity,Create_Customer.class);
        in.putExtra("name",name.getText());
        in.putExtra("phone",mobile.getText());
        in.putExtra("email",website.getText());
        in.putExtra("intent_from", "edit_customer");
        in.putExtra("id",id);
        startActivity(in);
        finish();
        overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
    }

    public void getContactlist() {
            StringRequest request = new StringRequest(Request.Method.POST, getResources().getString(R.string.url_motherland) + "customer_details.php",
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response) {
                        Log.v("response", response);
                        try {

                            PDialog.hide();


                            JSONObject jobj = new JSONObject(response);

                            String basic_details=jobj.getString("basic_details");
                             orders=jobj.getString("orders");

                            JSONArray ar = new JSONArray(basic_details);
                            for (int i = 0; i < ar.length(); i++) {
                                JSONObject parentObject = ar.getJSONObject(i);
                                name.setText(parentObject.getString("name"));
                                website.setText(parentObject.getString("website"));
                                mobile.setText(parentObject.getString("phone"));
                                companyId=parentObject.getString("id");
                                Log.v("contactList",parentObject.getString("name"));
                                StaticVariables.customerId=companyId;
                            }

                            person= Customer_Details_Parser.parserFeed(orders);
                            //listview.setAdapter(new CustomerDetailsAdapter(CustomerDetails.this,person));
                            recyclerView.setAdapter(new CustomerDetailsAdapter(thisActivity,person));

                            re_layout.setVisibility(View.VISIBLE);

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
                params.put("customer_id",StaticVariables.cus_id);
                params.put("password", StaticVariables.database.get(0).getPassword());
                params.put("id", StaticVariables.database.get(0).getId());
                return params;
            }
        };
        AppController.getInstance().addToRequestQueue(request, data_receive);
    }

    public void onBack(){

        Intent in=new Intent(thisActivity,NavigataionActivity.class);
        in.putExtra("redirection","Customers");
        startActivity(in);
        finish();

        overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                PDialog.hide();

                onBack();
                return true;
            case R.id.next_button:

            default:
                return true;

        }
    }
    public void onBackPressed() {
       PDialog.hide();
        onBack();
    }
}
