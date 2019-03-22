package com.example.navi_app;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    //UI
    Button btn_register;
    Button btn_login;
    EditText edit_username;
    EditText edit_password;

    //Location System Objects
    private LocationSystem ls;

    protected void onCreate(Bundle savedInstanceState) {
        // Normal Android Stuff
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // INIT UI
        btn_login = (Button) findViewById(R.id.btn_login);
        btn_register = (Button) findViewById(R.id.btn_register);
        edit_password = (EditText) findViewById(R.id.edit_password);
        edit_username = (EditText) findViewById(R.id.edit_email);
    }

    @Override
    protected void onResume(){
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    public void btn_nai_Clicked(View view) {
        final String searchString = "";
        //getLocationSpinner.setVisibility(View.VISIBLE);

        // Scan
        //scan();

        // Create new delay handler of 10 seconds
        Handler getCurrentLocationHandler = new Handler();
        getCurrentLocationHandler.postDelayed(new Runnable() {

            public void run() {
                // Get current location object
                ArrayList<BLNode> scannedNodes = new ArrayList<>();
                Location currentLocation = ls.getCurrentLocation(scannedNodes);

                // Check for null location
                if (currentLocation != null) {

                    // Output location details to UI
                   // buildingTxt.setText(ls.getCurrentBuilding(currentLocation).name);
                   // levelTxt.setText(ls.getCurrentLevel(currentLocation).name);
                   // locationTxt.setText(currentLocation.name);

                    // Log Nodes that belong to the location
                    String output = currentLocation.name + " - {";
                    for (Node node: currentLocation.nodes) {
                        output += " " + node.address;
                    }
                    output += " }";
                    Log.i("Current Location", output);

                    // Define destination location, this will be done by the user with a drop down menu
                    Location destination = ls.getLocation(searchString);
                    // Return new path object from Location object to Location object
                    if (destination != null) {
                        Path myPath = ls.navigate(currentLocation, destination);
                        // Log Output
                        //naviTxt.setText(myPath.convertToString());
                        Log.i("Navigation", String.valueOf(myPath.convertToString() + " - " + myPath.totalWeight));
                    }else {
                        // Create Alert Dialog
                        AlertDialog alertDialog = new AlertDialog.Builder(MainActivity.this).create();
                        alertDialog.setTitle("Navigation Error");
                        alertDialog.setMessage("Sorry can not find destination location");
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

                }else {
                    // cant get current location
                    // Create Alert Dialog
                    AlertDialog alertDialog = new AlertDialog.Builder(MainActivity.this).create();
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

            }
        }, 6000);
    }

    public void btn_login_Clicked(View view) {

        LocationSystem ls = new LocationSystem("https://gist.githubusercontent.com/dominicegginton/99dc73485e5a1937b2d0bfadd0fa8d0c/raw/366c0aa82a74cc03ec0a0f13cf6f1a146c82fa38/coventryUniNaviData.json");
        // Create new intent to open new page
        Intent intent = new Intent(getBaseContext(), menu.class);
        intent.putExtra("location_system", ls);
        // Open page
        startActivity(intent);
    }

    public void btn_register_Clicked(View view) {
        // Create new intent to open new page
        Intent intent = new Intent(getBaseContext(), registration.class);
        // Open page
        startActivity(intent);
    }
}


