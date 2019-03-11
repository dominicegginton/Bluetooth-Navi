package com.example.navi_app;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class Location {

    public String name;
    public ArrayList<Node> nodes = new ArrayList<>();
    public int computersAvadiable;
    private final String TAG = "LOCATION";

    //Location System
    private LocationSystem currentSystem;


    public Location(JSONObject location, LocationSystem currentSystem) {

        this.currentSystem = currentSystem;

        try {
            this.name = location.getString("name");

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
        }
    }
}