package com.dev.montherland.customers;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.dev.montherland.AppController;
import com.dev.montherland.R;
import com.dev.montherland.adapter.CustomerAddressesListAdapter;
import com.dev.montherland.model.Create_Address_Model;
import com.dev.montherland.model.Customer_Contact_Model;
import com.dev.montherland.parsers.Customer_Address_JSONParser;
import com.dev.montherland.util.PDialog;
import com.dev.montherland.util.StaticVariables;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class Customer_contact_details extends Activity {

    TextView name,website,mobile;
    String id,data_receive="data_receive",orders,contactPos;
    Activity thisActivity=this;
    Bundle bundle;
    private RecyclerView recyclerView;
    private List<Create_Address_Model> persons;
    StaggeredGridLayoutManager mLayoutManager;
    ArrayList<String> addressIdList=new ArrayList<>();
    ArrayList<String> selectList=new ArrayList<>();
    ArrayList<String> updatedSelectIdList=new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_contact_details);
        name=(TextView)findViewById(R.id.add1);
        website=(TextView)findViewById(R.id.website);
        mobile=(TextView)findViewById(R.id.phone);
        bundle=getIntent().getExtras();


        recyclerView = (RecyclerView) findViewById(R.id.rv);
        recyclerView.setHasFixedSize(true);
        mLayoutManager = new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(mLayoutManager);

        try {
            if (!(bundle.isEmpty())){
                StaticVariables.bundle=bundle;
            }

        }catch (Exception e){
            e.printStackTrace();
        }

        try {

            bundle=StaticVariables.bundle;
            id=bundle.getString("id");

            //  name.setText(bundle.getString("name"));
            //website.setText(bundle.getString("email"));
            //mobile.setText(bundle.getString("phone"));
            contactPos=bundle.getString("position");

            StaticVariables.customerContact=id;

            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
            if (Build.VERSION.SDK_INT > 19) {
                getActionBar().setDisplayHomeAsUpEnabled(true);
                getActionBar().setHomeAsUpIndicator(R.drawable.pf);
            } else {
                getActionBar().setHomeButtonEnabled(true);
                getActionBar().setIcon(R.drawable.pf);
            }
        }

        catch (Exception e){
            e.printStackTrace();
        }



        if (StaticVariables.isNetworkConnected(thisActivity)) {
            getCustomerList();
            getAddressList();
        }else {
            Toast.makeText(thisActivity, getResources().getString(R.string.no_internet_connection), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                updateOnbackPressed();
                return true;
            case R.id.next_button:

            default:
                return true;

        }
    }


    public void updateOnbackPressed(){


        try {


            for (int i = 0; i < persons.size(); i++) {
                Log.v("addressids", persons.get(i).getIsChecked() + "");

                updatedSelectIdList.add(persons.get(i).getIsChecked() + "");

                if (persons.get(i).getIsChecked() == true) {
                    addressIdList.add(persons.get(i).getId());
                }
            }

            Log.v("updatedlist", updatedSelectIdList.toString() + " " + "selectList=" + selectList.toString());

            Boolean isChanged = false;

            for (int i = 0; i < updatedSelectIdList.size(); i++) {

                if ((selectList.get(i).equals(updatedSelectIdList.get(i)))) {
                    isChanged = true;
                }
            }

            if (StaticVariables.isSelected) {
                android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(thisActivity);
                builder.setCancelable(false)
                        .setMessage(getString(R.string.save_selection_alert))
                        .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        if (StaticVariables.isNetworkConnected(thisActivity)) {
                                            updateAddressSelect();               }
                                        else {
                                            Toast.makeText(thisActivity, getResources().getString(R.string.no_internet_connection), Toast.LENGTH_SHORT).show();
                                        }


                                    }
                                }

                        ).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        Intent in = new Intent(thisActivity, Customer_contacts_list.class);
                        startActivity(in);
                        finish();
                        overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
                    }
                });

                builder.show();
            } else {

                Intent in = new Intent(thisActivity, Customer_contacts_list.class);
                startActivity(in);
                finish();

                overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
            }
        }catch (Exception e){
            Intent in = new Intent(thisActivity, Customer_contacts_list.class);
            startActivity(in);
            finish();

            overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
            e.printStackTrace();
        }
    }
    public void onBackPressed() {

    updateOnbackPressed();


    }

    public void getCustomerList() {


        //PDialog.show(thisActivity);
        StringRequest request = new StringRequest(Request.Method.POST, getResources().getString(R.string.url_motherland) + "company_contact_list.php?",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                       // PDialog.hide();
                        Log.v("response", response + "");
                        try {
                            JSONArray ar = new JSONArray(response);


                                JSONObject parentObject = ar.getJSONObject(Integer.parseInt(contactPos));
                                Customer_Contact_Model flower = new Customer_Contact_Model();
                                id=parentObject.getString("id");

                                name.setText(parentObject.getString("name"));
                                website.setText(parentObject.getString("email"));
                                mobile.setText(parentObject.getString("phone"));




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
                params.put("customer_id", StaticVariables.customerId);
                params.put("password", StaticVariables.database.get(0).getPassword());
                params.put("id", StaticVariables.database.get(0).getId());
                return params;
            }
        };
        AppController.getInstance().addToRequestQueue(request, "data_receive");
        Log.v("request", request + "");
    }

    public void edit_contact(View view){
        Intent in=new Intent(thisActivity,CreateCustomerContact.class);
        in.putExtra("name",name.getText());
        in.putExtra("phone",mobile.getText());
        in.putExtra("email",website.getText());
        in.putExtra("intent_from", "edit_contact");
        in.putExtra("position", contactPos);
        in.putExtra("id",id);
        startActivity(in);
        finish();
        overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
    }


    public void updateAddressSelect() {
        PDialog.show(thisActivity);
        StringRequest request = new StringRequest(Request.Method.POST, getResources().getString(R.string.url_motherland) + "company_customer_address_select.php?",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        PDialog.hide();
                        Log.v("response", response + "");

                        Intent in=new Intent(thisActivity,Customer_contacts_list.class);
                        startActivity(in);
                        finish();

                        overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);

                        try {
                            PDialog.hide();
                            JSONArray ar = new JSONArray(response);

                            // response_model= Response_JSONParser.parserFeed(response);

                        } catch (JSONException e) {
                            PDialog.hide();
                            //Log.d("response", response);
                            //Log.d("error in json", "l " + e);

                        } catch (Exception e) {
                            PDialog.hide();
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
                Gson gson=new Gson();
                String addressId=gson.toJson(addressIdList);

                Map<String, String> params = new HashMap<>();
                params.put("email", StaticVariables.database.get(0).getEmail());
                params.put("password", StaticVariables.database.get(0).getPassword());
                // params.put("customer_id", "1");
                params.put("customer_id", StaticVariables.customerId);
                params.put("customer_contact_id",id);
                params.put("id", StaticVariables.database.get(0).getId());
                params.put("phone", String.valueOf(mobile.getText()));
                params.put("address_id", addressId);
                params.put("name", String.valueOf(name.getText()));

                Log.v("cusId", StaticVariables.customerId);
                Log.v("result","customer_id="+StaticVariables.customerId+"company_customer_id="+id+"phone="+String.valueOf(mobile.getText())+"address_id="+addressId+"name="+String.valueOf(name.getText()));
                Log.v("contactId",id);
                return params;
            }
        };
        AppController.getInstance().addToRequestQueue(request, data_receive);
        Log.v("request", request + "");
    }


    public void getAddressList() {
        PDialog.show(thisActivity);
        StringRequest request = new StringRequest(Request.Method.POST, getResources().getString(R.string.url_motherland) + "customer_company_address_selected.php?",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        PDialog.hide();
                        Log.v("response", response + "");

                        try {
                            PDialog.hide();
                            JSONArray ar = new JSONArray(response);
                            persons = Customer_Address_JSONParser.parserFeed(response);

                            ArrayList<String> arList=new ArrayList<>();

                            for (int i=0;i<persons.size();i++) {
                                String addLIst=persons.get(i).getId();
                                selectList.add(persons.get(i).getIsChecked()+"");
                                arList.add(addLIst);
                            }

                            ArrayList<String> nonDupList = new ArrayList<String>();
                            ArrayList<String>Listpos = new ArrayList<>();

                            Iterator<String> dupIter = arList.iterator();
                            int j=0;
                            while(dupIter.hasNext())
                            {
                                String dupWord = dupIter.next();
                                if(nonDupList.contains(dupWord))
                                {
                                    dupIter.remove();
                                    Listpos.add(j-1+"");
                                    Log.v("listPos",j+"");
                                    j++;

                                }else
                                {
                                    nonDupList.add(dupWord);
                                    j++;
                                }
                            }
                            Collections.reverse(Listpos);

                            for (int i=0;i<Listpos.size();i++){
                                persons.remove(Integer.parseInt(Listpos.get(i)));
                            }




                            if (persons.get(0).getAddressline1().contains("No Data")||persons.get(0).getAddressline1().contains("NO Data")){

                                Toast.makeText(thisActivity,getResources().getString(R.string.no_address),Toast.LENGTH_SHORT).show();
                            }
                            else {


                                recyclerView.setAdapter(new CustomerAddressesListAdapter(persons, thisActivity,"contact_details_address"));
                            }
                            // response_model= Response_JSONParser.parserFeed(response);

                        } catch (JSONException e) {
                            PDialog.hide();
                            //Log.d("response", response);
                            //Log.d("error in json", "l " + e);

                        } catch (Exception e) {
                            PDialog.hide();
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
                params.put("email", StaticVariables.database.get(0).getEmail());
                params.put("password", StaticVariables.database.get(0).getPassword());
                // params.put("customer_id", "1");
                params.put("customer_id", StaticVariables.customerId);
                params.put("company_customer_id", StaticVariables.customerContact);
                params.put("id", StaticVariables.database.get(0).getId());

                Log.v("cusId", StaticVariables.customerId);
                Log.v("contactId",StaticVariables.customerContact);
                return params;
            }
        };
        AppController.getInstance().addToRequestQueue(request, data_receive);
        Log.v("request", request + "");
    }


}
