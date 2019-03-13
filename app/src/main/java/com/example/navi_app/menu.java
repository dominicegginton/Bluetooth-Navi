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
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;

public class menu extends AppCompatActivity {

    // Bluetooth objects
    private BluetoothAdapter adapter;
    private static final int REQUEST_ENABLE_BT = 99;
    private static final int SCAN_TIME_BT = 5000;
    private final ArrayList<BLNode> scannedNodes = new ArrayList<>();

    // UI
    Button btn_Navi;
    Button btn_Info;
    TextView txt_Welcome;
    TextView txt_Current_Building;
    TextView txt_Current_Level;
    TextView txt_Current_Location;
    ProgressBar progressSpinner_CurrentLocation;

    //Location System Objects
    private LocationSystem ls;

    // Log TAG
    private final String TAG = "MENU";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        // INIT Bluetooth
        this.adapter = BluetoothAdapter.getDefaultAdapter();
        IntentFilter filter = new IntentFilter();
        filter.addAction(BluetoothAdapter.ACTION_STATE_CHANGED);
        filter.addAction(BluetoothDevice.ACTION_FOUND);
        filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_STARTED);
        registerReceiver(mReceiver, filter);
        enableBluetooth();

        // Init UI
        btn_Navi = (Button) findViewById(R.id.btn_Navi);
        btn_Info = (Button) findViewById(R.id.btn_Info);
        txt_Welcome = (TextView) findViewById(R.id.txt_Welcome);
        txt_Current_Building = (TextView) findViewById(R.id.txt_Current_Building);
        txt_Current_Level = (TextView) findViewById(R.id.txt_Current_Level);
        txt_Current_Location = (TextView) findViewById(R.id.txt_Current_Location);
        progressSpinner_CurrentLocation = (ProgressBar) findViewById(R.id.progressSpinner_CurrentLocation);

        // INIT LocationSystem
        this.ls = new LocationSystem("https://gist.githubusercontent.com/dominicegginton/99dc73485e5a1937b2d0bfadd0fa8d0c/raw/b7eb320d35161edd8bcebf3dce43f8d907b433df/coventryUniNaviData.json");

        // update Current Location
        displayCurrentLocation();
    }

    @Override
    protected void onResume(){
        super.onResume();

        // Enable bluetooth
        enableBluetooth();
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
            AlertDialog alertDialog = new AlertDialog.Builder(menu.this).create();
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
        // Scan
        progressSpinner_CurrentLocation.setVisibility(View.VISIBLE);
        txt_Current_Building.setVisibility(View.INVISIBLE);
        txt_Current_Level.setVisibility(View.INVISIBLE);
        txt_Current_Location.setVisibility(View.INVISIBLE);
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
                    txt_Current_Building.setText(ls.getCurrentBuilding(currentLocation).name);
                    txt_Current_Level.setText(ls.getCurrentLevel(currentLocation).name);
                    txt_Current_Location.setText(currentLocation.name);

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
                    AlertDialog alertDialog = new AlertDialog.Builder(menu.this).create();
                    alertDialog.setTitle("Navigation Error");
                    alertDialog.setMessage("Sorry can not find your current location");
                    alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "Close", new DialogInterface.OnClickListener() {

                        // Exit button on click event
                        public void onClick(DialogInterface dialog, int which) {
                            // Exit this dialog
                            dialog.dismiss();
                        }

                    });
                    // Show Alert Dialog
                    alertDialog.show();
                }
                progressSpinner_CurrentLocation.setVisibility(View.GONE);
                txt_Current_Building.setVisibility(View.VISIBLE);
                txt_Current_Level.setVisibility(View.VISIBLE);
                txt_Current_Location.setVisibility(View.VISIBLE);
            }
        }, SCAN_TIME_BT);
    }

    public void btn_Navi_Clicked(View view){
        // Log
        Log.i(TAG, "Navigation Button Clicked - Sending user to navigation page");

        // Create new intent to open new page
        Intent intent = new Intent(getBaseContext(), navigation.class);
        // Open page
        startActivity(intent);
    }

    public void btn_Info_Clicked(View view){
        // Log
        Log.i(TAG, "Information Button Clicked - Sending user to information page");

        // Create new intent to open new page
        Intent intent = new Intent(getBaseContext(), information.class);
        // Open page
        startActivity(intent);
    }
}
