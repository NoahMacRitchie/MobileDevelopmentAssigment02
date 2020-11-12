package com.example.mobiledevelopmentassigment02;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bottomNavFunction();
    }

    public void addActivity(View v){
        Intent i = new Intent(this, AddActivity.class);
        startActivity(i);
    }

    public void viewReadings(View v){
        Intent i = new Intent(this, RetrieveReadings.class);
        startActivity(i);
    }

    public void bottomNavFunction(){
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigationl);


        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener(){

            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch(item.getItemId()){
                    case R.id.add:
                        startActivity(new Intent(getApplicationContext(), AddActivity.class));
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.viewReading:
                        startActivity(new Intent(getApplicationContext(), RetrieveReadings.class));
                        overridePendingTransition(0,0);
                        return true ;
//                    case R.id.report:
//                        startActivity(new Intent(getApplicationContext(), .class));
//                        overridePendingTransition(0,0);
//                        return true;
                    default:
                        return false;
                }

            }
        });
    }


}