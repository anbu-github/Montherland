package com.dev.montherland.adapter;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.dev.montherland.R;
import com.dev.montherland.model.GarmentListModel;

import java.util.List;

public class CreateOrderAdapter extends RecyclerView.Adapter<CreateOrderAdapter.ViewHolder>  {
    private Context context;

    List<GarmentListModel> garment_model;
    DataFromAdapterToActivity dataFromAdapterToActivity;


    public  CreateOrderAdapter(Context context,List<GarmentListModel> garment_model) {
        this.context = context;
        this.garment_model = garment_model;

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.inflate_create_order, viewGroup, false);

        return new ViewHolder(v);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        Log.v("modelsize",garment_model.size()+"");
        return garment_model.size();

    }

    public interface DataFromAdapterToActivity
    {
        void garmentQuantity(String quantity, int i);
    }

    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, final int position) {

        dataFromAdapterToActivity = (DataFromAdapterToActivity) context;


        viewHolder.garmentType.setText(garment_model.get(position).getGarmentType());

        viewHolder.quantity.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
              //  editQuantityList.remove(position);
               // editQuantityList.add(position, viewHolder.quantity.getText().toString());

                dataFromAdapterToActivity.garmentQuantity(viewHolder.quantity.getText().toString(), position);
                Log.v("position", position + "");
            }
        });




    }

    class ViewHolder extends RecyclerView.ViewHolder{
        CardView cv;
        EditText quantity;
        TextView garmentType;

        public ViewHolder(View itemView) {
            super(itemView);
            cv = (CardView)itemView.findViewById(R.id.cv_category);
            quantity = (EditText) itemView.findViewById(R.id.quantity);
            garmentType = (TextView) itemView.findViewById(R.id.garment_type);
        }
    }

}
