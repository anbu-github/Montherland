package com.dev.montherland.customers.adapter;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.dev.montherland.Customer_contact_details;
import com.dev.montherland.R;
import com.dev.montherland.model.Customer_Contact_Model;
import com.dev.montherland.util.PDialog;

import java.util.List;

/**
 * Created by pf-05 on 3/8/2016.
 */
public class CustomerContactAdapter extends RecyclerView.Adapter<CustomerContactAdapter.MyViewHolder> {

    List<Customer_Contact_Model> persons;
    String mTitle="";

    public CustomerContactAdapter(List<Customer_Contact_Model> persons) {
        this.persons = persons;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        PDialog.hide();
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item, parent, false);
        MyViewHolder myViewHolder = new MyViewHolder(v);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        holder.personName.setText(persons.get(position).getName());

        holder.cv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), com.dev.montherland.customers.Customer_contact_details.class);
                intent.putExtra("id", persons.get(position).getId());
                intent.putExtra("name", persons.get(position).getName());
                intent.putExtra("email", persons.get(position).getEmail());
                intent.putExtra("phone", persons.get(position).getPhone());
                intent.putExtra("position", position+"");
                v.getContext().startActivity(intent);

                Activity activity = (Activity) v.getContext();
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
        TextView personName;

        public MyViewHolder(View itemView){
            super(itemView);
            cv = (CardView)itemView.findViewById(R.id.cv);
            personName = (TextView)itemView.findViewById(R.id.add1);
        }
    }
}
