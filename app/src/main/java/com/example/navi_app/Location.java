package com.example.navi_app;

import android.content.Context;
import java.util.ArrayList;

public class Location {

    public int id;
    public String name;
    public ArrayList<Node> nodes = new ArrayList<>();
    public String type;
    public String computers;
    public String workspace;
    public String food;
    private final String TAG = "LOCATION";
    public Context context;

    public Location(int newLocationID, String newLocationName, String newLocationType, String newLocationComputers, String newLocationWorkspaces, String newLocationFood, Context context) {
        this.id = newLocationID;
        this.name = newLocationName;
        this.type = newLocationType;
        this.computers = newLocationComputers;
        this.workspace = newLocationWorkspaces;
        this.food = newLocationFood;
        this.context = context;

        DatabaseHelp databaseHelper = new DatabaseHelp(context);
        this.nodes = databaseHelper.getNodes(this.id);
    }

}