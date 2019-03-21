package com.example.navi_app;

import android.Manifest;
import android.app.Activity;
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
import android.os.TestLooperManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.TextView;

import java.util.ArrayList;

public class navigation extends AppCompatActivity implements SearchView.OnQueryTextListener {

    // Bluetooth objects
    private BluetoothAdapter adapter;
    private static final int REQUEST_ENABLE_BT = 99;
    private static final int SCAN_TIME_BT = 5000;
    private final ArrayList<BLNode> scannedNodes = new ArrayList<>();

    // Location System Objects
    private LocationSystem ls;

    // UI objects
    ListView list_navi;
    SearchView search_navi;
    ProgressBar progress_spinner;
    location_list_view location_search_adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation);

        // INIT Bluetooth
        this.adapter = BluetoothAdapter.getDefaultAdapter();
        IntentFilter filter = new IntentFilter();
        filter.addAction(BluetoothAdapter.ACTION_STATE_CHANGED);
        filter.addAction(BluetoothDevice.ACTION_FOUND);
        filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_STARTED);
        registerReceiver(mReceiver, filter);
        enableBluetooth();

        // INIT LocationSystem
        this.ls = (LocationSystem) getIntent().getExtras().getSerializable("location_system");

        list_navi = (ListView) findViewById(R.id.list_navi);
        location_search_adapter = new location_list_view(this, ls);
        list_navi.setAdapter(location_search_adapter);
        search_navi = (SearchView) findViewById(R.id.search_navi);
        search_navi.setOnQueryTextListener(this);
        progress_spinner = (ProgressBar) findViewById(R.id.progress_spinner);
        progress_spinner.setVisibility(View.GONE);

        list_navi.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                final Location destination = location_search_adapter.getItem(position);
                progress_spinner.setVisibility(View.VISIBLE);
                list_navi.setVisibility(View.GONE);
                search_navi.setVisibility(View.GONE);

                scan();
                // Create new delay handler of 10 seconds
                Handler getCurrentLocationHandler = new Handler();
                getCurrentLocationHandler.postDelayed(new Runnable() {

                    public void run() {
                        // Get current location object
                        Location currentLocation = ls.getCurrentLocation(scannedNodes);

                        // Check for null locations
                        if (currentLocation != null) {
                            Path myPath = ls.navigate(currentLocation, destination);
                            if(myPath != null) {
                                Intent intent = new Intent(getBaseContext(), navigation_display.class);
                                intent.putExtra("location_system", ls);
                                intent.putExtra("path", myPath);
                                // Open page
                                startActivity(intent);
                                progress_spinner.setVisibility(View.VISIBLE);
                                list_navi.setVisibility(View.GONE);
                                search_navi.setVisibility(View.GONE);
                            } else {
                                // cant get navi path
                                // Create Alert Dialog

                                AlertDialog alertDialog = new AlertDialog.Builder(navigation.this).create();
                                alertDialog.setTitle("Navigation Error");
                                alertDialog.setMessage("A navigation path could not be found");
                                alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "Close", new DialogInterface.OnClickListener() {

                                    // Exit button on click event
                                    public void onClick(DialogInterface dialog, int which) {
                                        // Exit this dialog
                                        dialog.dismiss();

                                    }

                                });

                                alertDialog.show();
                            }


                        }else {
                            // cant get current location
                            // Create Alert Dialog

                            AlertDialog alertDialog = new AlertDialog.Builder(navigation.this).create();
                            alertDialog.setTitle("Location System");
                            alertDialog.setMessage("Your current location could not be found");
                            alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "Close", new DialogInterface.OnClickListener() {

                                // Exit button on click event
                                public void onClick(DialogInterface dialog, int which) {
                                    // Exit this dialog
                                    dialog.dismiss();

                                }

                            });

                            alertDialog.show();


                        }
                    }
                }, SCAN_TIME_BT);

            }
        });
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
            AlertDialog alertDialog = new AlertDialog.Builder(navigation.this).create();
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

    @Override
    public boolean onQueryTextSubmit(String query) {
        location_search_adapter.filter(query);
        return false;
    }

    @Override
    public boolean onQueryTextChange(String query) {
        location_search_adapter.filter(query);
        return false;
    }
}
