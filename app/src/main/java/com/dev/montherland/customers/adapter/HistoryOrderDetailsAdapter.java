package com.dev.montherland.customers.adapter;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dev.montherland.R;
import com.dev.montherland.model.Customer_Contact_Model;
import com.dev.montherland.model.PurchaseOrderDetailsModel;
import com.dev.montherland.util.PDialog;
import com.dev.montherland.util.StaticVariables;

import java.util.List;

/**
 * Created by pf-05 on 3/17/2016.
 */
public class HistoryOrderDetailsAdapter extends RecyclerView.Adapter<HistoryOrderDetailsAdapter.MyViewHolder>{

    List<PurchaseOrderDetailsModel> feedlist;
    List<Customer_Contact_Model> person;
    String mTitle="";

    public HistoryOrderDetailsAdapter(List<PurchaseOrderDetailsModel> persons) {
        this.feedlist = persons;
    }

    public HistoryOrderDetailsAdapter(List<PurchaseOrderDetailsModel> persons,String from){
        this.feedlist = persons;
        mTitle="address_history";

    }
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        PDialog.hide();
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.history_order_details, parent, false);
        MyViewHolder myViewHolder = new MyViewHolder(v);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {


      /*  holder.cusName.setText(feedlist.get(position).getCustomerContact());
        holder.order_type.setText(feedlist.get(position).getOrder_type());
        holder.status.setText(feedlist.get(position).getStatus());
        holder.pickup_date.setText(feedlist.get(position).getExpected_pickup());
        holder.date.setText(feedlist.get(position).getExpected_delivery());
        holder.updatedby.setText(feedlist.get(position).getUpdatedBy()+" "+feedlist.get(position).getUpdated());*/
       /* holder.date.setText(feedlist.get(position).getExpected_delivery());
        holder.add1.setText(feedlist.get(position).getAddressline1());
        holder.add2.setText(feedlist.get(position).getAddressline2());
        holder.add3.setText(feedlist.get(position).getAddressline3());
        holder.state.setText(feedlist.get(position).getState());
        holder.city.setText(feedlist.get(position).getCity());
        holder.zipcode.setText(feedlist.get(position).getZipcode());
        holder.instr.setText(feedlist.get(position).getInstruction());
        holder.pickup_date.setText(feedlist.get(position).getExpected_pickup());
        holder.order_id.setText(feedlist.get(position).getUpdatedBy());
        holder.updated_on.setText(feedlist.get(position).getUpdated());*/
        holder.updatedby.setText(feedlist.get(position).getUpdatedBy() + " " + feedlist.get(position).getUpdated());

        try {

            if (StaticVariables.mode.equals("address_history")) {
                holder.layout2.setVisibility(View.GONE);
                holder.addressLayout.setVisibility(View.VISIBLE);
                holder.add1.setText(feedlist.get(position).getAddressline1());
                if (feedlist.get(position).getAddressline2().isEmpty()||feedlist.get(position).getAddressline2().equals("")){
                    holder.add2.setVisibility(View.GONE);
                }else {
                    holder.add2.setText(feedlist.get(position).getAddressline2());
                }

                if (feedlist.get(position).getAddressline3().isEmpty()||feedlist.get(position).getAddressline3().equals("")){
                    holder.add3.setVisibility(View.GONE);
                }else {
                    holder.add3.setText(feedlist.get(position).getAddressline3());
                }
                holder.state.setText(feedlist.get(position).getState());
                holder.city.setText(feedlist.get(position).getCity());
                holder.zipcode.setText(feedlist.get(position).getZipcode());
            } else if (StaticVariables.mode.equals("instr_history")){
                holder.addressLayout.setVisibility(View.GONE);
                holder.layout2.setVisibility(View.GONE);
                holder.instr_layout.setVisibility(View.VISIBLE);
                holder.instr.setText(feedlist.get(position).getInstruction());
            }
            else {
                holder.addressLayout.setVisibility(View.GONE);
                holder.cusName.setText(feedlist.get(position).getCustomerContact());
                holder.order_type.setText(feedlist.get(position).getOrder_type());
                holder.status.setText(feedlist.get(position).getStatus());
                holder.pickup_date.setText(feedlist.get(position).getExpected_pickup());
                holder.date.setText(feedlist.get(position).getExpected_delivery());
            }


        }catch (Exception e){
            e.printStackTrace();
        }
    }
    @Override
    public int getItemCount() {
        return feedlist.size();
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }
    public static class MyViewHolder extends RecyclerView.ViewHolder{
        CardView cv;
        TextView personName;
        LinearLayout addressLayout,orderdetailLayout;
        TextView cusName,cusCompany,add1,add2,add3,state,city,zipcode,date,pickup_date,status,order_type,instr,order_id,updated_on,updatedby;
        RelativeLayout layout1,layout2,instr_layout;
        public MyViewHolder(View itemView){
            super(itemView);
            cv = (CardView)itemView.findViewById(R.id.cv);
            personName = (TextView)itemView.findViewById(R.id.add1);

            cusName=(TextView)itemView.findViewById(R.id.quantity);
            cusCompany=(TextView)itemView.findViewById(R.id.item);
            date=(TextView)itemView.findViewById(R.id.date);
            add1=(TextView)itemView.findViewById(R.id.add1);
            add2=(TextView)itemView.findViewById(R.id.state);
            add3=(TextView)itemView.findViewById(R.id.add3);
            state=(TextView)itemView.findViewById(R.id.add2);
            city=(TextView)itemView.findViewById(R.id.city);
            status=(TextView)itemView.findViewById(R.id.status);
            pickup_date=(TextView)itemView.findViewById(R.id.style);
            order_type=(TextView)itemView.findViewById(R.id.order_type);
            instr=(TextView)itemView.findViewById(R.id.instr);
            zipcode=(TextView)itemView.findViewById(R.id.zipcode);
            order_id = (TextView) itemView.findViewById(R.id.order_id);
            updatedby = (TextView) itemView.findViewById(R.id.updatedby);
            addressLayout = (LinearLayout) itemView.findViewById(R.id.address_layout);
            orderdetailLayout = (LinearLayout) itemView.findViewById(R.id.layout);
            layout1 = (RelativeLayout) itemView.findViewById(R.id.layout1);
            layout2 = (RelativeLayout) itemView.findViewById(R.id.layout2);
            instr_layout = (RelativeLayout) itemView.findViewById(R.id.instr_layout);

        }
    }
}
