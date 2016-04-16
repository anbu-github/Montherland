package com.dev.montherland.customers.parsers;

import com.dev.montherland.model.Response_Model1;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by pf-05 on 2/6/2016.
 */
public class Response_JSONParser1 {
    public static Response_Model1 parserFeed(String content) {
        try {
            // JSONObject parentObject = new JSONObject(content);
            JSONArray ar = new JSONArray(content);
            Response_Model1 flower = new Response_Model1();
            ArrayList<String> id=new ArrayList<>();
            ArrayList<String> name=new ArrayList<>();
            for (int i = 0; i < ar.length(); i++) {
                JSONObject parentObject = ar.getJSONObject(i);
                id.add(parentObject.getString("id"));
                name.add(parentObject.getString("name"));
                //Log.d("success", parentObject.getString("success"));

            }
            flower.setId(id);
            flower.setName(name);

            return flower;
        } catch (JSONException e) {

            //Log.d("error in json", "l " + e);
            return null;
        } catch (Exception e) {
            //Log.d("json connection", "No internet access" + e);
            return null;
        }

    }
}
