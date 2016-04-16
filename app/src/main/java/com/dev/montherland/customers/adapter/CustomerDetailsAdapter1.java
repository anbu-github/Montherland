package com.dev.montherland.customers.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import com.dev.montherland.PurchaseOrderDetails;
import com.dev.montherland.R;
import com.dev.montherland.model.Customer_Details_Model;
import com.dev.montherland.util.PDialog;
import com.dev.montherland.util.StaticVariables;

import java.util.List;

/**
 * Created by pf-05 on 2/13/2016.
 */
public class CustomerDetailsAdapter1 extends RecyclerView.Adapter<CustomerDetailsAdapter1.MyViewHolder> {


    private LayoutInflater layoutInflater;
    private ArrayAdapter<String> adapter;
    List<Customer_Details_Model> persons;
    String date,contact,company,id;
    Context context;

    public  CustomerDetailsAdapter1(Context context,List<Customer_Details_Model> person) {
        this.context = context;
        this.persons=person;
        this.adapter = adapter;
        this.layoutInflater = LayoutInflater.from(context);

        Log.v("count", StaticVariables.editQuantityList.size() + "");
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
        contact=persons.get(position).getName();
        company=persons.get(position).getCompany_name();
        id=persons.get(position).getId();

        holder.date.setText(date);
        holder.contact.setText(contact);
        holder.company.setVisibility(View.INVISIBLE);
        holder.id.setText(id);

        holder.order_type.setText(persons.get(position).getOrder_type());
        holder.total.setText(persons.get(position).getQuanity());

        holder.cv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent in=new Intent(context, com.dev.montherland.customers.PurchaseOrderDetails.class);
                StaticVariables.mode1="customer_order";
                StaticVariables.order_id=persons.get(position).getId();
                StaticVariables.count=position;
                context.startActivity(in);
                Activity activity=(Activity)context;
                activity.finish();
                activity.overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);


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
