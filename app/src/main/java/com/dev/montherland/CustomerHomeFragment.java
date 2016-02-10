package com.dev.montherland;

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

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.dev.montherland.model.Response_Model;
import com.dev.montherland.parsers.Response_JSONParser;
import com.dev.montherland.util.PDialog;
import com.dev.montherland.util.StaticVariables;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class CustomerHomeFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private RecyclerView recyclerView;
    private List<Response_Model> persons;
    StaggeredGridLayoutManager mLayoutManager;
    String data_receive = "string_req_recieve",id,email,password;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    // TODO: Rename and change types and number of parameters

    public CustomerHomeFragment() {
        // Required empty public constructor
    }

    public void getMasterList() {
        PDialog.show(getActivity());
        StringRequest request = new StringRequest(Request.Method.POST, getResources().getString(R.string.url_motherland) + "customer_list.php?",
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response) {
                        PDialog.hide();
                        Log.v("response", response + "");
                        try {

                            JSONArray ar = new JSONArray(response);
                            persons= Response_JSONParser.parserFeed(response);
                            recyclerView.setAdapter(new RecyclerViewAdapter(persons));

                        } catch (JSONException e) {
                            //Log.d("response", response);
                            //Log.d("error in json", "l " + e);

                        } catch (Exception e) {
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
        AppController.getInstance().addToRequestQueue(request, data_receive);
        Log.v("request", request + "");
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.activity_home,container,false);

        //getActivity().getActionBar().setTitle("Customer List");
        try {
            id = getArguments().getString("id");
            email = getArguments().getString("email");
            password =getArguments().getString("password");
            StaticVariables.email=email;
            StaticVariables.password=password;

        }
        catch (Exception e){
            e.printStackTrace();
        }

        ActionBar mActionBar=((AppCompatActivity) getActivity()).getSupportActionBar();
        mActionBar.setTitle("Customer List");


        getMasterList();
        recyclerView = (RecyclerView)view.findViewById(R.id.rv);
        recyclerView.setHasFixedSize(true);
        mLayoutManager=new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL);
        mLayoutManager.setOrientation(mLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event


}
