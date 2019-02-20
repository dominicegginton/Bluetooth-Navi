package com.example.navi_app;

import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import java.util.ArrayList;

public class Bluetooth extends AppCompatActivity {
    private BluetoothAdapter adapter;
    private static final int REQUEST_ENABLE_BT = 99;
    public final ArrayList<BLNode> scannedNodes = new ArrayList<>();


    public Bluetooth() {

        this.adapter = BluetoothAdapter.getDefaultAdapter();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_ENABLE_BT)
        {
            if (resultCode == 0)
            {
                Log.e("BLE","User did not allowed bluetooth access");
            }
            else
                Log.i("BLE","User allowed bluetooth access");
        }
    }

    public void enableBluetooth() {
        if (this.adapter == null) {
            Log.e("BLE", "Sorry this device does not support bluetooth :(");
            finish();
        }
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, 99);
        }
        if (!this.adapter.isEnabled()) {
            Intent enableBluetoothIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBluetoothIntent, REQUEST_ENABLE_BT);
        }
    }


    public void scan() {

        final ArrayList<BLNode> scannedNodes = new ArrayList<>();

        BroadcastReceiver mReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                String action = intent.getAction();
                if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                    // Discovery has found a device. Get the BluetoothDevice
                    // object and its info from the Intent.
                    BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                    String deviceName = device.getName();
                    String deviceHardwareAddress = device.getAddress();
                    int rssi = intent.getShortExtra(BluetoothDevice.EXTRA_RSSI,Short.MIN_VALUE);

                    BLNode bluetoothObject = new BLNode(deviceHardwareAddress, deviceName, rssi);
                    scannedNodes.add(bluetoothObject);
                    String output = String.valueOf(bluetoothObject.address + " - "+ bluetoothObject.name +" - " + bluetoothObject.rssi);
                    Log.i("BLNode", output);
                }

                else {Log.i("Action", action);}
            }
        };

        IntentFilter filter = new IntentFilter();
        filter.addAction(BluetoothAdapter.ACTION_STATE_CHANGED);
        filter.addAction(BluetoothDevice.ACTION_FOUND);
        filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_STARTED);
        registerReceiver(mReceiver, filter);

        if (this.adapter.isDiscovering()) {
            this.adapter.cancelDiscovery();
        }
        if (scannedNodes.size() > 0) {
            scannedNodes.clear();
        }
        this.adapter.startDiscovery();


        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                adapter.cancelDiscovery();
            }
        }, 6000);

    }
}
