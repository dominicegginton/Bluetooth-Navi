package com.example.navi_app;

import android.content.Context;
import android.util.Log;

import java.util.ArrayList;

/**
 * Node
 * Node object holds all data about a given node within the location system
 */
public class Node implements Comparable<BLNode>{

    // Node address
    public String address;
    // Arraylist of connections
    public ArrayList<Connection> connections = new ArrayList<>();
    // Log TAG
    private final String TAG = "Node";
    // Context
    private transient Context context;

    /**
     * INIT Node
     * @param newNodeAddress new node address
     * @param context context
     */
    public Node(String newNodeAddress, Context context) {

        // Set data
        this.address = newNodeAddress;
        this.context = context;

        // Create new database helper object
        DatabaseHelp databaseHelper = new DatabaseHelp(context);
        // Get connections from database
        this.connections = databaseHelper.getConnections(address);

        // Log
        Log.v(TAG, "Node INIT");
    }

    /**
     * INIT Node
     * @param address new node address
     */
    public Node(String address) {

        // Set data
        this.address = address;

        // Log
        Log.v(TAG, "Node INIT");

    }

    /**
     * INIT Node
     * @param address new node address
     * @param connections arraylist of connections
     */
    public Node(String address, ArrayList<Connection> connections) {

        // Set data
        this.address = address;
        this.connections = connections;

        // Log
        Log.v(TAG, "Node INIT");

    }

    /**
     * Compare to another Node object to check if they match
     * @param o another Node object to compare to
     * @return int 0 if the objects match and 1 if not
     */
    public int compareTo(BLNode o) {

        // If this Node objects address is equal to the others objects address
        if (this.address == o.address) {

            // Return 0
            return 0;

        } else {

            // Return 1
            return 1;

        }

    }

}
