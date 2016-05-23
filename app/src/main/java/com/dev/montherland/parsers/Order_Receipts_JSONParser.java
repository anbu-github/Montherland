package com.dev.montherland.parsers;

import android.util.Log;

import com.dev.montherland.OrderReceipts;
import com.dev.montherland.model.Order_Receipt_Model;
import com.dev.montherland.model.Order_Receipts_Details_Model;
import com.dev.montherland.model.Profile_Model;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by pf-05 on 5/10/2016.
 */
public class Order_Receipts_JSONParser {

    public static List<Order_Receipts_Details_Model> parserFeed(String content) {
        try {
            JSONArray ar = new JSONArray(content);
            List<Order_Receipts_Details_Model> feedslist = new ArrayList<>();
            for (int i = 0; i < ar.length(); i++) {
                JSONObject obj = ar.getJSONObject(i);
                Order_Receipts_Details_Model flower = new Order_Receipts_Details_Model();

                flower.setId(obj.getString("id"));
                flower.setGarmentType(obj.getString("germent_type"));
                flower.setWashType(obj.getString("wash_type"));
                flower.setExpectedDeliveryDate(obj.getString("expected_delivery_date"));



                if (obj.getString("order_reciepts").equals("null")){
                    flower.setOrderReceipts("0");
                }
                else {
                    flower.setOrderReceipts(obj.getString("order_reciepts"));

                }

                if (obj.getString("order_delivery").equals("null")){
                    flower.setOrderDelivery("0");
                }
                else {
                    flower.setOrderDelivery(obj.getString("order_delivery"));

                }
                flower.setQuantityOrdered(obj.getString("quantity_ordered"));
                flower.setStyleNum(obj.getString("style_number"));

                feedslist.add(flower);
            }
            return feedslist;
        } catch (JSONException e) {
            e.printStackTrace();

            Log.d("error in json", "l " + e);
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            Log.d("json connection", "No internet access" + e);
            return null;
        }

    }
}
