package com.example.navi_app;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.TextView;

import java.io.IOException;
import java.io.Serializable;

public class information extends AppCompatActivity implements SearchView.OnQueryTextListener{

    // UI
    ListView list_information;
    SearchView search_information;
    location_list_view location_information_adapter;

    TextView txt_location_name;
    TextView txt_location_level;
    TextView txt_location_building;

    ConstraintLayout layout_location_info;
    ConstraintLayout layout_location_list;
    ConstraintLayout layout_location_data_type;
    ImageView img_location_data_type;
    TextView txt_location_data_type;
    ConstraintLayout layout_location_data_computers;
    ImageView img_location_data_computers;
    TextView txt_location_data_computers;
    ConstraintLayout layout_location_data_workspaces;
    ImageView img_location_data_workspaces;
    TextView txt_location_data_workspaces;
    ConstraintLayout layout_location_data_food;
    ImageView img_location_data_food;
    TextView txt_location_data_food;

    // Location System
    LocationSystem ls;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_information);

        // INIT LocationSystem
        this.ls = new LocationSystem(this);

        // INIT UI
        list_information = (ListView) findViewById(R.id.list_information);
        search_information = (SearchView) findViewById(R.id.search_information);
        search_information.setOnQueryTextListener(this);

        txt_location_name = (TextView) findViewById(R.id.txt_location_name);
        txt_location_level = (TextView) findViewById(R.id.txt_location_level);
        txt_location_building = (TextView) findViewById(R.id.txt_location_Building);

        layout_location_info = (ConstraintLayout) findViewById(R.id.layout_location_info);
        layout_location_list = (ConstraintLayout) findViewById(R.id.layout_location_list);
        layout_location_data_type = (ConstraintLayout) findViewById(R.id.layout_location_data_type);
        img_location_data_type = (ImageView) findViewById(R.id.img_location_data_type);
        txt_location_data_type = (TextView) findViewById(R.id.txt_location_data_type);
        layout_location_data_computers = (ConstraintLayout) findViewById(R.id.layout_location_data_computers);
        img_location_data_computers = (ImageView) findViewById(R.id.img_location_data_computer);
        txt_location_data_computers = (TextView) findViewById(R.id.txt_location_data_computers);
        layout_location_data_workspaces = (ConstraintLayout) findViewById(R.id.layout_location_data_workspace);
        img_location_data_workspaces = (ImageView) findViewById(R.id.img_location_data_workspaces);
        txt_location_data_workspaces = (TextView) findViewById(R.id.txt_location_data_workspaces);
        layout_location_data_food = (ConstraintLayout) findViewById(R.id.layout_location_data_food);
        img_location_data_food = (ImageView) findViewById(R.id.img_location_data_food);
        txt_location_data_food = (TextView) findViewById(R.id.txt_location_data_food);

        DatabaseHelp db = new DatabaseHelp(this);
        location_information_adapter = new location_list_view(this, db.getLocations(1), ls);


        list_information.setAdapter(location_information_adapter);

        list_information.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                final Location location_information = location_information_adapter.getItem(position);

                displayCurrentLocation(location_information);

            }
        });

    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        location_information_adapter.filter(query);
        return false;
    }

    @Override
    public boolean onQueryTextChange(String query) {
        location_information_adapter.filter(query);
        return false;
    }

    private void displayCurrentLocation (Location location_information) {

        txt_location_building.setText(ls.getCurrentBuilding(location_information).name);
        txt_location_level.setText(ls.getCurrentLevel(location_information).name);
        txt_location_name.setText(location_information.name);
        txt_location_name.setVisibility(View.VISIBLE);
        txt_location_level.setVisibility(View.VISIBLE);
        txt_location_building.setVisibility(View.VISIBLE);
        layout_location_list.setVisibility(View.GONE);
        layout_location_info.setVisibility(View.VISIBLE);

        txt_location_data_type.setText(location_information.type);
        txt_location_data_type.setVisibility(View.VISIBLE);

        if(location_information.type.equals("Classroom") | location_information.type.equals("Lecture")) {
            img_location_data_type.setImageResource(R.drawable.icon_presenter);
        } else if (location_information.type.equals("Reception")) {
            img_location_data_type.setImageResource(R.drawable.icon_reception);
        } else if (location_information.type.equals("Walkway")) {
            img_location_data_type.setImageResource(R.drawable.icon_path);
        } else {
            img_location_data_type.setImageResource(R.drawable.icon_building);
        }
        layout_location_data_type.setVisibility(View.VISIBLE);

        if(location_information.computers != null) {
            txt_location_data_computers.setText(location_information.computers);
            img_location_data_computers.setImageResource(R.drawable.icon_computer);
            layout_location_data_computers.setVisibility(View.VISIBLE);
        }
        if(location_information.workspace != null) {
            txt_location_data_workspaces.setText(location_information.workspace);
            img_location_data_workspaces.setImageResource(R.drawable.icon_table);
            layout_location_data_workspaces.setVisibility(View.VISIBLE);
        }
        if(location_information.food != null) {
            Log.i("Info","xx");
            txt_location_data_food.setText(location_information.food);
            img_location_data_food.setImageResource(R.drawable.icon_food);
            layout_location_data_food.setVisibility(View.VISIBLE);
        }
    }
}
