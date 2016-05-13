package com.dev.montherland.customers;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.dev.montherland.AppController;
import com.dev.montherland.R;
import com.dev.montherland.model.Response_Model;
import com.dev.montherland.parsers.Response_JSONParser;
import com.dev.montherland.util.PDialog;
import com.dev.montherland.util.StaticVariables;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InstructionActivity extends Activity {
    Bundle extras;
    Activity thisActivity=this;
    EditText in;
    String str_instr,order_id;
    List<Response_Model> feedlist;
    Bundle bundle;
    String intent_value="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_instruction);
      in=(EditText)findViewById(R.id.instruction);

        try {
            extras=getIntent().getExtras();
            intent_value=extras.getString("intent_from");
            str_instr=extras.getString("instr");
            order_id=extras.getString("order_id");
            in.setText(str_instr);

            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);



            if (Build.VERSION.SDK_INT > 19) {
                getActionBar().setDisplayHomeAsUpEnabled(true);
                getActionBar().setHomeAsUpIndicator(R.drawable.pf);
                getActionBar().setTitle(getResources().getString(R.string.edit_instr_title));
            } else {
                getActionBar().setHomeButtonEnabled(true);
                getActionBar().setIcon(R.drawable.pf);
                getActionBar().setTitle(getResources().getString(R.string.edit_instr_title));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu items for use in the action barenu
        getMenuInflater().inflate(R.menu.menu_save, menu);

        return true;
    }

    public void updateDisplay() {


         android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(thisActivity);
            builder.setCancelable(false)
                    .setTitle("Success")
                    .setMessage(getResources().getString(R.string.save_instruction))
                    .setNegativeButton("ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            Intent intent = new Intent(thisActivity, PurchaseOrderDetails.class);

                            try {
                                if (intent_value.contains("customer_order")) {
                                    intent.putExtra("intent_from", "customer_order");
                                }
                            }catch (Exception e){
                                e.printStackTrace();
                            }
                            startActivity(intent);
                            finish();
                            overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
                        }
                    });
            builder.show();
        }

    public void save() {
        PDialog.show(thisActivity);
        StringRequest request = new StringRequest(Request.Method.POST, getResources().getString(R.string.url_motherland) + "master_purchase_order_instructions_edit.php",
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response) {
                        PDialog.hide();

                        feedlist=Response_JSONParser.parserFeed(response);
                        updateDisplay();
                        Log.v("response", response);

                    }
                },

                new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError arg0) {
                        PDialog.hide();
                        Toast.makeText(thisActivity, getResources().getString(R.string.no_internet_access), Toast.LENGTH_LONG).show();
                    }
                }) {

            @Override
            protected Map<String, String> getParams() {

                Map<String, String> params = new HashMap<>();

                try {
                    params.put("email", StaticVariables.database.get(0).getEmail());
                    params.put("password", StaticVariables.database.get(0).getPassword());
                    params.put("id", StaticVariables.database.get(0).getId());
                    params.put("order_id", order_id);
                    params.put("instructions", in.getText()+" ");
                } catch (Exception e) {
                    e.printStackTrace();
                }

                return params;
            }
        };
        AppController.getInstance().addToRequestQueue(request, "data_receive3");
        Log.v("request", request + "");

    }

    public void goBack(){
        Intent in=new Intent(thisActivity,PurchaseOrderDetails.class);
        try {
            if (intent_value.contains("customer_order")) {
                in.putExtra("intent_from", "customer_order");
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        startActivity(in);
        finish();
        overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
    }

    @Override
    public void onBackPressed() {
    goBack();

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
              goBack();
                return true;
            case R.id.save_button:

                StaticVariables.hideKeyboard(thisActivity);
                if (StaticVariables.isNetworkConnected(thisActivity)) {
                    save();
                }
                else {
                    Toast.makeText(thisActivity, "Please check the network connection", Toast.LENGTH_SHORT).show();
                }

               /* Intent in=new Intent(thisActivity,OrderConfirmDetails.class);
                in.putExtra("bundle",extras);
                in.putExtra("instr",str_instr);
                startActivity(in);*/



        }
        return true;
    }

        }
