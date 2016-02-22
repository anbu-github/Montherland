package com.dev.montherland;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.dev.montherland.adapter.CreateOrderAdapter;
import com.dev.montherland.model.GarmentListModel;
import com.dev.montherland.parsers.Garment_JSONParer;
import com.dev.montherland.util.PDialog;
import com.dev.montherland.util.StaticVariables;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CreatePurchaseOrder extends AppCompatActivity implements CreateOrderAdapter.DataFromAdapterToActivity {

    Activity thisActivity = this;
    String data_receive = "string_req_recieve";

    ArrayList<String> customerLIst = new ArrayList<>();
    ArrayList<String> customerIdList = new ArrayList<>();
    ArrayList<String> companyList = new ArrayList<>();
    ArrayList<String> companyIdList = new ArrayList<>();
    ArrayList<String> garmentList = new ArrayList<>();
    ArrayList<String> orderTypeList = new ArrayList<>();
    ArrayList<String> orderTypeIdList = new ArrayList<>();
    ArrayList<String> garmentWashIdList = new ArrayList<>();
    ArrayList<String> garmentWashTypeList = new ArrayList<>();
    ArrayList<String> washTypeList = new ArrayList<>();

    Spinner customerList, Companylist, orderType;

    ArrayAdapter<String> dataAdapter, dataAdapter2, orderTypeAdapter, dataAdapter4, washTypeAdapter;
    List<GarmentListModel> garment_model;
    String companyId, customerId, companyName, customerName, order_type;
    LinearLayout dateLayout;
    DatePickerDialog.OnDateSetListener date, pickup_date;
    Calendar myCalendar = Calendar.getInstance();
    TextView dateText, timeText, pickupTime;
    EditText productInstr;
    TimePickerDialog.OnTimeSetListener time;

    String myFormat = "dd-MM-yyyy";
    String myFormat1 = "yyyy-MM-dd HH:mm:ss";
    SimpleDateFormat sdf = new SimpleDateFormat(myFormat);
    SimpleDateFormat sdf1 = new SimpleDateFormat(myFormat1);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_purchase_order);
        customerList = (Spinner) findViewById(R.id.customer_company_list);
        Companylist = (Spinner) findViewById(R.id.customer_list);
        orderType = (Spinner) findViewById(R.id.order_type);
        Companylist.requestFocus();
        dateLayout = (LinearLayout) findViewById(R.id.date_layout);
        timeText = (TextView) findViewById(R.id.expect_pickup_date);
        pickupTime = (TextView) findViewById(R.id.expect_pickup_time);
        dateText = (TextView) findViewById(R.id.expect_del_date);
        productInstr = (EditText) findViewById(R.id.instruction);


        if (StaticVariables.isNetworkConnected(thisActivity)) {
            getCompanyList();
            getGarmentTypes();
            getOrderTypeList();
        } else {
            Toast.makeText(thisActivity, getResources().getString(R.string.no_internet_access), Toast.LENGTH_LONG).show();
        }


        date = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                Log.v("date", monthOfYear + "");

                updateDate();

            }

        };
        pickup_date = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                Log.v("date", monthOfYear + "");

                updatePickupDate();

            }

        };


        dateLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                new DatePickerDialog(thisActivity, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();


            }
        });

        timeText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(thisActivity, pickup_date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();


            }
        });


        time = new TimePickerDialog.OnTimeSetListener() {
            public void onTimeSet(TimePicker view, int hourOfDay,
                                  int minute) {
                myCalendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                myCalendar.set(Calendar.MINUTE, minute);
                updateTime();
            }
        };


        pickupTime.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                new TimePickerDialog(thisActivity,
                        time,
                        myCalendar.get(Calendar.HOUR_OF_DAY),
                        myCalendar.get(Calendar.MINUTE),
                        true).show();
            }
        });


        try {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        } catch (Exception e) {
            e.printStackTrace();
        }


        dataAdapter = new ArrayAdapter<>(this,
                R.layout.spinner_layout, customerLIst);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        dataAdapter2 = new ArrayAdapter<>(this,
                R.layout.spinner_layout, companyList);
        dataAdapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        dataAdapter4 = new ArrayAdapter<>(this,
                R.layout.spinner_layout, garmentList);
        dataAdapter4.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        orderTypeAdapter = new ArrayAdapter<>(this,
                R.layout.spinner_layout, orderTypeList);
        orderTypeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        washTypeAdapter = new ArrayAdapter<>(thisActivity,
                android.R.layout.simple_spinner_item, washTypeList);
        washTypeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);


        customerList.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                try {
                   StaticVariables.customerId=customerId;
                    StaticVariables.customerContact = customerIdList.get(position-1).toString();
                    customerName = customerList.getSelectedItem().toString();

                    Toast.makeText(thisActivity, StaticVariables.customerContact,Toast.LENGTH_SHORT).show();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        Companylist.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                try {
                    companyId = position + "";
                    customerLIst.clear();
                    companyName = String.valueOf(Companylist.getSelectedItem());
                    StaticVariables.companyName = companyName;
                    if (StaticVariables.isNetworkConnected(thisActivity)) {
                        getCustomerList();
                    } else {
                        Toast.makeText(thisActivity, "Please check the network connection", Toast.LENGTH_SHORT).show();
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        orderType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                try {
                    order_type = orderType.getSelectedItem().toString();
                    StaticVariables.orderType = order_type;
                    StaticVariables.orderTypeId = orderTypeIdList.get(position - 1);


                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    public void updateDate() {
        //In which you need put here

        dateText.setText(sdf.format(myCalendar.getTime()));
        StaticVariables.deliveryDate = sdf.format(myCalendar.getTime());
        StaticVariables.deliveryDateTIme = sdf1.format(myCalendar.getTime());

        Log.v("currentTime", myCalendar.getTime() + "");
        Log.v("pickedDateTIme", StaticVariables.pickedDateTIme);

    }

    public void updatePickupDate() {

        timeText.setText(sdf.format(myCalendar.getTime()));
        String pick_date = sdf.format(myCalendar.getTime()) + "";
        StaticVariables.pickupDate = pick_date;

        StaticVariables.pickedDateTIme = sdf1.format(myCalendar.getTime());


        Log.v("currentTime", StaticVariables.pickedDateTIme);


    }


    private void updateTime() {
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
        pickupTime.setText(sdf.format(myCalendar.getTime()));
    }

    public void getCustomerList() {

        // PDialog.show(thisActivity);
        StringRequest request = new StringRequest(Request.Method.POST, getResources().getString(R.string.url_motherland) + "customer_company_list.php?",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        PDialog.hide();
                        Log.v("response", response + "");
                        try {
                            PDialog.hide();
                            JSONArray ar = new JSONArray(response);

                            customerLIst.add("Select Contact");
                            for (int i = 0; i < ar.length(); i++) {
                                JSONObject parentObject = ar.getJSONObject(i);
                                if (!parentObject.getString("name").equals("NO Data")) {
                                    customerLIst.add(parentObject.getString("name"));
                                    customerIdList.add(parentObject.getString("id"));
                                    StaticVariables.customerName = parentObject.getString("name");
                                    Log.v("name", parentObject.getString("name"));
                                }
                            }
                            customerList.setAdapter(dataAdapter);


                        } catch (Exception e) {
                            PDialog.hide();
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
                params.put("customer_id", companyId);
                params.put("password", StaticVariables.database.get(0).getPassword());
                params.put("id", StaticVariables.database.get(0).getId());
                return params;
            }
        };
        AppController.getInstance().addToRequestQueue(request, data_receive);
        Log.v("request", request + "");
    }

    public void getOrderTypeList() {

        // PDialog.show(thisActivity);
        StringRequest request = new StringRequest(Request.Method.POST, getResources().getString(R.string.url_motherland) + "order_type_list.php?",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        PDialog.hide();
                        Log.v("response", response + "");
                        try {
                            PDialog.hide();
                            JSONArray ar = new JSONArray(response);

                            orderTypeList.add("Select Order Type");
                            for (int i = 0; i < ar.length(); i++) {
                                JSONObject parentObject = ar.getJSONObject(i);
                                orderTypeList.add(parentObject.getString("name"));
                                orderTypeIdList.add(parentObject.getString("id"));
                                Log.v("name", parentObject.getString("name"));
                                //Log.d("success", parentObject.getString("success"));
                            }
                            orderType.setAdapter(orderTypeAdapter);


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
                params.put("id", StaticVariables.database.get(0).getId());
                return params;
            }
        };
        AppController.getInstance().addToRequestQueue(request, data_receive);
        Log.v("request", request + "");
    }

    public void getCompanyList() {
        PDialog.show(thisActivity);
        StringRequest request = new StringRequest(Request.Method.POST, getResources().getString(R.string.url_motherland) + "customer_list.php?",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.v("response", response + "");
                        try {
                            JSONArray ar = new JSONArray(response);
                            companyList.add("Select Company");
                            for (int i = 0; i < ar.length(); i++) {
                                JSONObject parentObject = ar.getJSONObject(i);
                                companyList.add(parentObject.getString("name"));
                                companyIdList.add(parentObject.getString("id"));

                                Log.v("company_name", parentObject.getString("name"));
                            }
                            Companylist.setAdapter(dataAdapter2);


                        } catch (Exception e) {
                            PDialog.hide();
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
                params.put("id", StaticVariables.database.get(0).getId());

                return params;
            }
        };
        AppController.getInstance().addToRequestQueue(request, data_receive);
        Log.v("request", request + "");
    }

    public void getGarmentTypes() {
        StringRequest request = new StringRequest(Request.Method.POST, getResources().getString(R.string.url_motherland) + "garment_type_list.php?",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        PDialog.hide();
                        Log.v("response", response + "");
                        try {
                            PDialog.hide();
                            JSONArray ar = new JSONArray(response);
                            garment_model = Garment_JSONParer.parserFeed(response);
                            Log.v("garment size", garment_model.size() + "");

                        } catch (Exception e) {
                            PDialog.hide();
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
                params.put("id", StaticVariables.database.get(0).getId());


                return params;
            }
        };
        AppController.getInstance().addToRequestQueue(request, data_receive);
        Log.v("request", request + "");
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu items for use in the action barenu
        getMenuInflater().inflate(R.menu.menu_next, menu);

        return true;
    }

    public void onBackPressed() {

        super.onBackPressed();
        Intent in = new Intent(thisActivity, NavigataionActivity.class);
        startActivity(in);
        finish();
        overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Intent intent = new Intent(thisActivity, NavigataionActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);

                return true;
            case R.id.next_button:
                Toast.makeText(thisActivity, "next", Toast.LENGTH_SHORT).show();

                if (companyName.equals("Select Company")) {

                    Toast.makeText(thisActivity, "Please select company", Toast.LENGTH_SHORT).show();
                } else if (customerName.equals("Select Contact")) {
                    Toast.makeText(thisActivity, "Please select contact", Toast.LENGTH_SHORT).show();

                } else if (order_type.equals("Select Order Type")) {
                    Toast.makeText(thisActivity, "Please select order type", Toast.LENGTH_SHORT).show();

                } else if (timeText.getText().toString().equals("DD-MM-YYYY")) {
                    Toast.makeText(thisActivity, "Please set estimated pickup date", Toast.LENGTH_SHORT).show();

                } else if (dateText.getText().toString().equals("DD-MM-YYYY")) {
                    Toast.makeText(thisActivity, "Please set estimated delivery date", Toast.LENGTH_SHORT).show();

                } else {
                    ArrayList<String> garment_type = new ArrayList<>();
                    ArrayList<String> garment_quantity = new ArrayList<>();
                    ArrayList<String> garment_id = new ArrayList<>();
                    int no = 0;

                    for (int i = 0; i <= garment_model.size(); i++) {

                        try {
                            if (garment_model.get(i).getGarmentQuantity().equals("")) {

                                no++;

                            } else {

                                Boolean isValidNo = StaticVariables.checkIfNumber(garment_model.get(i).getGarmentQuantity());

                                if (!isValidNo) {
                                    Toast.makeText(thisActivity, "Please enter valid number", Toast.LENGTH_SHORT).show();
                                } else {

                                    garment_quantity.add(garment_model.get(i).getGarmentQuantity());
                                    garment_type.add(garment_model.get(i).getGarmentType());
                                    garment_id.add(garment_model.get(i).getGarmentTypeId());
                                    garmentWashTypeList.add(garment_model.get(i).getGarmentWashType());
                                    garmentWashIdList.add(garment_model.get(i).getGarmentTypeId());
                                }

                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    }
                    StaticVariables.prodcutInstr = productInstr.getText().toString();


                    Log.v("quantity no", garment_model.get(0).getGarmentQuantity().toString());
                    Intent in = new Intent(CreatePurchaseOrder.this, GarmentsDataActivity.class);
                    startActivity(in);


                }


                return true;

            default:
                return true;
        }
    }

    @Override
    public void garmentQuantity(String quantity, int i) {
        garment_model.get(i).setGarmentQuantity(quantity);
        Log.v("quantity", quantity + "");

    }

    @Override
    public void garmentStyle(String style, int i) {
        //  garment_model.get(i).setGarmentStyle(style);
        StaticVariables.garmentStyle.add(style);
        Log.v("garmentStyle", style);


    }

    @Override
    public void washType(String type, int i, int pos) {
        try {


            // garment_model.get(i).setGarmentWashType(type);
            //  garment_model.get(i).setGarmentWashId(i + "");
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void garmentInstr(String type, int i) {

    }
}