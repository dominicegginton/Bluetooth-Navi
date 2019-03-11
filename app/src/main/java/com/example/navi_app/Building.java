package com.example.navi_app;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class Building {

    public String name;
    public ArrayList<Level> levels = new ArrayList<>();
    private final String TAG = "BUILDING";


    public Building(JSONObject building) {

        try {
            this.name = building.getString("name");

            JSONArray levelsArray = building.getJSONArray("levels");

            for (int i=0; i < levelsArray.length(); i++)
            {
                JSONObject buildingJSON = levelsArray.getJSONObject(i);
                // Pulling items from the array
                levels.add(new Level(buildingJSON));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
