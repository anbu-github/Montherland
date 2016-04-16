package com.dev.montherland.customers.parsers;

import com.dev.montherland.model.Customer_Contact_Model;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by pf-05 on 3/8/2016.
 */
public class Customer_Contact_JSONParser {


    public static List<Customer_Contact_Model> parserFeed(String content) {
        try {
            JSONArray ar = new JSONArray(content);
            List<Customer_Contact_Model> respnse = new ArrayList<>();
            for (int i = 0; i < ar.length(); i++) {
                JSONObject parentObject = ar.getJSONObject(i);
                Customer_Contact_Model flower = new Customer_Contact_Model();
                flower.setId(parentObject.getString("id"));
                flower.setName(parentObject.getString("name"));
                flower.setEmail(parentObject.getString("email"));
                flower.setPhone(parentObject.getString("phone"));
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
