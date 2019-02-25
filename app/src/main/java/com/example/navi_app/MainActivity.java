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
import android.widget.TextView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    //UI
    Button btn_scan;
    Button btn_getLocation;
    TextView text_location;

    private BluetoothAdapter adapter;
    private static final int REQUEST_ENABLE_BT = 99;
    private LocationSystem ls;
    private final ArrayList<BLNode> scannedNodes = new ArrayList<>();

    protected void onCreate(Bundle savedInstanceState) {
        // Normal Android Stuff
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Check For 'ACCESS_COARSE_LOCATION' permission and request on runtime
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, 99);
        }

        scannedNodes.add(new BLNode("","", 0));
        // INIT GUI
        btn_scan = (Button) findViewById(R.id.btn_scan);
        btn_getLocation = (Button) findViewById(R.id.btn_getLocation);
        text_location = (TextView) findViewById(R.id.txt_location);

        // INIT Bluetooth
        this.adapter = BluetoothAdapter.getDefaultAdapter();
        IntentFilter filter = new IntentFilter();
        filter.addAction(BluetoothAdapter.ACTION_STATE_CHANGED);
        filter.addAction(BluetoothDevice.ACTION_FOUND);
        filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_STARTED);
        registerReceiver(mReceiver, filter);
        enableBluetooth();
        scan();

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
                String output = String.valueOf(bluetoothObject.address + " : "+ bluetoothObject.name +" : " + bluetoothObject.rssi);
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
        }, 10000);
    }

    public void btn_scan_Clickec(View view) {
        scan();
    }

    public void btn_getLocation_Clicked(View view) {
        Location currentLocation = this.ls.getCurrentLocation(this.scannedNodes);
        if (currentLocation != null) {
            text_location.setText(currentLocation.name);
            String output = currentLocation.name + " -- Nodes: {";
            for (Node node: currentLocation.nodes) {
                output += " " + node.address;
            }
            output += " }";
            Log.i("Location", output);
        }
        this.text_location.setText(currentLocation.name);

    }
}


