package com.dev.montherland.customers;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.dev.montherland.AppController;
import com.dev.montherland.R;
import com.dev.montherland.model.Profile_Model;
import com.dev.montherland.parsers.Profile_JSONParser;
import com.dev.montherland.util.PDialog;
import com.dev.montherland.util.StaticVariables;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ViewProfile extends Activity {

    TextView nametv, emailtv, phonetv, addresstv, addresstv2, addresstv3, citytv, state;
    List<Profile_Model> feedlist;
    String zipcode,stateId;
    RelativeLayout layout;
    ImageView edit_address;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_profile);

        nametv = (TextView) findViewById(R.id.viewedit_name);
        emailtv = (TextView) findViewById(R.id.viewedit_email);
        phonetv = (TextView) findViewById(R.id.viewedit_phone);

        layout=(RelativeLayout)findViewById(R.id.layout);


        layout.setVisibility(View.INVISIBLE);

        if (StaticVariables.isNetworkConnected(ViewProfile.this)) {
            get_data();
        } else {
            Toast.makeText(ViewProfile.this, getResources().getString(R.string.no_internet_connection), Toast.LENGTH_LONG).show();
        }

        try {
            if (Build.VERSION.SDK_INT > 19) {
                getActionBar().setDisplayHomeAsUpEnabled(true);
                getActionBar().setHomeAsUpIndicator(R.drawable.pf);
            } else {
                getActionBar().setHomeButtonEnabled(true);
                getActionBar().setIcon(R.drawable.pf);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    private void update_display() {
        if (feedlist != null) {
            for (Profile_Model flower : feedlist) {
                nametv.setText(flower.getName());
                emailtv.setText(flower.getEmail());
                phonetv.setText(flower.getPhone());
                layout.setVisibility(View.VISIBLE);
              //  get_addresss();
            }
        } else {
            Toast.makeText(ViewProfile.this, getResources().getString(R.string.unknownerror7), Toast.LENGTH_LONG).show();
        }
    }
    private void get_addresss() {
       // PDialog.show(ViewProfile.this);
        StringRequest request = new StringRequest(Request.Method.POST,  getResources().getString(R.string.url_motherland) + "view_address.php", new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                Log.v("response", s);
                Log.d("response", s);

                try {
                JSONArray ar = new JSONArray(s);
                for (int i = 0; i < ar.length(); i++) {
                    JSONObject obj = ar.getJSONObject(i);


                    addresstv.setText(obj.getString("address_line1"));
                    addresstv2.setText(obj.getString("address_line2"));
                    addresstv3.setText(obj.getString("address_line3"));
                    state.setText(obj.getString("state"));
                    citytv.setText(obj.getString("city"));

                    stateId=obj.getString("state_id");

                    PDialog.hide();
                    layout.setVisibility(View.VISIBLE);
                    zipcode=obj.getString("zipcode");
                }

            } catch (JSONException e) {

                //Log.d("error in json", "l " + e);
            }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {

            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("email", StaticVariables.database.get(0).getEmail());
                params.put("id", StaticVariables.database.get(0).getId());
                return params;
            }

        };
        AppController.getInstance().addToRequestQueue(request, "Receive data");
    }

    private void get_data() {
        PDialog.show(ViewProfile.this);
        StringRequest request = new StringRequest(Request.Method.POST,  getResources().getString(R.string.url_motherland) + "view_profile.php", new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                Log.v("response", s);
                Log.d("response", s);

                feedlist = Profile_JSONParser.parserFeed(s);
                update_display();
                PDialog.hide();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {

            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("email", StaticVariables.database.get(0).getEmail());
                params.put("id", StaticVariables.database.get(0).getId());
                return params;
            }

        };
        AppController.getInstance().addToRequestQueue(request, "Receive data");
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_profile, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.edit_profile:
                Intent intent = new Intent(ViewProfile.this, EditProfile.class);

                intent.putExtra("name",nametv.getText());
                intent.putExtra("email",emailtv.getText());
                intent.putExtra("phone", phonetv.getText());
                startActivity(intent);
                finish();
                overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
                return true;
            case android.R.id.home:

                Intent intent1 = new Intent(ViewProfile.this,NavigataionActivity.class);
                //intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                intent1.putExtra("redirection", "Settings");
                finish();
                startActivity(intent1);

                overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
                return true;
            default:
                return true;
        }
    }

    @Override
    public void onBackPressed() {

        Intent intent = new Intent(ViewProfile.this,NavigataionActivity.class);
        //intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

        intent.putExtra("redirection", "Settings");
        finish();
        startActivity(intent);
        overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
    }
}
