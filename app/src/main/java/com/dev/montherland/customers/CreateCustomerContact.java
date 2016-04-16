package com.dev.montherland.customers;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.Spanned;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
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
import com.dev.montherland.util.utilActivity;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CreateCustomerContact extends Activity {

    String name, email, mobile, titleId;
    EditText et_name, et_email, et_mobile;
    Activity thisActivity = this;
    List<Response_Model> person;
    ArrayList<String> titleIdList = new ArrayList<>();
    ArrayList<String> titleList = new ArrayList<>();
    Spinner spinner2;
    ArrayAdapter<String> dataAdapter;
    List<Response_Model> respones;
    TextView textView11;
    String id="",intent_id,position;
    String menuTitle="Create_Contact",intent_from="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_customer_contact);


        et_name = (EditText) findViewById(R.id.viewedit_name_edit);
        et_email = (EditText) findViewById(R.id.create_email);
        et_mobile = (EditText) findViewById(R.id.viewedit_phone_edit);
        textView11 = (TextView) findViewById(R.id.contact);

        spinner2 = (Spinner) findViewById(R.id.spinner2);

        et_email.setVisibility(View.VISIBLE);
        textView11.setVisibility(View.VISIBLE);

        et_name.setFilters(new InputFilter[]{
                new InputFilter() {
                    @Override
                    public CharSequence filter(CharSequence cs, int start,
                                               int end, Spanned spanned, int dStart, int dEnd) {
                        // TODO Auto-generated method stub
                        if (cs.equals("")) { // for backspace
                            return cs;
                        }
                        if (cs.toString().matches("[a-zA-Z ]+")) {
                            return cs;
                        }
                        return "";
                    }
                }
        });

        try {

        if (getIntent().getExtras().getString("intent_from").equals("edit_contact")) {
            menuTitle = "Edit_Customer";
            getActionBar().setTitle("Edit Customer");
            name=getIntent().getExtras().getString("name");
            email=getIntent().getExtras().getString("email");
            mobile=getIntent().getExtras().getString("phone");
            intent_id=getIntent().getExtras().getString("id");
            position=getIntent().getExtras().getString("position");

            et_name.setText(name);
            et_mobile.setText(mobile);
            et_email.setVisibility(View.GONE);
            textView11.setVisibility(View.GONE);

        }
    }catch (Exception e){
            e.printStackTrace();
        }


        dataAdapter = new ArrayAdapter<>(this, R.layout.spinner_layout, titleList);

        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                try {
                    titleId = titleIdList.get(position).toString();
                    Log.v("customer_id", StaticVariables.customerContact);

                } catch (Exception e) {

                    e.printStackTrace();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        if (Build.VERSION.SDK_INT > 19) {
            getActionBar().setDisplayHomeAsUpEnabled(true);
            getActionBar().setHomeAsUpIndicator(R.drawable.pf);
        } else {
            getActionBar().setHomeButtonEnabled(true);
            getActionBar().setIcon(R.drawable.pf);
        }


        if (StaticVariables.isNetworkConnected(thisActivity)) {
            titleList();
        }
    }

    public void save() {
        name = et_name.getText().toString();
        email = et_email.getText().toString();
        mobile = et_mobile.getText().toString();

        if (name.equals("") || name.isEmpty()) {

            Toast.makeText(thisActivity, getResources().getString(R.string.correct_name), Toast.LENGTH_SHORT).show();
        } else if (email.equals("") || email.isEmpty()) {
            Toast.makeText(thisActivity, getResources().getString(R.string.correct_email_error), Toast.LENGTH_SHORT).show();
        }
         else if (!utilActivity.ismailVaild(email)){
            Toast.makeText(thisActivity, getResources().getString(R.string.correct_email2), Toast.LENGTH_SHORT).show();

        }
        else if (mobile.equals("") || mobile.isEmpty()) {
            Toast.makeText(thisActivity, getResources().getString(R.string.correct_mobile), Toast.LENGTH_SHORT).show();

        }
        else if (mobile.length() != 10) {
            Toast.makeText(thisActivity, getResources().getString(R.string.correct_limit_contact), Toast.LENGTH_SHORT).show();
        }
        else {

            createContact();
        }


    }

    private void titleList() {
        PDialog.show(thisActivity);
        StringRequest request = new StringRequest(Request.Method.POST, getResources().getString(R.string.url_motherland) + "title_list.php", new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                PDialog.hide();

                try {

                    JSONArray ar = new JSONArray(s);

                    for (int i = 0; i < ar.length(); i++) {
                        JSONObject parentObject = ar.getJSONObject(i);
                        if (!parentObject.getString("name").equals("NO Data")) {
                            titleList.add(parentObject.getString("name"));
                            titleIdList.add(parentObject.getString("id"));

                            Log.v("name", parentObject.getString("name"));
                        }
                        spinner2.setAdapter(dataAdapter);

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                //Log.d("response", s);
                // feedlist_response = Response_JSONParser.parserFeed(s);
                // updatedisplay();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                PDialog.hide();
                Toast.makeText(thisActivity, getResources().getString(R.string.no_internet_access), Toast.LENGTH_LONG).show();
                //Toast.makeText(Editprofile.this,volleyError.getMessage(),Toast.LENGTH_LONG).show();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();

                return params;
            }

        };
        AppController.getInstance().addToRequestQueue(request, "receive_data");
    }
    public void editContact() {

        name = et_name.getText().toString();
        mobile = et_mobile.getText().toString();

        if (name.equals("") || name.isEmpty()) {

            Toast.makeText(thisActivity, getResources().getString(R.string.correct_name), Toast.LENGTH_SHORT).show();
        } else if (mobile.equals("") || mobile.isEmpty()) {
            Toast.makeText(thisActivity, getResources().getString(R.string.correct_mobile), Toast.LENGTH_SHORT).show();

        } else if (mobile.length() != 10) {
            Toast.makeText(thisActivity, getResources().getString(R.string.correct_limit_contact), Toast.LENGTH_SHORT).show();
        } else {


            PDialog.show(thisActivity);
            StringRequest request = new StringRequest(Request.Method.POST, getResources().getString(R.string.url_motherland) + "company_customer_edit.php",
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String s) {
                            Log.v("response", s);
                            PDialog.hide();
                            try {
                                JSONObject ar = new JSONObject(s);

                                android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(thisActivity);
                                builder.setCancelable(false)
                                        .setTitle("Success")
                                        .setMessage("contact details has updated successfully")
                                        .setNegativeButton("ok", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                Intent intent = new Intent(thisActivity, Customer_contact_details.class);
                                                thisActivity.finish();
                                                startActivity(intent);
                                                overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
                                            }
                                        });
                                builder.show();

                                id = ar.getString("id");
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            //Toast.makeText(thisActivity,s,Toast.LENGTH_LONG).show();

                            //Log.d("response", s);

                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError volleyError) {
                    PDialog.hide();
                    Toast.makeText(thisActivity, getResources().getString(R.string.no_internet_access), Toast.LENGTH_LONG).show();
                    //Toast.makeText(Editprofile.this,volleyError.getMessage(),Toast.LENGTH_LONG).show();
                }
            }) {

                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<>();
                    params.put("email", StaticVariables.database.get(0).getEmail());
                    params.put("id", StaticVariables.database.get(0).getId());
                    params.put("name_str", name);
                    params.put("password", StaticVariables.database.get(0).getPassword());
                    params.put("phone", StaticVariables.database.get(0).getPhone());
                    params.put("phone_str", mobile);
                    params.put("customer_id", StaticVariables.customerId);
                    params.put("customer_contact_id", StaticVariables.customerContact);
                    params.put("title_id", titleId);

                    Log.v("name_str", name + mobile + StaticVariables.customerId + StaticVariables.customerContact + titleId);
                    return params;
                }

            };
            AppController.getInstance().addToRequestQueue(request, "receive_data");
            Log.v("url", request + "");
        }

    }
    void updatedisplay(String id){

        if(id != null) {

                if(id.equals("Success")) {
                    android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(thisActivity);
                    builder.setCancelable(false)
                            .setTitle("Success")
                            .setMessage("New contact has saved successfully")
                            .setNegativeButton("ok", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Intent intent = new Intent(thisActivity,Customer_contacts_list.class);
                                    thisActivity.finish();
                                    startActivity(intent);
                                    overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);                                }
                            });
                    builder.show();
                } else if(id.equals("Already Exists")) {
                    Toast.makeText(thisActivity,getResources().getString(R.string.already_exist),Toast.LENGTH_LONG).show();
                }

        } else {
            Toast.makeText(thisActivity,getResources().getString(R.string.error_occurred1),Toast.LENGTH_LONG).show();
        }

    }

    private void createContact() {
        PDialog.show(thisActivity);
        StringRequest request = new StringRequest(Request.Method.POST, getResources().getString(R.string.url_motherland) + "company_customer_create.php",
                new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                Log.v("response", s);
                PDialog.hide();
                try {
                    JSONObject ar = new JSONObject(s);

                     id=ar.getString("id");
                }catch (Exception e){
                    e.printStackTrace();
                }
                //Toast.makeText(thisActivity,s,Toast.LENGTH_LONG).show();

                //Log.d("response", s);
                respones = Response_JSONParser.parserFeed(s);
                 updatedisplay(id);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                PDialog.hide();
                Toast.makeText(thisActivity, getResources().getString(R.string.no_internet_access), Toast.LENGTH_LONG).show();
                //Toast.makeText(Editprofile.this,volleyError.getMessage(),Toast.LENGTH_LONG).show();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("email", StaticVariables.database.get(0).getEmail());
                params.put("id", StaticVariables.database.get(0).getId());
                params.put("name_str", name);
                params.put("password", StaticVariables.database.get(0).getPassword());
                params.put("phone", mobile);
                params.put("email_str", email);
                params.put("phone_str", mobile);
                params.put("customer_id", StaticVariables.customerId);
                params.put("title_id", titleId);

                return params;
            }

        };
        AppController.getInstance().addToRequestQueue(request, "receive_data");
        Log.v("url",request+"");
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu items for use in the action barenu
        getMenuInflater().inflate(R.menu.menu_save, menu);

        return true;
    }

    public void onBack(){
        if (menuTitle.contains("Edit_Contact")){
            Intent in=new Intent(thisActivity, com.dev.montherland.customers.Customer_contact_details.class);
            in.putExtra("name",name);
            in.putExtra("phone",mobile);
            in.putExtra("email", email);
            in.putExtra("id", intent_id);
            in.putExtra("position", position);

            startActivity(in);
            overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
            finish();
        }else {

            Intent in=new Intent(thisActivity, com.dev.montherland.customers.Customer_contacts_list.class);
            startActivity(in);
            finish();
            //super.onBackPressed();
            overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
        }
    }

    @Override
    public void onBackPressed() {
        onBack();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
               onBack();
                return true;
            case R.id.save_button:

                StaticVariables.hideKeyboard(CreateCustomerContact.this);
                if (menuTitle.contains("Create_Contact")) {
                    save();
                }else {
                    editContact();
                }

                return true;

            default:
                return true;
        }
    }
}
