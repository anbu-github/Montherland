package com.dev.montherland.customers;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.dev.montherland.AppController;
import com.dev.montherland.R;
import com.dev.montherland.model.Profile_Model;
import com.dev.montherland.util.PDialog;
import com.dev.montherland.util.StaticVariables;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ViewAddress extends Activity {

    TextView  addresstv, addresstv2, addresstv3, citytv, state,edit_pincode;
    List<Profile_Model> feedlist;
    String zipcode,stateId,address_id;
    LinearLayout layout;
    ImageView edit_address;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_address);

        addresstv = (TextView) findViewById(R.id.viewedit_address1);
        addresstv2 = (TextView) findViewById(R.id.viewedit_address2);
        addresstv3 = (TextView) findViewById(R.id.viewedit_address3);
        state = (TextView) findViewById(R.id.state);
        citytv = (TextView) findViewById(R.id.viewedit_city);
        edit_pincode = (TextView) findViewById(R.id.edit_pincode);
        layout=(LinearLayout)findViewById(R.id.layout);


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
        if (StaticVariables.isNetworkConnected(ViewAddress.this)) {
            PDialog.show(ViewAddress.this);
            get_addresss();
        } else {
            Toast.makeText(ViewAddress.this, getResources().getString(R.string.no_internet_connection), Toast.LENGTH_LONG).show();
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
                        address_id=obj.getString("address_id");

                        stateId=obj.getString("state_id");

                        PDialog.hide();
                        zipcode=obj.getString("zipcode");
                        edit_pincode.setText(zipcode);
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

                Log.v("addressid",StaticVariables.database.get(0).getId());
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
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.edit_profile:
                Intent intent=new Intent(ViewAddress.this,EditAddress.class);
                intent.putExtra("address1", addresstv.getText());
                intent.putExtra("address2", addresstv2.getText());
                intent.putExtra("address3", addresstv3.getText());
                intent.putExtra("state", state.getText());
                intent.putExtra("city", citytv.getText());
                intent.putExtra("zipcode", zipcode);
                intent.putExtra("stateid", stateId);
                intent.putExtra("addressId", address_id);
                startActivity(intent);
                finish();
                overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);

                return true;

            case android.R.id.home:

                super.onBackPressed();
                finish();
                overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
                return true;
            default:
                return true;
        }

    }

}
