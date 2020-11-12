package com.example.mobiledevelopmentassigment02;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void addActivity(View v){
        Intent i = new Intent(this, AddActivity.class);
        startActivity(i);
    }

    public void viewReadings(View v){
        Intent i = new Intent(this, RetrieveReadings.class);
        startActivity(i);
    }
}