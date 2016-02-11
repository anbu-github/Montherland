package com.dev.montherland.adapter;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.dev.montherland.R;
import com.dev.montherland.util.StaticVariables;

import java.util.ArrayList;

public class CreateOrderAdapter extends RecyclerView.Adapter<CreateOrderAdapter.ViewHolder>  {

    private Context context;
    private LayoutInflater layoutInflater;
     int count;
    private ArrayAdapter<String> adapter;
    ArrayList<String> editQuantityList = new ArrayList<>();
    ArrayList<String> garmentTypeList = new ArrayList<>();
    ArrayList<String> garmentIdList = new ArrayList<>();

    public  CreateOrderAdapter(Context context,int count,ArrayAdapter<String> adapter) {
        this.context = context;
        this.count = count;
        this.adapter = adapter;
        this.layoutInflater = LayoutInflater.from(context);

        for(int i = 0; i <10 ; i++) {
            editQuantityList.add("TEST");
            garmentIdList.add("TEST");
            garmentTypeList.add("TEST");
        }

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.inflate_create_order, viewGroup, false);
        ViewHolder viewHolder = new ViewHolder(v);

        return viewHolder;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return count;
    }
    public void delete(int position) { //removes the row

        count=count-1;
        StaticVariables.count=count;
        notifyItemRemoved(position);
    }
    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, final int position) {

        viewHolder.spinner1.setAdapter(adapter);
        //convertView.setTag(viewHolder);

        viewHolder.spinner1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position2, long id) {

                String garmentType = parent.getSelectedItem().toString();
                garmentTypeList.remove(position);
                garmentTypeList.add(position, garmentType);
                garmentIdList.remove(position);
                garmentIdList.add(position, position2 + 1 + "");
                StaticVariables.garmentTypeList=garmentTypeList;
                StaticVariables.garmentIdList=garmentIdList;

                InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
                // imm.hideSoftInputFromWindow(context.getCurrentFocus().getWindowToken(), 0);
                imm.hideSoftInputFromWindow(new View(context).getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                //Log.v("id", id + "");
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        viewHolder.quantity.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                editQuantityList.remove(position);
                editQuantityList.add(position, viewHolder.quantity.getText().toString());
                StaticVariables.editQuantityList = editQuantityList;
            }
        });


        viewHolder.cv.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Toast.makeText(context,"delte",Toast.LENGTH_SHORT).show();
                delete(position);
                editQuantityList.remove(position);
                garmentIdList.remove(position);
                garmentTypeList.remove(position);
                return false;
            }
        });
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        CardView cv;
        EditText quantity;
        Spinner spinner1;
        TextView delete;

        public ViewHolder(View itemView) {
            super(itemView);
            cv = (CardView)itemView.findViewById(R.id.cv_category);
            quantity = (EditText) itemView.findViewById(R.id.quantity);
            spinner1 = (Spinner) itemView.findViewById(R.id.garmentType);
        }
    }

}
