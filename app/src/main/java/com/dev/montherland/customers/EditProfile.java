package com.dev.montherland.customers;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.Spanned;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.dev.montherland.AppController;
import com.dev.montherland.R;
import com.dev.montherland.model.Database;
import com.dev.montherland.model.Profile_Model;
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

public class EditProfile extends Activity {


    String name = "", phone = "", address = "", address2 = "", address3 = "", city = "", pincode = "",stateId="";
    EditText nameet, addresset, addresset2, addresset3, cityet, pincodeet, phoneet;
    Button b;
    List<Profile_Model> feedlist;
    String tag_string_req_category3 = "string_req_warrenty";
    String state_id = "";
    ArrayList<String> myarray_state = new ArrayList<>();
    ArrayList<String> myarray2_state = new ArrayList<>();
    Spinner spnr_state;
    List<Response_Model> feedlist_response;
    private String tag_string_req = "string_req";
    List<Database> database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        nameet = (EditText) findViewById(R.id.viewedit_name_edit);
        addresset = (EditText) findViewById(R.id.viewedit_address_edit);
        addresset2 = (EditText) findViewById(R.id.viewedit_address_edit2);
        addresset3 = (EditText) findViewById(R.id.viewedit_address_edit3);
        cityet = (EditText) findViewById(R.id.viewedit_city_edit);
        pincodeet = (EditText) findViewById(R.id.viewedit_city_pincode);
        phoneet = (EditText) findViewById(R.id.viewedit_phone_edit);
        try {
            if (Build.VERSION.SDK_INT > 19) {
                getActionBar().setDisplayHomeAsUpEnabled(true);
                getActionBar().setHomeAsUpIndicator(R.drawable.pf);
            } else {
                getActionBar().setHomeButtonEnabled(true);
                getActionBar().setIcon(R.drawable.pf);
            }


            nameet.setFilters(new InputFilter[] {
                    new InputFilter() {
                        @Override
                        public CharSequence filter(CharSequence cs, int start,
                                                   int end, Spanned spanned, int dStart, int dEnd) {
                            // TODO Auto-generated method stub
                            if(cs.equals("")){ // for backspace
                                return cs;
                            }
                            if(cs.toString().matches("[a-zA-Z ]+")){
                                return cs;
                            }
                            return "";
                        }
                    }
            });

            nameet.setText(getIntent().getExtras().getString("name"));
            addresset.setText(getIntent().getExtras().getString("address1"));
            addresset2.setText(getIntent().getExtras().getString("address2"));
            addresset3.setText(getIntent().getExtras().getString("address3"));
            cityet.setText(getIntent().getExtras().getString("city"));
            pincodeet.setText(getIntent().getExtras().getString("zipcode"));
            phoneet.setText(getIntent().getExtras().getString("phone"));
            stateId=getIntent().getExtras().getString("stateid");



        } catch (Exception e) {
            e.printStackTrace();
        }


        myarray_state.add("Select");
        myarray2_state.add("Select");
        if (StaticVariables.isNetworkConnected(EditProfile.this)) {
            get_states();
        } else {
            Toast.makeText(EditProfile.this,getResources().getString(R.string.no_internet_connection),Toast.LENGTH_SHORT).show();
        }

        spnr_state = (Spinner) findViewById(R.id.registration_state);
        ArrayAdapter<String> adapter4_branch = new ArrayAdapter<>(EditProfile.this, android.R.layout.simple_spinner_dropdown_item, myarray_state);
        adapter4_branch.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnr_state.setAdapter(adapter4_branch);

        spnr_state.setFocusableInTouchMode(true);

