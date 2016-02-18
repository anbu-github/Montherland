package com.dev.montherland.parsers;

import com.dev.montherland.model.Create_Address_Model;
import com.dev.montherland.model.Purchase_Order_Model;
import com.dev.montherland.util.StaticVariables;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by pf-05 on 2/8/2016.
 */
public class Purchase_Order_JSONParser {
    public static List<Purchase_Order_Model> parserFeed(String content) {
        try {
            // JSONObject parentObject = new JSONObject(content);
            StaticVariables.quantityList.clear();
            JSONArray ar = new JSONArray(content);
            List<Purchase_Order_Model> respnse = new ArrayList<>();
            for (int i = 0; i < ar.length(); i++) {
                JSONObject parentObject = ar.getJSONObject(i);
                Purchase_Order_Model flower = new Purchase_Order_Model();
                flower.setId(parentObject.getString("id"));
                StaticVariables.quantityList.add(parentObject.getString("id"));
                flower.setDate(parentObject.getString("DATE"));
                flower.setCustomer_company(parentObject.getString("customer_company"));
                flower.setCustomer_contact(parentObject.getString("customer_contact"));
                flower.setOrder_type(parentObject.getString("order_type"));
                flower.setCustomer_contact(parentObject.getString("customer_contact"));
                flower.setQuantity(parentObject.getString("quantity"));
                //Log.d("success", parentObject.getString("success"));
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
