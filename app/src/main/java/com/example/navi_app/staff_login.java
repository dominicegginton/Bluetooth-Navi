package com.example.navi_app;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class staff_login extends AppCompatActivity {

    //UI
    Button btn_register;
    Button btn_login;
    EditText edit_email;
    EditText edit_password;

    // Log TAG
    private static final String TAG = "Visitor Login";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_staff_login);

        // INIT UI
        btn_login = (Button) findViewById(R.id.btn_login);
        btn_register = (Button) findViewById(R.id.btn_register);
        edit_password = (EditText) findViewById(R.id.edit_password);
        edit_email = (EditText) findViewById(R.id.edit_email);
    }

    public void btn_login_Clicked(View view) {

        // Log
        Log.e(TAG, "Login clicked");

    }

    public void btn_register_Clicked(View view) {

        // Log
        Log.e(TAG, "Register clicked");

    }

}
