package com.example.navi_app;

import android.content.Context;

/**
 * Connection
 * Connection to another node, holds information about the neighbouring node and the weight of the connection
 */
public class Connection {

    // Connection Neighbor
    public String neighbor;
    // Connection Weight
    public int weight;
    // Log TAG
    private final String TAG = "CONNECTION";
    // Context
    private Context context;

    /**
     * INIT Connection
     * @param newNeighbor new neighbor address
     * @param newWeight new connection weight
     * @param context context
     */
    public Connection (String newNeighbor, int newWeight, Context context) {

        // Set data
        this.neighbor = newNeighbor;
        this.weight = newWeight;
        this.context = context;

    }

    /**
     * getNeighbor
     * get the connections neighbor object
     * @return Node object that is neighbor of this connection
     */
    public Node getNeighbor() {

        // Create new databasehelp object
        DatabaseHelp databaseHelper = new DatabaseHelp(this.context);
        // Return nodd object from datase
        return databaseHelper.getNode(this.neighbor);

    }
}
