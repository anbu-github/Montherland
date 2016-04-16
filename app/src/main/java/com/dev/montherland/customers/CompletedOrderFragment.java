package com.dev.montherland.customers;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.dev.montherland.AppController;
import com.dev.montherland.R;
import com.dev.montherland.adapter.CustomerDetailsAdapter;
import com.dev.montherland.adapter.PurchaseOrderListAdapter;
import com.dev.montherland.customers.adapter.CustomerDetailsAdapter1;
import com.dev.montherland.model.Customer_Details_Model;
import com.dev.montherland.model.Purchase_Order_Model;
import com.dev.montherland.parsers.Customer_Details_Parser;
import com.dev.montherland.parsers.Purchase_Order_JSONParser;
import com.dev.montherland.util.PDialog;
import com.dev.montherland.util.StaticVariables;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class CompletedOrderFragment extends Fragment {

    ImageButton addButton;
    StaggeredGridLayoutManager mLayoutManager;
    private RecyclerView recyclerView;
    private List<Purchase_Order_Model> persons;
    String orders;
    List<Customer_Details_Model> person;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_purchase_order, container, false);
        recyclerView = (RecyclerView) view.findViewById(R.id.rv);
        addButton = (ImageButton) view.findViewById(R.id.btn_add_address);

       addButton.setVisibility(View.GONE);


        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        getActivity().getActionBar().setTitle("Orders");

        recyclerView.setHasFixedSize(true);
        mLayoutManager = new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL);
        mLayoutManager.setOrientation(mLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        if (StaticVariables.isNetworkConnected(getActivity())) {
            getPurchaseOrderLIst();
        } else {
            Toast.makeText(getActivity(), getResources().getString(R.string.no_internet_connection), Toast.LENGTH_SHORT).show();
        }

        return view;
    }

    public void getPurchaseOrderLIst() {
        PDialog.show(getActivity());
        StringRequest request = new StringRequest(Request.Method.POST, getResources().getString(R.string.url_motherland) + "master_customer_purchase_order.php",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        PDialog.hide();
                        Log.v("response", response);
                        PDialog.hide();
                        try {

                            JSONObject jobj = new JSONObject(response);

                            orders = jobj.getString("orders");

                            person = Customer_Details_Parser.parserFeed(orders);



                        if (person == null) {
                            Toast.makeText(getActivity(), getResources().getString(R.string.no_order_created), Toast.LENGTH_LONG).show();
                        } else if (person.get(0).getCompany_name().isEmpty()) {
                            Toast.makeText(getActivity(), getResources().getString(R.string.no_order_created), Toast.LENGTH_LONG).show();

                        } else {
                            if (person.get(0).getId().equals("No Data")) {
                                Toast.makeText(getActivity(), getResources().getString(R.string.no_order_created), Toast.LENGTH_LONG).show();
                            } else if (person.get(0).getId().equals("Error")) {
                                Toast.makeText(getActivity(), getResources().getString(R.string.error_occurred1), Toast.LENGTH_LONG).show();
                            } else if (person.get(0).getId().equals("")) {
                                Toast.makeText(getActivity(), getResources().getString(R.string.error_occurred1), Toast.LENGTH_LONG).show();
                            } else {

                                Log.v("data", person.get(0).getId());
                                recyclerView.setAdapter(new CustomerDetailsAdapter1(getActivity(), person));
                            }

                        }
                        } catch (Exception e) {
                            e.printStackTrace();
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
                try {
                    params.put("email", StaticVariables.database.get(0).getEmail());
                    params.put("password", StaticVariables.database.get(0).getPassword());
                    params.put("id", StaticVariables.database.get(0).getId());
                    params.put("customer_id", StaticVariables.database.get(0).getCustomer_id());
                    params.put("tab_id", "1");
                }
                catch (Exception e){
                    e.printStackTrace();
                }

                return params;
            }
        };
        AppController.getInstance().addToRequestQueue(request, "data_receive");
    }


}
