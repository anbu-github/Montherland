package com.dev.montherland;

import android.app.ActionBar;
import android.content.Intent;
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

import com.dev.montherland.model.Purchase_Order_Model;
import com.dev.montherland.util.StaticVariables;

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
    Button customer_report,monthly_report,created,inprogress,completed,title;
    LinearLayout monthly_layout;
    TranslateAnimation translateAnimation;
    TableLayout table_layout;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_order_report, container, false);


       final ArrayAdapter<String> listAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1,StaticVariables.customerList );

        listview=(ListView)view.findViewById(R.id.listview);
        monthly_report=(Button)view.findViewById(R.id.btn_monthly);
        customer_report=(Button)view.findViewById(R.id.btn_customer);
        created=(Button)view.findViewById(R.id.created);
        inprogress=(Button)view.findViewById(R.id.inprogress);
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

        monthly_report.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                table_layout.removeAllViews();
                listview.setVisibility(View.INVISIBLE);
                monthly_layout.setVisibility(View.VISIBLE);

                monthly_report.setBackgroundColor(Color.parseColor("#a2b3c2"));
                customer_report.setBackgroundResource(R.color.grey_light);
                BuildTable(12,3);



            }
        });



        customer_report.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                table_layout.removeAllViews();
                listview.setVisibility(View.VISIBLE);
                monthly_layout.setVisibility(View.INVISIBLE);

                customer_report.setBackgroundColor(Color.parseColor("#a2b3c2"));
                monthly_report.setBackgroundResource(R.color.grey_light);

                BuildTable1(StaticVariables.customerList.size(),3);
            }
        });


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


        if (StaticVariables.isNetworkConnected(getActivity())) {

        } else {
            Toast.makeText(getActivity(), getResources().getString(R.string.no_internet_connection), Toast.LENGTH_SHORT).show();
        }

        return view;
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
                    tv.setText("   " + i);

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
                    tv.setText(""+i);

                    row1.addView(tv);

                }

                table_layout.addView(row1);

            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

}
