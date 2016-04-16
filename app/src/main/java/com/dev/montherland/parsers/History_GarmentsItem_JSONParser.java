package com.dev.montherland.parsers;

import com.dev.montherland.model.GarmentListModel1;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by pf-05 on 3/18/2016.
 */
public class History_GarmentsItem_JSONParser {


    public static List<GarmentListModel1> parserFeed(String content) {
        try {
            // JSONObject parentObject = new JSONObject(content);
            JSONArray ar = new JSONArray(content);
            List<GarmentListModel1> respnse = new ArrayList<>();
            for (int i = 0; i < ar.length(); i++) {
                JSONObject parentObject = ar.getJSONObject(i);
                GarmentListModel1 flower = new GarmentListModel1();
                flower.setGarmentName(parentObject.getString("garment"));
                flower.setGarmentQuantity(parentObject.getString("quantity"));
                flower.setId(parentObject.getString("purchase_order_id"));
                flower.setWashType(parentObject.getString("wash"));
                flower.setStyleNumber(parentObject.getString("style_number"));
                flower.setGarmentInstruction(parentObject.getString("instructions"));
                flower.setExpectedDelivery(parentObject.getString("expected_delivery"));
                flower.setStatus(parentObject.getString("status"));
                flower.setUpdatedBy(parentObject.getString("updated_by"));
                flower.setUpdated(parentObject.getString("updated"));
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
