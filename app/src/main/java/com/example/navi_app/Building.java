package com.example.navi_app;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;

public class Building implements Serializable {

    public String name;
    public ArrayList<Level> levels = new ArrayList<>();
    private final String TAG = "BUILDING";

    //Location System
    private LocationSystem currentSystem;


    public Building(JSONObject building, LocationSystem currentSystem) {

        try {
            this.currentSystem = currentSystem;
            this.name = building.getString("name");

            JSONArray levelsArray = building.getJSONArray("levels");

            for (int i=0; i < levelsArray.length(); i++)
            {
                JSONObject buildingJSON = levelsArray.getJSONObject(i);
                // Pulling items from the array
                levels.add(new Level(buildingJSON, currentSystem));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
