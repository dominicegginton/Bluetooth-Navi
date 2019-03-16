package com.example.navi_app;

import android.os.TestLooperManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class navigation extends AppCompatActivity {

    TextView txt_test;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation);

        //Path navigationPath = (Path) getIntent().getExtras().getSerializable("Path");

        txt_test = (TextView) findViewById(R.id.txt_test);
        //txt_test.setText(navigationPath.convertToString());

    }
}
