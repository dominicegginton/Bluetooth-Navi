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
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    //UI
    Button btn_getLocation;
    TextView locationTxt;
    TextView buildingTxt;
    TextView levelTxt;
    ProgressBar getLocationSpinner;

    //Bluetooth Objects
    private BluetoothAdapter adapter;
    private static final int REQUEST_ENABLE_BT = 99;
    private final ArrayList<BLNode> scannedNodes = new ArrayList<>();

    //Location System Objects
    private LocationSystem ls;

    protected void onCreate(Bundle savedInstanceState) {
        // Normal Android Stuff
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Check For 'ACCESS_COARSE_LOCATION' permission and request on runtime this is needed for bluetooth scanning
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, 99);
        }

        // Init scanned nodes
        scannedNodes.add(new BLNode("","", 0));

        // INIT UI
        btn_getLocation = (Button) findViewById(R.id.btn_getLocation);
        buildingTxt = (TextView) findViewById(R.id.buildingTxt);
        levelTxt = (TextView) findViewById(R.id.levelTxt);
        locationTxt = (TextView) findViewById(R.id.locationTxt);
        getLocationSpinner = (ProgressBar)findViewById(R.id.getLocationSpinner);
        getLocationSpinner.setVisibility(View.GONE);

        // INIT Bluetooth
        this.adapter = BluetoothAdapter.getDefaultAdapter();
        IntentFilter filter = new IntentFilter();
        filter.addAction(BluetoothAdapter.ACTION_STATE_CHANGED);
        filter.addAction(BluetoothDevice.ACTION_FOUND);
        filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_STARTED);
        registerReceiver(mReceiver, filter);
        enableBluetooth();

        // INIT LocationSystem
        this.ls = new LocationSystem();
        this.ls.name = "Coventry Uni";

        Building building1 = new Building();
        building1.name = "Engineering & Computing";

        Level ground = new Level();
        ground.name = "Ground";

        Location roomECG01 = new Location();
        roomECG01.name = "Demo Room - Macbook";
        Node node01 = new Node();
        node01.address = "D0:A6:37:E9:A2:62";
        roomECG01.nodes.add(node01);

        Location roomECG02 = new Location();
        roomECG02.name = "Demo Room - Random Person Phone";
        Node node02 = new Node();
        node02.address = "38:A4:ED:94:BA:E7";
        roomECG02.nodes.add(node02);

        ground.locations.add(roomECG01);
        ground.locations.add(roomECG02);
        building1.levels.add(ground);
        this.ls.buildings.add(building1);

    }


    public void enableBluetooth() {

        // This there is no bluetooth adapter
        if (this.adapter == null) {
            // Log error
            Log.e("Bluetooth", "Sorry this device does not support bluetooth :(");

            // Create Alert Dialog
            AlertDialog alertDialog = new AlertDialog.Builder(MainActivity.this).create();
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

    @Override
    protected void onResume(){
        super.onResume();

        // Enable bluetooth
        enableBluetooth();
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

        }, 10000);
    }

    public void btn_getLocation_Clicked(View view) {
        getLocationSpinner.setVisibility(View.VISIBLE);


        // Scan
        scan();

        // Create new delay handler of 10 seconds
        Handler getCurrentLocationHandler = new Handler();
        getCurrentLocationHandler.postDelayed(new Runnable() {

            public void run() {
                // Get current location object
                Location currentLocation = ls.getCurrentLocation(scannedNodes);

                // Check for null location
                if (currentLocation != null) {

                    // Output location details to UI
                    buildingTxt.setText(ls.getCurrentBuilding(currentLocation).name);
                    levelTxt.setText(ls.getCurrentBuilding(currentLocation).name);
                    locationTxt.setText(currentLocation.name);

                    // Log Nodes that belong to the location
                    String output = currentLocation.name + " -- Nodes: {";
                    for (Node node: currentLocation.nodes) {
                        output += " " + node.address;
                    }
                    output += " }";
                    Log.i("Location", output);
                }
                getLocationSpinner.setVisibility(View.GONE);
            }
        }, 10000);

    }
}


