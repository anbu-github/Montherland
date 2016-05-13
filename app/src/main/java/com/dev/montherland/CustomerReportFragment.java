package com.dev.montherland;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.dev.montherland.adapter.OrderReportAdapter;
import com.dev.montherland.model.Customer_Report_Model;
import com.dev.montherland.model.Purchase_Order_Model;
import com.dev.montherland.parsers.Customer_Orders_Report_JSONParser;
import com.dev.montherland.util.PDialog;
import com.dev.montherland.util.StaticVariables;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class CustomerReportFragment extends Fragment {

    ImageButton addButton;
    StaggeredGridLayoutManager mLayoutManager;
    private RecyclerView recyclerView;
    private List<Customer_Report_Model> persons;
    ListView listView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.customer_report_frgment, container, false);

        listView=(ListView)view.findViewById(R.id.listView);

        getCustmerOrders();
        return view;


    }

    public void getCustmerOrders() {
        StringRequest request = new StringRequest(Request.Method.POST, getResources().getString(R.string.url_motherland) + "customers_monthly_report.php?",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                      //  PDialog.hide();
                        Log.v("response",response);
                        try {
                            JSONObject jobj = new JSONObject(response);
                             String customer_orders = jobj.getString("customer_orders");
                             persons= Customer_Orders_Report_JSONParser.parserFeed(customer_orders);

                            listView.setAdapter(new OrderReportAdapter(getActivity(),persons));




                        }catch (Exception e){
                            e.printStackTrace();
                        }



                    }
                },

                new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError arg0) {
                        PDialog.hide();
                        Toast.makeText(getActivity(), getResources().getString(R.string.no_internet_access), Toast.LENGTH_LONG).show();
                    }
                }) {

            @Override
            protected Map<String, String> getParams() {

                Map<String, String> params = new HashMap<>();
                params.put("email", StaticVariables.database.get(0).getEmail());
                params.put("password", StaticVariables.database.get(0).getPassword());
                params.put("id", StaticVariables.database.get(0).getId());
                return params;
            }
        };
        AppController.getInstance().addToRequestQueue(request, "data_receive");
    }


}
