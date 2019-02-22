package com.example.navi_app;

import java.util.ArrayList;

public class Node implements Comparable<BLNode>{

    public String address;
    public ArrayList<Node> connectedNodes;

    public Node() {
        this.connectedNodes = new ArrayList<>();
    }

    public int compareTo(BLNode o) {
        if (this.address == o.address) {
            return 0;
        } else {
            return 1;
        }
    }
}
