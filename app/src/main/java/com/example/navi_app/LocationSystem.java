package com.example.navi_app;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.concurrent.ExecutionException;

public class LocationSystem implements Serializable {

    public String name;
    public ArrayList<Building> buildings = new ArrayList<>();
    private String URL;
    private final String TAG = "LOCATION_SYSTEM";

    public LocationSystem(String URL) {

        // Init Buildings
        this.buildings = new ArrayList<>();

        // Init json request variables
        this.URL = URL;
        JSONObject locationSystemJSON;
        HttpGetRequest getRequest = new HttpGetRequest();

        // Execute the getRequest do in background method
        try {
            // Get result
            String resultStirng = getRequest.execute(this.URL).get();
            // Try Init json
            locationSystemJSON = new JSONObject(resultStirng);
            // Set system name
            name = locationSystemJSON.getString("systemName");
            // Get buildings json data
            JSONArray buildingsArray = locationSystemJSON.getJSONArray("buildings");

            // For each building in boilings json data
            for (int i=0; i < buildingsArray.length(); i++)
            {
                JSONObject buildingJSON = buildingsArray.getJSONObject(i);
                // Pulling items from the array
                buildings.add(new Building(buildingJSON, this));
            }

        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (NullPointerException e) {
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

                        // If node is the same as search node object
                        if (node.equals(searchNode)) {
                            return location;
                        }

                    }
                }
            }
        }
        return null;
    }

    public Location getLocation(String searchString) {
        // Search for the building using location object
        for (Building building : buildings) {
            for (Level level : building.levels) {
                for (Location location : level.locations) {

                    // If location name is equal to search string
                    if(location.name.equals(searchString)) {
                        return location;
                    }

                }
            }
        }
        return null;
    }

    public Path navigate(Location originLocation, Location destinationLocation){

        // Get root nodes for both origin and destination - first nodes in array
        Node originRootNode = originLocation.nodes.get(0);
        Node destinationRootNode = destinationLocation.nodes.get(0);
        // Define new asrraylist for stack and visited
        ArrayList<Path> stack = new ArrayList<>();
        ArrayList<Node> visited = new ArrayList<>();

        // Add new Path to originRootnNode to the stack
        stack.add(new Path(originRootNode, this));


        // While the stack is empty
        while (!stack.isEmpty()) {

            // Get smallest path from stack
            Path smallestPath = stack.remove(0);

            // For each connection from destination of smallest path
            for (Connection connection: smallestPath.destination.connections){

                // If we haven not visited this node before
                if (!visited.contains(connection.getNeighbor())){
                    // Add new Path to this connection destination
                    stack.add(new Path(connection, smallestPath, this));
                    Collections.sort(stack);
                }
            }

            // Add this destination to visited
            visited.add(smallestPath.destination);

            // The smallest path has reached the destination root node return the path
            if (smallestPath.destination == destinationRootNode) {
                return smallestPath;
            }
        }

        // Can not find path to node
        return null;
    }
}
