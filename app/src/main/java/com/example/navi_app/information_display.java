package com.example.navi_app;

import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

public class information_display extends AppCompatActivity {

    // UI Objects
    TextView txt_location_name;
    TextView txt_location_level;
    TextView txt_location_building;
    ProgressBar progress_spinner;

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

    //Location System Objects
    private LocationSystem ls;

    private Location location_information;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_information_display);

        // INIT LocationSystem
        this.ls = (LocationSystem) getIntent().getExtras().getSerializable("location_system");
        this.location_information = (Location) getIntent().getExtras().getSerializable("location");

        // INIT UI
        txt_location_name = (TextView) findViewById(R.id.txt_location_name);
        txt_location_level = (TextView) findViewById(R.id.txt_location_level);
        txt_location_building = (TextView) findViewById(R.id.txt_location_Building);
        progress_spinner = (ProgressBar) findViewById(R.id.progress_spinner);

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

        displayCurrentLocation(location_information);
    }

    private void displayCurrentLocation (Location location_information) {

        //txt_location_building.setText(ls.getCurrentBuilding(location_information).name);
        //txt_location_level.setText(ls.getCurrentLevel(location_information).name);
        txt_location_name.setText(location_information.name);
        txt_location_name.setVisibility(View.VISIBLE);
        txt_location_level.setVisibility(View.VISIBLE);
        txt_location_building.setVisibility(View.VISIBLE);

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
