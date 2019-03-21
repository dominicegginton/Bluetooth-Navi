package com.example.navi_app;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SearchView;

public class information extends AppCompatActivity implements SearchView.OnQueryTextListener{

    // UI
    ListView list_information;
    SearchView search_information;
    location_list_view location_information_adapter;

    // Location System
    LocationSystem ls;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_information);

        // INIT LocationSystem
        this.ls = (LocationSystem) getIntent().getExtras().getSerializable("location_system");

        // INIT UI
        list_information = (ListView) findViewById(R.id.list_information);
        search_information = (SearchView) findViewById(R.id.search_information);
        location_information_adapter = new location_list_view(this, ls);
        list_information.setAdapter(location_information_adapter);
        search_information.setOnQueryTextListener(this);

        list_information.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                final Location location_information = location_information_adapter.getItem(position);

                Intent intent = new Intent(getBaseContext(), information_display.class);
                intent.putExtra("location_system", ls);
                intent.putExtra("location", location_information);
                // Open page
                startActivity(intent);

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
}
