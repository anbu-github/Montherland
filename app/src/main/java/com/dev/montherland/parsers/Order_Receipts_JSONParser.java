package com.dev.montherland.parsers;

import com.dev.montherland.OrderReceipts;
import com.dev.montherland.model.Order_Receipt_Model;
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

    public static List<Order_Receipt_Model> parserFeed(String content) {
        try {
            JSONArray ar = new JSONArray(content);
            List<Order_Receipt_Model> feedslist = new ArrayList<>();
            for (int i = 0; i < ar.length(); i++) {
                JSONObject obj = ar.getJSONObject(i);
                Order_Receipt_Model flower = new Order_Receipt_Model();

                flower.setQuantity(obj.getString("quantity"));
                flower.setDc_number(obj.getString("dc_number"));
                // flower.setAddress_line1(obj.getString("address_line1"));
                //flower.setAddress_line2(obj.getString("address_line2"));
                //flower.setAddress_line3(obj.getString("address_line3"));
                //flower.setCity(obj.getString("city"));
                //flower.setPincode(obj.getString("zipcode"));
                //flower.setState(obj.getString("state"));
                //flower.setStateId(obj.getString("state_id"));
                feedslist.add(flower);
            }
            return feedslist;
        } catch (JSONException e) {

            //Log.d("error in json", "l " + e);
            return null;
        } catch (Exception e) {
            //Log.d("json connection", "No internet access" + e);
            return null;
        }

    }
}
