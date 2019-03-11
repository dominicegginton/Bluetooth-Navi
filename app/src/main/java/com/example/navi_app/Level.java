package com.example.navi_app;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class Level {

    public String name;
    public ArrayList<Location> locations = new ArrayList<>();
    private final String TAG = "LEVEL";

    public Level(JSONObject level) {

        try {
            this.name = level.getString("name");

            JSONArray locationsArray = level.getJSONArray("locations");

            for (int i=0; i < locationsArray.length(); i++)
            {
                JSONObject locationJSON = locationsArray.getJSONObject(i);
                // Pulling items from the array
                locations.add(new Location(locationJSON));
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
}
