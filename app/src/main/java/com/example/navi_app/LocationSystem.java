package com.example.navi_app;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;

import java.util.ArrayList;
import java.util.Collections;

/**
 * LocationSystem
 * LocationSystem object holds all data about the system and the buildings within it
 */
public class LocationSystem {

    // Arraylist of buildings
    public ArrayList<Building> buildings;
    // Log TAF
    private final String TAG = "LOCATION_SYSTEM";
    // Context
    private Context context;

    /**
     * INIT LocationSystem
     * @param context context
     */
    public LocationSystem(Context context) {

        // Set data
        this.context = context;

        // Create new database helper object
        DatabaseHelp databaseHelper = new DatabaseHelp(this.context);
        // Get buildings from database
        this.buildings = databaseHelper.getBuildings();

        // Log
        Log.v(TAG, "INIT LocationSystem");
    }

    /**
     *
     * @param Origin Location of the start of the path
     * @param Destination Location of the end of the path
     * @return Path object containing the nodes to navigate from Origin to Destination
     */
    public Path getPath(Location Origin, Location Destination)
    {

        // Get Nodes for Origin and Destination Locations
        Node OriginNode = Origin.nodes.get(0);
        Node DestinationNode = Destination.nodes.get(0);

        // Define new arraylist for the stack and visited nodes
        ArrayList<Path> stack = new ArrayList<>();
        ArrayList<String> visited = new ArrayList<>();

        // Add a new Path to the OriginNode
        stack.add(new Path(OriginNode));

        // While the stack is not empty
        while (!stack.isEmpty())
        {
            // Remove the smallest path from the stack and store as smallestPath
            Path smallestPath = stack.remove(0);

            // For each connection from the destination of the smallest path
            for (Connection connection : smallestPath.destination.connections)
            {

                // If the connections neighbor nodes has not already been visited
                if (!visited.contains(connection.getNeighbor().address)) {

                    // Add a new Path to the connections nodes containing the current smallest path
                    stack.add(new Path(connection, smallestPath));
                    // Sort the arraylist of paths
                    Collections.sort(stack);
                }
            }

            // Add this smallests paths destination to the visited array
            visited.add(smallestPath.destination.address);

            // If the smallest path has reached the destination
            if(smallestPath.destination.address.equals(DestinationNode.address))
            {

                // Return the smallest path
                return smallestPath;
            }
        }

        // Path was not found - return null
        return null;
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

            // For each BLNode in scannedNodes
            for (BLNode scannedNode: scannedNodes) {

                // For each Building in this location system
                for (Building building : this.buildings) {

                    // For each level in building
                    for (Level level : building.levels) {

                        // For each location in level
                        for (Location location : level.locations) {

                            // For each node in location
                            for (Node node : location.nodes) {

                                // If scanned node is in location system
                                if (scannedNode.address.equals(node.address)) {

                                    // Add scanned node to system nodes
                                    systemNodes.add(scannedNode);

                                }
                            }
                        }
                    }
                }
            }

            // Define new node as closest node <- '-10000' used as rssi as this will always be larger than real scanned nodes
            BLNode closestNode = new BLNode("", "", -10000);

            // For each node in systemNodes
            for (BLNode node: systemNodes) {

                // If node is closer than closest node assign closest node to it
                if (node.rssi.intValue() > closestNode.rssi.intValue()) {

                    // Set closestNode to node
                    closestNode = node;

                }

            }

            // For each building in this location system
            for (Building building : buildings) {

                // For each level in building
                for (Level level : building.levels) {

                    // For each location in level
                    for (Location location : level.locations) {

                        // For each node in location
                        for (Node node : location.nodes) {

                            // If node address is the same as the closest node address
                            if (node.address.equals(closestNode.address)) {

                                // Return the location that the node belongs to
                                return location;

                            }
                        }
                    }
                }
            }

        }

        // Return null
        return null;
    }

    /**
     * getCurrentLevel
     * search for the current level object based upon the users Location object
     * @param currentLocation users current Lcoation object
     * @return Level object that the user is on
     */
    public Level getCurrentLevel(Location currentLocation){

        // For building in this location system
        for (Building building : buildings) {

            // For level in buidling
            for (Level level : building.levels) {

                // For location level
                for (Location location : level.locations) {

                    // If location is the same as current location
                    if (location.id == currentLocation.id) {

                        //return level
                        return level;
                    }
                }
            }
        }

        // Return null
        return null;
    }

    /**
     * getCurrentBuilding
     * search for the current building object based upon the users Location object
     * @param currentLocation users current Lcoation object
     * @return Building object that the user is in
     */
    public Building getCurrentBuilding(Location currentLocation) {

        // For building in this location system
        for (Building building : buildings) {

            // For level in buidling
            for (Level level : building.levels) {

                // For location level
                for (Location location : level.locations) {

                    // If location is the same as current location
                    if (location.id == currentLocation.id) {

                        //return building
                        return building;
                    }
                }
            }
        }

        // Return null
        return null;
    }

    public Path navigate(Location originLocation, Location destinationLocation){

        // Get root nodes for both origin and destination - first nodes in array
        Node originRootNode = originLocation.nodes.get(0);
        Node destinationRootNode = destinationLocation.nodes.get(0);
        // Define new asrraylist for stack and visited
        ArrayList<Path> stack = new ArrayList<>();
        ArrayList<String> visited = new ArrayList<>();

        // Add new Path to originRootnNode to the stack
        stack.add(new Path(originRootNode));

        // While the stack is empty
        while (!stack.isEmpty()) {

            // Get smallest path from stack
            Path smallestPath = stack.remove(0);

            // For each connection from destination of smallest path
            for (Connection connection: smallestPath.destination.connections){

                if (!visited.contains(connection.getNeighbor().address)) {
                    stack.add(new Path(connection, smallestPath));
                    Collections.sort(stack);
                }
            }

            // Add this destination to visited
            visited.add(smallestPath.destination.address);

            // The smallest path has reached the destination root node return the path
            if (smallestPath.destination.address.equals(destinationRootNode.address)) {
                return smallestPath;
            }
        }

        // Can not find path to node
        return null;
    }

    /**
     * alertBuilder
     * @param context context
     * @param alertTitle new alert title
     * @param alertMessage new alert message
     * @return AlertDialog with data already to called .show()
     */
    public AlertDialog alertBuilder(Context context, String alertTitle, String alertMessage) {

        // Create new alert dialog
        AlertDialog alertDialog = new AlertDialog.Builder(context).create();
        alertDialog.setTitle(alertTitle);
        alertDialog.setMessage(alertMessage);
        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "Close", new DialogInterface.OnClickListener() {

            // Exit button on click event
            public void onClick(DialogInterface dialog, int which) {
                // Exit this dialog
                dialog.dismiss();

            }

        });

        // Return alertDialog
        return alertDialog;
    }
}
