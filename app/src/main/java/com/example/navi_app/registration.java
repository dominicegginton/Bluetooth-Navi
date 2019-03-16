package com.example.navi_app;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class registration extends AppCompatActivity {

    EditText edit_username;
    EditText edit_password;
    EditText edit_confirm_password;
    Button btn_register;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        // INIT UI
        edit_username = (EditText) findViewById(R.id.edit_username);
        edit_password = (EditText) findViewById(R.id.edit_password);
        edit_confirm_password = (EditText) findViewById(R.id.edit_confirm_password);
        btn_register = (Button) findViewById(R.id.btn_register);
    }

    public void btn_register_clicked(View view) {

    }
}
