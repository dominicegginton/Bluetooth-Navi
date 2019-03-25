package com.example.navi_app;

import android.Manifest;
import android.app.AlertDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;

public class menu extends AppCompatActivity {

    // UI objects
    Button btn_locate;
    Button btn_current_location;
    Button btn_information;

    //Location System Objects
    private LocationSystem ls;

    // Log TAG
    private final String TAG = "MENU";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        // INIT UI
        btn_locate = (Button) findViewById(R.id.btn_locate);
        btn_current_location = (Button) findViewById(R.id.btn_current_location);
        btn_information = (Button) findViewById(R.id.btn_information);

        // INIT LocationSystem
        //this.ls = (LocationSystem) getIntent().getExtras().getSerializable("location_system");
        this.ls = new LocationSystem(this);
        Log.i(TAG , String.valueOf(ls));


    }

    @Override
    protected void onResume(){
        super.onResume();
    }

    public void btn_locate_clicked(View view) {
        // Log
        Log.i(TAG, "Navigation Button Clicked - Sending user to navigation page");

        // Create new intent to open new page
        Intent intent = new Intent(getBaseContext(), navigation.class);
        // Open page
        startActivity(intent);
    }

    public void btn_current_location_clicked(View view) {
        // Log
        Log.i(TAG, "Current Location Button Clicked - Sending user to current location page");

        // Create new intent to open new page
        Intent intent = new Intent(getBaseContext(), current_location.class);
        // Open page
        startActivity(intent);
    }

    public void btn_Info_Clicked(View view) {
        // Log
        Log.i(TAG, "Information Button Clicked - Sending user to information page");

        // Create new intent to open new page
        Intent intent = new Intent(getBaseContext(), information.class);
        // Open page
        startActivity(intent);
    }
}
