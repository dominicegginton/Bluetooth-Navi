package com.example.navi_app;

public class BLNode implements Comparable<Node>{

    public String address;
    public String name;
    public Integer rssi;

    BLNode(String Nodeadrress, String name, Integer rssi) {
        this.address = Nodeadrress;
        this.name = name;
        this.rssi = rssi;
    }

    @Override
    public int compareTo(Node o) {
        if (this.address == o.address){
            return 0;
        }else {
            return 1;
        }
    }
}
