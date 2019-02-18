package com.example.navi_app;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.util.Log;
import java.util.ArrayList;

public class Bluetooth extends Activity{
    private BluetoothAdapter adapter;
    private static final int REQUEST_ENABLE_BT = 1;


    public Bluetooth() {
        this.adapter = BluetoothAdapter.getDefaultAdapter();
    }


    private void CheckBlueToothState(){
        if (this.adapter == null) {
            Log.e("BLE","Bluetooth is not supported on the current device :(");
        }
        else {
            if (this.adapter.isEnabled()){
                if(this.adapter.isDiscovering()){
                    Log.i("BLE","Bluetooth is currently in device discovery process.");
                }else{
                    Log.i("BLE","Bluetooth is Enabled.");
                }
            }else{
                Log.e("BLE","Bluetooth is NOT Enabled!");
                Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_ENABLE_BT)
        {
            if (resultCode == 0)
            {
                Log.e("BLE","User did not allowed bluetooth acces");
            }
            else
                Log.i("BLE","User allowed bluetooth acces");
        }
    }

    public ArrayList<BLNode> scan(ArrayList<Node> filterNodes)
    {
        final ArrayList<BLNode> scannedNodes = new ArrayList<BLNode>();

        // start looking for bluetooth devices
        adapter.startDiscovery();

        // Discover new devices
        // Create a BroadcastReceiver for ACTION_FOUND
        final BroadcastReceiver mReceiver = new BroadcastReceiver()
        {
            @Override
            public void onReceive(Context context, Intent intent)
            {
                String action = intent.getAction();
                // When discovery finds a device
                if (BluetoothDevice.ACTION_FOUND.equals(action))
                {
                    // Get the bluetoothDevice object from the Intent
                    BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);

                    // Get the “RSSI” to get the signal strength as integer,
                    // but should be displayed in “dBm” units
                    int rssi = intent.getShortExtra(BluetoothDevice.EXTRA_RSSI,Short.MIN_VALUE);

                    // Create the device object and add it to the arrayList of devices
                    BLNode bluetoothObject = new BLNode(device.getAddress(), rssi);

                    scannedNodes.add(bluetoothObject);
                }
            }
        };
        // Register the BroadcastReceiver
        IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        registerReceiver(mReceiver, filter);
        if (filterNodes.size() > 0) {
            ArrayList<BLNode> nodes = new ArrayList<BLNode>();
            for (BLNode node : scannedNodes) {
                if (filterNodes.contains(node)) {
                    nodes.add(node);
                }
            }
            return nodes;
        } else {
            return scannedNodes;
        }
    }
}
