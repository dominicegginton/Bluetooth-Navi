package com.example.navi_app;

import android.content.Context;
import android.util.Log;

import java.util.ArrayList;

/**
 * Level
 * Level objets holds all data about a given level
 */
public class Level {

    // Level id
    private int id;
    // Level name
    public String name;
    // Arraylist of locations
    public ArrayList<Location> locations;
    // Log TAG
    private final String TAG = "Level";
    // Context
    private Context context;

    /**
     * INIT Level
     * @param newLevelID new level id
     * @param newLevelName new level name
     * @param context context
     */
    public Level(int newLevelID, String newLevelName, Context context) {

        // Set data
        this.id = newLevelID;
        this.name = newLevelName;
        this.context = context;

        // Create new databasehelper object
        DatabaseHelp databaseHelper = new DatabaseHelp(context);
        // Get location from data in this level
        this.locations = databaseHelper.getLocations(this.id);

        // Log
        Log.v(TAG, "New Level INIT");
    }
}
