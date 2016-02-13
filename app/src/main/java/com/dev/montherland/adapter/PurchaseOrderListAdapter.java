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
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;

import com.dev.montherland.OrderConfirmDetails;
import com.dev.montherland.PurchaseOrderDetails;
import com.dev.montherland.R;
import com.dev.montherland.model.Create_Address_Model;
import com.dev.montherland.model.Purchase_Order_Model;
import com.dev.montherland.util.PDialog;
import com.dev.montherland.util.StaticVariables;

import java.util.List;

public class PurchaseOrderListAdapter extends RecyclerView.Adapter<PurchaseOrderListAdapter.MyViewHolder> {
    List<Purchase_Order_Model> persons;
    String date,contact,company,id;
    Context context;
    Integer cbPos=0,selctedPos=0;


    public PurchaseOrderListAdapter(List<Purchase_Order_Model> persons,Context context) {
        this.persons = persons;
        this.context=context;
    }
    public PurchaseOrderListAdapter(Context context,List<Purchase_Order_Model> persons){
        this.persons = persons;
        Intent intent=new Intent(context, OrderConfirmDetails.class);

        cbPos= StaticVariables.cbpos;
        intent.putExtra("company",persons.get(cbPos).getCusomer_company());
        intent.putExtra("date",persons.get(cbPos).getDate());
        intent.putExtra("contact",persons.get(cbPos).getCustomer_contact());
        context.startActivity(intent);

    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        PDialog.hide();
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.purchase_order_adapter, parent, false);
        MyViewHolder myViewHolder = new MyViewHolder(v);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {

        date=persons.get(position).getDate();
        contact=persons.get(position).getCustomer_contact();
        company=persons.get(position).getCusomer_company();
        id=persons.get(position).getId();

        holder.date.setText(date);
        holder.contact.setText(contact);
        holder.company.setText(company);

        holder.cv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent in=new Intent(context, PurchaseOrderDetails.class);
                in.putExtra("id",id);
                StaticVariables.count=position;
                context.startActivity(in);


               //Toast.makeText(context, persons.get(position).getId().toString(), Toast.LENGTH_SHORT).show();
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
        TextView city;

        public MyViewHolder(View itemView){
            super(itemView);
            cv = (CardView)itemView.findViewById(R.id.select_address);
            date = (TextView)itemView.findViewById(R.id.date);
            contact = (TextView)itemView.findViewById(R.id.contact);
            company = (TextView)itemView.findViewById(R.id.company);

        }
    }
}
