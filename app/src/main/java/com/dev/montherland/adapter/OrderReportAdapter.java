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
import com.dev.montherland.util.StaticVariables;

import java.util.ArrayList;

/**
 * Created by pf-05 on 4/12/2016.
 */
public class OrderReportAdapter extends BaseAdapter {

    private Context context;
    private LayoutInflater layoutInflater;
    private int count;
    private ArrayAdapter<String> adapter;
    ArrayList<String> editQuantityList = new ArrayList<>();
    ArrayList<String> editQuantityList1 = new ArrayList<>();
    public  OrderReportAdapter(Context context) {
        this.context = context;

    }
    @Override
    public int getCount() {
        return StaticVariables.customerList.size();
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
        convertView = layoutInflater.inflate(R.layout.order_report_adapter, null);
            /*ViewHolder viewHolder = new ViewHolder(convertView);

            convertView.setTag(viewHolder);
*/
        holder = new ViewHolder();

       /* try {
            holder.text = (TextView) convertView.findViewById(R.id.text);

            holder.text.setText(StaticVariables.customerList.get(position).toString());
        }
        catch (Exception e){
            e.printStackTrace();
        }
*/
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
        TextView text;
    }


}
