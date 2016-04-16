package com.dev.montherland.customers.adapter;

/**
 * Created by pf-05 on 2/12/2016.
 */

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.dev.montherland.OrderConfirmDetails;
import com.dev.montherland.R;
import com.dev.montherland.model.GarmentListModel1;
import com.dev.montherland.util.PDialog;

import java.util.List;

public class PurchaseOrderDetailsAdapter extends RecyclerView.Adapter<PurchaseOrderDetailsAdapter.MyViewHolder> {
    List<GarmentListModel1> persons;
    String date,contact,company,name,quantity;
    Context context;
    Integer cbPos=0,selctedPos=0;


    public PurchaseOrderDetailsAdapter(List<GarmentListModel1> persons, Context context) {
        this.persons = persons;
        this.context=context;
    }
    public PurchaseOrderDetailsAdapter(Context context, List<GarmentListModel1> persons){
        this.persons = persons;
        Intent intent=new Intent(context, OrderConfirmDetails.class);

        context.startActivity(intent);

    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        PDialog.hide();
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.purchase_order_details_adapter, parent, false);
        MyViewHolder myViewHolder = new MyViewHolder(v);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {

        name=persons.get(position).getGarmentName();
        quantity=persons.get(position).getGarmentQuantity();


        holder.garment_type.setText(name);
        holder.quantity.setText(quantity);
        holder.cv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, "This feature is under construction", Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    public int getItemCount() {
        return 2;
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }
    public static class MyViewHolder extends RecyclerView.ViewHolder{
        CardView cv;

        TextView quantity,garment_type;


        public MyViewHolder(View itemView){
            super(itemView);
            cv = (CardView)itemView.findViewById(R.id.select_address);
            quantity = (TextView) itemView.findViewById(R.id.total_quantity);
            garment_type = (TextView) itemView.findViewById(R.id.garment_type);
        }
    }
}
