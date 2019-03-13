package com.example.navi_app;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;

public class Level implements Serializable {

    public String name;
    public ArrayList<Location> locations = new ArrayList<>();
    private final String TAG = "LEVEL";

    //Location System
    private LocationSystem currentSystem;

    public Level(JSONObject level, LocationSystem currentSystem) {

        this.currentSystem = currentSystem;

        try {
            this.name = level.getString("name");

            JSONArray locationsArray = level.getJSONArray("locations");

            for (int i=0; i < locationsArray.length(); i++)
            {
                JSONObject locationJSON = locationsArray.getJSONObject(i);
                // Pulling items from the array
                locations.add(new Location(locationJSON, currentSystem));
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
}
