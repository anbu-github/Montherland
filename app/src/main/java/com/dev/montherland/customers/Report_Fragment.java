package com.dev.montherland.customers;

import android.app.ActionBar;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.TranslateAnimation;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.dev.montherland.AppController;
import com.dev.montherland.R;
import com.dev.montherland.model.Purchase_Order_Model;
import com.dev.montherland.util.PDialog;
import com.dev.montherland.util.StaticVariables;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by pf-05 on 4/12/2016.
 */
public class Report_Fragment extends Fragment {
    ImageButton addButton;
    private RecyclerView recyclerView;
    private List<Purchase_Order_Model> persons;
    ListView listview;
    Button created,inprogress,completed,title;
    LinearLayout monthly_layout;
    TranslateAnimation translateAnimation;
    TableLayout table_layout;
    ArrayList<String> monthlyOrders=new ArrayList<>();
    ArrayList<String> orders_inprogress=new ArrayList<>();
    ArrayList<String> orders_completed=new ArrayList<>();
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_order_report, container, false);
        final ArrayList<String> list=new ArrayList<>();
        list.add(StaticVariables.companyName);


        final ArrayAdapter<String> listAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1,list);


        listview=(ListView)view.findViewById(R.id.listview);

        created=(Button)view.findViewById(R.id.created);
        inprogress=(Button)view.findViewById(R.id.inprogress);
        title=(Button)view.findViewById(R.id.title);
        completed=(Button)view.findViewById(R.id.completed);
        monthly_layout=(LinearLayout)view.findViewById(R.id.monthly_layout);
        table_layout = (TableLayout) view.findViewById(R.id.tableLayout1);

        BuildTable(12,3);


