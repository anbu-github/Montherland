package com.dev.montherland.customers;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.dev.montherland.*;
import com.dev.montherland.adapter.CustomerDetailsAdapter;
import com.dev.montherland.model.Customer_Details_Model;
import com.dev.montherland.model.Response_Model;
import com.dev.montherland.parsers.Customer_Details_Parser;
import com.dev.montherland.parsers.Response_JSONParser;
import com.dev.montherland.util.PDialog;
import com.dev.montherland.util.StaticVariables;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class CustomerHomeFragment extends Fragment {

    List<Customer_Details_Model> person;
    TextView name,website,mobile;
    String id,data_receive="data_receive",orders;

    StaggeredGridLayoutManager mLayoutManager;
    private RecyclerView recyclerView;
    RelativeLayout re_layout;
    String companyId;
    Button btn_order,btn_contact,btn_address;
    ImageView edit_button;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View view=inflater.inflate(R.layout.activity_customer_details,container,false);
        getActivity().getActionBar().setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);

        recyclerView = (RecyclerView) view.findViewById(R.id.rv);
        name=(TextView)view.findViewById(R.id.add1);
        website=(TextView)view.findViewById(R.id.website);
        mobile=(TextView)view.findViewById(R.id.phone);
        re_layout=(RelativeLayout)view.findViewById(R.id.re_layout);
        btn_address=(Button)view.findViewById(R.id.address);
        btn_contact=(Button)view.findViewById(R.id.contact);
        btn_order=(Button)view.findViewById(R.id.order);
        edit_button=(ImageView)view.findViewById(R.id.edit_customer);
        re_layout.setVisibility(View.INVISIBLE);


        edit_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in=new Intent(getActivity(), com.dev.montherland.customers.Create_Customer.class);
                in.putExtra("name",name.getText());
                in.putExtra("phone",mobile.getText());
                in.putExtra("email",website.getText());
                in.putExtra("intent_from", "edit_customer");
                in.putExtra("id",id);
                startActivity(in);
                getActivity().finish();
                getActivity().overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
            }
        });

        btn_address.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in=new Intent(getActivity(), com.dev.montherland.customers.SelectAddress.class);
                in.putExtra("customer_address", "customer_address");
                startActivity(in);
                getActivity().overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);

            }
        });
         btn_order.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in=new Intent(getActivity(), com.dev.montherland.customers.Customer_details_orders.class);
                in.putExtra("orders",orders);
                startActivity(in);
                getActivity().overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
            }
        });
         btn_contact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in=new Intent(getActivity(), com.dev.montherland.customers.Customer_contacts_list.class);
                in.putExtra("companyid", companyId);
                startActivity(in);
                getActivity().overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
            }
        });



        if (StaticVariables.isNetworkConnected(getActivity())) {

            getContactlist();
        }else {
            Toast.makeText(getActivity(),getResources().getString(R.string.no_internet_connection),Toast.LENGTH_SHORT).show();
        }

        recyclerView.setHasFixedSize(true);
        mLayoutManager = new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL);
        mLayoutManager.setOrientation(mLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        try {
            getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

            id=getArguments().getString("id");
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return view;
    }




    public void getContactlist() {
        PDialog.show(getActivity());
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

                            JSONArray ar = new JSONArray(basic_details);
                            for (int i = 0; i < ar.length(); i++) {
                                JSONObject parentObject = ar.getJSONObject(i);
                                name.setText(parentObject.getString("name"));
                                StaticVariables.companyName=parentObject.getString("name");
                                website.setText(parentObject.getString("website"));
                                mobile.setText(parentObject.getString("phone"));
                                companyId=parentObject.getString("id");
                                Log.v("contactList",parentObject.getString("name"));
                                StaticVariables.customerId=companyId;
                            }

                        //    person= Customer_Details_Parser.parserFeed(orders);
                            //listview.setAdapter(new CustomerDetailsAdapter(CustomerDetails.this,person));
                       //     recyclerView.setAdapter(new CustomerDetailsAdapter(getActivity(),person));

                            PDialog.hide();
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
                params.put("customer_id",StaticVariables.database.get(0).getCustomer_id());
                params.put("password", StaticVariables.database.get(0).getPassword());
                params.put("id", StaticVariables.database.get(0).getId());
                return params;
            }
        };
        AppController.getInstance().addToRequestQueue(request, data_receive);
    }


    }

