package com.dev.montherland;

import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.dev.montherland.adapter.PurchaseOrderListAdapter;
import com.dev.montherland.model.Purchase_Order_Model;
import com.dev.montherland.parsers.Purchase_Order_JSONParser;
import com.dev.montherland.util.PDialog;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PurchaseOrderActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private List<Purchase_Order_Model> persons;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    StaggeredGridLayoutManager mLayoutManager;
    Activity thisActivity=this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_purchase_order);
        recyclerView = (RecyclerView)findViewById(R.id.rv);
        recyclerView.setHasFixedSize(true);
        mLayoutManager = new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL);
        mLayoutManager.setOrientation(mLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(new LinearLayoutManager(thisActivity));
        getPurchaseOrderLIst();
    }

    public void getPurchaseOrderLIst() {
        PDialog.show(thisActivity);
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
                            recyclerView.setAdapter(new PurchaseOrderListAdapter(persons,thisActivity));
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
