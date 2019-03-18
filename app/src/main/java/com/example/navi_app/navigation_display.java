package com.example.navi_app;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

public class navigation_display extends AppCompatActivity {

    // UI
    ListView list_nodes;

    // Location System
    LocationSystem ls;

    // Path
    Path path;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation_display);

        list_nodes = (ListView) findViewById(R.id.list_nodes);

        // INIT LocationSystem
        this.ls = (LocationSystem) getIntent().getExtras().getSerializable("location_system");
        this.path = (Path) getIntent().getExtras().getSerializable("path");

        ArrayList<String> nodeStrings = new ArrayList<>();
        for (Node node: path.convertToArrayList()) {
            nodeStrings.add(node.address);
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1, nodeStrings);
        list_nodes.setAdapter(adapter);
    }
}
