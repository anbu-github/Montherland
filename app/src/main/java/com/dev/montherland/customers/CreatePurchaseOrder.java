package com.dev.montherland.customers;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
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
import com.dev.montherland.AppController;
import com.dev.montherland.R;
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
    static final int SHOW_DATE_PICKER_DIALOG = 1;
    Spinner customerList, Companylist, orderType;

    ArrayAdapter<String> dataAdapter, dataAdapter2, orderTypeAdapter, dataAdapter4, washTypeAdapter;

    String companyId, customerId, companyName, customerName, order_type, menuTitle, order_id, statusId;
    DatePickerDialog.OnDateSetListener date, pickup_date;
    Calendar myCalendar = Calendar.getInstance();
    Calendar myCalendar1 = Calendar.getInstance();
    TextView deliveryDate, pickupDate, pickupTime, deliveryTime,textView2;
    EditText productInstr;
    TimePickerDialog.OnTimeSetListener time;
    String contactId, editCustomerContactId;
    String myFormat = "dd-MM-yyyy";
    String myFormat1 = "yyyy-MM-dd HH:mm:ss";
    SimpleDateFormat sdf = new SimpleDateFormat(myFormat);
    SimpleDateFormat sdf1 = new SimpleDateFormat(myFormat1);

    LinearLayout instr_layout,linearLayout,delivery_layout,customer_layout;
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
        Companylist.requestFocus();
        pickupDate = (TextView) findViewById(R.id.expect_pickup_date);
        pickupTime = (TextView) findViewById(R.id.expect_pickup_time);
        deliveryDate = (TextView) findViewById(R.id.expect_del_date);
        deliveryTime = (TextView) findViewById(R.id.expect_del_time);
        textView2 = (TextView) findViewById(R.id.textView2);
        productInstr = (EditText) findViewById(R.id.instruction);
        instr_layout = (LinearLayout) findViewById(R.id.instr_layout);
        linearLayout = (LinearLayout) findViewById(R.id.linearLayout);
        delivery_layout = (LinearLayout) findViewById(R.id.delivery_layout);
        customer_layout = (LinearLayout) findViewById(R.id.customer_layout);
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

        textView2.setVisibility(View.GONE);
        customer_layout.setVisibility(View.GONE);


        if (StaticVariables.isNetworkConnected(thisActivity)) {
            PDialog.show(thisActivity);
            getCustomerList();

        } else {
            PDialog.hide();
            Toast.makeText(thisActivity, getResources().getString(R.string.no_internet_access), Toast.LENGTH_LONG).show();
        }


        try {
            intent_from=getIntent().getExtras().getString("intent_from");
        }catch (Exception e){
            e.printStackTrace();

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



    public void getCustomerList() {



        StringRequest request = new StringRequest(Request.Method.POST, getResources().getString(R.string.url_motherland) + "customer_company_list.php?",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
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
                                }
                                if (menuTitle.contains("Save")) {

                                } else {

                                    isload=true;
                                    linearLayout.setVisibility(View.VISIBLE);
                                }


                            }
                            customerList.setAdapter(dataAdapter);

                            getOrderTypeList();


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
                params.put("customer_id", StaticVariables.database.get(0).getCustomer_id());
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
                        PDialog.hide();
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