package com.example.navi_app;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class Node implements Comparable<BLNode>{

    public String address;
    public ArrayList<Connection> connections = new ArrayList<>();
    private final String TAG = "NODE";

    public Node(JSONObject node) {

        try {
            this.address = node.getString("address");

            JSONArray connectionsArray = node.getJSONArray("connections");

            Log.i(TAG, String.valueOf("Node: " + address + " Connections Length " + connectionsArray.length()));

            for (int i=0; i < connectionsArray.length(); i++)
            {
                JSONObject connectionJSON = connectionsArray.getJSONObject(i);
                String connectionDestination = connectionJSON.getString("destination");
                int connectionWeight = connectionJSON.getInt("weight");
                // Add new connection
                //Connection newConnection = new Connection(ls.getNode(connectionDestination), connectionWeight);
                //this.connections.add(newConnection);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public Node(String address) {
        this.address = address;
    }

    public void addConnection(Node neighbor, int weight){
        this.connections.add(new Connection(neighbor,weight));
    }

    public int compareTo(BLNode o) {
        if (this.address == o.address) {
            return 0;
        } else {
            return 1;
        }
    }
}
