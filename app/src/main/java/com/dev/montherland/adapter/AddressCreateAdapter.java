package com.dev.montherland.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.dev.montherland.OrderConfirmDetails;
import com.dev.montherland.R;
import com.dev.montherland.model.Create_Address_Model;
import com.dev.montherland.util.PDialog;
import com.dev.montherland.util.StaticVariables;

import java.util.ArrayList;
import java.util.List;

public class AddressCreateAdapter extends RecyclerView.Adapter<AddressCreateAdapter.MyViewHolder> {
    List<Create_Address_Model> persons;
    String address1,address2,address3,city,state,zipcode;
    Integer cbPos=0,selctedPos=0;
    ArrayList<CheckBox> mCheckBoxes = new ArrayList<CheckBox>();
    private static int lastCheckedPos = 0;
    private static CheckBox lastChecked = null;
    private Integer selected_position = -1;
    public AddressCreateAdapter(List<Create_Address_Model> persons) {
        this.persons = persons;
    }
    public AddressCreateAdapter(Context context,List<Create_Address_Model> persons){
        this.persons = persons;
        Intent intent=new Intent(context, OrderConfirmDetails.class);

        try {
            cbPos = StaticVariables.cbpos;
            intent.putExtra("address1", persons.get(cbPos).getAddressline1());
            intent.putExtra("address2", persons.get(cbPos).getAddressline2());
            intent.putExtra("address3", persons.get(cbPos).getAddressline3());
            intent.putExtra("city", persons.get(cbPos).getCity());
            intent.putExtra("state", persons.get(cbPos).getState());
            intent.putExtra("zipcode", persons.get(cbPos).getZipcode());
            intent.putExtra("address_id", persons.get(cbPos).getId());
            Log.v("add1",persons.get(cbPos).getAddressline1());
            context.startActivity(intent);
        }catch (Exception e){
            e.printStackTrace();
        }
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
        city=persons.get(position).getCity();
        state=persons.get(position).getState();
        zipcode=persons.get(position).getZipcode();
        holder.address1.setText(address1);
        holder.address2.setText(address2);
        holder.address3.setText(address3);
        holder.city.setText(city);
        holder.zipcode.setText(zipcode);
        holder.state.setText(state);


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
        }
    }
}
