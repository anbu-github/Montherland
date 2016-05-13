package com.dev.montherland.parsers;

import com.dev.montherland.model.Customer_Report_Model;
import com.dev.montherland.model.ExistingUser_Model;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by pf-05 on 4/29/2016.
 */
public class Customer_Orders_Report_JSONParser {
    public static List<Customer_Report_Model> parserFeed(String content) {
        try {
            JSONArray ar = new JSONArray(content);
            List<Customer_Report_Model> feedslist = new ArrayList<>();
            for (int i = 0; i < ar.length(); i++) {
                JSONObject obj = ar.getJSONObject(i);
                Customer_Report_Model flower = new Customer_Report_Model();
                flower.setName(obj.getString("name"));
                flower.setTotal_orders(obj.getString("total_orders"));
                flower.setStarted_orders(obj.getString("started_orders"));
                flower.setName(obj.getString("name"));
                flower.setInprogress_orders(obj.getString("inprogress_orders"));
                flower.setCompleted_orders(obj.getString("completed_orders"));
                flower.setCancelled_orders(obj.getString("cancelled_orders"));
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
