package com.dev.montherland.customers;

import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.dev.montherland.AppController;
import com.dev.montherland.R;
import com.dev.montherland.customers.adapter.CustomerDetailsAdapter;
import com.dev.montherland.model.Customer_Details_Model;
import com.dev.montherland.parsers.Customer_Details_Parser;
import com.dev.montherland.util.PDialog;
import com.dev.montherland.util.StaticVariables;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Customer_details_orders extends Activity {


    List<Customer_Details_Model> person;
    TextView name,website,mobile;
    String id,data_receive="data_receive";
    Activity thisActivity=this;
    StaggeredGridLayoutManager mLayoutManager;
    private RecyclerView recyclerView;
    RelativeLayout re_layout;
    String orders;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_details_orders);

        recyclerView = (RecyclerView) findViewById(R.id.rv);

        re_layout=(RelativeLayout)findViewById(R.id.re_layout);

        if (Build.VERSION.SDK_INT > 19) {
            getActionBar().setDisplayHomeAsUpEnabled(true);
            getActionBar().setHomeAsUpIndicator(R.drawable.pf);
        } else {
            getActionBar().setHomeButtonEnabled(true);
            getActionBar().setIcon(R.drawable.pf);
        }

        if (StaticVariables.isNetworkConnected(thisActivity)) {
            PDialog.show(thisActivity);
            getContactlist();
        }else {
            Toast.makeText(thisActivity, getResources().getString(R.string.no_internet_connection), Toast.LENGTH_SHORT).show();
        }

        recyclerView.setHasFixedSize(true);
        mLayoutManager = new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL);
        mLayoutManager.setOrientation(mLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(new LinearLayoutManager(thisActivity));


    }

    public void getContactlist() {
       // PDialog.show(thisActivity);
        StringRequest request = new StringRequest(Request.Method.POST, getResources().getString(R.string.url_motherland) + "customer_details.php",
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response) {
                        Log.v("response", response);
                        try {
                            JSONObject jobj = new JSONObject(response);

                            String basic_details=jobj.getString("basic_details");
                            orders=jobj.getString("orders");

                            person= Customer_Details_Parser.parserFeed(orders);
                            //listview.setAdapter(new CustomerDetailsAdapter(CustomerDetails.this,person));

                            Log.v("persondata",person.size()+""+person.toString());

                            if (person.size()==0){
                                Toast.makeText(thisActivity, getResources().getString(R.string.no_order_created), Toast.LENGTH_LONG).show();

                            }

                            if(person == null) {
                                Toast.makeText(thisActivity, getResources().getString(R.string.no_order_created), Toast.LENGTH_LONG).show();
                            }
                            else if (person.get(0).getCompany_name().isEmpty()) {
                                Toast.makeText(thisActivity,getResources().getString(R.string.no_order_created),Toast.LENGTH_LONG).show();

                            } else {
                                if (person.get(0).getId().equals("No Data")) {
                                    Toast.makeText(thisActivity, getResources().getString(R.string.no_order_created), Toast.LENGTH_LONG).show();
                                }
                                else if (person.get(0).getId().equals("Error")) {
                                    Toast.makeText(thisActivity, getResources().getString(R.string.error_occurred1), Toast.LENGTH_LONG).show();
                                }
                                else if (person.get(0).getId().equals("")) {
                                    Toast.makeText(thisActivity, getResources().getString(R.string.error_occurred1), Toast.LENGTH_LONG).show();
                                }
                                else {

                                    Log.v("data",person.get(0).getId());
                                    recyclerView.setAdapter(new CustomerDetailsAdapter(thisActivity, person));
                                }

                            }

                            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

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
                params.put("customer_id",StaticVariables.database.get(0).getCustomer_id());
                params.put("password", StaticVariables.database.get(0).getPassword());
                params.put("id", StaticVariables.database.get(0).getId());
                return params;
            }
        };
        AppController.getInstance().addToRequestQueue(request, data_receive);
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
