package com.dev.montherland.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.dev.montherland.R;
import com.dev.montherland.model.Customer_Report_Model;
import com.dev.montherland.util.StaticVariables;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by pf-05 on 4/12/2016.
 */
public class OrderReportAdapter extends BaseAdapter {

    private Context context;
    private LayoutInflater layoutInflater;
    private int count;
    private List<Customer_Report_Model> persons;

    private ArrayAdapter<String> adapter;
    ArrayList<String> editQuantityList = new ArrayList<>();
    ArrayList<String> editQuantityList1 = new ArrayList<>();
    public  OrderReportAdapter(Context context,List<Customer_Report_Model> persons) {
        this.context = context;
        this.persons=persons;
        this.layoutInflater = LayoutInflater.from(context);

    }
    @Override
    public int getCount() {
        return persons.size();
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
        convertView = layoutInflater.inflate(R.layout.inflate_customer_report, null);
            /*ViewHolder viewHolder = new ViewHolder(convertView);

            convertView.setTag(viewHolder);
*/
        holder = new ViewHolder();

        holder.customer=(TextView)convertView.findViewById(R.id.customer);
        holder.total_orders=(TextView)convertView.findViewById(R.id.total_orders);
        holder.inprogress_orders=(TextView)convertView.findViewById(R.id.inprogress_orders);
        holder.completed_orders=(TextView)convertView.findViewById(R.id.completed_orders);

        try {

            holder.customer.setText(persons.get(position).getName());

            if (persons.get(position).getTotal_orders().length()>1){
                holder.total_orders.setText(persons.get(position).getTotal_orders());

            }
            else {
                holder.total_orders.setText(" "+persons.get(position).getTotal_orders());

            }

            if (persons.get(position).getInprogress_orders().length()>1){
                holder.inprogress_orders.setText(persons.get(position).getInprogress_orders()+" ");

            }
            else {
                holder.inprogress_orders.setText(" "+persons.get(position).getInprogress_orders());

            }

            if (persons.get(position).getCompleted_orders().length()>1){
                holder.completed_orders.setText(persons.get(position).getCompleted_orders()+" ");

            }
            else {
                holder.completed_orders.setText(" "+persons.get(position).getCompleted_orders());

            }


        }catch (Exception e){
            e.printStackTrace();
        }
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
        TextView customer,total_orders,inprogress_orders,completed_orders;
    }


}
