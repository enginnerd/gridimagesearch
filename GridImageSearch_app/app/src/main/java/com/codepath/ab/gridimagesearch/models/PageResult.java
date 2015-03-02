package com.codepath.ab.gridimagesearch.models;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by andrewblaich on 2/26/15.
 */
public class PageResult {
    public String start;

    public PageResult(JSONObject json){
        try{
            this.start = json.getString("start");
        }catch(JSONException e){
            e.printStackTrace();
        }
    }

    public static ArrayList<PageResult> fromJsonArray(JSONArray array){
        ArrayList<PageResult> results = new ArrayList<PageResult>();
        for(int i=0; i<array.length(); i++){
            try{
                results.add(new PageResult(array.getJSONObject(i)));
            }catch(JSONException e){
                e.printStackTrace();
            }
        }
        return results;
    }
}
