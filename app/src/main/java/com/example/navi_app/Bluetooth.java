package com.example.navi_app;

import android.Manifest;
import android.app.AlertDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import java.util.ArrayList;

/**
 * Bluetooth
 * Wrapper class for interfacing with androids bluetooth interface
 */
public class Bluetooth extends AppCompatActivity {

    // Enable bluetooth request
    private static final int REQUEST_ENABLE_BT = 99;
    // Bluetooth scan time
    private static final int SCAN_TIME_BT = 5000;
    // TAG for logging
    private static final String TAG = "Bluetooth";
    // Bluetooth adapter
    private BluetoothAdapter adapter;
    // Scanned Nodes
    private ArrayList<BLNode> scannedNodes = new ArrayList<>();

    /**
     * INIT Bluetooth
     */
    public Bluetooth() {

        // Log
        Log.v(TAG, "Bluetooth class init");

    }

    /**
     * onActivityResult
     * @param requestCode the request code that was sent
     * @param resultCode the result code that was received
     * @param data the intent data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {

        // Run super
        super.onActivityResult(requestCode, resultCode, data);

        // If the request code was enable bluetooth
        if (requestCode == REQUEST_ENABLE_BT)
        {

            // If the result code is equal to 0
            if (resultCode == 0)
            {

                // Log that user did not enable bluetooth
                Log.e(TAG,"User did not allowed bluetooth access");

            }
            else

                // Log that user enabled bluetooth
                Log.i(TAG,"User allowed bluetooth access");

        }
    }


    /**
     * enableBleutooth
     * run to enable bluetooth on the users androids device
     */
    public void enableBluetooth() {

        // This there is no bluetooth adapter
        if (adapter == null) {

            // Log error tha there is no bluetooth adapter
            Log.e(TAG, "Sorry this device does not support bluetooth :(");

            // Create Alert Dialog
            AlertDialog alertDialog = new AlertDialog.Builder(this).create();
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

                // Log
                Log.v(TAG, "Requesting permission for ACCESS_COARSE_LOCATION");

            }

            // Check is bluetooth adapter is already enabled
            if (!adapter.isEnabled()) {

                // Create new intent to enable bluetooth
                Intent enableBluetoothIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                // Start intent in current activity
                startActivityForResult(enableBluetoothIntent, REQUEST_ENABLE_BT);

            }
        }
    }

    // Define new BroadcastReceiver
    BroadcastReceiver mReceiver = new BroadcastReceiver() {

        /**
         * onReceive
         * when a bluetooth object is found onReceived is called, this will create a new bluetooth object and add it to the scanned nodes array
         * @param context the context
         * @param intent intent holding the bluetooth nodes data
         */
        @Override
        public void onReceive(Context context, Intent intent) {

            // Get the received action
            String action = intent.getAction();

            // If action is equal to the bluetooth device found action
            if (BluetoothDevice.ACTION_FOUND.equals(action)) {

                // Create new BluetoothDevice object from intent and get device info
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                // Get device name
                String deviceName = device.getName();
                // Get deivce address
                String deviceHardwareAddress = device.getAddress();
                // Get device rssi
                int rssi = intent.getShortExtra(BluetoothDevice.EXTRA_RSSI,Short.MIN_VALUE);
                // Create new BLNode object
                BLNode bluetoothObject = new BLNode(deviceHardwareAddress, deviceName, rssi);

                // Add node to list on scanned nodes
                scannedNodes.add(bluetoothObject);

                // Log node has been found
                String output = String.valueOf(bluetoothObject.address + " : "+ bluetoothObject.name +" : " + bluetoothObject.rssi);
                Log.i(TAG, "Bluetooth Node Found: " + bluetoothObject.address + " - " + bluetoothObject.name + " - " + bluetoothObject.rssi);

            }
            else {

                // Log action
                Log.i(TAG, "Action: " + action);

            }
        }
    };

    /**
     * scan
     * scan for bluetooth nodes and store them in the scannedNodes arraylist
     */
    private void scan() {

        // If adapter is already discovering cancel the discovery
        if (this.adapter.isDiscovering()) {

            // Cancel discovery
            this.adapter.cancelDiscovery();

        }

        // Clear scanned nodes list
        scannedNodes.clear();

        // Start discovery on adapter
        this.adapter.startDiscovery();

        // Create new delay handler of SCAN_TIME_BT
        Handler cancelDiscoveryHandler = new Handler();
        cancelDiscoveryHandler.postDelayed(new Runnable() {

            public void run() {
                // cancel the discovery on the adapter
                adapter.cancelDiscovery();
            }

        }, SCAN_TIME_BT);
    }

}
