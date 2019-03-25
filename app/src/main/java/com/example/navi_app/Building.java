package com.example.navi_app;

import android.content.Context;
import java.util.ArrayList;

public class Building {

    private int id;
    public String name;
    public ArrayList<Level> levels;
    private final String TAG = "BUILDING";
    private Context context;

    public Building(int newBuildingID, String newBuidlingName, Context context) {
        this.id = newBuildingID;
        this.name = newBuidlingName;
        this.context = context;

        DatabaseHelp databaseHelper = new DatabaseHelp(context);
        this.levels = databaseHelper.getLevels(this.id);
    }
}
