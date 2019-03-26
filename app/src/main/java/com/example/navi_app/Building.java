package com.example.navi_app;

import android.content.Context;
import android.util.Log;

import java.util.ArrayList;

/**
 * Building
 * Building object holds all data for a building
 */
public class Building {

    // Building id
    private int id;
    // Building name
    public String name;
    // Arraylist of Levels
    public ArrayList<Level> levels;
    // Log TAG
    private final String TAG = "Building";
    // Context
    private Context context;

    /**
     * INIT Building
     * @param newBuildingID id of new building object
     * @param newBuidlingName name of new building object
     * @param context context of the new building object
     */
    public Building(int newBuildingID, String newBuidlingName, Context context) {

        // Set data
        this.id = newBuildingID;
        this.name = newBuidlingName;
        this.context = context;

        // Create new database helper object
        DatabaseHelp databaseHelper = new DatabaseHelp(context);
        // Get levels from database
        this.levels = databaseHelper.getLevels(this.id);

        // Log
        Log.v(TAG, "Building object init");

    }
}
