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
import android.os.Handler;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;

public class current_location extends AppCompatActivity {

    // Bluetooth objects
    private BluetoothAdapter adapter;
    private static final int REQUEST_ENABLE_BT = 99;
    private static final int SCAN_TIME_BT = 5000;
    private final ArrayList<BLNode> scannedNodes = new ArrayList<>();

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

    // Log TAG
    private final String TAG = "CURRENT_LOCATION";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_current_location);

        // INIT Bluetooth
        this.adapter = BluetoothAdapter.getDefaultAdapter();
        IntentFilter filter = new IntentFilter();
        filter.addAction(BluetoothAdapter.ACTION_STATE_CHANGED);
        filter.addAction(BluetoothDevice.ACTION_FOUND);
        filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_STARTED);
        registerReceiver(mReceiver, filter);
        enableBluetooth();

        // INIT LocationSystem
        this.ls = new LocationSystem(this);

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

        // update Current Location
        displayCurrentLocation();
    }

    // Define new BroadcastReceiver
    BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            // Get the received action
            String action = intent.getAction();

            // If action is equal to the bluetooth device found action
            if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                // Found bluetooth node

                // Create new BluetoothDevice object from intent and get device info
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                String deviceName = device.getName();
                String deviceHardwareAddress = device.getAddress();
                int rssi = intent.getShortExtra(BluetoothDevice.EXTRA_RSSI,Short.MIN_VALUE);
                BLNode bluetoothObject = new BLNode(deviceHardwareAddress, deviceName, rssi);

                // Add node to list on scanned nodes
                scannedNodes.add(bluetoothObject);

                // Log node has been found
                String output = String.valueOf(bluetoothObject.address + " : "+ bluetoothObject.name +" : " + bluetoothObject.rssi);
                Log.i("BLNode", output);
            }

            // Log action
            else {Log.i("Action", action);}
        }
    };

    public void enableBluetooth() {

        // This there is no bluetooth adapter
        if (this.adapter == null) {
            // Log error
            Log.e("Bluetooth", "Sorry this device does not support bluetooth :(");

            // Create Alert Dialog
            AlertDialog alertDialog = new AlertDialog.Builder(current_location.this).create();
            alertDialog.setTitle("Bluetooth not supported");
            alertDialog.setMessage("Sorry your device doesn't support bluetooth");
            alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "Exit App", new DialogInterface.OnClickListener() {

                // Exit button on click event
                public void onClick(DialogInterface dialog, int which) {
                    // Exit this dialog
                    dialog.dismiss();
                    // Exit App
                    System.exit(1);
                }

            });
            // Show Alert Dialog
            alertDialog.show();
        }
        else {
            // Check for 'ACCESS_COARSE_LOCATION' permission and request on runtime this is needed for bluetooth scanning
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // Request permission for 'ACCESS_COARSE_LOCATION'
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, 99);
            }

            // Check is bluetooth adapter is already enabled
            if (!this.adapter.isEnabled()) {
                // Create new intent to enable bluetooth
                Intent enableBluetoothIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                // Start intent in current activity
                startActivityForResult(enableBluetoothIntent, REQUEST_ENABLE_BT);
            }
        }
    }

    private void scan() {

        // If adapter is already discovering cancel the discovery
        if (this.adapter.isDiscovering()) {
            this.adapter.cancelDiscovery();
        }

        // Clear scanned nodes list
        scannedNodes.clear();

        // Start discovery on adapter
        this.adapter.startDiscovery();

        // Create new delay handler of 10 seconds
        Handler cancelDiscoveryHandler = new Handler();
        cancelDiscoveryHandler.postDelayed(new Runnable() {

            public void run() {
                // cancel the discovery on the adapter
                adapter.cancelDiscovery();
            }

        }, SCAN_TIME_BT);
    }

    private void displayCurrentLocation () {
        // Scan for nodes
        progress_spinner.setVisibility(View.VISIBLE);
        txt_location_name.setVisibility(View.GONE);
        txt_location_level.setVisibility(View.GONE);
        txt_location_building.setVisibility(View.GONE);

        layout_location_data_type.setVisibility(View.GONE);
        //img_location_data_type.setVisibility(View.GONE);
        //txt_location_data_type.setVisibility(View.GONE);
        layout_location_data_computers.setVisibility(View.GONE);
        //img_location_data_computers.setVisibility(View.GONE);
        //txt_location_data_computers.setVisibility(View.GONE);
        layout_location_data_workspaces.setVisibility(View.GONE);
        //img_location_data_workspaces.setVisibility(View.GONE);
        //txt_location_data_workspaces.setVisibility(View.GONE);
        layout_location_data_food.setVisibility(View.GONE);
        //img_location_data_food.setVisibility(View.GONE);
        //txt_location_data_food.setVisibility(View.GONE);
        scan();

        // Create new delay handler of 10 seconds
        Handler getCurrentLocationHandler = new Handler();
        getCurrentLocationHandler.postDelayed(new Runnable() {

            public void run() {
                // Get current location object
                Location currentLocation = ls.getCurrentLocation(scannedNodes);

                // Check for null locations
                if (currentLocation != null) {
                    // End Spinner
                    progress_spinner.setVisibility(View.GONE);

                    // Output location details to UI
                    txt_location_building.setText(ls.getCurrentBuilding(currentLocation).name);
                    txt_location_level.setText(ls.getCurrentLevel(currentLocation).name);
                    txt_location_name.setText(currentLocation.name);
                    txt_location_name.setVisibility(View.VISIBLE);
                    txt_location_level.setVisibility(View.VISIBLE);
                    txt_location_building.setVisibility(View.VISIBLE);
                    
                    txt_location_data_type.setText(currentLocation.type);
                    //txt_location_data_type.setVisibility(View.VISIBLE);
                    Log.i("computers", currentLocation.computers);
                    if(currentLocation.type.equals("Classroom") | currentLocation.type.equals("Lecture")) {
                        img_location_data_type.setImageResource(R.drawable.icon_presenter);
                    } else if (currentLocation.type.equals("Reception")) {
                        img_location_data_type.setImageResource(R.drawable.icon_reception);
                    } else if (currentLocation.type.equals("Walkway")) {
                        img_location_data_type.setImageResource(R.drawable.icon_path);
                    } else {
                        img_location_data_type.setImageResource(R.drawable.icon_building);
                    }
                    //img_location_data_type.setVisibility(View.VISIBLE);
                    layout_location_data_type.setVisibility(View.VISIBLE);
                    
                    if(currentLocation.computers != null && !currentLocation.computers.equals("")) {
                        txt_location_data_computers.setText(currentLocation.computers);
                        img_location_data_computers.setImageResource(R.drawable.icon_computer);
                        layout_location_data_computers.setVisibility(View.VISIBLE);
                    }
                    if(currentLocation.workspace != null &&!currentLocation.workspace.equals("")) {
                        txt_location_data_workspaces.setText(currentLocation.workspace);
                        img_location_data_workspaces.setImageResource(R.drawable.icon_table);
                        layout_location_data_workspaces.setVisibility(View.VISIBLE);
                    }
                    if(currentLocation.food != null &&!currentLocation.food.equals("")) {
                        txt_location_data_food.setText(currentLocation.food);
                        img_location_data_food.setImageResource(R.drawable.icon_food);
                        layout_location_data_food.setVisibility(View.VISIBLE);
                    }

                    // Log Nodes that belong to the location
                    String output = currentLocation.name + " - {";
                    for (Node node: currentLocation.nodes) {
                        output += " " + node.address;
                    }
                    output += " }";
                    Log.i("Current Location", output);


                }else {
                    // cant get current location
                    // Create Alert Dialog

                    AlertDialog alertDialog = new AlertDialog.Builder(current_location.this).create();
                    alertDialog.setTitle("Location System");
                    alertDialog.setMessage("Your current location could not be found");
                    alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "Close", new DialogInterface.OnClickListener() {

                        // Exit button on click event
                        public void onClick(DialogInterface dialog, int which) {
                            // Exit this dialog
                            dialog.dismiss();

                        }

                    });


                }
            }
        }, SCAN_TIME_BT);
    }
}
