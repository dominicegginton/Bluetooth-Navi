package com.example.navi_app;

import android.content.Context;

public class Connection {

    public String neighbor;
    public int weight;
    private final String TAG = "CONNECTION";

    //Location System
    private LocationSystem currentSystem;
    private Context context;

    public Connection (String newNeighbor, int newWeight, Context context) {
        this.neighbor = newNeighbor;
        this.weight = newWeight;
        this.context = context;
    }

    public Connection (String newNeighbor, int newWeight, LocationSystem currentSystem) {
        this.currentSystem = currentSystem;
        this.neighbor = newNeighbor;
        this.weight = newWeight;
    }

    public Node getNeighbor() {
        DatabaseHelp databaseHelper = new DatabaseHelp(this.context);
        return databaseHelper.getNode(this.neighbor);
    }
}
