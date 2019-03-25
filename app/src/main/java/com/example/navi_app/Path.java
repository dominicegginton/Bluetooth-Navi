package com.example.navi_app;

import java.util.ArrayList;

public class Path implements Comparable<Path> {

    public int totalWeight;
    public Node destination;
    public Path previousPath;
    private final String TAG = "PATH";

    public Path(Node destination) {
        this.destination = destination;
        this.totalWeight = 0;
    }

    public Path(Connection connection, Path previousPath) {
        this.destination = connection.getNeighbor();
        this.previousPath = previousPath;
        this.totalWeight = previousPath.totalWeight + connection.weight;
        //this.context = context;
    }

    public int compareTo(Path other){
        return Integer.compare(this.totalWeight, other.totalWeight);
    }

    public ArrayList<Node> convertToArrayList() {
        ArrayList<Node> pathOrder = new ArrayList<>();

        Path interativePath = this;

        if (interativePath != null) {
            while (interativePath != null) {
                pathOrder.add(interativePath.destination);
                interativePath = interativePath.previousPath;
            }
        }
        return pathOrder;
    }
}