/*

        TableRow tr_head = new TableRow(getActivity());
       // tr_head.setId(10);
        tr_head.setBackgroundColor(Color.GRAY);
        tr_head.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.FILL_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT));

        TextView label_date = new TextView(getActivity());
      //  label_date.setId(20);
        label_date.setText("DATE");
        label_date.setTextColor(Color.WHITE);
        label_date.setPadding(5, 5, 5, 5);
        tr_head.addView(label_date);// add the column to the table row here

        TextView label_weight_kg = new TextView(getActivity());
       // label_weight_kg.setId(21);// define id that must be unique
        label_weight_kg.setText("Wt(Kg.)"); // set the text for the header
        label_weight_kg.setTextColor(Color.WHITE); // set the color
        label_weight_kg.setPadding(5, 5, 5, 5); // set the padding (if required)
        tr_head.addView(label_weight_kg); // add the column to the table row here

        t1.addView(tr_head, new TableLayout.LayoutParams(
                LinearLayout.LayoutParams.FILL_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT));
*/





        /*created.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                created.setBackgroundColor(Color.parseColor("#a2c2b2"));
                inprogress.setBackgroundResource(R.color.graylight);
                completed.setBackgroundResource(R.color.graylight);
            }
        });

        inprogress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                inprogress.setBackgroundColor(Color.parseColor("#a2c2b2"));
                created.setBackgroundResource(R.color.graylight);
                completed.setBackgroundResource(R.color.graylight);
            }
        });

        completed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                completed.setBackgroundColor(Color.parseColor("#a2c2b2"));
                created.setBackgroundResource(R.color.graylight);
                inprogress.setBackgroundResource(R.color.graylight);
            }
        });*/

        listview.setAdapter(listAdapter);

        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        getActivity().getActionBar().setTitle("Reports");
        getActivity().getActionBar().setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);


        if (StaticVariables.isNetworkConnected(getActivity())){
            getMasterList();
        }
        else {
            Toast.makeText(getActivity(),getResources().getString(R.string.no_internet_connection),Toast.LENGTH_SHORT).show();
        }
        return view;
    }

    public void getMasterList() {
        PDialog.show(getActivity());
        StringRequest request = new StringRequest(Request.Method.POST, getResources().getString(R.string.url_motherland) + "customers_monthly_report.php?",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        PDialog.hide();
                        Log.v("response",response);
                        try {
                            JSONObject jobj = new JSONObject(response);
                            String order_report = jobj.getString("orders");
                            String order_inprogress = jobj.getString("inprogress");
                            String order_completed = jobj.getString("completed");


                            JSONArray ar = new JSONArray(order_report);
                            JSONArray ar_inprogress = new JSONArray(order_inprogress);
                            JSONArray ar_completed = new JSONArray(order_completed);
                            JSONObject obj = ar.getJSONObject(0);
                            JSONObject obj_inprogress = ar_inprogress.getJSONObject(0);
                            JSONObject obj_completed = ar_completed.getJSONObject(0);


                            monthlyOrders.add(obj.getString("jan"));
                            monthlyOrders.add(obj.getString("feb"));
                            monthlyOrders.add(obj.getString("mar"));
                            monthlyOrders.add(obj.getString("Apr"));
                            monthlyOrders.add(obj.getString("May"));
                            monthlyOrders.add(obj.getString("Jun"));
                            monthlyOrders.add(obj.getString("July"));
                            monthlyOrders.add(obj.getString("Aug"));
                            monthlyOrders.add(obj.getString("Sep"));
                            monthlyOrders.add(obj.getString("Oct"));
                            monthlyOrders.add(obj.getString("Nov"));
                            monthlyOrders.add(obj.getString("December"));

                            orders_inprogress.add(obj_inprogress.getString("jan"));
                            orders_inprogress.add(obj_inprogress.getString("feb"));
                            orders_inprogress.add(obj_inprogress.getString("mar"));
                            orders_inprogress.add(obj_inprogress.getString("Apr"));
                            orders_inprogress.add(obj_inprogress.getString("May"));
                            orders_inprogress.add(obj_inprogress.getString("Jun"));
                            orders_inprogress.add(obj_inprogress.getString("July"));
                            orders_inprogress.add(obj_inprogress.getString("Aug"));
                            orders_inprogress.add(obj_inprogress.getString("Sep"));
                            orders_inprogress.add(obj_inprogress.getString("Oct"));
                            orders_inprogress.add(obj_inprogress.getString("Nov"));
                            orders_inprogress.add(obj_inprogress.getString("December"));

                            orders_completed.add(obj_completed.getString("jan"));
                            orders_completed.add(obj_completed.getString("feb"));
                            orders_completed.add(obj_completed.getString("mar"));
                            orders_completed.add(obj_completed.getString("Apr"));
                            orders_completed.add(obj_completed.getString("May"));
                            orders_completed.add(obj_completed.getString("Jun"));
                            orders_completed.add(obj_completed.getString("July"));
                            orders_completed.add(obj_completed.getString("Aug"));
                            orders_completed.add(obj_completed.getString("Sep"));
                            orders_completed.add(obj_completed.getString("Oct"));
                            orders_completed.add(obj_completed.getString("Nov"));
                            orders_completed.add(obj_completed.getString("December"));

                            Log.v("size",monthlyOrders.size()+"");
//                            Log.v("line_r")

                            BuildTable(12,3);

                        }catch (Exception e){
                            e.printStackTrace();
                        }



                    }
                },

                new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError arg0) {
                        PDialog.hide();
                        Toast.makeText(getActivity(), getResources().getString(R.string.no_internet_access), Toast.LENGTH_LONG).show();
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

    private void BuildTable(int rows, int cols) {

        try {


            // outer for loop
            for (int i = 1; i <= rows; i++) {

                TableRow row = new TableRow(getActivity());
                row.setBackgroundResource(R.drawable.layout_border5);

               /* row.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT,
                        TableRow.LayoutParams.WRAP_CONTENT));
*/
                TableRow.LayoutParams params = new TableRow.LayoutParams(
                        TableRow.LayoutParams.MATCH_PARENT,
                        TableRow.LayoutParams.WRAP_CONTENT
                );
                params.setMargins(2,2,0,0);
                row.setLayoutParams(params);

                // inner for loop
                for (int j = 1; j <= cols; j++) {

                    TextView tv = new TextView(getActivity());
                    tv.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT,
                            TableRow.LayoutParams.WRAP_CONTENT));
                    // tv.setBackgroundResource(R.drawable.cell_shape);
                    if (j==1) {
                        tv.setPadding(40, 5, 10, 10);
                    }else {
                        tv.setPadding(80, 20, 10, 17);

                    }


                    try {
                        tv.setText("   " + monthlyOrders.get(i-1));

                    if (j==2){
                        tv.setText("   " + orders_inprogress.get(i-1));

                    } if (j==3){
                        tv.setText("   " + orders_completed.get(i-1));

                    }
                    }catch (Exception e){
                        e.printStackTrace();
                    }

                    row.addView(tv);

                }

                table_layout.addView(row);

            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void BuildTable1(int rows1, int cols1) {

        try {


            // outer for loop
            for (int i = 1; i <= rows1; i++) {

                TableRow row1 = new TableRow(getActivity());
                row1.setBackgroundResource(R.drawable.layout_border5);

               /* row.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT,
                        TableRow.LayoutParams.WRAP_CONTENT));
*/
                TableRow.LayoutParams params = new TableRow.LayoutParams(
                        TableRow.LayoutParams.MATCH_PARENT,
                        TableRow.LayoutParams.WRAP_CONTENT
                );
                params.setMargins(2,2,0,0);
                row1.setLayoutParams(params);

                // inner for loop
                for (int j = 1; j <= cols1; j++) {

                    TextView tv = new TextView(getActivity());
                    tv.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT,
                            TableRow.LayoutParams.WRAP_CONTENT));
                    // tv.setBackgroundResource(R.drawable.cell_shape);
                    if (j==1) {
                        tv.setPadding(40, 5, 10, 10);
                    }else {
                        tv.setPadding(80, 33, 10, 30);

                    }
                    tv.setText("    "+j);

                    row1.addView(tv);

                }

                table_layout.addView(row1);

            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

}
