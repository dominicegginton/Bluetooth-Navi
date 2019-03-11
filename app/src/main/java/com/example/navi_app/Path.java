package com.example.navi_app;

import android.util.Log;

import java.util.ArrayList;

public class Path implements Comparable<Path> {

    public int totalWeight;
    public Node destination;
    public Path previousPath;
    private final String TAG = "PATH";

    //Location System
    private LocationSystem currentSystem;

    public Path(Node destination, LocationSystem currentSystem) {
        this.destination = destination;
        this.currentSystem = currentSystem;
        this.totalWeight = 0;
    }

    public Path(Connection connection, LocationSystem currentSystem) {
        this.destination = connection.getNeighbor();
        this.totalWeight = connection.weight;
        this.currentSystem = currentSystem;
    }

    public Path(Connection connection, Path previousPath, LocationSystem currentSystem) {
        this.destination = connection.getNeighbor();
        this.previousPath = previousPath;
        this.totalWeight = previousPath.totalWeight + connection.weight;
        this.currentSystem = currentSystem;
    }

    public int compareTo(Path other){
        return Integer.compare(this.totalWeight, other.totalWeight);
    }

    public String convertToString() {
        String output = new String();
        ArrayList<Node> pathOrder = new ArrayList<>();

        Path interativePath = this;

        if (interativePath != null) {
            while (interativePath != null) {
                pathOrder.add(interativePath.destination);
                interativePath = interativePath.previousPath;
            }
        }

        for(int i = pathOrder.size() - 1; i >= 0; i--){
            Location navigationLocation = currentSystem.getLocation(pathOrder.get(i));
            output += navigationLocation.name + " { " + pathOrder.get(i).address + " } ---> ";
        }
        output += "Arrived :)";
        return output;

    }
}
