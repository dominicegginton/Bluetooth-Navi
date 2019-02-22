package com.example.navi_app;

import java.util.ArrayList;

public class LocationSystem {

    public String name;
    public ArrayList<Building> buildings;

    /**
     * Returns Location object bacesed on the users location.
     * @return      the location object of where the users is in
     */
    public Location getCurrentLocation(ArrayList<BLNode> scannedNodes) {
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

        throw new IllegalArgumentException("No Bluetooth Devices Found :(");
        /// Location Not Found
    }
}