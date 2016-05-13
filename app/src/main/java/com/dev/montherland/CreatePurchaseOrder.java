package com.dev.montherland;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.dev.montherland.util.PDialog;
import com.dev.montherland.util.StaticVariables;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class CreatePurchaseOrder extends Activity {

    Activity thisActivity = this;
    String data_receive = "string_req_recieve";

    ArrayList<String> customerLIst = new ArrayList<>();
    ArrayList<String> customerIdList = new ArrayList<>();
    ArrayList<String> companyList = new ArrayList<>();
    ArrayList<String> companyIdList = new ArrayList<>();
    ArrayList<String> garmentList = new ArrayList<>();
    ArrayList<String> orderTypeList = new ArrayList<>();
    ArrayList<String> orderTypeIdList = new ArrayList<>();
    ArrayList<String> washTypeList = new ArrayList<>();
    ArrayList<String> serviceTypeList = new ArrayList<>();
    ArrayList<String> serviceTypeIdList = new ArrayList<>();
    static final int SHOW_DATE_PICKER_DIALOG = 1;
    Spinner customerList, Companylist, orderType,ServiceType;

    ArrayAdapter<String> dataAdapter, dataAdapter2, orderTypeAdapter, dataAdapter4, washTypeAdapter,serviceTypeAdapter;

    String companyId, customerId, companyName, customerName, order_type, menuTitle="", order_id, statusId;
    DatePickerDialog.OnDateSetListener date, pickup_date;
    Calendar myCalendar = Calendar.getInstance();
    Calendar myCalendar1 = Calendar.getInstance();
    TextView deliveryDate, pickupDate, pickupTime, deliveryTime;
    EditText productInstr;
    TimePickerDialog.OnTimeSetListener time;
    String contactId, editCustomerContactId;
    String myFormat = "dd-MM-yyyy";
    String myFormat1 = "yyyy-MM-dd HH:mm:ss";
    SimpleDateFormat sdf = new SimpleDateFormat(myFormat);
    SimpleDateFormat sdf1 = new SimpleDateFormat(myFormat1);

    LinearLayout instr_layout,linearLayout,delivery_layout;
    ScrollView scrollview;
    String selectedTime,intent_from="";
    Date pDate, dDate,date1;

    Boolean isload=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_create_purchase_order);
        customerList = (Spinner) findViewById(R.id.customer_company_list);
        Companylist = (Spinner) findViewById(R.id.customer_list);
        orderType = (Spinner) findViewById(R.id.order_type);
        ServiceType = (Spinner) findViewById(R.id.service_type);
        Companylist.requestFocus();
        pickupDate = (TextView) findViewById(R.id.expect_pickup_date);
        pickupTime = (TextView) findViewById(R.id.expect_pickup_time);
        deliveryDate = (TextView) findViewById(R.id.expect_del_date);
        deliveryTime = (TextView) findViewById(R.id.expect_del_time);
        productInstr = (EditText) findViewById(R.id.instruction);
        instr_layout = (LinearLayout) findViewById(R.id.instr_layout);
        linearLayout = (LinearLayout) findViewById(R.id.linearLayout);
        delivery_layout = (LinearLayout) findViewById(R.id.delivery_layout);
        scrollview = (ScrollView) findViewById(R.id.scrollview);

        myCalendar.set(Calendar.HOUR,9);
        myCalendar1.set(Calendar.HOUR,9);
        myCalendar.set(Calendar.AM_PM,0);
        myCalendar1.set(Calendar.AM_PM,0);
        myCalendar.set(Calendar.MINUTE,0);
        myCalendar1.set(Calendar.MINUTE,0);
        pickupTime.setText("09:00");
        deliveryTime.setText("09:00");

        menuTitle = "Next";

        customerLIst.add("Select");
        customerIdList.add("Select");

        companyIdList.add("Select");
        companyList.add("Select");
        linearLayout.setVisibility(View.INVISIBLE);


        try {
            intent_from=getIntent().getExtras().getString("intent_from");
        }catch (Exception e){
            e.printStackTrace();

        }

        try {
            if (!(getIntent().getExtras().getString("order_details") == null)) {
                delivery_layout.setBackground(getResources().getDrawable(R.drawable.layout_border4));
                PDialog.show(thisActivity);
                getActionBar().setTitle("Edit Order Details");
                Intent intent = getIntent();
                //   Bundle args = intent.getBundleExtra("BUNDLE");
                order_id = intent.getExtras().getString("order_id");
                menuTitle = "Save";
                Log.v("order_id", order_id);
                instr_layout.setVisibility(View.GONE);

                Companylist.setBackgroundColor(Color.parseColor("#F1F1F1"));
                Companylist.setClickable(false);
            }


        } catch (Exception e) {
            PDialog.show(thisActivity);
            e.printStackTrace();
        }


        if (StaticVariables.isNetworkConnected(thisActivity)) {
            // PDialog.show(thisActivity);
            getCompanyList();
            getOrderTypeList();
            getServiceList();

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
                myCalendar1.set(Calendar.YEAR, year);
                myCalendar1.set(Calendar.MONTH, monthOfYear);
                myCalendar1.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                Log.v("date", monthOfYear + "");


                updatePickupDate();

            }

        };

        deliveryDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (menuTitle.equals("Save")){

                }else {

                    new DatePickerDialog(thisActivity, date, myCalendar
                            .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                            myCalendar.get(Calendar.DAY_OF_MONTH)).show();
                }


            }
        });



        pickupDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(thisActivity, pickup_date, myCalendar1
                        .get(Calendar.YEAR), myCalendar1.get(Calendar.MONTH),
                        myCalendar1.get(Calendar.DAY_OF_MONTH)).show();


            }
        });


        time = new TimePickerDialog.OnTimeSetListener() {
            public void onTimeSet(TimePicker view, int hourOfDay,
                                  int minute) {
                if (selectedTime.contains("pickedTime")) {
                    myCalendar1.set(Calendar.HOUR_OF_DAY, hourOfDay);
                    myCalendar1.set(Calendar.MINUTE, minute);
                    updateTime();
                }
                else {
                    myCalendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                    myCalendar.set(Calendar.MINUTE, minute);
                    updateTime();
                }
            }
        };

        pickupTime.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                new TimePickerDialog(thisActivity,
                        time,
                        myCalendar1.get(myCalendar1.HOUR_OF_DAY),
                        myCalendar1.get(myCalendar1.MINUTE),
                        true).show();
                selectedTime = "pickedTime";
            }
        });


        deliveryTime.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                if (menuTitle.contains("Save")){

                }else {
                    new TimePickerDialog(thisActivity,
                            time,
                            myCalendar.get(myCalendar.HOUR_OF_DAY),
                            myCalendar.get(myCalendar.MINUTE),
                            true).show();
                    selectedTime = "deliveryTime";
                }
            }
        });

        try {
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
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
        serviceTypeAdapter = new ArrayAdapter<>(thisActivity,
                android.R.layout.simple_spinner_item, serviceTypeList);
        serviceTypeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        customerList.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                if (customerLIst.size()==1){
                    Toast.makeText(thisActivity,getResources().getString(R.string.no_contacts_found),Toast.LENGTH_LONG).show();
                }

                return false;
            }
        });

        customerList.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                try {

                    StaticVariables.customerContact = customerIdList.get(position).toString();
                    Log.v("customer_id", StaticVariables.customerContact);
                    customerName = customerList.getSelectedItem().toString();

                    StaticVariables.customerName=customerName;

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
                    StaticVariables.customerId=companyIdList.get(position).toString();
                    Log.v("customerid",StaticVariables.customerId);

                    if (companyName.equals("Select")){
                        customerList.setClickable(false);
                    }else {
                        customerList.setClickable(true);
                    }

                    if (StaticVariables.isNetworkConnected(thisActivity)) {
                        getCustomerList();
                    } else {
                        Toast.makeText(thisActivity, "Please check the network connection", Toast.LENGTH_SHORT).show();
                    }


                } catch (Exception e) {
                    // PDialog.hide();
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
                    // PDialog.hide();

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



        Calendar calc = Calendar.getInstance();
        calc.set(Calendar.DAY_OF_MONTH, calc.get(Calendar.DAY_OF_MONTH) - 1);
        if (myCalendar.getTime().before(calc.getTime())) {
            Toast.makeText(thisActivity, "Please select valid date", Toast.LENGTH_SHORT).show();
        } else {

            deliveryDate.setText(sdf.format(myCalendar.getTime()));
            StaticVariables.deliveryDate = sdf.format(myCalendar.getTime());
            StaticVariables.deliveryDateTIme = sdf1.format(myCalendar.getTime());

            Log.v("currentTime", myCalendar.getTime() + "");
            Log.v("pickedDateTIme", StaticVariables.pickedDateTIme);

        }

    }

    public void updatePickupDate() {

        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.DAY_OF_MONTH, cal.get(Calendar.DAY_OF_MONTH) - 1);
        if (myCalendar1.getTime().before(cal.getTime())) {
            Toast.makeText(thisActivity, "Please select valid date", Toast.LENGTH_SHORT).show();
        } else {
            pickupDate.setText(sdf.format(myCalendar1.getTime()));
            String pick_date = sdf.format(myCalendar1.getTime()) + "";
            StaticVariables.pickupDate = pick_date;

            StaticVariables.pickedDateTIme = sdf1.format(myCalendar1.getTime());

            Log.v("currentTime", StaticVariables.pickedDateTIme);
        }

    }



    private void updateTime() {
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");

        if (selectedTime.contains("pickedTime")) {
            pickupTime.setText(sdf.format(myCalendar1.getTime()));
            //StaticVariables.pickedDateTIme = sdf1.format(myCalendar1.getTime());
        }

        if (selectedTime.contains("deliveryTime")) {
            deliveryTime.setText(sdf.format(myCalendar.getTime()));
            StaticVariables.deliveryDateTIme = sdf1.format(myCalendar.getTime());

        }
    }

    public void getEditOrderDetails() {

        //PDialog.show(thisActivity);
        StringRequest request = new StringRequest(Request.Method.POST, getResources().getString(R.string.url_motherland) + "edit_order_details_view.php?",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // PDialog.hide();
                        Log.v("response", response + "");
                        try {
                            JSONArray ar = new JSONArray(response);
                            for (int i = 0; i < ar.length(); i++) {
                                JSONObject parentObject = ar.getJSONObject(i);
                                if (!parentObject.getString("status_id").equals("NO Data")) {

                                    editCustomerContactId = (parentObject.getString("customer_contact_id"));
                                    statusId = parentObject.getString("status_id");
                                    orderType.setSelection(Integer.parseInt(parentObject.getString("order_type_id")));
                                    Companylist.setSelection(Integer.parseInt(parentObject.getString("customer_id")));
                                    contactId = parentObject.getString("customer_id");

                                    Log.v("editCustomerContactId", editCustomerContactId);

                                    for (int in = 0; in < customerIdList.size(); in++) {
                                        if (customerIdList.get(in).equals(editCustomerContactId)) {
                                            customerList.setSelection(in);
                                            PDialog.hide();
                                            linearLayout.setVisibility(View.VISIBLE);

                                        }
                                    }

                                    String start_dt = parentObject.getString("expected_pick_up_date");
                                    String toDeliveryDate = parentObject.getString("expected_delivery_date");
                                    StaticVariables.deliveryDefultDate=toDeliveryDate;
                                    StaticVariables.pickupDefaultDate=start_dt;


                                    Log.v("Str date",start_dt);
                                    Log.v("del date",toDeliveryDate);

                                    DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                                    Date date = formatter.parse(start_dt);

                                    myCalendar.setTime(formatter.parse(toDeliveryDate));
                                    myCalendar1.setTime(formatter.parse(start_dt));
                                   // myCalendar1.set(Calendar.MINUTE,date.getMinutes());


                                    SimpleDateFormat newFormat = new SimpleDateFormat("HH:mm");
                                    SimpleDateFormat newFormat1 = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
                                    String pickTime = newFormat.format(date);

                                    String pickup_date = newFormat1.format(date);

                                    if (StaticVariables.delDate!=""){
                                         date1 = formatter.parse(StaticVariables.delDate);
                                         myCalendar.setTime(date1);
                                    }
                                    else {
                                         date1 = formatter.parse(toDeliveryDate);
                                    }
                                    String delivery_date = newFormat1.format(date1);
                                    String delTime = newFormat.format(date1);

                                    deliveryDate.setText(delivery_date);
                                    deliveryTime.setText(delTime);
                                    pickupTime.setText(pickTime);
                                    pickupDate.setText(pickup_date);

                                }
                            }


                        } catch (Exception e) {
                            //PDialog.hide();
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
                params.put("id", StaticVariables.database.get(0).getId());
                params.put("password", StaticVariables.database.get(0).getPassword());
                params.put("order_id", order_id);
                return params;
            }
        };
        AppController.getInstance().addToRequestQueue(request, data_receive);
        Log.v("request", request + "");
    }

    public void getServiceList() {

        //PDialog.show(thisActivity);
        StringRequest request = new StringRequest(Request.Method.POST, getResources().getString(R.string.url_motherland) + "service_type_list.php?",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // PDialog.hide();
                        Log.v("response", response + "");
                        try {
                            JSONArray ar = new JSONArray(response);

                            serviceTypeList.clear();
                            serviceTypeIdList.clear();

                            serviceTypeList.add("Select");
                            serviceTypeIdList.add("Select");

                            for (int i = 0; i < ar.length(); i++) {
                                JSONObject parentObject = ar.getJSONObject(i);
                                if (!parentObject.getString("name").equals("NO Data")) {
                                    serviceTypeList.add(parentObject.getString("name"));
                                    serviceTypeIdList.add(parentObject.getString("id"));
                                    Log.v("name", parentObject.getString("name"));
                                }

                            }
                            ServiceType.setAdapter(serviceTypeAdapter);


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

                return params;
            }
        };
        AppController.getInstance().addToRequestQueue(request, data_receive);
        Log.v("request", request + "");
    }

    public void getCustomerList() {

        if (!menuTitle.contains("Save")){
            if (isload) {
                PDialog.show(thisActivity);

            }
        }
        //PDialog.show(thisActivity);
        StringRequest request = new StringRequest(Request.Method.POST, getResources().getString(R.string.url_motherland) + "customer_company_list.php?",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // PDialog.hide();
                        Log.v("response", response + "");
                        try {
                            JSONArray ar = new JSONArray(response);

                            customerIdList.clear();
                            customerLIst.clear();

                            customerLIst.add("Select");
                            customerIdList.add("Select");

                            for (int i = 0; i < ar.length(); i++) {
                                JSONObject parentObject = ar.getJSONObject(i);
                                if (!parentObject.getString("name").equals("NO Data")) {
                                    customerLIst.add(parentObject.getString("name"));
                                    customerIdList.add(parentObject.getString("id"));
                                    StaticVariables.customerName = parentObject.getString("name");
                                    Log.v("name", parentObject.getString("name"));
                                } else if (parentObject.getString("name").equals("NO Data")){

                                 //   Toast.makeText(thisActivity, getResources().getString(R.string.no_contacts_found), Toast.LENGTH_SHORT).show();

                                }
                                if (menuTitle.contains("Save")) {


                                    if (StaticVariables.isNetworkConnected(thisActivity)) {
                                        getEditOrderDetails();                   }
                                    else {
                                        Toast.makeText(thisActivity, getResources().getString(R.string.no_internet_connection), Toast.LENGTH_SHORT).show();
                                    }

                                } else {
                                    PDialog.hide();
                                    isload=true;
                                    linearLayout.setVisibility(View.VISIBLE);
                                }


                            }
                            customerList.setAdapter(dataAdapter);


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

        //  PDialog.show(thisActivity);
        StringRequest request = new StringRequest(Request.Method.POST, getResources().getString(R.string.url_motherland) + "order_type_list.php?",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // PDialog.hide();
                        Log.v("response", response + "");
                        try {
                            JSONArray ar = new JSONArray(response);
                            orderTypeList.add("Select");
                            for (int i = 0; i < ar.length(); i++) {
                                JSONObject parentObject = ar.getJSONObject(i);
                                orderTypeList.add(parentObject.getString("name"));
                                orderTypeIdList.add(parentObject.getString("id"));
                                Log.v("name", parentObject.getString("name"));
                                //Log.d("success", parentObject.getString("success"));
                            }
                            orderType.setAdapter(orderTypeAdapter);


                        } catch (Exception e) {
                            //  PDialog.hide();
//                            Log.d("json connection", "No internet access" + e);
                        }
                    }
                },

                new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError arg0) {
                        //PDialog.hide();

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
        // PDialog.show(thisActivity);
        StringRequest request = new StringRequest(Request.Method.POST, getResources().getString(R.string.url_motherland) + "customer_list.php?",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //PDialog.hide();
                        Log.v("response", response + "");
                        try {
                            JSONArray ar = new JSONArray(response);
                            companyIdList.clear();
                            companyList.clear();

                            companyList.add("Select");
                            companyIdList.add("Select");
                            for (int i = 0; i < ar.length(); i++) {
                                JSONObject parentObject = ar.getJSONObject(i);
                                companyList.add(parentObject.getString("name"));
                                companyIdList.add(parentObject.getString("id"));

                                Log.v("company_name", parentObject.getString("name"));
                            }
                            Companylist.setAdapter(dataAdapter2);


                        } catch (Exception e) {
                            //  PDialog.hide();
                        }
                    }
                },

                new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError arg0) {
                        // PDialog.hide();

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

    public void saveOrderRequest() {
        Log.v("de time", StaticVariables.pickupDefaultDate);


        String finalPickupDate=String.valueOf(pickupDate.getText()+" "+String.valueOf(pickupTime.getText()));
        String finalDeliveryDate=String.valueOf(deliveryDate.getText()+" "+String.valueOf(deliveryTime.getText()));

        Calendar pickupCalendar = Calendar.getInstance();
        Calendar deliveryCalendar = Calendar.getInstance();

        DateFormat ddf = new SimpleDateFormat("dd-MM-yyyy HH:mm");
        DateFormat ddf1 = new SimpleDateFormat("dd-MM-yyyy HH:mm");

        String myFormat1 = "yyyy-MM-dd HH:mm:ss";
        SimpleDateFormat ssdf1 = new SimpleDateFormat(myFormat1);
        SimpleDateFormat ssdf2 = new SimpleDateFormat(myFormat1);
        try{

            pickupCalendar.setTime(ddf.parse(finalPickupDate));
            deliveryCalendar.setTime(ddf1.parse(finalDeliveryDate));


        }
        catch (Exception e){
            e.printStackTrace();
        }
        StaticVariables.pickedDateTIme=ssdf1.format(pickupCalendar.getTime());
        StaticVariables.deliveryDateTIme=ssdf2.format(deliveryCalendar.getTime());


      /*  try {

            Log.v("de time", StaticVariables.deliveryDateTIme);
            if (StaticVariables.pickedDateTIme.isEmpty()) {
                Log.v("Picdate", "empty");
            }
        } catch (Exception e) {
            e.printStackTrace();
            StaticVariables.pickedDateTIme = finalPicdate;

        }

        try {
            if (StaticVariables.deliveryDateTIme.isEmpty()) {
                Log.v("Deldate", "empty");

            }
        } catch (Exception e) {
            e.printStackTrace();
            StaticVariables.deliveryDateTIme = finaldelDate;
        }*/


        PDialog.show(thisActivity);
        //  String url= "http://purplefront.net/motherland_dev/home/purchase_order_submit.php?id=4&email=test@test.com&password=aaa&customer_contact_id=5&customer_contact_address_id=6&garment_type_json="+jsonGarmentId+"&quantity_json="+jsonQuantity+"&wash_type_id_json="+jsonWashTypeId+"&style_number_json="+jsonStyle+"&wash_instructions_type=no&expected_pick_up=2016-02-18 02:15:52&expected_delivery=2016-02-18 02:15:52&order_type_id=2&instructions_json="+jsonInstr+"";
        StringRequest request = new StringRequest(Request.Method.POST, getResources().getString(R.string.url_motherland) + "edit_order_details_edit.php",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.v("response",response);

                        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(thisActivity);
                        builder.setCancelable(false)
                                .setTitle("Success")
                                .setMessage("Order detail has saved successfully ")
                                .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {

                                        Intent intent = new Intent(thisActivity, PurchaseOrderDetails.class);
                                        try {
                                            if (intent_from.contains("customer_order")){
                                                intent.putExtra("intent_from","customer_order");
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

                        try {

                            JSONArray ar = new JSONArray(response);

                        } catch (Exception e) {
                          //  PDialog.hide();
//                            Log.d("json connection", "No internet access" + e);
                        }
                    }
                },

                new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError arg0) {
                       // PDialog.hide();

                    }
                }) {

            @Override
            protected Map<String, String> getParams() {

                Map<String, String> params = new HashMap<>();

                params.put("id", StaticVariables.database.get(0).getId());
                params.put("email", StaticVariables.database.get(0).getEmail());
                params.put("password", StaticVariables.database.get(0).getPassword());
                params.put("customer_contact_id", StaticVariables.customerContact);
                params.put("order_id", order_id);
                params.put("status_id", statusId);
                params.put("expected_delivery_date", StaticVariables.deliveryDateTIme);
                params.put("expected_pick_up_date", StaticVariables.pickedDateTIme);
                params.put("order_type_id", StaticVariables.orderTypeId);

                return params;
            }
        };
        AppController.getInstance().addToRequestQueue(request, "data_receive3");
        Log.v("request", request + "");

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu items for use in the action barenu
        if (menuTitle.contains("Save")) {
            getMenuInflater().inflate(R.menu.menu_save, menu);

        } else {
            getMenuInflater().inflate(R.menu.menu_next, menu);
            final MenuItem item = menu.findItem(R.id.next_button);
            item.setTitle(menuTitle);


        }
        return true;
    }

    public void goBack(){
        if (menuTitle.contains("Save")) {

            Intent in = new Intent(thisActivity, PurchaseOrderDetails.class);

            try {
                if (intent_from.contains("customer_order")){
                    in.putExtra("intent_from","customer_order");
                }

            }catch (Exception e){
                e.printStackTrace();
            }

            startActivity(in);
            finish();
            overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);

        } else {
            Intent in = new Intent(thisActivity, NavigataionActivity.class);
            in.putExtra("redirection","Orders");
            startActivity(in);
            finish();
            overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
        }
    }

    public void onBackPressed() {

        //super.onBackPressed();
   goBack();


    }

    public void nextPage() {
        StaticVariables.hideKeyboard(thisActivity);

        DateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
        StaticVariables.deliveryTime=deliveryTime.getText().toString();
        StaticVariables.pickupTime=pickupTime.getText().toString();


        try {

             pDate = formatter.parse(String.valueOf(pickupDate.getText()));
             dDate = formatter.parse(String.valueOf(deliveryDate.getText()));
        }catch (Exception e){
            e.printStackTrace();
        }

        if (companyName.equals("Select")) {
            Toast.makeText(thisActivity, "Please select customer", Toast.LENGTH_SHORT).show();
        } else if (customerName.equals("Select")) {
            Toast.makeText(thisActivity, "Please select contact", Toast.LENGTH_SHORT).show();

        } else if (order_type.equals("Select")) {
            Toast.makeText(thisActivity, "Please select order type", Toast.LENGTH_SHORT).show();

        } else if (pickupDate.getText().toString().equals("DD-MM-YYYY")) {
            Toast.makeText(thisActivity, "Please set estimated pickup date", Toast.LENGTH_SHORT).show();

        } else if (pickupTime.getText().toString().equals("HH-MM")) {
            Toast.makeText(thisActivity, "Please set estimated pickup time", Toast.LENGTH_SHORT).show();

        } else if (deliveryDate.getText().toString().equals("DD-MM-YYYY")) {
            Toast.makeText(thisActivity, "Please set estimated delivery date", Toast.LENGTH_SHORT).show();

        }
        else if (deliveryTime.getText().toString().equals("HH-MM")) {
            Toast.makeText(thisActivity, "Please set estimated delivery time", Toast.LENGTH_SHORT).show();

        }
        else if (pDate.equals(dDate)&&(myCalendar1.after(myCalendar))){
            Toast.makeText(thisActivity, "Pickup time cannot be later than delivery time for same date", Toast.LENGTH_SHORT).show();

        }
        else if (pDate.equals(dDate)&&(myCalendar1.equals(myCalendar))){
            Toast.makeText(thisActivity, "Pickup time and delivery time cannot be equal for same date ", Toast.LENGTH_LONG).show();

        }
        else if (myCalendar1.after(myCalendar)){

            Toast.makeText(thisActivity, "Delivery date cannot be earlier than pickup date", Toast.LENGTH_SHORT).show();

        }

        else {

            Log.v("date1",myCalendar1.getTime()+"date2"+myCalendar.getTime());
            StaticVariables.prodcutInstr = productInstr.getText().toString();

            Intent in = new Intent(CreatePurchaseOrder.this, GarmentsDataActivity.class);
            startActivity(in);
            overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);

        }

    }

    public void save(){


        Log.v("pickdattime",myCalendar1.getTime()+"");
        Log.v("delivery",myCalendar.getTime()+"");

        DateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
        try {
            pDate = formatter.parse(String.valueOf(pickupDate.getText()));
            dDate = formatter.parse(String.valueOf(deliveryDate.getText()));
        }catch (Exception e){
            e.printStackTrace();
        }

        if (customerName.equals("Select")) {
            Toast.makeText(thisActivity, "Please select contact", Toast.LENGTH_SHORT).show();

        } else if (order_type.equals("Select")) {
            Toast.makeText(thisActivity, "Please select order type", Toast.LENGTH_SHORT).show();

        } else if (pickupDate.getText().toString().equals("DD-MM-YYYY")) {
            Toast.makeText(thisActivity, "Please set estimated pickup date", Toast.LENGTH_SHORT).show();

        } else if (pickupTime.getText().toString().equals("HH-MM")) {
            Toast.makeText(thisActivity, "Please set estimated pickup time", Toast.LENGTH_SHORT).show();

        } else if (deliveryDate.getText().toString().equals("DD-MM-YYYY")) {
            Toast.makeText(thisActivity, "Please set estimated delivery date", Toast.LENGTH_SHORT).show();
        }
        else if (deliveryTime.getText().toString().equals("HH-MM")) {
            Toast.makeText(thisActivity, "Please set estimated delivery time", Toast.LENGTH_SHORT).show();
        }

        else if (pDate.equals(dDate)&&(myCalendar1.after(myCalendar))){
                Toast.makeText(thisActivity, "Pickup time cannot be later than delivery time for same date", Toast.LENGTH_SHORT).show();

        }
        else if (pDate.equals(dDate)&&(myCalendar1.equals(myCalendar))){
            Toast.makeText(thisActivity, "Pickup time and delivery time cannot be equal for same date ", Toast.LENGTH_LONG).show();

        }
        else if (myCalendar1.after(myCalendar)){
            Log.v("calender1",myCalendar1+"cal2"+myCalendar);
            Toast.makeText(thisActivity, "Delivery date cannot be earlier than pickup date", Toast.LENGTH_SHORT).show();

        }
        else {
            if (StaticVariables.isNetworkConnected(thisActivity)) {
                saveOrderRequest();                 }
            else {
                Toast.makeText(thisActivity, getResources().getString(R.string.no_internet_connection), Toast.LENGTH_SHORT).show();
            }


        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                goBack();
                return true;

            case R.id.next_button:

                nextPage();
                return true;
            case R.id.save_button:

                try {
                    StaticVariables.hideKeyboard(thisActivity);


                    if (menuTitle.contains("Save")) {

                        save();

                    } else {
                        nextPage();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return true;

            default:
                return true;
        }
    }


}