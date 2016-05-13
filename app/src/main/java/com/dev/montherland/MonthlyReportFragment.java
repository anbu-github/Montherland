package com.dev.montherland;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
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
import com.dev.montherland.adapter.PurchaseOrderListAdapter;
import com.dev.montherland.model.Purchase_Order_Model;
import com.dev.montherland.parsers.Purchase_Order_JSONParser;
import com.dev.montherland.util.PDialog;
import com.dev.montherland.util.StaticVariables;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class MonthlyReportFragment extends Fragment {

    ImageButton addButton;
    StaggeredGridLayoutManager mLayoutManager;
    private RecyclerView recyclerView;

    private List<Purchase_Order_Model> persons;
    Button created,inprogress,completed,title;
    LinearLayout monthly_layout;
    ArrayList<String> monthlyOrders=new ArrayList<>();
    ArrayList<String> orders_inprogress=new ArrayList<>();
    ArrayList<String> orders_completed=new ArrayList<>();
    TableLayout table_layout;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_order_report, container, false);

        created=(Button)view.findViewById(R.id.created);
        inprogress=(Button)view.findViewById(R.id.inprogress);
        completed=(Button)view.findViewById(R.id.completed);
        monthly_layout=(LinearLayout)view.findViewById(R.id.monthly_layout);
        table_layout = (TableLayout) view.findViewById(R.id.tableLayout1);

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
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                    if (j==2){
                        tv.setText("   " + orders_inprogress.get(i-1));

                    } if (j==3){
                        tv.setText("   " + orders_completed.get(i-1));

                    }
                    row.addView(tv);

                }

                table_layout.addView(row);

            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

}
