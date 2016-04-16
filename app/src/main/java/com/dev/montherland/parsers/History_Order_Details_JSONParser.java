package com.dev.montherland.parsers;

import com.dev.montherland.model.PurchaseOrderDetailsModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by pf-05 on 3/17/2016.
 */
public class History_Order_Details_JSONParser {


    public static List<PurchaseOrderDetailsModel> parserFeed(String content) {
        try {
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
                flower.setId(parentObject.getString("purchase_order_header_id"));
                flower.setExpected_delivery(parentObject.getString("expected_delivery"));
                flower.setExpected_pickup(parentObject.getString("expected_pickup"));
                flower.setInstruction(parentObject.getString("instructions"));
                flower.setStatus(parentObject.getString("status"));
                flower.setOrder_type(parentObject.getString("order_type"));
                flower.setCustomerId(parentObject.getString("customer_contact_id"));
                flower.setPickupdate(parentObject.getString("expected_pick_up_date"));
                flower.setDeliverydate(parentObject.getString("expected_delivery_date"));
                flower.setAddressid(parentObject.getString("customer_contact_address_id"));
                flower.setCreatedBy(parentObject.getString("created_by"));
                flower.setUpdated(parentObject.getString("updated"));
                flower.setUpdatedBy(parentObject.getString("updated_by"));
                flower.setCreated(parentObject.getString("created"));

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
