package com.dev.montherland;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.dev.montherland.adapter.AddressCreateAdapter;
import com.dev.montherland.adapter.PurchaseOrderListAdapter;
import com.dev.montherland.model.Create_Address_Model;
import com.dev.montherland.model.Purchase_Order_Model;
import com.dev.montherland.parsers.Create_Address_JSONParser;
import com.dev.montherland.parsers.Purchase_Order_JSONParser;
import com.dev.montherland.parsers.Response_JSONParser;
import com.dev.montherland.util.PDialog;
import org.json.JSONArray;
import org.json.JSONException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class PurchaseOrderFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private RecyclerView recyclerView;
    private List<Purchase_Order_Model> persons;
    ImageButton addButton;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    StaggeredGridLayoutManager mLayoutManager;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_purchase_order,container,false);
        recyclerView = (RecyclerView)view.findViewById(R.id.rv);
        addButton = (ImageButton)view.findViewById(R.id.btn_add_address);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), CreatePurchaseOrder.class);
                getActivity().finish();
                startActivity(intent);
            }
        });


        ActionBar mActionBar=((AppCompatActivity) getActivity()).getSupportActionBar();
        mActionBar.setTitle("Purchased Order List");

        recyclerView.setHasFixedSize(true);
        mLayoutManager = new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL);
        mLayoutManager.setOrientation(mLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        getPurchaseOrderLIst();
        return view;
    }


    public void getPurchaseOrderLIst() {
        PDialog.show(getActivity());
        StringRequest request = new StringRequest(Request.Method.POST, getResources().getString(R.string.url_motherland) + "master_purchase_order.php?",
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response) {
                        PDialog.hide();
                        Log.v("response", response + "");
                        try {
                            PDialog.hide();
                            JSONArray ar = new JSONArray(response);
                            persons = Purchase_Order_JSONParser.parserFeed(response);
                            recyclerView.setAdapter(new PurchaseOrderListAdapter(persons,getActivity()));
                        } catch (JSONException e) {
                            PDialog.hide();
                            //Log.d("response", response);
                            //Log.d("error in json", "l " + e);

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
                params.put("email", "test@test.com");
                params.put("password", "e48900ace570708079d07244154aa64a");
                params.put("id", "4");

                //Log.d("params", database.get(0).getId());
                //Log.d("service_id", StaticVariables.service_id);
                return params;
            }
        };
        AppController.getInstance().addToRequestQueue(request, "data_receive");
        Log.v("request", request + "");
    }



}
