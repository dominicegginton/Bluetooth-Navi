package com.example.navi_app;

import android.content.Context;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;

public class Building implements Serializable {

    private int id;
    public String name;
    public ArrayList<Level> levels = new ArrayList<>();
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
