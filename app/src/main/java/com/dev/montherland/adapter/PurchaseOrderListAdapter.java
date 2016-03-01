package com.dev.montherland.adapter;

/**
 * Created by pf-05 on 2/8/2016.
 */

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import com.dev.montherland.PurchaseOrderDetails;
import com.dev.montherland.R;
import com.dev.montherland.model.Purchase_Order_Model;
import com.dev.montherland.util.PDialog;
import com.dev.montherland.util.StaticVariables;

import java.util.List;

public class PurchaseOrderListAdapter extends RecyclerView.Adapter<PurchaseOrderListAdapter.MyViewHolder> {

    List<Purchase_Order_Model> persons;
    String date,contact,company,id;
    Context context;


    public PurchaseOrderListAdapter(List<Purchase_Order_Model> persons,Context context) {
        this.persons = persons;
        this.context=context;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        PDialog.hide();
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.purchase_order_adapter, parent, false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {

        date=persons.get(position).getDate();
        contact=persons.get(position).getCustomer_contact();

        company=persons.get(position).getCustomer_company();
        id=persons.get(position).getId();

        holder.date.setText(date);
        holder.contact.setText(contact);

      /*  if (contact.contains("Lalit Panchal")){
            holder.contact.setText("Test contact");

        }else {
            holder.contact.setText(contact);
        }*/

        holder.company.setText(company);
        holder.id.setText(id);

        holder.order_type.setText(persons.get(position).getOrder_type());
        holder.total.setText(persons.get(position).getQuantity());

        holder.cv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent in=new Intent(context, PurchaseOrderDetails.class);
                StaticVariables.order_id = persons.get(position).getId();
                context.startActivity(in);
            }
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
        TextView date,contact,company;
        CheckBox cb;
        TextView city,id,order_type,total;

        public MyViewHolder(View itemView){
            super(itemView);
            cv = (CardView)itemView.findViewById(R.id.select_address);
            date = (TextView)itemView.findViewById(R.id.date);
            contact = (TextView)itemView.findViewById(R.id.contact);
            company = (TextView)itemView.findViewById(R.id.item);
            order_type = (TextView)itemView.findViewById(R.id.order_type);
            id = (TextView)itemView.findViewById(R.id.id);
            total = (TextView)itemView.findViewById(R.id.total);

        }
    }
}
