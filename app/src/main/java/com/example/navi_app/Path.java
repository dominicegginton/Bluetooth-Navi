package com.example.navi_app;

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
        this.destination = connection.neighbor;
        this.totalWeight = connection.weight;
    }

    public Path(Connection connection, Path previousPath) {
        this.destination = connection.neighbor;
        this.previousPath = previousPath;
        this.totalWeight = previousPath.totalWeight + connection.weight;
    }

    public int compareTo(Path other){
        return Integer.compare(this.totalWeight, other.totalWeight);
    }

    public String convertToString() {
        Path interativePath = this;
        ArrayList<Node> pathOrder = new ArrayList<>();
        pathOrder.add(this.destination);
        Path previousPath = interativePath.previousPath;
        while (previousPath != null) {
            pathOrder.add(interativePath.previousPath.destination);
            previousPath = interativePath.previousPath;
        }


        String output = new String();
        for (int i = pathOrder.size(); i > 0; i--) {
            output += " [" + pathOrder.get(i-1).address + "] ";
        }

        return output;
    }
}
