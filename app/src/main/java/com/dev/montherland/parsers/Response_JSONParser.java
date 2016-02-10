package com.dev.montherland.parsers;



import com.dev.montherland.model.Response_Model;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class Response_JSONParser {

    public static List<Response_Model> parserFeed(String content) {
        try {
            // JSONObject parentObject = new JSONObject(content);
            JSONArray ar = new JSONArray(content);
            List<Response_Model> respnse = new ArrayList<>();
            for (int i = 0; i < ar.length(); i++) {
                JSONObject parentObject = ar.getJSONObject(i);
                Response_Model flower = new Response_Model();
                flower.setId(parentObject.getString("id"));
                flower.setName(parentObject.getString("name"));
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

