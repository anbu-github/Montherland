package com.dev.montherland.parsers;

import com.dev.montherland.model.Create_Address_Model;
import com.dev.montherland.model.PurchaseOrderDetailsModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by pf-05 on 2/12/2016.
 */
public class Purchase_OrderDetails_JSONParser {

    public static List<PurchaseOrderDetailsModel> parserFeed(String content) {
        try {
            // JSONObject parentObject = new JSONObject(content);
            JSONArray ar = new JSONArray(content);
            List<PurchaseOrderDetailsModel> respnse = new ArrayList<>();
            for (int i = 0; i < ar.length(); i++) {
                JSONObject parentObject = ar.getJSONObject(i);
                PurchaseOrderDetailsModel flower = new PurchaseOrderDetailsModel();
                flower.setAddressline1(parentObject.getString("address_line1"));
                flower.setAddressline2(parentObject.getString("address_line2"));
                flower.setAddressline3(parentObject.getString("address_line3"));
                flower.setCity(parentObject.getString("city"));
                flower.setZipcode(parentObject.getString("zipcode"));
                flower.setState(parentObject.getString("state"));
                flower.setCountry(parentObject.getString("country"));
                flower.setCustomerContact(parentObject.getString("customer_contact"));
                flower.setCustomerCompany(parentObject.getString("customer_company"));
                flower.setDate(parentObject.getString("DATE"));
                flower.setId(parentObject.getString("id"));
                flower.setExpected_delivery(parentObject.getString("expected_delivery"));
                flower.setExpected_pickup(parentObject.getString("expected_pickup"));
                flower.setInstruction(parentObject.getString("instructions"));
                flower.setStatus(parentObject.getString("status"));
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
