package com.example.navi_app;

import android.content.Context;
import android.util.Log;

import java.util.ArrayList;

/**
 * Location
 * Location object holds all data about a given location
 */
public class Location {

    // Location id
    public int id;
    // Location name
    public String name;
    // Arraylist of nodes
    public ArrayList<Node> nodes = new ArrayList<>();
    // Location type
    public String type;
    // Location computers
    public String computers;
    // Location workspaces
    public String workspace;
    // Location food
    public String food;
    // Log TAG
    private final String TAG = "LOCATION";
    // Context
    public Context context;

    /**
     * INIT Location
     * @param newLocationID new location id
     * @param newLocationName new location name
     * @param newLocationType new location type
     * @param newLocationComputers new location computers
     * @param newLocationWorkspaces new location workspaces
     * @param newLocationFood new locaiton food
     * @param context context
     */
    public Location(int newLocationID, String newLocationName, String newLocationType, String newLocationComputers, String newLocationWorkspaces, String newLocationFood, Context context) {

        // Set data
        this.id = newLocationID;
        this.name = newLocationName;
        this.type = newLocationType;
        this.computers = newLocationComputers;
        this.workspace = newLocationWorkspaces;
        this.food = newLocationFood;
        this.context = context;

        // Create new databse helper objectg
        DatabaseHelp databaseHelper = new DatabaseHelp(context);
        // Get node objects for location from database
        this.nodes = databaseHelper.getNodes(this.id);

        // Log
        Log.v(TAG, "INIT Location");
    }

}