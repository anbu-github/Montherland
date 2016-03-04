package com.dev.montherland;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.dev.montherland.model.Response_Model;
import com.dev.montherland.parsers.Response_JSONParser;
import com.dev.montherland.util.PDialog;
import com.dev.montherland.util.StaticVariables;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EditOrderDetails extends Activity {

    Activity thisActivity=this;

    ArrayList<String> washTypeList = new ArrayList<>();
    ArrayList<String> washIdList = new ArrayList<>();

    ArrayList<String> statusTypeList = new ArrayList<>();
    ArrayList<String> statusIdList = new ArrayList<>();

    String line_id, garment_id, wash_id, status_id, get_date;

    Spinner order_type,status;

    TextView item,date;

    Calendar myCalendar = Calendar.getInstance();

    String myFormat1 = "yyyy-MM-dd HH:mm:ss";

    SimpleDateFormat sdf = new SimpleDateFormat(myFormat1);
    SimpleDateFormat sdf1 = new SimpleDateFormat(myFormat1);

    DatePickerDialog.OnDateSetListener  calender_date;
    EditText quantity,style,instr;
    TimePickerDialog.OnTimeSetListener time;

    List<Response_Model> respones;
    ArrayAdapter<String> dataAdapter2;
    RelativeLayout layout;
    String wash_test="",status_test="";

    Calendar calc,deliveryCalendar,pickupCalendar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_order_details);

        order_type=(Spinner)findViewById(R.id.order_type);
        status=(Spinner)findViewById(R.id.status);
        quantity=(EditText)findViewById(R.id.quantity);
        instr=(EditText)findViewById(R.id.instr);
        style=(EditText)findViewById(R.id.style);
        item=(TextView)findViewById(R.id.item);
        date=(TextView)findViewById(R.id.date_id);

        layout=(RelativeLayout)findViewById(R.id.layout);

        washIdList.add("Select");
        washTypeList.add("Select");
        if(StaticVariables.isNetworkConnected(EditOrderDetails.this)) {

            getWashTypes();
            getStatusList();

        } else {
            Toast.makeText(EditOrderDetails.this,getResources().getString(R.string.no_internet_connection),Toast.LENGTH_LONG).show();
        }

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<>(this,
                R.layout.spinner_layout, washTypeList);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        order_type.setAdapter(dataAdapter);
        order_type.setFocusableInTouchMode(true);

        order_type.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                StaticVariables.hideKeyboard(thisActivity);
                return false;
            }
        });
        order_type.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                wash_id = String.valueOf(washIdList.get(position));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                wash_id = "";
            }
        });

        statusIdList.add("Select");
        statusTypeList.add("Select");

        dataAdapter2 = new ArrayAdapter<>(this,
                R.layout.spinner_layout, statusTypeList);
        dataAdapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        status.setAdapter(dataAdapter);
        status.setFocusableInTouchMode(true);
        status.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                StaticVariables.hideKeyboard(thisActivity);
                return false;
            }
        });
        status.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                try {


                    status_id = String.valueOf(statusIdList.get(position));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                status_id = "";
            }
        });

        date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(thisActivity, calender_date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        time = new TimePickerDialog.OnTimeSetListener() {
            public void onTimeSet(TimePicker view, int hourOfDay,
                                  int minute) {
                myCalendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                myCalendar.set(Calendar.MINUTE, minute);
                //updateTime();
            }
        };

        calender_date = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                Log.v("date", monthOfYear + "");

                updateDate();

            }

        };
        try {
            line_id=getIntent().getExtras().getString("id");
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


    }

    public void updateDate() {
        //In which you need put here
         calc=Calendar.getInstance();
        calc.set(Calendar.DAY_OF_MONTH, calc.get(Calendar.DAY_OF_MONTH) - 1);


         pickupCalendar = Calendar.getInstance();
         deliveryCalendar = Calendar.getInstance();

        DateFormat ddf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        DateFormat ddf1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        String myFormat1 = "yyyy-MM-dd HH:mm:ss";
        SimpleDateFormat ssdf1 = new SimpleDateFormat(myFormat1);
        SimpleDateFormat ssdf2 = new SimpleDateFormat(myFormat1);
        try{

            pickupCalendar.setTime(ddf.parse(StaticVariables.pickupDefaultDate));
            deliveryCalendar.setTime(ddf1.parse(StaticVariables.deliveryDefultDate));

        }
        catch (Exception e){
            e.printStackTrace();
        }

        Log.v("picktm",pickupCalendar.getTime().toString());
        Log.v("deltm",deliveryCalendar.getTime().toString());
        Log.v("currentTIme",calc.getTime().toString());
        if (myCalendar.getTime().before(calc.getTime())){
            Toast.makeText(thisActivity,"Please select valid date",Toast.LENGTH_SHORT).show();
        }


        else {

            date.setText(sdf.format(myCalendar.getTime()));
            get_date = sdf1.format(myCalendar.getTime());
            Log.v("currentTime", myCalendar.getTime() + "");
            Log.v("pickedDateTIme", StaticVariables.pickedDateTIme);

            new TimePickerDialog(thisActivity,
                    time,
                    myCalendar.get(Calendar.HOUR_OF_DAY),
                    myCalendar.get(Calendar.MINUTE),
                    true).show();
        }


    }

    void update_display() {
        if(respones != null) {
            for(Response_Model flower: respones) {
                if(flower.getId().equals("Success")) {
                    android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(new ContextThemeWrapper(thisActivity,R.style.myDialog));
                    builder.setCancelable(false)
                            .setTitle("Success")
                            .setMessage("Order detail has saved successfully")
                            .setNegativeButton("ok", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Intent intent = new Intent(EditOrderDetails.this,PurchaseOrderDetails.class);
                                    thisActivity.finish();
                                    startActivity(intent);
                                    overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);                                }
                            });
                    builder.show();
                } else {
                    Toast.makeText(EditOrderDetails.this,getResources().getString(R.string.error_occurred2),Toast.LENGTH_LONG).show();
                }
            }
        } else {
            Toast.makeText(EditOrderDetails.this,getResources().getString(R.string.error_occurred1),Toast.LENGTH_LONG).show();
        }
    }

    public void saveOrder() {
        PDialog.show(thisActivity);
        StringRequest request = new StringRequest(Request.Method.POST, getResources().getString(R.string.url_motherland) + "master_purchase_order_edit.php",
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response) {
                        PDialog.hide();
                        Log.v("response", response);
                        respones = Response_JSONParser.parserFeed(response);
                        update_display();
                    }
                },

                new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError arg0) {
                        PDialog.hide();
                        Toast.makeText(thisActivity,getResources().getString(R.string.no_internet_access),Toast.LENGTH_LONG).show();
                    }
                }) {

            @Override
            protected Map<String, String> getParams() {

                Map<String, String> params = new HashMap<>();

                try {
                    String quantity_str = quantity.getText().toString();
                    String instru = instr.getText().toString();
                    String style_no = style.getText().toString();

                    params.put("email", StaticVariables.database.get(0).getEmail());
                    params.put("password", StaticVariables.database.get(0).getPassword());
                    params.put("id", StaticVariables.database.get(0).getId());
                    params.put("order_line_id", line_id);
                    params.put("garment_id", garment_id);
                    params.put("quantity", quantity_str);
                    params.put("wash_id", wash_id);
                    params.put("style_number", style_no);
                    params.put("expected_delivery_date", get_date);
                    params.put("status_id", status_id);
                    params.put("instructions", instru);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                return params;
            }
        };
        AppController.getInstance().addToRequestQueue(request, "data_receive3");
        Log.v("request", request + "");

    }


    public void getWashTypes() {
        PDialog.show(thisActivity);
        StringRequest request = new StringRequest(Request.Method.POST, getResources().getString(R.string.url_motherland) + "wash_type_list.php",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                       // PDialog.hide();
                        Log.v("response", response + "");
                        try {
                            //PDialog.hide();
                            JSONArray ar = new JSONArray(response);

                            washIdList.clear();
                            washTypeList.clear();

                            washIdList.add("Select");
                            washTypeList.add("Select");

                            for (int i = 0; i < ar.length(); i++) {
                                JSONObject parentObject = ar.getJSONObject(i);
                                washTypeList.add(parentObject.getString("name"));
                                washIdList.add(parentObject.getString("id"));

                                Log.v("name", parentObject.getString("name"));
                            }

                            ArrayAdapter<String> adapter3 = new ArrayAdapter<>(EditOrderDetails.this, android.R.layout.simple_spinner_dropdown_item, washTypeList);
                            adapter3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            order_type.setAdapter(adapter3);

                            wash_test = "data recieved";
                            update_display2();

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
        AppController.getInstance().addToRequestQueue(request, "data_receive");
    }

    public void update_display2() {
        if(!wash_test .equals("") && !status_test.equals("")) {
            getPurchseOrder();
        }
    }

    public void getPurchseOrder() {


        StringRequest request = new StringRequest(Request.Method.POST, getResources().getString(R.string.url_motherland) + "master_purchase_order_view.php",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        Log.v("response", response + "");
                        try {

                            JSONArray ar = new JSONArray(response);

                             layout.setVisibility(View.VISIBLE);

                                JSONObject parentObject = ar.getJSONObject(0);

                                //Log.d("success", parentObject.getString("success"));
                                item.setText(parentObject.getString("garment"));
                                quantity.setText(parentObject.getString("quantity"));
                               // style.setText(parentObject.getString("style_number"));
                                instr.setText(parentObject.getString("instructions"));
                                garment_id=parentObject.getString("garment_id");

                                get_date = parentObject.getString("expected_delivery_date");

                            if (parentObject.getString("style_number").contains("null")){
                                style.setText("");

                            }else {
                                style.setText(parentObject.getString("style_number"));
                            }


                            if (parentObject.getString("expected_delivery_date").contains("null")) {

                                DateFormat dateFormat = new SimpleDateFormat("dd-MMM-yyyy HH:mm:ss");
                                Calendar cal = Calendar.getInstance();
                                date.setText(dateFormat.format(cal.getTime()));
                            }
                            else {
                                date.setText(parentObject.getString("expected_delivery_date"));

                            }

                            if(!parentObject.getString("wash_id").equals("") && !parentObject.getString("wash_id").equals("null")) {
                                order_type.setSelection(Integer.parseInt(parentObject.getString("wash_id")));
                            }

                            if(!parentObject.getString("status_id").equals("") && !parentObject.getString("status_id").equals("null")) {



                                Log.d("status do get",parentObject.getString("status"));
                                    int spinnerPosition = dataAdapter2.getPosition(parentObject.getString("status"));
                                    status.setSelection(spinnerPosition);
                            }

                            PDialog.hide();
                        } catch (Exception e) {
                            PDialog.hide();
                            e.printStackTrace();
                        }
                    }
                },

                new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError arg0) {
                        PDialog.hide();
                        Toast.makeText(EditOrderDetails.this,getResources().getString(R.string.no_internet_access),Toast.LENGTH_LONG).show();
                    }
                }) {

            @Override
            protected Map<String, String> getParams() {

                Map<String, String> params = new HashMap<>();
                params.put("email", StaticVariables.database.get(0).getEmail());
                params.put("password", StaticVariables.database.get(0).getPassword());
                params.put("id", StaticVariables.database.get(0).getId());
                params.put("order_line_id", line_id);
                params.put("order_id", StaticVariables.order_id);
                return params;
            }
        };
        AppController.getInstance().addToRequestQueue(request, "data_receive");
    }
    public void getStatusList() {

         //PDialog.show(thisActivity);
        StringRequest request = new StringRequest(Request.Method.POST, getResources().getString(R.string.url_motherland) + "status_item_list.php?",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //PDialog.hide();
                        Log.v("response", response + "");
                        try {
                           // PDialog.hide();
                            JSONArray ar = new JSONArray(response);

                            statusIdList.clear();
                            statusTypeList.clear();

                            statusIdList.add("Select");
                            statusTypeList.add("Select");

                            for (int i = 0; i < ar.length(); i++) {
                                JSONObject parentObject = ar.getJSONObject(i);
                                statusTypeList.add(parentObject.getString("name"));
                                statusIdList.add(parentObject.getString("id"));

                                Log.v("name", parentObject.getString("name"));
                                //Log.d("success", parentObject.getString("success"));
                            }

                            dataAdapter2 = new ArrayAdapter<>(EditOrderDetails.this, android.R.layout.simple_spinner_dropdown_item, statusTypeList);
                            dataAdapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            status.setAdapter(dataAdapter2);

                            status_test = "data recieved";
                            update_display2();

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

                return params;
            }
        };
        AppController.getInstance().addToRequestQueue(request, "data_receive");
        Log.v("request", request + "");
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu items for use in the action barenu
        getMenuInflater().inflate(R.menu.menu_save, menu);

        return true;
    }

    @Override
    public void onBackPressed() {

        Intent in = new Intent(thisActivity, PurchaseOrderDetails.class);
        startActivity(in);
        finish();
        overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                 Intent in = new Intent(thisActivity, PurchaseOrderDetails.class);
                startActivity(in);
                finish();
                overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
                return true;
            case R.id.save_button:

                String quantity_str = quantity.getText().toString();
                String instru = instr.getText().toString();
                String style_no = style.getText().toString();


                DateFormat ddf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");

                try {
                    calc.setTime(ddf.parse(get_date));

                }
                catch (Exception e){
                    e.printStackTrace();
                }

                if(quantity_str.trim().equals("")) {
                    Toast.makeText(thisActivity,getResources().getString(R.string.enter_quantity),Toast.LENGTH_LONG).show();
                } else if(wash_id.equals("") || wash_id.equals("Select")) {
                    Toast.makeText(thisActivity,getResources().getString(R.string.select_wash_type),Toast.LENGTH_LONG).show();
                } else if(style_no.trim().equals("")) {
                    Toast.makeText(thisActivity,getResources().getString(R.string.select_style_code),Toast.LENGTH_LONG).show();
                } else if(String.valueOf(date).equals("null") || String.valueOf(date).equals("") || String.valueOf(date).equals("0000-00-00 00:00:00")) {
                    Toast.makeText(thisActivity,getResources().getString(R.string.expected_delivery_time),Toast.LENGTH_LONG).show();
                } else if(status_id.equals("") || status_id.equals("Select")) {
                    Toast.makeText(thisActivity,getResources().getString(R.string.select_status),Toast.LENGTH_LONG).show();
                } else if (instru.trim().equals("") || instru.equals("null")) {
                    Toast.makeText(thisActivity,getResources().getString(R.string.enter_instructions),Toast.LENGTH_LONG).show();
                }

               /* else if (pickupCalendar.getTime().after(calc.getTime())){

                    Log.v("caltime",calc.getTime()+"");
                    Toast.makeText(thisActivity,"Item delivery date cannot be earlier than order pickup date",Toast.LENGTH_SHORT).show();

                }
                else if (deliveryCalendar.getTime().before(calc.getTime())){

                    Log.v("deltim",calc.getTime()+"");
                    Toast.makeText(thisActivity,"Item delivery date cannot be later than order delivery date",Toast.LENGTH_SHORT).show();

                }*/


                else {


                    if (StaticVariables.isNetworkConnected(thisActivity)) {
                        saveOrder();
                    } else {
                        Toast.makeText(thisActivity, getResources().getString(R.string.no_internet_connection), Toast.LENGTH_SHORT).show();
                    }
                }
                return true;

            default:
                return true;
        }
    }
}
