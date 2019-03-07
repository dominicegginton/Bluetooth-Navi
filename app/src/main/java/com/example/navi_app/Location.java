package com.example.navi_app;

import java.util.ArrayList;

public class Location {

    public String name;
    public ArrayList<Node> nodes;
    public int computersAvadiable;

    public Location() {
        this.nodes = new ArrayList<>();
    }
}