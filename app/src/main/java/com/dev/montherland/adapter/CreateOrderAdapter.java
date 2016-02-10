package com.dev.montherland.adapter;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.dev.montherland.R;
import com.dev.montherland.util.StaticVariables;

import java.util.ArrayList;

/**
 * Created by pf-05 on 2/5/2016.
 */

public class CreateOrderAdapter extends BaseAdapter {

    private Context context;
    private LayoutInflater layoutInflater;
    private int count;
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
    public int getCount() {
        return count;
    }

    @Override
    public String getItem(int position) {
        return "";
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        // if (convertView == null) {
        final ViewHolder holder;
        convertView = layoutInflater.inflate(R.layout.inflate_create_order, null);
            /*ViewHolder viewHolder = new ViewHolder(convertView);

            convertView.setTag(viewHolder);
*/
        holder = new ViewHolder();

        holder.quantity = (EditText) convertView.findViewById(R.id.quantity);
        holder.spinner1 = (Spinner) convertView.findViewById(R.id.garmentType);

        holder.spinner1.setAdapter(adapter);
        convertView.setTag(holder);
        //holder = (ViewHolder) convertView.getTag();


        holder.spinner1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
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

        holder.quantity.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                // TODO Auto-generated method stub
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                // TODO Auto-generated method stub
            }

            @Override
            public void afterTextChanged(Editable s) {
                editQuantityList.remove(position);
                editQuantityList.add(position, holder.quantity.getText().toString());
                StaticVariables.editQuantityList=editQuantityList;


                // TODO Auto-generated method stub
            }
        });

        return convertView;
    }

    private void initializeViews(String object, ViewHolder holder) {
        //TODO implement
    }

    /**
     * This class contains all butterknife-injected Views & Layouts from layout file 'inflate_assignmentlist.xml'
     * for easy to all layout elements.
     *
     * @author ButterKnifeZelezny, plugin for Android Studio by Avast Developers (http://github.com/avast)
     */

    private class ViewHolder {
        TextView device_name;
        TextView problem_desc;
        TextView type;
        EditText quantity;
        Spinner spinner1,spinner2;
    }

}
