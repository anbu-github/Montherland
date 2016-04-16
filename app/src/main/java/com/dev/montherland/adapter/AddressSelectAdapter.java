package com.dev.montherland.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.dev.montherland.AppController;
import com.dev.montherland.CreateAddress;
import com.dev.montherland.CustomerEditAddress;
import com.dev.montherland.Customer_contact_details;
import com.dev.montherland.OrderConfirmDetails;
import com.dev.montherland.PurchaseOrderDetails;
import com.dev.montherland.R;
import com.dev.montherland.SelectAddress;
import com.dev.montherland.model.Address_Model;
import com.dev.montherland.model.Create_Address_Model;
import com.dev.montherland.util.PDialog;
import com.dev.montherland.util.StaticVariables;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class AddressSelectAdapter extends RecyclerView.Adapter<AddressSelectAdapter.MyViewHolder> {
    List<Create_Address_Model> persons;
    String address1="",address2="",address3="",city,state,zipcode;
    Integer cbPos=0,selctedPos=0;
    ArrayList<CheckBox> mCheckBoxes = new ArrayList<CheckBox>();
    private static int lastCheckedPos = 0;
    private static CheckBox lastChecked = null;
    private Integer selected_position = -1;
    List<Address_Model> feedslist;
    String intent_from="";
    Context mContext;
    private int count;
    private boolean[] thumbnailsselection;
    public AddressSelectAdapter(List<Create_Address_Model> persons,Context context) {
        this.persons = persons;
        this.mContext=context;
    }


    public AddressSelectAdapter(List<Create_Address_Model> persons,Context context,String intent_from) {
        this.persons = persons;
        this.mContext = context;
        this.intent_from=intent_from;
        cbPos = StaticVariables.cbpos;

        for (int i=0;i<persons.size();i++){
            StaticVariables.addressIdList.add(i,"id");
        }
        Log.v("idsize",StaticVariables.addressIdList.size()+"personsize"+persons.size());
    }
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        PDialog.hide();
        View v;

            v = LayoutInflater.from(parent.getContext()).inflate(R.layout.select_address_adapter2, parent, false);

        MyViewHolder myViewHolder = new MyViewHolder(v);

        return myViewHolder;
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
                                android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(mContext);
                                builder.setCancelable(false)
                                        .setTitle("Success")
                                        .setMessage("Successfully Deleted")
                                        .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {

                                                if (StaticVariables.mode.equals("customor_address")) {
                                                    Intent intent = new Intent(mContext, SelectAddress.class);
                                                    intent.putExtra("customer_address", "customer_address");
                                                    mContext.startActivity(intent);
                                                    ((Activity) mContext).finish();
                                                } else {
                                                    Intent intent = new Intent(mContext, Customer_contact_details.class);
                                                    mContext.startActivity(intent);
                                                    ((Activity) mContext).finish();

                                                }
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
    public void onBindViewHolder(final MyViewHolder holder, final int position) {

        View convertView=holder.itemView;
        selctedPos=position;

        address1=persons.get(position).getAddressline1();
        address2=persons.get(position).getAddressline2();
        address3=persons.get(position).getAddressline3();
        city = persons.get(position).getCity();
        state= persons.get(position).getState();
        zipcode=persons.get(position).getZipcode();

        //holder.edit.setVisibility(View.INVISIBLE);
        //holder.delete.setVisibility(View.INVISIBLE);

        Log.v("checked","here");


        holder.cb.setSelected(persons.get(position).getIsChecked());

        holder.cb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                //  Log.v("ischecked", isChecked + "");
                //  Toast.makeText(mContext, isChecked + "", Toast.LENGTH_SHORT).show();
                persons.get(position).setIsChecked(isChecked);

                StaticVariables.address_id="-1";

            }
        });

        holder.cb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                StaticVariables.isSelected=true;

                CheckBox cb = (CheckBox) v;
                cbPos = ((Integer) cb.getTag()).intValue();
                StaticVariables.cbpos = cbPos;

                if (((CheckBox) v).isChecked())
                {
                    for (int i = 0; i < mCheckBoxes.size(); i++) {
                        if (mCheckBoxes.get(i) == v) {
                            selected_position = i;

                        }
                        else{
                            mCheckBoxes.get(i).setChecked(false);
                        }
                    }

                }
                else
                {
                    selected_position=-1;
                    StaticVariables.cbpos=-1;

                }



            }
        });


        holder.edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, CustomerEditAddress.class);
                cbPos = position;
                try {
                    intent.putExtra("intent_name", "customer_edit_address");

                    intent.putExtra("address1", persons.get(cbPos).getAddressline1());
                    intent.putExtra("address2", persons.get(cbPos).getAddressline2());
                    intent.putExtra("address3", persons.get(cbPos).getAddressline3());
                    intent.putExtra("city", persons.get(cbPos).getCity());
                    intent.putExtra("state", persons.get(cbPos).getState());
                    intent.putExtra("zipcode", persons.get(cbPos).getZipcode());
                    intent.putExtra("address_id", persons.get(cbPos).getId());
                    Log.v("add1", persons.get(cbPos).getAddressline1());
                    StaticVariables.addressId = persons.get(position).getId();

                    mContext.startActivity(intent);
                    Activity activity=(Activity)mContext;
                    activity.overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
                    ((Activity) mContext).finish();
                } catch (Exception e) {
                    e.printStackTrace();
                }


            }
        });

        try {
            holder.cb.setChecked(persons.get(position).getIsChecked());

        }catch (Exception e){
            e.printStackTrace();
        }


        holder.cb.setTag(new Integer(position));
        mCheckBoxes.add(holder.cb);

        if (StaticVariables.address_id.equals("")){
            if (position==0){
                holder.cb.setChecked(true);
                StaticVariables.cbpos=0;
            }
        }
        else if (StaticVariables.address_id.equals(persons.get(position).getId())){

            holder.cb.setChecked(true);
            StaticVariables.cbpos=position;
        }

        lastChecked = holder.cb;














//        Log.v("status",persons.get(position).getStatus());
        /*if(persons.get(position).getStatus().equals("selected")){
            holder.cb.setChecked(true);
        }
        else {
            holder.cb.setChecked(false);
        }*/



        if (persons.get(position).getAddressline2().equals("")){
            holder.address2.setVisibility(View.GONE);
        }else {
            holder.address2.setVisibility(View.VISIBLE);
            holder.address2.setText(address2);
        }

        if (address3.equals("")||address3.isEmpty()||address3.equals(null)){

            holder.address3.setVisibility(View.GONE);
        }else {
            holder.address3.setVisibility(View.VISIBLE);
            holder.address3.setText(address3);
        }


//
       holder.address1.setText(address1);
//        holder.address2.setText(address2);
//        holder.address3.setText(address3);
        holder.city.setText(city);
        holder.zipcode.setText(zipcode);
        holder.state.setText(state);




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
