package com.example.navi_app;

import android.content.Context;
import java.util.ArrayList;

public class Node implements Comparable<BLNode>{

    public String address;
    public ArrayList<Connection> connections = new ArrayList<>();
    private final String TAG = "NODE";
    private transient Context context;

    public Node(String newNodeAddress, Context context) {
        this.address = newNodeAddress;
        this.context = context;

        DatabaseHelp databaseHelper = new DatabaseHelp(context);
        this.connections = databaseHelper.getConnections(address);
    }

    public Node(String address) {
        this.address = address;
    }

    public Node(String address, ArrayList<Connection> connections) {
        this.address = address;
        this.connections = connections;
    }

    public int compareTo(BLNode o) {
        if (this.address == o.address) {
            return 0;
        } else {
            return 1;
        }
    }
}
