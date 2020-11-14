package com.example.mobiledevelopmentassigment02;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormatSymbols;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class RetrieveReadings extends AppCompatActivity {

    ListView lvReadings;
    List<Reading> readingList;
    Spinner mySpinner;
    DatabaseReference databaseReadings;
    String member;
    String sys;
    String dias;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_retrieve_readings);

        lvReadings = findViewById(R.id.lvReadings);
        mySpinner= findViewById(R.id.spinner);
        readingList = new ArrayList<Reading>();
        member = mySpinner.getSelectedItem().toString().trim();
        databaseReadings = FirebaseDatabase.getInstance().getReference("familymembers");

        //bottomNavFunction();
        mySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                onStart();
                member = mySpinner.getSelectedItem().toString().trim();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }

        });

        lvReadings.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                Reading reading = readingList.get(position);

                showUpdateDialog(reading.getReadingId(),
                        reading.getSystolicNum(),
                        reading.getDiastolicNum(),
                        reading.getFamilyMember());

                return false;
            }
        });

        bottomNavFunction();
    }

    @Override
    protected void onStart() {
        super.onStart();
        databaseReadings.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                readingList.clear();
                for (DataSnapshot studentSnapshot : dataSnapshot.getChildren()) {
                    Reading reading = studentSnapshot.getValue(Reading.class);
                    if(reading.getFamilyMember().equals(member)) {
                        readingList.add(reading);
                    }
                }

                ReadingListAdapter adapter = new ReadingListAdapter(RetrieveReadings.this, readingList);
                lvReadings.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) { }
        });
    }

    private void updateStudent(String id,int systolicNum,
                               int diastolicNum, String familyMember) {
        DatabaseReference dbRef = databaseReadings.child(id);

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
        String condition;

        if(systolicNum < 120 && diastolicNum < 80){
            condition = "Normal";
        } else if((systolicNum <= 129 && systolicNum >= 120)
                && (diastolicNum < 80)){
            condition = "Elevated";
        } else if((systolicNum <= 139 && systolicNum >= 130)){
            condition = "High Blood Pressure (Stage 1)";
        } else if((diastolicNum <= 89 && diastolicNum >= 80)){
            condition = "High Blood Pressure (Stage 1)";
        } else if ((systolicNum >= 140 && systolicNum <= 180)){
            condition = "High Blood Pressure (Stage 2)";
        } else if (diastolicNum >= 90 && diastolicNum <= 120){
            condition = "High Blood Pressure (Stage 2)";
        } else if (systolicNum > 180 || diastolicNum > 120){
            condition = "Hypertensive Crisis";
        } else if (systolicNum > 180 && diastolicNum > 120){
            condition = "Hypertensive Crisis";
        }else {
            condition = "Unknown";
        }

        Reading reading = new Reading(id,systolicNum,diastolicNum,familyMember, date, time, condition);

        Task setValueTask = dbRef.setValue(reading);

        setValueTask.addOnSuccessListener(new OnSuccessListener() {
            @Override
            public void onSuccess(Object o) {
                Toast.makeText(RetrieveReadings.this,
                        "Reading Updated.",Toast.LENGTH_LONG).show();
            }
        });

        setValueTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(RetrieveReadings.this,
                        "Something went wrong.\n" + e.toString(),
                        Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void showUpdateDialog(final String readingId, final int sys, int dias, final String name) {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);

        LayoutInflater inflater = getLayoutInflater();

        final View dialogView = inflater.inflate(R.layout.update_dialog, null);
        dialogBuilder.setView(dialogView);

        final EditText editSysReading = dialogView.findViewById(R.id.editTextTextPersonName);
        editSysReading.setText(String.valueOf(sys));

        final EditText editDiasReading = dialogView.findViewById(R.id.editTextTextPersonName2);
        editDiasReading.setText(String.valueOf(dias));


        final Button btnUpdate = dialogView.findViewById(R.id.button2);

        dialogBuilder.setTitle("Update reading");

        final AlertDialog alertDialog = dialogBuilder.create();
        alertDialog.show();

        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int sys_reading = Integer.parseInt(editSysReading.getText().toString().trim());
                int dias_reading = Integer.parseInt(editDiasReading.getText().toString().trim());

                if (TextUtils.isEmpty(editSysReading.getText().toString().trim())) {
                    Toast.makeText(RetrieveReadings.this, "You must enter a systolic value.", Toast.LENGTH_LONG).show();
                    return;
                }

                if (TextUtils.isEmpty(editDiasReading.getText().toString().trim())) {
                    Toast.makeText(RetrieveReadings.this, "You must enter a diastolic value.", Toast.LENGTH_LONG).show();
                    return;
                }
                int systolic = Integer.parseInt(editSysReading.getText().toString());
                int diastolic = Integer.parseInt(editDiasReading.getText().toString());
                if(systolic > 180 || diastolic > 120){
                    showWarningDialog();
                }

                updateStudent(readingId, sys_reading, dias_reading, name);

                alertDialog.dismiss();
            }
        });

        final Button btnDelete = dialogView.findViewById(R.id.btnDelete);
        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteStudent(readingId);

                alertDialog.dismiss();
            }
        });

    }

    private void deleteStudent(String id) {
        DatabaseReference dbRef = databaseReadings.child(id);

        Task setRemoveTask = dbRef.removeValue();
        setRemoveTask.addOnSuccessListener(new OnSuccessListener() {
            @Override
            public void onSuccess(Object o) {
                Toast.makeText(RetrieveReadings.this,
                        "Reading Deleted.",Toast.LENGTH_LONG).show();
            }
        });

        setRemoveTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(RetrieveReadings.this,
                        "Something went wrong.\n" + e.toString(),
                        Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void bottomNavFunction(){
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigationl);
        bottomNavigationView.setSelectedItemId(R.id.viewReading);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener(){

            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch(item.getItemId()){
                    case R.id.add:
                        startActivity(new Intent(getApplicationContext(), AddActivity.class));
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.viewReading:
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




}