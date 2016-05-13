package com.dev.montherland;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.dev.montherland.adapter.CustomerContactAdapter;
import com.dev.montherland.parsers.Customer_Contact_JSONParser;
import com.dev.montherland.util.PDialog;
import com.dev.montherland.util.StaticVariables;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class FeedbackActivity extends Activity {

    Activity thisActivity=this;
    RatingBar ratingBar;
    Button fbSubmit;
    EditText fbDesc;
    Float ratingStar=0f;
    String fb_desc;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);


        ratingBar=(RatingBar)findViewById(R.id.ratingBar);
        fbSubmit=(Button) findViewById(R.id.fbSubmit);
        fbDesc=(EditText) findViewById(R.id.fbDesc);

        fbSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                submit();
            }
        });

        ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {

                Toast.makeText(thisActivity,rating+"",Toast.LENGTH_SHORT).show();
                ratingStar=rating;
            }
        });

        if (Build.VERSION.SDK_INT > 19) {
            getActionBar().setDisplayHomeAsUpEnabled(true);
            getActionBar().setHomeAsUpIndicator(R.drawable.pf);
        }
        else {
            getActionBar().setHomeButtonEnabled(true);
            getActionBar().setIcon(R.drawable.pf);
        }
    }

    public void goBack(){
        Intent in=new Intent(FeedbackActivity.this,PurchaseOrderDetails.class);
        startActivity(in);
        finish();
        overridePendingTransition(R.anim.push_right_in
                , R.anim.push_right_out);

    }

    public void submit(){
        fb_desc=fbDesc.getText().toString();
        if (ratingStar==0){
            Toast.makeText(thisActivity,getResources().getString(R.string.hint_starrating),Toast.LENGTH_SHORT).show();
        }
        else {
            submitFeedback();
        }

    }

    public void submitFeedback() {
        PDialog.show(thisActivity);
        StringRequest request = new StringRequest(Request.Method.POST, getResources().getString(R.string.url_motherland) + "order_feedback.php?",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        PDialog.hide();
                        Log.v("response", response + "");
                        try {
                            JSONObject ar = new JSONObject(response);

                            if (ar.get("id").equals("Success")){
                                android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(thisActivity);
                                builder.setCancelable(false)
                                        .setMessage("Thank you for feedback")
                                        .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {

                                                Intent intent = new Intent(thisActivity, NavigataionActivity.class);
                                                startActivity(intent);
                                                finish();
                                                overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
                                            }
                                        });
                                builder.show();
                            }




                        } catch (Exception e) {
                            // PDialog.hide();
                        }
                    }
                },

                new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError arg0) {
                        //  PDialog.hide();

                    }
                }) {

            @Override
            protected Map<String, String> getParams() {

                Map<String, String> params = new HashMap<>();
                params.put("email", StaticVariables.database.get(0).getEmail());
                params.put("order_id", StaticVariables.order_id);
                params.put("password", StaticVariables.database.get(0).getPassword());
                params.put("id", StaticVariables.database.get(0).getId());
                params.put("fb_desc", fb_desc);
                params.put("rating", String.valueOf(ratingStar));
                return params;
            }
        };
        AppController.getInstance().addToRequestQueue(request, "data_receive");
        Log.v("request", request + "");
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                goBack();
                return true;

            default:
                return true;
        }
    }


    @Override
    public void onBackPressed() {
        goBack();
    }


}
