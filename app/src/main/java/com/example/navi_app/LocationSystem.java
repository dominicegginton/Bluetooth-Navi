package com.example.navi_app;

import android.util.Log;

import java.util.ArrayList;

public class LocationSystem {

    public String name;
    public ArrayList<Building> buildings;

    public LocationSystem() {
        this.buildings = new ArrayList<>();
    }

    /**
     * Returns Location object bacesed on the users location.
     * @return      the location object of where the users is in
     */
    public Location getCurrentLocation(ArrayList<BLNode> scannedNodes) {

        // Filter scannedNodes

        if (scannedNodes.size() != 0) {
            ArrayList<BLNode> systemNodes = new ArrayList<>();

            for (BLNode scannedNode: scannedNodes) {
                for (Building building : this.buildings) {
                    for (Level level : building.levels) {
                        for (Location location : level.locations) {
                            for (Node node : location.nodes) {
                                if (scannedNode.address.equals(node.address)) {
                                    systemNodes.add(scannedNode);
                                }
                            }
                        }
                    }
                }
            }

            // Sort BLNodes

            BLNode closestNode = new BLNode("", "", -10000);
            for (BLNode node: systemNodes) {
                if (node.rssi.intValue() > closestNode.rssi.intValue()) {
                    closestNode = node;
                }
            }
            Log.i("closestNode", String.valueOf(closestNode.address));

            // Return Location Object
            for (Building building : buildings) {
                for (Level level : building.levels) {
                    for (Location location : level.locations) {
                        for (Node node : location.nodes) {
                            if (node.address.equals(closestNode.address)) {
                                return location;
                            }
                        }
                    }
                }
            }

        }else {

            throw new IllegalArgumentException("No Bluetooth Devices Found :(");
            /// Location Not Found
        }
        return null;
    }

    public Level getCurrentLevel(Location currentLocation){
        // Return Location Object
        for (Building building : buildings) {
            for (Level level : building.levels) {
                for (Location location : level.locations) {
                    if (location == currentLocation) {
                        return level;
                    }
                }
            }
        }
        return null;
    };

    public Building getCurrentBuilding(Location currentLocation) {
        for (Building building : buildings) {
            for (Level level : building.levels) {
                for (Location location : level.locations) {
                    if (location == currentLocation) {
                        return building;
                    }
                }
            }
        }
        return null;
    }
}
