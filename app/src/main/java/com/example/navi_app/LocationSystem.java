package com.example.navi_app;

import java.util.ArrayList;

public class LocationSystem {

    public String name;
    public ArrayList<Building> buildings;
    public ArrayList<Alert> alerts;

    /**
     * Returns Location object bacesed on the users location.
     * @return      the location object of where the users is in
     */
    public Location getCurrentLocation() {
        Bluetooth ble = new Bluetooth();
        ble.scan();

        if (ble.scannedNodes.size() != 0) {

            for (Building building : buildings) {
                for (Level level : building.Levels) {
                    for (Location location : level.locations) {
                        for (Node node : location.nodes) {

                            if (node.address == ble.scannedNodes.get(0).address) {
                                return location;
                            }
                        }
                    }
                }
            }

        }

        throw new IllegalArgumentException("Location out of bounds");
        /// Location Not Found
    }
}