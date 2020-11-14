package com.example.mobiledevelopmentassigment02;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.DateFormat;
import java.text.DateFormatSymbols;
import java.util.Calendar;
import java.util.Date;

public class AddActivity extends AppCompatActivity {

    Spinner mySpin;
    EditText sys;
    EditText dia;
    Button addButton;

    DatabaseReference databaseReadings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);
        databaseReadings = FirebaseDatabase.getInstance().getReference("familymembers");
        mySpin = findViewById(R.id.spinner);
        sys = findViewById(R.id.editTextTextPersonName);
        dia = findViewById(R.id.editTextTextPersonName2);
        addButton = findViewById(R.id.button2);
        bottomNavFunction();
        Button.OnClickListener listener2 = new Button.OnClickListener(){
            @Override
            public void onClick(View v) {
                int systolic = Integer.parseInt(sys.getText().toString());
                int diastolic = Integer.parseInt(dia.getText().toString());
                if(systolic > 180 || diastolic > 120){
                    showWarningDialog();
                }
                addReading();
            }
        };
        //dia.setOnEditorActionListener(listener);
        //sys.setOnEditorActionListener(listener);
        addButton.setOnClickListener(listener2);
    }
    private void showWarningDialog() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);

        LayoutInflater inflater = getLayoutInflater();

        final View dialogView = inflater.inflate(R.layout.warning_dialog, null);
        dialogBuilder.setView(dialogView);

        final Button btnUpdate = dialogView.findViewById(R.id.btnDismiss);

        final AlertDialog alertDialog = dialogBuilder.create();
        alertDialog.show();

        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });

    }

    private void addReading() {
        int systolic = Integer.parseInt(sys.getText().toString().trim());
        int diastolic = Integer.parseInt(dia.getText().toString().trim());
        String familyMem = mySpin.getSelectedItem().toString().trim();

        if (TextUtils.isEmpty(sys.getText().toString().trim())) {
            Toast.makeText(this, "You must enter a systolic value.", Toast.LENGTH_LONG).show();
            return;
        }

        if (TextUtils.isEmpty(dia.getText().toString().trim())) {
            Toast.makeText(this, "You must enter a diastolic value.", Toast.LENGTH_LONG).show();
            return;
        }

        String id = databaseReadings.push().getKey();
        Calendar cal = Calendar.getInstance();
        String month = "";
        DateFormatSymbols dfs =  new DateFormatSymbols();
        String[] months = dfs.getMonths();
        int number = cal.get(Calendar.MONTH);
        if(number >= 0 && number <= 11){
            month = months[number];
        }
        String date = month + " " + cal.get(Calendar.DAY_OF_MONTH) + ", " + cal.get(Calendar.YEAR);
        String time = String.format("%2d", cal.get(Calendar.HOUR)) + ":" + String.format("%2d", cal.get(Calendar.MINUTE)) + " ";
        if(cal.get(Calendar.AM_PM) != 0){
            time += "PM";
        } else {
            time += "AM";
        }
        String condition = "";

        if(systolic < 120 && diastolic < 80){
            condition = "Normal";
        } else if((systolic <= 129 && systolic >= 120)
                && (diastolic < 80)){
            condition = "Elevated";
        } else if((systolic <= 139 && systolic >= 130)){
            condition = "High Blood Pressure (Stage 1)";
        } else if((diastolic <= 89 && diastolic >= 80)){
            condition = "High Blood Pressure (Stage 1)";
        } else if ((systolic >= 140 && systolic <= 180)){
            condition = "High Blood Pressure (Stage 2)";
        } else if (diastolic >= 90 && diastolic <= 120){
            condition = "High Blood Pressure (Stage 2)";
        } else if (systolic > 180 || diastolic > 120){
            condition = "Hypertensive Crisis";
        } else if (systolic > 180 && diastolic > 120){
            condition = "Hypertensive Crisis";
        }else {
            condition = "Unknown";
        }

        Reading reading = new Reading(id, systolic, diastolic, familyMem, date, time, condition);

        Task setValueTask = databaseReadings.child(id).setValue(reading);

        setValueTask.addOnSuccessListener(new OnSuccessListener() {
            @Override
            public void onSuccess(Object o) {
                Toast.makeText(AddActivity.this,"Reading added.",Toast.LENGTH_LONG).show();
                sys.setText("");
                dia.setText("");
                //finish();
            }
        });

        setValueTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(AddActivity.this,
                        "something went wrong.\n" + e.toString(),
                        Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void bottomNavFunction(){
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigationl);
        bottomNavigationView.setSelectedItemId(R.id.add);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener(){

            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch(item.getItemId()){
                    case R.id.add:
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
