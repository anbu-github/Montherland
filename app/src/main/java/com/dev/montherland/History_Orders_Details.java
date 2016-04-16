package com.dev.montherland;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.res.TypedArrayUtils;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.dev.montherland.adapter.HistoryOrderDetailsAdapter;
import com.dev.montherland.adapter.HistoryOrderDetailsAdapter1;
import com.dev.montherland.adapter.PurchaseOrderDetailadapter;
import com.dev.montherland.model.GarmentListModel1;
import com.dev.montherland.model.PurchaseOrderDetailsModel;
import com.dev.montherland.model.Response_Model;
import com.dev.montherland.parsers.Garment_JSONParser1;
import com.dev.montherland.parsers.History_GarmentsItem_JSONParser;
import com.dev.montherland.parsers.History_Order_Details_JSONParser;
import com.dev.montherland.parsers.Purchase_OrderDetails_JSONParser;
import com.dev.montherland.parsers.Response_JSONParser;
import com.dev.montherland.util.PDialog;
import com.dev.montherland.util.StaticVariables;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;

public class History_Orders_Details extends Activity {
    private RecyclerView recyclerView;
    private List<PurchaseOrderDetailsModel> persons;
    StaggeredGridLayoutManager mLayoutManager;
    List<GarmentListModel1> feedlist;
    ListView listview;
    String mTitle="";
    TextView updatedby;
    Activity thisActivity = this;
    String data_receive = "string_req_recieve", id, email, password;
    Bundle bundle,bundle1;
    String garmentItem="",intent_from="customer_order";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_history__orders__details);
        listview=(ListView)findViewById(R.id.listView);
        updatedby=(TextView)findViewById(R.id.updatedby);


        try {
            bundle1=getIntent().getExtras();
            intent_from=bundle1.getString("intent_to");
        }catch (Exception e){
            e.printStackTrace();
        }


        try {
            bundle=getIntent().getExtras();


            garmentItem=bundle.getString("item");

            mTitle=bundle.getString("intent_from");
            if (mTitle.equals("instr_history")){
                getActionBar().setTitle(getResources().getString(R.string.history_instr));
            }
            if (mTitle.equals("address_history")){
                getActionBar().setTitle(getResources().getString(R.string.history_order_address));
            }
            if (mTitle.equals("order_details")){
                getActionBar().setTitle(getResources().getString(R.string.history_order_details));
            }

        }catch (Exception e){
            e.printStackTrace();
        }

        if (StaticVariables.isNetworkConnected(thisActivity)) {
            getMasterList();
        } else {
            Toast.makeText(thisActivity, getResources().getString(R.string.no_internet_connection), Toast.LENGTH_LONG).show();
        }

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        if (Build.VERSION.SDK_INT > 19) {
            getActionBar().setDisplayHomeAsUpEnabled(true);
            getActionBar().setHomeAsUpIndicator(R.drawable.pf);
        } else {
            getActionBar().setHomeButtonEnabled(true);
            getActionBar().setIcon(R.drawable.pf);
        }

        recyclerView = (RecyclerView) findViewById(R.id.rv);
        recyclerView.setHasFixedSize(true);
        mLayoutManager = new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL);
        mLayoutManager.setOrientation(mLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(new LinearLayoutManager(thisActivity));
    }
    public void getMasterList() {
        PDialog.show(thisActivity);
        StringRequest request = new StringRequest(Request.Method.POST, getResources().getString(R.string.url_motherland) + "order_history_details.php?",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        PDialog.hide();
                        try {
                            JSONObject jobj = new JSONObject(response);
                            String basic_details = jobj.getString("header");
                            String orders = jobj.getString("line");
//                            Log.v("line_r")
                            persons = History_Order_Details_JSONParser.parserFeed(basic_details);
                            update_display();
                            feedlist= History_GarmentsItem_JSONParser.parserFeed(orders);
                        }catch (Exception e){
                            e.printStackTrace();
                        }

                        if(persons != null) {
                            if (StaticVariables.mode.equals("order_history")){
                             Log.v("garmentItem",garmentItem);
                                ArrayList<String> itempos=new ArrayList<>();
                               for (int i=0;i<feedlist.size();i++){
                                   if (!(feedlist.get(i).getGarmentName().equals(garmentItem))){
                                       itempos.add(i+"");
                                   }
                               }

                                Collections.reverse(itempos);
                                if (!(feedlist.isEmpty())) {
                                    for (int i = 0; i < itempos.size(); i++) {
                                        feedlist.remove(Integer.parseInt(itempos.get(i)));
                                    }
                                }

                                if (feedlist.isEmpty()) {
                               Toast.makeText(thisActivity,getResources().getString(R.string.no_history_found),Toast.LENGTH_SHORT).show();
                                }
                                else {
                                    Collections.reverse(feedlist);
                                    listview.setAdapter(new HistoryOrderDetailsAdapter1(thisActivity, feedlist, persons));

                                }
                            }

                        } else {
                   //  Toast.makeText(thisActivity,getResources().getString(R.string.no_order_created),Toast.LENGTH_LONG).show();

                            // Toast.makeText(thisActivity,getResources().getString(R.string.error_occurred1),Toast.LENGTH_LONG).show();
                        }


                        Log.v("response", response);
/*

                        ArrayList<String> uniqueDatas=new ArrayList<>();

                        for (int i = 0; i < persons.size(); i++) {
                            int position=i;

                            uniqueDatas.add(persons.get(position).getCustomerContact() + persons.get(position).getOrder_type() + persons.get(position).getPickupdate() + persons.get(position).getDeliverydate());

                            for (int j = i + 1; j < uniqueDatas.size(); j++) {
                                if (uniqueDatas.get(i).equals(uniqueDatas.get(j))) {
                                    // / got the duplicate element } }
                                    Log.v("testsampel",uniqueDatas.get(i));
                                    // }
                                }
                            }
                        }
*/



                    }
                },

                new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError arg0) {
                        PDialog.hide();
                        Toast.makeText(thisActivity, getResources().getString(R.string.no_internet_access), Toast.LENGTH_LONG).show();
                    }
                }) {

            @Override
            protected Map<String, String> getParams() {

                Map<String, String> params = new HashMap<>();
                params.put("email", StaticVariables.database.get(0).getEmail());
                params.put("password", StaticVariables.database.get(0).getPassword());
                params.put("id", StaticVariables.database.get(0).getId());
                params.put("order_id", StaticVariables.order_id);
                return params;
            }
        };
        AppController.getInstance().addToRequestQueue(request, data_receive);
    }

    public static <T> boolean hasDuplicates(List<T> list){
        int count = list.size();
        T t1,t2;

        for(int i=0;i<count;i++){
            t1 = list.get(i);
            for(int j=i+1;j<count;j++){
                t2 = list.get(j);
                if(t2.equals(t1)){
                    Log.v("list pos",i+"");
                    return true;
                }
            }
        }
        return false;
    }

    void update_display() {

        if (persons != null) {
               if (StaticVariables.mode.equals("order_history")){
                  // listview.setAdapter(new HistoryOrderDetailsAdapter1(thisActivity, feedlist));
               }
            else {
                  ArrayList<String> arList=new ArrayList<>();
                   ArrayList<String> addList=new ArrayList<>();
                   ArrayList<String> instrList=new ArrayList<>();
                   ArrayList<Integer> itemList=new ArrayList<>();
                   Set<String> hs = new HashSet<>();

                   for (int i=0;i<persons.size();i++) {
                       String getLIst=persons.get(i).getCustomerContact()+persons.get(i).getPickupdate()+persons.get(i).getOrder_type();
                       String addLIst=persons.get(i).getAddressid();
                       String instr=persons.get(i).getInstruction();
                       addList.add(addLIst);
                       arList.add(getLIst);
                       instrList.add(instr);
                   }


                   if (mTitle.equals("instr_history")){

                       ArrayList<String> nonDupList = new ArrayList<String>();
                       ArrayList<String>Listpos = new ArrayList<>();
                      Log.v("testinstr",instrList.toString());
                       Iterator<String> dupIter = instrList.iterator();
                       int j=0;
                       while(dupIter.hasNext())
                       {
                           String dupWord = dupIter.next();
                           if(nonDupList.contains(dupWord))
                           {
                               dupIter.remove();
                              // persons.remove(j);
                               Log.v("listPos", j + "");
                               Listpos.add(j-1+"");
                               j++;

                           }else
                           {
                               nonDupList.add(dupWord);
                               j++;
                           }
                       }
                       Collections.reverse(Listpos);
                       Log.v("listPositions", Listpos.toString() + "");

                       for (int i=0;i<Listpos.size();i++){
                           persons.remove(Integer.parseInt(Listpos.get(i)));
                       }
                   }

                  else if (mTitle.equals("address_history")){
                       ArrayList<String> nonDupList = new ArrayList<String>();
                       ArrayList<String>Listpos = new ArrayList<>();

                       Iterator<String> dupIter = addList.iterator();
                       int j=0;
                       while(dupIter.hasNext())
                       {
                           String dupWord = dupIter.next();
                           if(nonDupList.contains(dupWord))
                           {
                               dupIter.remove();
                               Listpos.add(j+"");
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
                   }

               else if (mTitle.equals("order_details")) {

                    ArrayList<String> nonDupList = new ArrayList<String>();
                    ArrayList<String>Listpos = new ArrayList<>();
                //    Collections.reverse(arList);

                    Iterator<String> dupIter = arList.iterator();
                    int j = 0;
                    while (dupIter.hasNext()) {
                        String dupWord = dupIter.next();
                        if (nonDupList.contains(dupWord)) {
                            dupIter.remove();
                            Listpos.add(j-1+"");
                            j++;

                        } else {
                            nonDupList.add(dupWord);
                            j++;
                        }
                    }
                       Collections.reverse(Listpos);

                       for (int i=0;i<Listpos.size();i++){
                           persons.remove(Integer.parseInt(Listpos.get(i)));
                       }

                }
                   recyclerView.setAdapter(new HistoryOrderDetailsAdapter(persons));
               }

        } else {
            Toast.makeText(thisActivity, getResources().getString(R.string.no_history_found), Toast.LENGTH_LONG).show();
        }
    }

    public void onBack(){
        if (StaticVariables.mode.equals("instr_history")||StaticVariables.mode.equals("order_details_history")||StaticVariables.mode.equals("address_history")||StaticVariables.mode.equals("order_history")){

            Intent in=new Intent(thisActivity,PurchaseOrderDetails.class);
            in.putExtra("order_history", "order_history");

         try {
            /* if (StaticVariables.mode1.contains("customer_order")){
                 in.putExtra("intent_from", "customer_order");
             }

            else *//*if (intent_from.contains("customer_order")) {
                 in.putExtra("intent_from", "customer_order");
             }*/
             if (StaticVariables.mode1.contains("customer_order")) {
                 in.putExtra("intent_from", "customer_order");
             }


         }catch (Exception e){
             e.printStackTrace();

         }
            startActivity(in);
            overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
            finish();
        }else {
            super.onBackPressed();
            finish();
            overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
        }


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
              onBack();
                return true;
            default:
                return true;
        }
    }

    @Override
    public void onBackPressed() {
        onBack();

    }
    public interface DynamicHeight {
        void HeightChange (int position, int height);
    }
}

