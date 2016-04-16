package com.dev.montherland.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.dev.montherland.EditOrderDetails;
import com.dev.montherland.R;
import com.dev.montherland.model.GarmentListModel1;
import com.dev.montherland.model.PurchaseOrderDetailsModel;
import com.dev.montherland.util.StaticVariables;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by pf-05 on 2/13/2016.
 */
public class HistoryOrderDetailsAdapter1 extends BaseAdapter{

    private Context context;
    private LayoutInflater layoutInflater;
    private int count;
    private ArrayAdapter<String> adapter;
    List<GarmentListModel1> person;
    ArrayList<String> editQuantityList = new ArrayList<>();
    ArrayList<String> editQuantityList1 = new ArrayList<>();

    public  HistoryOrderDetailsAdapter1(Context context,List<GarmentListModel1> person,  List<PurchaseOrderDetailsModel> persons ) {
        this.context = context;
        this.person=person;
        count = StaticVariables.editQuantityList.size();
        this.adapter = adapter;
        this.layoutInflater = LayoutInflater.from(context);

        Log.v("count", StaticVariables.editQuantityList.size() + "");
    }
    @Override
    public int getCount() {
        return person.size();
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
        convertView = layoutInflater.inflate(R.layout.customer_order_details_adapter, null);
            /*ViewHolder viewHolder = new ViewHolder(convertView);

            convertView.setTag(viewHolder);
*/
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Toast.makeText(context,""+person.get(position).getId(),Toast.LENGTH_SHORT).show();

            }
        });

        holder = new ViewHolder();
        holder.layout1=(RelativeLayout)convertView.findViewById(R.id.layout1);
        holder.updatedby=(TextView)convertView.findViewById(R.id.updatedby);
        holder.garment_instruction = (TextView) convertView.findViewById(R.id.instruction);
        holder.quantity = (TextView) convertView.findViewById(R.id.total_quantity);
        holder.garment_type = (TextView) convertView.findViewById(R.id.garment_type);
        holder.wash = (TextView) convertView.findViewById(R.id.wash);
        holder.wash_type = (TextView) convertView.findViewById(R.id.wash_type);
        holder.style = (TextView) convertView.findViewById(R.id.style);
        holder.date = (TextView) convertView.findViewById(R.id.date);
        holder.edit=(ImageView)convertView.findViewById(R.id.edit);
        holder.itemImage=(ImageView)convertView.findViewById(R.id.imageView);

        holder.edit.setVisibility(View.GONE);
        holder.layout1.setVisibility(View.VISIBLE);

        holder.quantity.setText(person.get(position).getGarmentQuantity());
        holder.garment_type.setText(person.get(position).getGarmentName());
        // holder.wash_type.setText(person.get(position).getWashType());
        holder.style.setText(person.get(position).getStyleNumber());
        holder.wash.setText(person.get(position).getStatus());

        try {
            holder.updatedby.setText(person.get(position).getUpdatedBy() + " " + person.get(position).getUpdated());

        }catch (Exception e){
//            holder.updatedby.setText(persons.get(position-1).getUpdatedBy() + " " + persons.get(position-1).getUpdated());

            e.printStackTrace();
        }
        if (person.get(position).getGarmentName().contains("Shirts")){

            holder.itemImage.setImageResource(R.drawable.item_shirts);
        }
        if (person.get(position).getGarmentName().contains("Jeans")){
            holder.itemImage.setImageResource(R.drawable.item_jeans);
        }
        if (person.get(position).getGarmentName().contains("Pants")){
            holder.itemImage.setImageResource(R.drawable.item_pants);
        }

        if (person.get(position).getWashType().contains("null")){
            holder.wash_type.setText("");

        }else {
            holder.wash_type.setText(person.get(position).getWashType());

        }
        if (person.get(position).getStatus().contains("null")){
            holder.wash.setText("");

        }else {
            holder.wash.setText(person.get(position).getStatus());

        }

        if (person.get(position).getStyleNumber().contains("null")){
            holder.style.setText("");

        }else {

            holder.style.setText(person.get(position).getStyleNumber());

        }


        if (person.get(position).getExpectedDelivery().contains("null")) {

            holder.date.setText(StaticVariables.deliveryDefultDate);
        }
        else
        {
            holder.date.setText(person.get(position).getExpectedDelivery());
        }
        holder.garment_instruction.setText(person.get(position).getGarmentInstruction());


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
        TextView quantity,garment_type,wash,wash_type,style,date,garment_instruction,updatedby;
        ImageView edit,itemImage;
        RelativeLayout layout1;

    }

}