        spnr_state.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                if (myarray_state.get(position).equals("Select") || myarray_state.get(position).equals("")) {
                    state_id = "";
                } else {
                    state_id = myarray2_state.get(position);
                }

            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                state_id = "";
            }
        });

    }

    public void get_states() {
        PDialog.show(EditProfile.this);
        StringRequest request = new StringRequest(Request.Method.GET, getResources().getString(R.string.url_motherland) + "state_list.php",
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        PDialog.hide();
                        myarray_state.clear();
                        myarray2_state.clear();

                        myarray_state.add("Select");
                        myarray2_state.add("Select");
                        try {
                            JSONArray ar = new JSONArray(response);
                            for (int i = 0; i < ar.length(); i++) {
                                JSONObject obj = ar.getJSONObject(i);
                                String id = obj.getString("id");
                                myarray2_state.add(id);
                                String name = obj.getString("name");
                                myarray_state.add(name);
                            }

                            ArrayAdapter<String> adapter3_branch = new ArrayAdapter<>(EditProfile.this, android.R.layout.simple_spinner_dropdown_item, myarray_state);
                            adapter3_branch.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            spnr_state.setAdapter(adapter3_branch);
                            spnr_state.setSelection(Integer.parseInt(stateId));

                            try {
                                if (feedlist != null) {
                                    spnr_state.setSelection(adapter3_branch.getPosition(feedlist.get(0).getState()));
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        } catch (JSONException e) {
//                            Log.d("response",response);
//                            Log.d("error in json", "l "+ e);

                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    }
                },

                new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError arg0) {
                        PDialog.hide();
//
                    }
                });
        AppController.getInstance().addToRequestQueue(request, tag_string_req_category3);
    }


    public void updatedisplay() {


        StaticVariables.database.get(0).setName(name);
        StaticVariables.database.get(0).setPhone(phone);

        AlertDialog.Builder builder = new AlertDialog.Builder(EditProfile.this);
        builder.setMessage(getResources().getString(R.string.update_saved))
                .setCancelable(false)
                .setTitle("Success")
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        Intent intent = new Intent(EditProfile.this,ViewProfile.class);
                        //intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

                        intent.putExtra("redirection", "Settings");
                        finish();
                        startActivity(intent);
                        overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }

    private void updata() {
        StringRequest request = new StringRequest(Request.Method.POST, getResources().getString(R.string.url_motherland) + "edit_profile.php", new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                PDialog.hide();
                //Log.d("response", s);
                feedlist_response = Response_JSONParser.parserFeed(s);
                updatedisplay();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                PDialog.hide();
                Toast.makeText(EditProfile.this, getResources().getString(R.string.no_internet_access), Toast.LENGTH_LONG).show();
                //Toast.makeText(Editprofile.this,volleyError.getMessage(),Toast.LENGTH_LONG).show();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("email", StaticVariables.database.get(0).getEmail());
                params.put("id", StaticVariables.database.get(0).getId());
                params.put("name", name);
                params.put("password", StaticVariables.database.get(0).getPassword());
                params.put("phone", phone);

                return params;
            }

        };
        AppController.getInstance().addToRequestQueue(request, tag_string_req);
    }


    public void save(){
        name = nameet.getText().toString();
        address = addresset.getText().toString();
        address2 = addresset2.getText().toString();
        address3 = addresset3.getText().toString();
        city = cityet.getText().toString();
        pincode = pincodeet.getText().toString();
        phone = phoneet.getText().toString();
        validationfunction();
    }

    public void validationfunction() {

        if (name.equals("") || name.isEmpty() || name.trim().isEmpty()) {
            nameet.setError(getResources().getString(R.string.correct_name));
            Toast.makeText(EditProfile.this, R.string.correct_name, Toast.LENGTH_LONG).show();
        } else if (phone.length() != 10) {
            phoneet.setError(getResources().getString(R.string.correct_limit_contact));
            Toast.makeText(EditProfile.this, R.string.correct_limit_contact, Toast.LENGTH_LONG).show();
        } /*else if (address.equals("") || address.isEmpty() || address.trim().isEmpty()) {
            addresset.setError(getResources().getString(R.string.correct_address_error));
            Toast.makeText(EditProfile.this, R.string.correct_address_error, Toast.LENGTH_LONG).show();
        } else if (pincode.equals("") || pincode.isEmpty() || pincode.trim().isEmpty()) {
            pincodeet.setError(getResources().getString(R.string.contact_city));
            Toast.makeText(EditProfile.this, R.string.contact_city, Toast.LENGTH_LONG).show();
        } else if (state_id.equals("")) {
            Toast.makeText(EditProfile.this, getResources().getString(R.string.contact_state), Toast.LENGTH_LONG).show();
        }*/
        else {
            if (StaticVariables.isNetworkConnected(EditProfile.this)) {
                PDialog.show(EditProfile.this);
                updata();
            } else {
                Toast.makeText(EditProfile.this, getResources().getString(R.string.no_internet_connection), Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu items for use in the action barenu
        getMenuInflater().inflate(R.menu.menu_save, menu);

        return true;
    }

    @Override
    public void onBackPressed() {
          Intent in=new Intent(EditProfile.this,ViewProfile.class);
        startActivity(in);
        finish();
        overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                     Intent in=new Intent(EditProfile.this,ViewProfile.class);
                startActivity(in);
                finish();
                    overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);

                return true;
            case R.id.save_button:

                StaticVariables.hideKeyboard(EditProfile.this);
                save();

                return true;

            default:
                return true;
        }
    }
}

