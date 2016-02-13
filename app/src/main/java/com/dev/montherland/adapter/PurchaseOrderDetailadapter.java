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
import com.dev.montherland.model.GarmentListModel1;
import com.dev.montherland.util.StaticVariables;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by pf-05 on 2/13/2016.
 */
public class PurchaseOrderDetailadapter extends BaseAdapter{

    private Context context;
    private LayoutInflater layoutInflater;
    private int count;
    private ArrayAdapter<String> adapter;
    List<GarmentListModel1> person;
    ArrayList<String> editQuantityList = new ArrayList<>();
    ArrayList<String> editQuantityList1 = new ArrayList<>();
    public  PurchaseOrderDetailadapter(Context context,List<GarmentListModel1> person) {
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
        convertView = layoutInflater.inflate(R.layout.purchase_order_details_adaptor, null);
            /*ViewHolder viewHolder = new ViewHolder(convertView);

            convertView.setTag(viewHolder);
*/
        holder = new ViewHolder();

        try {

            holder.quantity = (TextView) convertView.findViewById(R.id.quantity);
            holder.garment_type = (TextView) convertView.findViewById(R.id.garment_type);
            holder.quantity.setText(person.get(position).getGarmentQuantity());
            holder.garment_type.setText(person.get(position).getGarmentName());

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
        TextView quantity,garment_type;
    }

}
