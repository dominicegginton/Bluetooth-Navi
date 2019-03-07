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
     * @return the location object of where the users is in
     */
    public Location getCurrentLocation(ArrayList<BLNode> scannedNodes) {

        // Filter scannedNodes
        // Check if scanned nodes size is 0
        if (scannedNodes.size() != 0) {

            // Define new list of node as system nodes <- nodes that belong to our location system
            ArrayList<BLNode> systemNodes = new ArrayList<>();


            for (BLNode scannedNode: scannedNodes) {
                for (Building building : this.buildings) {
                    for (Level level : building.levels) {
                        for (Location location : level.locations) {
                            for (Node node : location.nodes) {

                                // If scanned node is in location system add it to the system nodes list
                                if (scannedNode.address.equals(node.address)) {
                                    systemNodes.add(scannedNode);
                                }

                            }
                        }
                    }
                }
            }

            // Sort BLNodes
            // Define new node as closest node <- '-10000' used as rssi as this will always be larger than real scanned nodes
            BLNode closestNode = new BLNode("", "", -10000);
            for (BLNode node: systemNodes) {

                // If node is closer than closest node assign closest node to it
                if (node.rssi.intValue() > closestNode.rssi.intValue()) {
                    closestNode = node;
                }

            }

            // Return the location object that the closest node belongs to
            for (Building building : buildings) {
                for (Level level : building.levels) {
                    for (Location location : level.locations) {
                        for (Node node : location.nodes) {

                            // If node address is the same as the closest node address return the location that the node belongs to
                            if (node.address.equals(closestNode.address)) {
                                return location;
                            }

                        }
                    }
                }
            }

        }else {
            // No scanned nodes
            throw new IllegalArgumentException("No Bluetooth Devices Found :(");
        }
        // Return null
        return null;
    }

    public Level getCurrentLevel(Location currentLocation){

        // Search for level using location object
        for (Building building : buildings) {
            for (Level level : building.levels) {
                for (Location location : level.locations) {

                    // If location is the same as current location return level
                    if (location == currentLocation) {
                        return level;
                    }

                }
            }
        }
        return null;
    };

    public Building getCurrentBuilding(Location currentLocation) {

        // Search for the building using location object
        for (Building building : buildings) {
            for (Level level : building.levels) {
                for (Location location : level.locations) {

                    // If location is the same as current location return building
                    if (location == currentLocation) {
                        return building;
                    }

                }
            }
        }
        return null;
    }
}
