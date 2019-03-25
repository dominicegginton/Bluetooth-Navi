package com.example.navi_app;

import android.content.Context;

import java.util.ArrayList;

public class Level {

    private int id;
    public String name;
    public ArrayList<Location> locations;
    private final String TAG = "LEVEL";
    private Context context;

    public Level(int newLevelID, String newLevelName, Context context) {
        this.id = newLevelID;
        this.name = newLevelName;
        this.context = context;

        DatabaseHelp databaseHelper = new DatabaseHelp(context);
        this.locations = databaseHelper.getLocations(this.id);
    }
}
