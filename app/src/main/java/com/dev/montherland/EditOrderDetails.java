package com.dev.montherland;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.dev.montherland.adapter.CreateOrderAdapter;
import com.dev.montherland.util.PDialog;
import com.dev.montherland.util.StaticVariables;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class EditOrderDetails extends AppCompatActivity {


    Activity thisActivity=this;
    ArrayList<String> washTypeList=new ArrayList<>();
    ArrayList<String> garmentWashIdList=new ArrayList<>();
    ArrayList<String> statusTypeList=new ArrayList<>();
    ArrayList<String> statusIdList=new ArrayList<>();
    ArrayAdapter<String> dataAdapter, dataAdapter2;
    String line_id, garment_id, wash_id, status_id, get_date,Instrction;
    Spinner order_type,status;
    TextView item,date;
    Calendar myCalendar = Calendar.getInstance();
   String myFormat1 = "yyyy-MM-dd HH:mm:ss";

    SimpleDateFormat sdf = new SimpleDateFormat(myFormat1);
    SimpleDateFormat sdf1 = new SimpleDateFormat(myFormat1);
    DatePickerDialog.OnDateSetListener  calender_date;
    EditText quantity,style,instr;
    TimePickerDialog.OnTimeSetListener time;
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



        order_type.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                try {

                    wash_id= garmentWashIdList.get(position).toString();


                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        status.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                try {

                    status_id= statusIdList.get(position).toString();


                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

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
                // TODO Auto-generated method stub
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                Log.v("date", monthOfYear + "");

                updateDate();

            }

        };
        try {
            line_id=getIntent().getExtras().getString("id");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        }
        catch (Exception e){
            e.printStackTrace();
        }

        //getPurchseOrder();
        getWashTypes();
        getStatusList();

        dataAdapter = new ArrayAdapter<String>(this,
               R.layout.spinner_layout, washTypeList);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        dataAdapter2 = new ArrayAdapter<String>(this,
                R.layout.spinner_layout, statusTypeList);
        dataAdapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
    }

    public void updateDate() {
        //In which you need put here

        date.setText(sdf.format(myCalendar.getTime()));
       get_date=sdf1.format(myCalendar.getTime());
        Log.v("currentTime", myCalendar.getTime() + "");
        Log.v("pickedDateTIme", StaticVariables.pickedDateTIme);

        new TimePickerDialog(thisActivity,
                time,
                myCalendar.get(Calendar.HOUR_OF_DAY),
                myCalendar.get(Calendar.MINUTE),
                true).show();

    }

    public void saveEditOrders() {

        Instrction=instr.getText().toString();
        Log.v("instr",instr.getText().toString());
        PDialog.show(thisActivity);
        String url="http://purplefront.net/motherland_dev/home/master_purchase_order_edit.php?id=4&email=test@test.com&password=aa&order_line_id="+line_id+"&garment_id="+garment_id+"&instructions="+Instrction+"&quantity="+quantity.getText().toString()+"&wash_id="+wash_id+"&style_number="+style.getText().toString()+"&expected_delivery_date="+date.getText().toString()+"&status_id="+status_id;
        StringRequest request = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        PDialog.hide();
                        AlertDialog.Builder builder = new AlertDialog.Builder(thisActivity);
                        builder.setCancelable(false)
                                .setTitle("Success")
                                .setMessage("Your order details have been updated successfully")
                                .setNegativeButton("ok", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {

                                        Intent intent = new Intent(thisActivity, NavigataionActivity.class);
                                        intent.putExtra("from", "backtohome");
                                        startActivity(intent);
                                        overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);

                                    }
                                });
                        builder.show();
                        Log.v("response", response + "");
                        try {
                            PDialog.hide();
                            JSONArray ar = new JSONArray(response);

                            for (int i = 0; i < ar.length(); i++) {
                                JSONObject parentObject = ar.getJSONObject(i);

                                Log.v("name", parentObject.getString("name"));
                                //Log.d("success", parentObject.getString("success"));
                            }


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
                params.put("email", "test@test.com");
                params.put("password", "e48900ace570708079d07244154aa64a");
                params.put("id", "4");
                params.put("order_line_id", line_id);
                params.put("garment_id", garment_id);
                params.put("quantity", quantity.getText().toString());
                params.put("wash_id", wash_id);
                params.put("style_number", style.getText().toString());
                params.put("expected_delivery_date", get_date);
                params.put("status_id", status_id);
                params.put("instructions", Instrction);

                //Log.d("params", database.get(0).getId());
                //Log.d("service_id", StaticVariables.service_id);
                return params;
            }
        };
        AppController.getInstance().addToRequestQueue(request, "data_receive5");
        Log.v("request", request + "");
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
                        try {
                            PDialog.hide();
                            JSONArray ar = new JSONArray(response);


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

//                try {
//                    params.put("email", "test@test.com");
//                    params.put("password", "aa");
//                    params.put("id", "4");
//                    params.put("order_line_id", line_id);
//                    params.put("garment_id", garment_id);
//                    params.put("quantity", "4000");
//                    params.put("wash_id", wash_id);
//                    params.put("style_number", "test");
//                    params.put("expected_delivery_date", get_date);
//                    params.put("status_id", status_id);
//                    params.put("instructions", "test");
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }

                return params;
            }
        };
        AppController.getInstance().addToRequestQueue(request, "data_receive3");
        Log.v("request", request + "");

    }


    public void getWashTypes() {

      //  PDialog.show(thisActivity);
        StringRequest request = new StringRequest(Request.Method.POST, getResources().getString(R.string.url_motherland) + "wash_type_list.php?",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        PDialog.hide();
                        Log.v("response", response + "");
                        try {
                            PDialog.hide();
                            JSONArray ar = new JSONArray(response);

                            for (int i = 0; i < ar.length(); i++) {
                                JSONObject parentObject = ar.getJSONObject(i);
                                washTypeList.add(parentObject.getString("name"));
                                garmentWashIdList.add(parentObject.getString("id"));

                                Log.v("name", parentObject.getString("name"));
                                //Log.d("success", parentObject.getString("success"));
                            }

                            order_type.setAdapter(dataAdapter);
                            getPurchseOrder();

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
                params.put("email", "test@test.com");
                params.put("password", "e48900ace570708079d07244154aa64a");
                params.put("id", "4");
                return params;
            }
        };
        AppController.getInstance().addToRequestQueue(request, "data_receive");
        Log.v("request", request + "");
    }


    public void getPurchseOrder() {

       PDialog.show(thisActivity);
        StringRequest request = new StringRequest(Request.Method.POST, getResources().getString(R.string.url_motherland) + "master_purchase_order_view.php?",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        PDialog.hide();
                        Log.v("response", response + "");
                        try {
                            PDialog.hide();
                            JSONArray ar = new JSONArray(response);

                                JSONObject parentObject = ar.getJSONObject(0);

                                //Log.d("success", parentObject.getString("success"));
                                item.setText(parentObject.getString("garment"));
                                quantity.setText(parentObject.getString("quantity"));
                                style.setText(parentObject.getString("style_number"));
                                instr.setText(parentObject.getString("instructions"));
                                garment_id=parentObject.getString("garment_id");


                            if (parentObject.getString("expected_delivery_date").contains("null")) {

                                DateFormat dateFormat = new SimpleDateFormat("dd-MMM-yyyy HH:mm:ss");
                                Calendar cal = Calendar.getInstance();
                                date.setText(dateFormat.format(cal.getTime()));
                            }
                            else {
                                date.setText(parentObject.getString("expected_delivery_date"));

                            }

                                order_type.setSelection(Integer.parseInt(parentObject.getString("wash_id"))-1);
                                status.setSelection(Integer.parseInt(parentObject.getString("status_id"))-1);





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
                params.put("email", "test@test.com");
                params.put("password", "e48900ace570708079d07244154aa64a");
                params.put("id", "4");
                params.put("order_line_id", line_id);
                params.put("order_id", line_id);

                //Log.d("params", database.get(0).getId());
                //Log.d("service_id", StaticVariables.service_id);
                return params;
            }
        };
        AppController.getInstance().addToRequestQueue(request, "data_receive");
        Log.v("request", request + "");
    }
    public void getStatusList() {

         PDialog.show(thisActivity);
        StringRequest request = new StringRequest(Request.Method.POST, getResources().getString(R.string.url_motherland) + "status_list.php?",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        PDialog.hide();
                        Log.v("response", response + "");
                        try {
                            PDialog.hide();
                            JSONArray ar = new JSONArray(response);


                            for (int i = 0; i < ar.length(); i++) {
                                JSONObject parentObject = ar.getJSONObject(i);
                                statusTypeList.add(parentObject.getString("name"));
                                statusIdList.add(parentObject.getString("id"));

                                Log.v("name", parentObject.getString("name"));
                                //Log.d("success", parentObject.getString("success"));
                            }

                            status.setAdapter(dataAdapter2);


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
        getMenuInflater().inflate(R.menu.menu_next, menu);
        MenuItem item = menu.findItem(R.id.next_button);
        item.setTitle("Save");
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                super.onBackPressed();
                overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);

                return true;
            case R.id.next_button:
                // Toast.makeText(thisActivity, "confirm", Toast.LENGTH_SHORT).show();
                if (StaticVariables.isNetworkConnected(thisActivity)) {
                    saveOrder();
//                    Toast.makeText(thisActivity, "This feature is under construction", Toast.LENGTH_SHORT).show();

                }
                else {
                    Toast.makeText(thisActivity,"Please check the network connection",Toast.LENGTH_SHORT).show();
                }



                return true;

            default:
                return true;
        }
    }
}
