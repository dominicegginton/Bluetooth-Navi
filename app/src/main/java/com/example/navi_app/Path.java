package com.example.navi_app;

import android.util.Log;

import java.util.ArrayList;

/**
 * Path
 * Path object holds all data about a given path to navigate around the location system
 */
public class Path implements Comparable<Path> {

    // Path total weight
    public int totalWeight;
    // Path destination Node
    public Node destination;
    // Previous path
    public Path previousPath;
    // Log TAF
    private final String TAG = "Path";

    /**
     * INIT Path
     * @param destination destination Node
     */
    public Path(Node destination) {

        // Set data
        this.destination = destination;
        this.totalWeight = 0;

        // Log
        Log.v(TAG, "Path INIT");

    }

    /**
     * INIT Path
     * @param connection connection with data about destination and path total weight
     * @param previousPath previous path object
     */
    public Path(Connection connection, Path previousPath) {

        // Set data
        this.destination = connection.getNeighbor();
        this.previousPath = previousPath;

        // Calculate and set total path weight
        this.totalWeight = previousPath.totalWeight + connection.weight;

        // Log
        Log.v(TAG, "Path INIT");

    }

    /**
     * compareTO
     * compare to another Path object
     * @param other another Path obejct to compare against
     * @return integer
     */
    public int compareTo(Path other){

        // Return
        return Integer.compare(this.totalWeight, other.totalWeight);
    }

    /**
     * convertToArrayList
     * Convert this path object to a arraylist of Nodes
     * @return ArrayList of Nodes that represent the path object
     */
    public ArrayList<Node> convertToArrayList() {

        // Create new array list of Nodes
        ArrayList<Node> pathOrder = new ArrayList<>();
        // Create new path object
        Path interativePath = this;

        // If path object is not null
        if (interativePath != null) {

            // While path object is not null
            while (interativePath != null) {

                // Add path objects destination node to pathOrder arraylist
                pathOrder.add(interativePath.destination);
                // Set Path to previousPath
                interativePath = interativePath.previousPath;

            }
        }

        // Return pathOder
        return pathOrder;

    }
}
