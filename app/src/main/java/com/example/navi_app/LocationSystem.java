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
        Log.i("Nodes", String.valueOf(scannedNodes.size()));

        if (scannedNodes.size() != 0) {
            ArrayList<BLNode> removalList = new ArrayList<>();

            for (BLNode scannedNode: scannedNodes) {
                for (Building building : this.buildings) {
                    for (Level level : building.levels) {
                        for (Location location : level.locations) {
                            for (Node node : location.nodes) {
                                if (scannedNode.address != node.address ) {
                                    // add to removal list
                                    if (!removalList.contains(scannedNode)) {
                                        removalList.add(scannedNode);
                                    }
                                }
                            }
                        }
                    }
                }
            }
            Log.i("Nodes to Remove", String.valueOf(removalList.size()));
            for (BLNode nodeToRemove: removalList) {
                scannedNodes.remove(nodeToRemove);
            }
            Log.i("Nodes", String.valueOf(scannedNodes.size()));
            // Sort BLNodes

            BLNode closestNode = new BLNode("aaaaa", "test", -10000);
            for (BLNode node: scannedNodes) {
                if (node.rssi < closestNode.rssi) {
                    closestNode = node;
                }
            }

            Log.i("closestNode", String.valueOf(closestNode.address));
            for (Building building : buildings) {
                for (Level level : building.levels) {
                    for (Location location : level.locations) {
                        for (Node node : location.nodes) {
                            if (node.address == closestNode.address) {
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
}
