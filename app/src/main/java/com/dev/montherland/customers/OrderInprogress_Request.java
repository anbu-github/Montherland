package com.dev.montherland.customers;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.dev.montherland.R;

public class OrderInprogress_Request extends Fragment {

    ImageButton create_service;
    ProgressDialog progress;
    int i;
    String id;
    //RVAdapter mAdapter;
    View rootView;
    private String tag_string_req = "string_req";
    private RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.order_progress_request, container, false);
        setHasOptionsMenu(true);

     //   Toast.makeText(getActivity(),"None of orders completed",Toast.LENGTH_SHORT).show();

        try {
            getActivity().getActionBar().setHomeButtonEnabled(true);
//            getActivity().getActionBar().setIcon(R.drawable.pf);
        } catch (Resources.NotFoundException e) {
            e.printStackTrace();
        }



        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.rv);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setClickable(true);

        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);

        //progress.show();

        return rootView;
    }



    protected boolean isonline() {
        ConnectivityManager cm = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netinfo = cm.getActiveNetworkInfo();
        return netinfo != null && netinfo.isConnectedOrConnecting();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Intent intent = new Intent(getActivity(), MainActivity.class);
                getActivity().finish();
                startActivity(intent);
                getActivity().overridePendingTransition(R.anim.left_right, R.anim.right_left);
                return true;
            default:
                return true;
        }
    }
}

