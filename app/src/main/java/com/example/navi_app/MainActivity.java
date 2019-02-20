package com.example.navi_app;

import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
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

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    //UI
    Button btn_scan;

    private BluetoothAdapter adapter;
    private static final int REQUEST_ENABLE_BT = 99;
    private LocationSystem locationSystem;
    private final ArrayList<BLNode> scannedNodes = new ArrayList<>();

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, 99);
        }


        int permissionCheck = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION);
        Log.i("perm", String.valueOf(permissionCheck));

        //UI
        btn_scan = (Button) findViewById(R.id.btn_scan);
        btn_scan.setOnClickListener(this);

        this.adapter = BluetoothAdapter.getDefaultAdapter();

        IntentFilter filter = new IntentFilter();
        filter.addAction(BluetoothAdapter.ACTION_STATE_CHANGED);
        filter.addAction(BluetoothDevice.ACTION_FOUND);
        filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_STARTED);
        registerReceiver(mReceiver, filter);

        enableBluetooth();

        scan();
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

    @Override
    protected void onResume(){
        super.onResume();
        enableBluetooth();
    }

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


    private void scan() {

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

    public void onClick(View view) {
        scan();
    }
}


