package com.example.navi_app;

import android.util.Log;

import java.util.ArrayList;

public class Path implements Comparable<Path> {

    public int totalWeight;
    public Node destination;
    public Path previousPath;
    private final String TAG = "PATH";

    public Path(Node destination) {
        this.destination = destination;
    }

    public Path(Connection connection) {
        this.destination = connection.getNeighbor();
        this.totalWeight = connection.weight;
    }

    public Path(Connection connection, Path previousPath) {
        this.destination = connection.getNeighbor();
        this.previousPath = previousPath;
        this.totalWeight = previousPath.totalWeight + connection.weight;
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
        for (int i = pathOrder.size(); i > 0; i--) {
            output += " [" + pathOrder.get(i-1).address + "] ";
        }
        return output;

    }
}
