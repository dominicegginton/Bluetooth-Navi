package com.example.navi_app;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.concurrent.ExecutionException;

public class LocationSystem {

    public String name;
    public ArrayList<Building> buildings = new ArrayList<>();
    private String URL;
    private final String TAG = "LOCATION_SYSTEM";

    public LocationSystem(String URL) throws IOException {

        this.buildings = new ArrayList<>();
        this.URL = URL;
        //Some url endpoint that you may have
        //String to place our result in
        String result = null;
        JSONObject locationSystemJSON = null;
        String lsname = null;
        //Instantiate new instance of our class
        HttpGetRequest getRequest = new HttpGetRequest();
        //Perform the doInBackground method, passing in our url
        try {
            result = getRequest.execute(this.URL).get();
            locationSystemJSON = new JSONObject(result);
            lsname = locationSystemJSON.getString("systemName");
            JSONArray buildingsArray = locationSystemJSON.getJSONArray("buildings");

            for (int i=0; i < buildingsArray.length(); i++)
            {
                try {
                    JSONObject buildingJSON = buildingsArray.getJSONObject(i);
                    // Pulling items from the array
                    buildings.add(new Building(buildingJSON, this));
                } catch (JSONException e) {
                    // Oops
                }
            }

        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

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
            String output = String.valueOf(closestNode.name + " : " + closestNode.address + " : " + closestNode.rssi );

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

    public Node getNode(String address){
        // Search for the building using location object
        for (Building building : buildings) {
            for (Level level : building.levels) {
                for (Location location : level.locations) {
                    for (Node node: location.nodes) {

                        // If node is the same as search address
                        if (node.address.equals(address)) {
                            return node;
                        }

                    }
                }
            }
        }
        return null;
    }

    public Location getLocation(Node searchNode) {
        // Search for the building using location object
        for (Building building : buildings) {
            for (Level level : building.levels) {
                for (Location location : level.locations) {
                    for (Node node: location.nodes) {

                        // If node is the same as search address
                        if (node.equals(searchNode)) {
                            return location;
                        }

                    }
                }
            }
        }
        return null;
    }

    public Path navigate(Location originLocation, Location destinationLocation){
        Node originRootNode = originLocation.nodes.get(0);
        Node destinationRootNode = destinationLocation.nodes.get(0);
        ArrayList<Path> stack = new ArrayList<>();
        ArrayList<Node> visited = new ArrayList<>();

        stack.add(new Path(originRootNode, this));

        while (!stack.isEmpty()) {

            Path smallestPath = stack.remove(0);
            for (Connection connection: smallestPath.destination.connections){

                if (!visited.contains(connection.getNeighbor())){
                    stack.add(new Path(connection, smallestPath, this));
                    Collections.sort(stack);
                }
            }

            visited.add(smallestPath.destination);

            if (smallestPath.destination == destinationRootNode) {
                return smallestPath;
            }
        }
        return null;
    }
}
