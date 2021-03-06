package com.dev.montherland.parsers;

import com.dev.montherland.model.Customer_Details_Model;
import com.dev.montherland.util.StaticVariables;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class Customer_Details_Parser {

    public static List<Customer_Details_Model> parserFeed(String content) {
        try {
            // JSONObject parentObject = new JSONObject(content);
            JSONArray ar = new JSONArray(content);
            List<Customer_Details_Model> respnse = new ArrayList<>();
            for (int i = 0; i < ar.length(); i++) {
                JSONObject parentObject = ar.getJSONObject(i);
                Customer_Details_Model flower = new Customer_Details_Model();
                flower.setId(parentObject.getString("id"));
                StaticVariables.quantityList.add(parentObject.getString("id"));
                flower.setName(parentObject.getString("name"));
                flower.setDate(parentObject.getString("DATE"));
                flower.setCompany_name(parentObject.getString("order_type"));
                flower.setOrder_type(parentObject.getString("order_type"));
                flower.setQuanity(parentObject.getString("quantity"));
                //Log.d("set", parentObject.getString("success"));
                respnse.add(flower);
            }

            return respnse;
        } catch (JSONException e) {

            //Log.d("error in json", "l " + e);
            return null;
        } catch (Exception e) {
            //Log.d("json connection", "No internet access" + e);
            return null;
        }

    }
}
