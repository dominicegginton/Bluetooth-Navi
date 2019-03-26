package com.example.navi_app;

import android.util.Log;

/**
 * BLNode
 * Bluetooth Node object containing information about physically scanned bluetooth nodes.
 */
public class BLNode implements Comparable<Node>{

    // Bluetooth Node Address
    public String address;
    // Bluetooth Node Name
    public String name;
    // Bluetooth Node RSSI - Signal Strength
    public Integer rssi;
    // TAG for logging
    private final String TAG = "BLNODE";

    /**
     * INIT BLNode
     * @param Nodeadrress address of bluetooth node
     * @param name name of bluetooth node
     * @param rssi RSSI signal strength number of bluetooth node
     */
    BLNode(String Nodeadrress, String name, Integer rssi) {

        // Set data
        this.address = Nodeadrress;
        this.name = name;
        this.rssi = rssi;

        // Log
        Log.v(TAG, "New BLNode object has been created");

    }

    /**
     * Compare to another BLNode object to check if they match
     * @param o another BLNode object to compare to
     * @return int 0 if the objects match and 1 if not
     */
    @Override
    public int compareTo(Node o) {

        // If this BLNode objects address is equal to the others objects address
        if (this.address == o.address){

            // Return 0
            return 0;

        }else {

            // Return 1
            return 1;
        }

    }
}
