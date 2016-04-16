package com.dev.montherland.customers.adapter;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.dev.montherland.R;
import com.dev.montherland.model.GarmentListModel;
import com.dev.montherland.model.Response_Model;
import com.dev.montherland.util.StaticVariables;

import java.util.ArrayList;
import java.util.List;

public class CreateOrderAdapter extends RecyclerView.Adapter<CreateOrderAdapter.ViewHolder> {
    private Context context;
    ArrayList<String> washType = new ArrayList<>();
    ArrayList<String> washTypeId = new ArrayList<>();

    List<GarmentListModel> garment_model;
    List<Response_Model> feedList;
    DataFromAdapterToActivity dataFromAdapterToActivity;
    ArrayAdapter<String> dataAdapter;
    ArrayList<String> arrayList;


    public CreateOrderAdapter(Context context, List<GarmentListModel> garment_model, ArrayList<String> arrayList) {
        this.context = context;
        this.garment_model = garment_model;
        this.arrayList=arrayList;

        //getWashTypes();
        Log.v("washtype",arrayList.toString());

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
        Log.v("modelsize", garment_model.size() + "");
        return garment_model.size();

    }

    public interface DataFromAdapterToActivity {
        void garmentQuantity(String quantity, int i);

        void garmentStyle(String style, int i);

        void washType(String type, int i, int spinPosition);
        void garmentInstr(String type, int i);
    }

    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, final int position) {

        dataFromAdapterToActivity = (DataFromAdapterToActivity) context;

        ArrayAdapter<String> washTypeAdapter;

        washTypeAdapter = new ArrayAdapter<String>(context,
                android.R.layout.simple_spinner_item, arrayList);
        washTypeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);


        viewHolder.spinner.setAdapter(washTypeAdapter);


    /*    if (viewHolder.style.getText().toString().isEmpty()){

            viewHolder.style.requestFocus();
        }

      else  if (viewHolder.quantity.getText().toString().isEmpty()){

            viewHolder.quantity.requestFocus();
        }*/

        viewHolder.style.setInputType(InputType.TYPE_TEXT_FLAG_CAP_CHARACTERS);

        viewHolder.garmentType.setText(garment_model.get(position).getGarmentType());

        viewHolder.spinner.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                Activity activity=(Activity)context;
                StaticVariables.hideKeyboard(activity);
                return false;
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

                dataFromAdapterToActivity.garmentQuantity(viewHolder.quantity.getText().toString(), position);
                Log.v("position", position + "");
            }
        });


        viewHolder.style.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {

                dataFromAdapterToActivity.garmentStyle(viewHolder.style.getText().toString(), position);
            }
        });

        viewHolder.garment_instr.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {

                dataFromAdapterToActivity.garmentInstr(viewHolder.garment_instr.getText().toString(), position);
                Log.v("position", position + "");
            }
        });

        viewHolder.spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position2, long id) {
                dataFromAdapterToActivity.washType(viewHolder.spinner.getSelectedItem().toString(), position, position2);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


    }

    class ViewHolder extends RecyclerView.ViewHolder {
        CardView cv;
        EditText quantity, style,garment_instr;
        TextView garmentType;
        Spinner spinner;

        public ViewHolder(View itemView) {
            super(itemView);
            cv = (CardView) itemView.findViewById(R.id.cv_category);
            quantity = (EditText) itemView.findViewById(R.id.total_quantity);
            garmentType = (TextView) itemView.findViewById(R.id.garment_type);
            spinner = (Spinner) itemView.findViewById(R.id.spinner);
            style = (EditText) itemView.findViewById(R.id.styleno);
            garment_instr = (EditText) itemView.findViewById(R.id.garment_instr);


        }
    }

}