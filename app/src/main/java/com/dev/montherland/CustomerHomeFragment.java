package com.dev.montherland;

import android.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.dev.montherland.model.Response_Model;
import com.dev.montherland.parsers.Response_JSONParser;
import com.dev.montherland.util.PDialog;
import com.dev.montherland.util.StaticVariables;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class CustomerHomeFragment extends Fragment {

    private RecyclerView recyclerView;
    private List<Response_Model> persons;
    StaggeredGridLayoutManager mLayoutManager;
    String data_receive = "string_req_recieve",id,email,password;

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
                        Log.v("response", response);
                        persons = Response_JSONParser.parserFeed(response);
                        update_display();
                    }
                    },

                new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError arg0) {
                        PDialog.hide();
                        Toast.makeText(getActivity(),getResources().getString(R.string.no_internet_access),Toast.LENGTH_LONG).show();
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
        AppController.getInstance().addToRequestQueue(request, data_receive);
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

        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
      //  getActivity().getActionBar().setTitle("Customer List");

        if (StaticVariables.isNetworkConnected(getActivity())) {
            getMasterList();
        }
        else {
            Toast.makeText(getActivity(),getResources().getString(R.string.no_internet_connection),Toast.LENGTH_LONG).show();

        }
        recyclerView = (RecyclerView)view.findViewById(R.id.rv);
        recyclerView.setHasFixedSize(true);
        mLayoutManager=new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL);
        mLayoutManager.setOrientation(mLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        return view;
    }

        void update_display() {
        if(persons != null) {
            recyclerView.setAdapter(new RecyclerViewAdapter(persons));
        } else {
            Toast.makeText(getActivity(),getResources().getString(R.string.no_customer_found),Toast.LENGTH_LONG).show();
        }
    }


}
