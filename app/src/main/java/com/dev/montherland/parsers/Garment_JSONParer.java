package com.dev.montherland.parsers;

import com.dev.montherland.model.GarmentListModel;
import com.dev.montherland.model.Response_Model;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by pf-05 on 2/12/2016.
 */
public class Garment_JSONParer {
    public static List<GarmentListModel> parserFeed() {
        try {
            // JSONObject parentObject = new JSONObject(content);
            List<GarmentListModel> respnse = new ArrayList<>();
            for (int i = 0; i < 20; i++) {
                GarmentListModel flower = new GarmentListModel();
                flower.setGarmentTypeId("");
                flower.setGarmentType("Select Garment Type");
                flower.setGarmentQuantity("");
                flower.setGarmentStyle("");
                flower.setGarmentInstr("");
                flower.setGarmentWashType("Select Wash Type");
                //Log.d("success", parentObject.getString("success"));
                respnse.add(flower);
            }

            return respnse;
        }  catch (Exception e) {
            //Log.d("json connection", "No internet access" + e);
            return null;
        }

    }
}
