package com.example.navi_app;

public class BLNode{

    public String address;
    public String name;
    public int rssi;

    BLNode(String Nodeadrress, String name, int rssi) {
        this.address = Nodeadrress;
        this.name = name;
        this.rssi = rssi;
    }
}
