package com.example.mobiledevelopmentassigment02;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.jaredrummler.materialspinner.MaterialSpinner;

import java.text.DateFormatSymbols;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class ReportActivity extends AppCompatActivity {
    MaterialSpinner mySpin;
    DatabaseReference databaseReadings;
    List<Reading> readingList;
    String member;
    String[] familyMembers;
    TextView tvAvgSystolicNum;
    TextView tvAvgDiastolicNum;
    TextView tvAvgCondition;
    TextView tvHeading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report);
        readingList = new ArrayList<Reading>();
        mySpin= findViewById(R.id.spinner);
        familyMembers = getResources().getStringArray(R.array.familyMember);
        mySpin.setItems(familyMembers);
        member = familyMembers[mySpin.getSelectedIndex()].trim();
        tvAvgSystolicNum = findViewById(R.id.avgSystolicNum);
        tvAvgDiastolicNum = findViewById(R.id.avgDiastolicNum);
        tvAvgCondition = findViewById(R.id.avgCondition);
        tvHeading = findViewById(R.id.heading);

        databaseReadings = FirebaseDatabase.getInstance().getReference("familymembers");
        setReportHeading();
        mySpin.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener<String>() {
            @Override
            public void onItemSelected(MaterialSpinner view, int position, long id, String item) {
                member = familyMembers[mySpin.getSelectedIndex()].trim();
                onStart();
            }

        });

        bottomNavFunction();
    }
    public void setReportHeading(){
        Calendar cal = Calendar.getInstance();
        DateFormatSymbols dfs =  new DateFormatSymbols();
        String[] months = dfs.getMonths();
        int number = cal.get(Calendar.MONTH);
        String month = "";
        if(number <= 11){
            month = months[number];
        }
        int year = cal.get(Calendar.YEAR);
        String heading = member + "'s Month-to-date average\n" + month + ", " + year;
        tvHeading.setText(heading);

    }
    public void bottomNavFunction(){
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigationl);
        bottomNavigationView.setSelectedItemId(R.id.report);
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
                    case R.id.report:
                        startActivity(new Intent(getApplicationContext(), ReportActivity.class));
                        overridePendingTransition(0,0);
                        return true;
                    default:
                        return false;
                }

            }
        });
    }
    private void setReportTextViews(){
        if(readingList.size() > 0) {
            int avgSystolicReadingValue = 0;
            int avgDiastolicReadingValue = 0;
            for (Reading r : readingList) {
                avgSystolicReadingValue += r.getSystolicNum();
                avgDiastolicReadingValue += r.getDiastolicNum();
            }
            avgSystolicReadingValue /= readingList.size();
            avgDiastolicReadingValue /= readingList.size();
            String avgSystolicStr = "Systolic Reading: " + avgSystolicReadingValue;
            tvAvgSystolicNum.setText(avgSystolicStr);
            String avgDiastolicStr = "Diastolic Reading: " + avgDiastolicReadingValue;
            tvAvgDiastolicNum.setText(avgDiastolicStr);
            String avgCondition = "Average Condition: " + Reading.calcCondition(avgSystolicReadingValue, avgDiastolicReadingValue);
            tvAvgCondition.setText(avgCondition);
        } else {
            tvAvgSystolicNum.setText(R.string.noReadingsFound + member);
        }
        setReportHeading();
    }
    protected void onStart() {
        super.onStart();
        databaseReadings.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                readingList.clear();
                for (DataSnapshot studentSnapshot : dataSnapshot.getChildren()) {
                    Reading reading = studentSnapshot.getValue(Reading.class);
                    assert reading != null;
                    if(reading.getFamilyMember().equals(member)  && reading.isCurMonth()) {
                        readingList.add(reading);
                    }
                }
                setReportTextViews();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) { }
        });
    }
}