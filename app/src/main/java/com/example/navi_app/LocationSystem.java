package com.example.navi_app;

import java.util.ArrayList;

public class LocationSystem {

    public String name;
    public ArrayList<Building> buildings;
    public ArrayList<Alert> alerts;

    public Location () {
        Bluetooth ble = new Bluetooth();
        ArrayList<BLNode> scannedNodes = ble.scan(new ArrayList<Node>());

        if (scannedNodes.size() != 0) {

            for (Building building : buildings) {
                for (Level level : building.Levels) {
                    for (Location location : level.locations) {
                        for (Node node : location.nodes) {

                            if (node.address == scannedNodes.get(0).address) {
                                return location;
                            }
                        }
                    }
                }
            }

        }

        throw new IllegalArgumentException("Location out of bounds");
    }
}