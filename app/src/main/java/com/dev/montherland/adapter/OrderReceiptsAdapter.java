package com.dev.montherland.adapter;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.dev.montherland.AppController;
import com.dev.montherland.PurchaseOrderDetails;
import com.dev.montherland.R;
import com.dev.montherland.model.Order_Receipts_Details_Model;
import com.dev.montherland.util.PDialog;
import com.dev.montherland.util.StaticVariables;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by pf-05 on 2/13/2016.
 */
public class OrderReceiptsAdapter extends BaseAdapter{

    private Context context;
    private LayoutInflater layoutInflater;
    private int count,pos;
    private ArrayAdapter<String> adapter;
    List<Order_Receipts_Details_Model> person;
    ArrayList<String> dcNumberList = new ArrayList<>();
    ArrayList<String> quantityList = new ArrayList<>();
    ArrayList<String> manualDcNumberList = new ArrayList<>();
    String received_quantity;
    Integer required_amount;
    public OrderReceiptsAdapter(Context context, List<Order_Receipts_Details_Model> person) {
        this.context = context;
        this.person=person;
        count = StaticVariables.editQuantityList.size();
        this.adapter = adapter;
        this.layoutInflater = LayoutInflater.from(context);

        for (int i=0;i<person.size();i++){
            dcNumberList.add("");
            quantityList.add("");
            manualDcNumberList.add("");
        }

//        orderReceiptsDetails();

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
        pos=position;

        if(StaticVariables.mode.equals("delivery")){
            convertView = layoutInflater.inflate(R.layout.order_delivery_adapter1, null);


        }else {
            convertView = layoutInflater.inflate(R.layout.order_receipts_adapter, null);
        }
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
        holder.garment_instruction = (TextView) convertView.findViewById(R.id.instruction);
        holder.quantity = (EditText) convertView.findViewById(R.id.total_quantity);
        holder.garment_type = (TextView) convertView.findViewById(R.id.garment_type);
        holder.received_Qty = (TextView) convertView.findViewById(R.id.status);
        holder.wash_type = (TextView) convertView.findViewById(R.id.wash_type);
        holder.style = (TextView) convertView.findViewById(R.id.style);
        holder.date = (TextView) convertView.findViewById(R.id.date);
        holder.itemImage=(ImageView)convertView.findViewById(R.id.imageView);
        holder.dc_number=(EditText) convertView.findViewById(R.id.dc_number);
        holder.order_button=(Button) convertView.findViewById(R.id.order_button);
        holder.manual_dc_number=(EditText)convertView.findViewById(R.id.manual_dc_number);


        //holder.quantity.setText(person.get(position).getQuantityOrdered());
        holder.garment_type.setText(person.get(position).getGarmentType());
        // holder.wash_type.setText(person.get(position).getWashType());
        holder.style.setText(person.get(position).getStyleNum());
        //holder.wash.setText(person.get(position).getStatus());
        holder.quantity.setHint(context.getResources().getString(R.string.enter_quantity2));


        holder.received_Qty.setText(person.get(position).getOrderReceipts());

        if (person.get(position).getOrderReceipts().equals("null")||person.get(position).getOrderReceipts().isEmpty()){
            holder.received_Qty.setText(" 0");

        }else {
            holder.received_Qty.setText(" "+person.get(position).getOrderReceipts());

        }

       // Toast.makeText(context,received_quantity,Toast.LENGTH_SHORT).show();


        required_amount=Integer.parseInt(person.get(position).getQuantityOrdered())-Integer.parseInt(person.get(position).getOrderReceipts().trim());


        if (StaticVariables.mode.equals("delivery")){
            required_amount=Integer.parseInt(person.get(position).getOrderReceipts())-Integer.parseInt(person.get(position).getOrderDelivery().trim());

            holder.received_Qty.setText(" "+person.get(position).getOrderDelivery());
           // holder.quantity.setText(""+required_amount);
        }
        else {
          //  holder.quantity.setText(""+required_amount);

        }

        holder.manual_dc_number.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                manualDcNumberList.remove(position);
                manualDcNumberList.add(position,holder.manual_dc_number.getText().toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        if(StaticVariables.mode.equals("delivery")){
            holder.order_button.setText("DELIVER ORDER");

        }

        holder.order_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                holder.order_button.setTag(position);

                StaticVariables.hideKeyboard((Activity)context);

                if (StaticVariables.mode.equals("delivery")) {

                    if ((dcNumberList.get(position).equals("") || dcNumberList.get(position).isEmpty())&&(manualDcNumberList.get(position).equals("")||manualDcNumberList.get(position).isEmpty())) {

                        Toast.makeText(context, "Please enter either Manual Dc number or Dc number", Toast.LENGTH_SHORT).show();
                    } else if (quantityList.get(position).equals("") || quantityList.get(position).isEmpty()) {
                        Toast.makeText(context, "Please enter the quantity amount", Toast.LENGTH_SHORT).show();
                    }
                    else if (person.get(position).getOrderReceipts().equals("0")){
                        Toast.makeText(context, "No order received for deliver", Toast.LENGTH_SHORT).show();
                    }
                    else if (Integer.parseInt(quantityList.get(position).toString()) > required_amount) {
                        Toast.makeText(context, "Order item's Delivery amount cannot be greater than Ordered received item amount", Toast.LENGTH_SHORT).show();

                    } else {

                        pos=Integer.parseInt(v.getTag()+"");

                      // Toast.makeText(context,v.getTag()+"",Toast.LENGTH_SHORT).show();
                         deliveryOrder();
                    }

                } else {

                    if (dcNumberList.get(position).equals("") || dcNumberList.get(position).isEmpty()) {

                        Toast.makeText(context, "Please enter the DC number", Toast.LENGTH_SHORT).show();
                    } else if (quantityList.get(position).equals("") || quantityList.get(position).isEmpty()) {

                        Toast.makeText(context, "Please enter the quantity amount", Toast.LENGTH_SHORT).show();
                    } else if (Integer.parseInt(person.get(position).getQuantityOrdered())-Integer.parseInt(person.get(position).getOrderReceipts().trim()) < Integer.parseInt(quantityList.get(position).trim())) {
                        Toast.makeText(context, "Order item's Receiving amount cannot be greater than Ordered item amount", Toast.LENGTH_SHORT).show();

                    } else {
                        pos=Integer.parseInt(v.getTag()+"");

                       // Toast.makeText(context, dcNumberList.toString(), Toast.LENGTH_SHORT).show();
                        receiveOrder();
                    }
                }
            }
        });

        holder.dc_number.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                dcNumberList.remove(position);
                dcNumberList.add(position,holder.dc_number.getText().toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        holder.quantity.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                quantityList.remove(position);
                quantityList.add(position,holder.quantity.getText().toString());

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });







        if (person.get(position).getGarmentType().contains("Shirts")){

            holder.itemImage.setImageResource(R.drawable.item_shirts);
        }
        if (person.get(position).getGarmentType().contains("Jeans")){
            holder.itemImage.setImageResource(R.drawable.item_jeans);
        }
        if (person.get(position).getGarmentType().contains("Pants")){
            holder.itemImage.setImageResource(R.drawable.item_pants);
        }

        if (person.get(position).getWashType().contains("null")){
            holder.wash_type.setText("");

        }else {
            holder.wash_type.setText(" "+person.get(position).getWashType());

        }

        if (person.get(position).getStyleNum().contains("null")){
            holder.style.setText("");

        }else {

            holder.style.setText(" "+person.get(position).getStyleNum());

        }


        if (person.get(position).getExpectedDeliveryDate().contains("null")) {

            holder.date.setText(StaticVariables.deliveryDefultDate);
        }
        else
        {
            holder.date.setText(" "+person.get(position).getExpectedDeliveryDate());
        }

        /*if (person.get(position).getGarmentInstruction().equals("")||person.get(position).getGarmentInstruction().isEmpty()){

            holder.garment_instruction.setVisibility(View.GONE);
        }else {
            holder.garment_instruction.setText(person.get(position).getGarmentInstruction());
        }
*/
        PDialog.hide();

        return convertView;
    }

    public void receiveOrder() {
        PDialog.show(context);
        StringRequest request = new StringRequest(Request.Method.POST, context.getResources().getString(R.string.url_motherland) + "receive_order_submit.php?",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        PDialog.hide();
//                        Toast.makeText(thisActivity,"address saved",Toast.LENGTH_SHORT).show();
                        AlertDialog.Builder builder = new AlertDialog.Builder(context);
                        builder.setCancelable(false)
                                .setTitle("Success")
                                .setMessage("Order has received successfully")
                                .setNegativeButton("ok", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {

                                        Intent in=new Intent(context, PurchaseOrderDetails.class);
                                        context.startActivity(in);
                                        Activity activity=(Activity)context;
                                        activity.finish();
                                        activity.overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
                                    }
                                });
                        builder.show();

                        Log.v("response", response + "");
                        try {
                            PDialog.hide();
                            JSONArray ar = new JSONArray(response);



                        } catch (JSONException e) {
                            PDialog.hide();

                        } catch (Exception e) {
                            PDialog.hide();
                        }
                    }
                },

                new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError arg0) {
                        PDialog.hide();

                    }
                }) {

            @Override
            protected Map<String, String> getParams() {

                Map<String, String> params = new HashMap<>();

                params.put("id", StaticVariables.database.get(0).getId());
                params.put("order_id", StaticVariables.order_id);
                params.put("password", StaticVariables.database.get(0).getPassword());
                params.put("email", StaticVariables.database.get(0).getEmail());
                params.put("order_line_id", person.get(pos).getId());
                params.put("quantity", quantityList.get(pos));
                params.put("dc_number", dcNumberList.get(pos));


                return params;
            }
        };
        AppController.getInstance().addToRequestQueue(request, "data_receive");
        Log.v("request", request + "");
    }


    public void deliveryOrder() {
        PDialog.show(context);
        StringRequest request = new StringRequest(Request.Method.POST, context.getResources().getString(R.string.url_motherland) + "delivery_order_submit.php?",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        PDialog.hide();
//                        Toast.makeText(thisActivity,"address saved",Toast.LENGTH_SHORT).show();
                        AlertDialog.Builder builder = new AlertDialog.Builder(context);
                        builder.setCancelable(false)
                                .setTitle("Success")
                                .setMessage("Order has been submit for deliver")
                                .setNegativeButton("ok", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {

                                        Intent in=new Intent(context, PurchaseOrderDetails.class);
                                        context.startActivity(in);
                                        Activity activity=(Activity)context;
                                        activity.finish();
                                        activity.overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);


                                    }
                                });
                        builder.show();

                        Log.v("response", response + "");
                        try {
                            PDialog.hide();
                            JSONArray ar = new JSONArray(response);



                        } catch (JSONException e) {
                            PDialog.hide();

                        } catch (Exception e) {
                            PDialog.hide();
                        }
                    }
                },

                new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError arg0) {
                        PDialog.hide();

                    }
                }) {

            @Override
            protected Map<String, String> getParams() {

                Map<String, String> params = new HashMap<>();

                params.put("id", StaticVariables.database.get(0).getId());
                params.put("order_id", StaticVariables.order_id);
                params.put("password", StaticVariables.database.get(0).getPassword());
                params.put("email", StaticVariables.database.get(0).getEmail());
                params.put("order_line_id", person.get(pos).getId());
                params.put("quantity", quantityList.get(pos));
                params.put("dc_number", dcNumberList.get(pos));
                params.put("manual_dc_number", manualDcNumberList.get(pos));


                return params;
            }
        };
        AppController.getInstance().addToRequestQueue(request, "data_receive");
        Log.v("request", request + "");
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
        TextView garment_type,wash,wash_type,style,date,garment_instruction,received_Qty;
        EditText quantity,dc_number,manual_dc_number;
        ImageView edit,itemImage;
        Button order_button;

    }



}
