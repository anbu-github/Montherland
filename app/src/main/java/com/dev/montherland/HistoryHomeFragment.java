package com.dev.montherland;

import android.app.ActionBar;
import android.content.Intent;
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
import com.dev.montherland.adapter.PurchaseOrderListAdapter;
import com.dev.montherland.model.Purchase_Order_Model;
import com.dev.montherland.model.Response_Model;
import com.dev.montherland.parsers.History_Order_JSONParser;
import com.dev.montherland.parsers.Purchase_Order_JSONParser;
import com.dev.montherland.parsers.Response_JSONParser;
import com.dev.montherland.util.PDialog;
import com.dev.montherland.util.StaticVariables;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by pf-05 on 3/17/2016.
 */
public class HistoryHomeFragment extends Fragment {

    ImageButton addButton;
    StaggeredGridLayoutManager mLayoutManager;
    private RecyclerView recyclerView;
    private List<Purchase_Order_Model> persons;

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


        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), CreatePurchaseOrder.class);
                getActivity().finish();
                startActivity(intent);
                getActivity().overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
            }
        });

        addButton.setVisibility(View.INVISIBLE);

        getActivity().getActionBar().setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);

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
        StringRequest request = new StringRequest(Request.Method.POST, getResources().getString(R.string.url_motherland) + "order_history_home.php",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        PDialog.hide();
                        Log.v("response", response);
                        PDialog.hide();
                        persons = History_Order_JSONParser.parserFeed(response);

                        if(persons == null) {
                            Toast.makeText(getActivity(),getResources().getString(R.string.no_order_created),Toast.LENGTH_LONG).show();
                        } else {
                            if(persons.get(0).getId().equals("No Data")) {
                                Toast.makeText(getActivity(),getResources().getString(R.string.no_order_created),Toast.LENGTH_LONG).show();
                            } else if (persons.get(0).getId().equals("Error")) {
                                Toast.makeText(getActivity(),getResources().getString(R.string.error_occurred1),Toast.LENGTH_LONG).show();
                            } else {
                                recyclerView.setAdapter(new PurchaseOrderListAdapter(getActivity(),persons));
                            }
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
