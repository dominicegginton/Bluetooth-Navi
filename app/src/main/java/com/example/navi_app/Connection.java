package com.example.navi_app;

public class Connection {
    public Node neighbor;
    public int weight;
    private final String TAG = "CONNECTION";

    public Connection (Node newNeighbor, int newWeight) {
        this.neighbor = newNeighbor;
        this.weight = newWeight;
    }
}
