package com.example.navi_app;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.io.IOException;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    //UI
    Button btn_register;
    Button btn_login;
    EditText edit_email;
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
        edit_email = (EditText) findViewById(R.id.edit_email);
    }

    @Override
    protected void onResume(){
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    public void btn_login_Clicked(View view) {

        String email = edit_email.getText().toString();
        String password = edit_password.getText().toString();

        DatabaseHelp databaseHelper = new DatabaseHelp(this);

        User user = databaseHelper.login(email, password);
        if (user != null) {
            // Create new intent to open new page
            Intent intent = new Intent(getBaseContext(), menu.class);
            //intent.putExtra("location_system", newLS);
            // Open page
            startActivity(intent);
        } else {

            AlertDialog alertDialog = new AlertDialog.Builder(MainActivity.this).create();
            alertDialog.setTitle("Login Error");
            alertDialog.setMessage("Sorry your login details did not work :(");
            alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "Try Again", new DialogInterface.OnClickListener() {

                // Exit button on click event
                public void onClick(DialogInterface dialog, int which) {
                    // Exit this dialog
                    dialog.dismiss();
                    edit_email.getText().clear();
                    edit_password.getText().clear();
                }

            });
            alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "Register", new DialogInterface.OnClickListener() {

                // Exit button on click event
                public void onClick(DialogInterface dialog, int which) {
                    // Exit this dialog
                    dialog.dismiss();
                    Intent intent = new Intent(getBaseContext(), registration.class);
                    startActivity(intent);
                }

            });
            // Show Alert Dialog
            alertDialog.show();

        }
    }

    public void btn_register_Clicked(View view) {
        // Create new intent to open new page
        Intent intent = new Intent(getBaseContext(), registration.class);
        // Open page
        startActivity(intent);
    }
}


