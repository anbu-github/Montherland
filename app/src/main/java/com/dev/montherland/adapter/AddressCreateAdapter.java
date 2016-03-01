package com.dev.montherland.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.dev.montherland.AppController;
import com.dev.montherland.CreateAddress;
import com.dev.montherland.NavigataionActivity;
import com.dev.montherland.OrderConfirmDetails;
import com.dev.montherland.PurchaseOrderDetails;
import com.dev.montherland.R;
import com.dev.montherland.SelectAddress;
import com.dev.montherland.model.Create_Address_Model;
import com.dev.montherland.model.Response_Model;
import com.dev.montherland.parsers.Create_Address_JSONParser;
import com.dev.montherland.parsers.Response_JSONParser;
import com.dev.montherland.util.PDialog;
import com.dev.montherland.util.StaticVariables;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AddressCreateAdapter extends RecyclerView.Adapter<AddressCreateAdapter.MyViewHolder> {
    List<Create_Address_Model> persons;
    String address1,address2,address3,city,state,zipcode;
    Integer cbPos=0,selctedPos=0;
    ArrayList<CheckBox> mCheckBoxes = new ArrayList<CheckBox>();
    private static int lastCheckedPos = 0;
    private static CheckBox lastChecked = null;
    private Integer selected_position = -1;
    Context mContext;

    public AddressCreateAdapter(List<Create_Address_Model> persons,Context context) {
        this.persons = persons;
        this.mContext=context;
    }
    public AddressCreateAdapter(Context context,List<Create_Address_Model> persons) {
        this.persons = persons;
        this.mContext = context;
        cbPos = StaticVariables.cbpos;

        if (!(StaticVariables.status.contains("Save"))) {
            Intent intent = new Intent(context, OrderConfirmDetails.class);

            try {
                intent.putExtra("address1", persons.get(cbPos).getAddressline1());
                intent.putExtra("address2", persons.get(cbPos).getAddressline2());
                intent.putExtra("address3", persons.get(cbPos).getAddressline3());
                intent.putExtra("city", persons.get(cbPos).getCity());
                intent.putExtra("state", persons.get(cbPos).getState());
                intent.putExtra("zipcode", persons.get(cbPos).getZipcode());
                intent.putExtra("address_id", persons.get(cbPos).getId());
                Log.v("add1", persons.get(cbPos).getAddressline1());
                context.startActivity(intent);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        else {

            changeAddressRequest();

        }
    }


    public void changeAddressRequest() {
        PDialog.show(mContext);
        StringRequest request = new StringRequest(Request.Method.POST, mContext.getResources().getString(R.string.url_motherland) + "master_purchase_order_address_update.php?",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        PDialog.hide();
                        Log.v("response", response + "");
                        AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(mContext,R.style.myDialog));
                        builder.setCancelable(false)
                                .setTitle("Success")
                                .setMessage("Successfully Address Changed")
                                .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        StaticVariables.cbpos=-1;
                                        Intent intent = new Intent(mContext, PurchaseOrderDetails.class);
                                        mContext.startActivity(intent);
                                        ((Activity)mContext).finish();

                                    }
                                });
                        builder.show();


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

                Map<String, String> params = new HashMap<>();
                params.put("email", StaticVariables.database.get(0).getEmail());
                params.put("password", StaticVariables.database.get(0).getPassword());
                params.put("address_id",persons.get(cbPos).getId());
                params.put("id", StaticVariables.database.get(0).getId());
                params.put("order_id", StaticVariables.order_id);
                return params;
            }
        };
        AppController.getInstance().addToRequestQueue(request, "data_receive");
        Log.v("request", request + "");
    }


    public void deleteAddressRequest(){

        PDialog.show(mContext);
        StringRequest request = new StringRequest(Request.Method.POST, mContext.getResources().getString(R.string.url_motherland) + "customer_addresses_delete.php",
                new Response.Listener<String>()

                {
                    @Override
                    public void onResponse(String response) {
                        PDialog.hide();
                        Log.v("response", response);

                        try {
                            JSONObject ar = new JSONObject(response);
                            String ER=ar.getString("id");

                                if (ER.contains("Error")){
                                  Toast.makeText(mContext," As this Address has been assigned to one of the orders,unable to delete ",Toast.LENGTH_SHORT).show();
                                }else {
                                    AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(mContext,R.style.myDialog));
                                    builder.setCancelable(false)
                                            .setTitle("Success")
                                            .setMessage("Successfully Deleted")
                                            .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {

                                                    Intent intent = new Intent(mContext, SelectAddress.class);
                                                    mContext.startActivity(intent);

                                                }
                                            });
                                    builder.show();
                                }

                                //Log.d("success", parentObject.getString("success"));


                        } catch (Exception e) {
                            e.printStackTrace();
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
                params.put("id", StaticVariables.database.get(0).getId());
                params.put("email", StaticVariables.database.get(0).getId());
                params.put("password", StaticVariables.database.get(0).getEmail());
                params.put("address_id", persons.get(selctedPos).getId());
                Log.v("address_id",persons.get(selctedPos).getId());
                return params;
            }
        };
        AppController.getInstance().addToRequestQueue(request, "data_receive");
    }



    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        PDialog.hide();
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.select_address_adpter, parent, false);
        MyViewHolder myViewHolder = new MyViewHolder(v);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {

        address1=persons.get(position).getAddressline1();
        address2=persons.get(position).getAddressline2();
        address3=persons.get(position).getAddressline3();
        city = persons.get(position).getCity();
        state= persons.get(position).getState();
        zipcode=persons.get(position).getZipcode();
        holder.address1.setText(address1);
        holder.address2.setText(address2);
        holder.address3.setText(address3);
        holder.city.setText(city);
        holder.zipcode.setText(zipcode);
        holder.state.setText(state);


        holder.edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(mContext, CreateAddress.class);
                cbPos=position;
                try {
                    intent.putExtra("edit_address", "edit_address");
                    intent.putExtra("address1", persons.get(cbPos).getAddressline1());
                    intent.putExtra("address2", persons.get(cbPos).getAddressline2());
                    intent.putExtra("address3", persons.get(cbPos).getAddressline3());
                    intent.putExtra("city", persons.get(cbPos).getCity());
                    intent.putExtra("state", persons.get(cbPos).getState());
                    intent.putExtra("zipcode", persons.get(cbPos).getZipcode());
                    intent.putExtra("address_id", persons.get(cbPos).getId());
                    Log.v("add1", persons.get(cbPos).getAddressline1());
                    StaticVariables.addressId=persons.get(position).getId();

                    mContext.startActivity(intent);
                    ((Activity)mContext).finish();
                } catch (Exception e) {
                    e.printStackTrace();
                }






            }
        });

        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selctedPos = position;


                AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(mContext,R.style.myDialog));
                builder.setCancelable(false)
                        .setTitle("Delete Address")
                        .setMessage("Are you sure want to delete this address?")
                        .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                deleteAddressRequest();


                            }
                        })
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {


                            }
                        });
                builder.show();
            }
        });

        holder.cb.setTag(new Integer(position));
        mCheckBoxes.add(holder.cb);

        holder.cb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {


            }
        });

        lastChecked = holder.cb;
        holder.cb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CheckBox cb = (CheckBox) v;
                cbPos = ((Integer) cb.getTag()).intValue();
                StaticVariables.cbpos = cbPos;

                if (((CheckBox) v).isChecked())
                {
                    for (int i = 0; i < mCheckBoxes.size(); i++) {
                        if (mCheckBoxes.get(i) == v)
                            selected_position = i;

                        else
                            mCheckBoxes.get(i).setChecked(false);
                    }

                }
                else
                {
                    selected_position=-1;
                }
            }






               /* CheckBox cb = (CheckBox) v;
                cbPos = ((Integer) cb.getTag()).intValue();
                StaticVariables.cbpos = cbPos;

                if (cb.isChecked()) {
                    if (lastChecked != null) {
                        lastChecked.setChecked(false);
                        // feedslist.get(lastCheckedPos).setIsSelected(false);
                    }

                    lastChecked = cb;
                    lastCheckedPos = cbPos;
                } else
                    lastChecked = null;*/


        });



    }

    @Override
    public int getItemCount() {
        return persons.size();
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }
    public static class MyViewHolder extends RecyclerView.ViewHolder{
        CardView cv;
        TextView address1,address2,address3,mcity,zipcode,state;
        CheckBox cb;
        TextView city;
        ImageView delete,edit;

        public MyViewHolder(View itemView){
            super(itemView);
            cv = (CardView)itemView.findViewById(R.id.cv);
            address1 = (TextView)itemView.findViewById(R.id.add1);
            address2 = (TextView)itemView.findViewById(R.id.add3);
            address3 = (TextView)itemView.findViewById(R.id.address_line3);
            city = (TextView)itemView.findViewById(R.id.add2);
            state = (TextView)itemView.findViewById(R.id.state);
            zipcode = (TextView)itemView.findViewById(R.id.zipcode);
            cb = (CheckBox)itemView.findViewById(R.id.checkBox);
            delete=(ImageView)itemView.findViewById(R.id.delete);
            edit=(ImageView)itemView.findViewById(R.id.edit);
        }
    }
}
