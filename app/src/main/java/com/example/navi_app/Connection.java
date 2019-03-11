package com.example.navi_app;

public class Connection {
    private String neighbor;
    public int weight;
    private final String TAG = "CONNECTION";

    //Location System
    private LocationSystem currentSystem;

    public Connection (String newNeighbor, int newWeight, LocationSystem currentSystem) {
        this.currentSystem = currentSystem;
        this.neighbor = newNeighbor;
        this.weight = newWeight;
    }

    public Node getNeighbor() {
        return currentSystem.getNode(neighbor);
    }
}
