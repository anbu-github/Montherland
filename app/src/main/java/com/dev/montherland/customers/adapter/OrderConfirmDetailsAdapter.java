package com.dev.montherland.customers.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.dev.montherland.R;
import com.dev.montherland.util.StaticVariables;

import java.util.ArrayList;

/**
 * Created by pf-05 on 2/6/2016.
 */
public class OrderConfirmDetailsAdapter  extends BaseAdapter{

   private Context context;
    private LayoutInflater layoutInflater;
    private int count;
    private ArrayAdapter<String> adapter;
    ArrayList<String> editQuantityList = new ArrayList<>();
    ArrayList<String> editQuantityList1 = new ArrayList<>();
    public  OrderConfirmDetailsAdapter(Context context) {
        this.context = context;
        count = StaticVariables.editQuantityList.size();
        this.adapter = adapter;
        this.layoutInflater = LayoutInflater.from(context);

        Log.v("count", StaticVariables.editQuantityList.size()+"");
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
        convertView = layoutInflater.inflate(R.layout.purchase_order_details_adaptor, null);
            /*ViewHolder viewHolder = new ViewHolder(convertView);

            convertView.setTag(viewHolder);
*/
        holder = new ViewHolder();

        try {
            holder.quantity = (TextView) convertView.findViewById(R.id.total_quantity);
            holder.washType = (TextView) convertView.findViewById(R.id.status);
            holder.style = (TextView) convertView.findViewById(R.id.styleno);
            holder.garment_type = (TextView) convertView.findViewById(R.id.garment_type);
            holder.instr = (TextView) convertView.findViewById(R.id.instr);

            holder.quantity.setText(StaticVariables.editQuantityList.get(position));
            holder.garment_type.setText(StaticVariables.garmentTypeList.get(position));
            holder.style.setText(StaticVariables.garmentStyle.get(position));
            holder.washType.setText(StaticVariables.garmentWashtype.get(position));

            if (StaticVariables.garmentInstr.get(position).isEmpty()||StaticVariables.garmentInstr.equals("")){
                holder.instr.setVisibility(View.GONE);
            }
            else {
                holder.instr.setText(StaticVariables.garmentInstr.get(position));
            }


        }
        catch (Exception e){
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
        TextView quantity,garment_type,washType,style,instr;
    }

}
