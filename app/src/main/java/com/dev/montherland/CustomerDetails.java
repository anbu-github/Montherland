package com.dev.montherland;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

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
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CustomerDetails extends AppCompatActivity {

    TextView name,website,mobile;
    String id,email,password,cus_name,cus_mobile,cus_website,data_receive="data_receive";
    Activity thisActivity=this;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_details);

        name=(TextView)findViewById(R.id.name);
        website=(TextView)findViewById(R.id.website);
        mobile=(TextView)findViewById(R.id.phone);
        customerDetailsRequest();

    }
    public void customerDetailsRequest() {
        PDialog.show(thisActivity);

        StringRequest request = new StringRequest(Request.Method.POST, getResources().getString(R.string.url_motherland) + "customer_details.php?",
                new Response.Listener<String>()

                {
                    @Override
                    public void onResponse(String response) {

                        PDialog.hide();
                        Log.v("response", response + "");
                        try {
                            PDialog.hide();
                            JSONArray ar = new JSONArray(response);
                            for (int i = 0; i < ar.length(); i++) {
                                JSONObject parentObject = ar.getJSONObject(i);
                                cus_name=parentObject.getString("name");
                                cus_mobile=parentObject.getString("phone");
                                cus_website=parentObject.getString("website");

                                name.setText(cus_name);
                                mobile.setText(cus_mobile);
                                website.setText(cus_website);
                                //Log.d("success", parentObject.getString("success"));
                            }

                        } catch (JSONException e) {
                            //PDialog.hide();

                        } catch (Exception e) {
                           // PDialog.hide();
//                            Log.d("json connection", "No internet access" + e);
                        }
                    }
                },

                new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError arg0) {
                        //PDialog.hide();
                        // Toast.makeText(Login.this,"Invalid username or password",Toast.LENGTH_SHORT).show();



                    }
                }) {

            @Override
            protected Map<String, String> getParams() {

                Map<String, String> params = new HashMap<>();
                params.put("email", "test@test.com");
                params.put("password", "e48900ace570708079d07244154aa64a");
                params.put("customer_id", "1");

                //Log.d("params", database.get(0).getId());
                //Log.d("service_id", StaticVariables.service_id);
                return params;
            }
        };
        AppController.getInstance().addToRequestQueue(request, data_receive);
    }

}
