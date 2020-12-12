package com.example.weekendactivity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class LaunchActivity extends AppCompatActivity {

    Button btnLaunchApp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launch);

        btnLaunchApp = findViewById(R.id.btnLaunchApp);

        btnLaunchApp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(LaunchActivity.this, MainActivity.class);
                startActivity(i);
            }
        });
    }
}