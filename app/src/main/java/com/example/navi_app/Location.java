package com.example.navi_app;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;

public class Location implements Serializable {

    public String name;
    public ArrayList<Node> nodes = new ArrayList<>();
    public String type;

    public String computers;
    public String workspace;
    public String food;


    private final String TAG = "LOCATION";

    //Location System
    private LocationSystem currentSystem;


    public Location(JSONObject location, LocationSystem currentSystem) {

        // INIT Current location system
        this.currentSystem = currentSystem;

        // Try INIT location from json
        try {
            this.name = location.getString("name");

            this.type = location.getString("type");

            JSONObject locationData = location.getJSONObject("data");

            try{
                this.computers = locationData.getString("computers");
            }catch (JSONException e){
                this.computers = "";
            }
            try{
                this.workspace = locationData.getString("workspace");
            }catch (JSONException e){
                this.workspace = "";
            }
            try{
                this.food = locationData.getString("food");
            }catch (JSONException e){
                this.food = "";
            }

            JSONArray nodesArray = location.getJSONArray("nodes");

            for (int i=0; i < nodesArray.length(); i++)
            {
                JSONObject nodeJSON = nodesArray.getJSONObject(i);
                // Pulling items from the array
                nodes.add(new Node(nodeJSON, currentSystem));
            }
            Log.i(TAG, name);
        } catch (JSONException e) {
            e.printStackTrace();
            this.name = "";
            this.type = "";
            this.computers = "";
            this.workspace = "";
            this.food = "";
        }
    }
}