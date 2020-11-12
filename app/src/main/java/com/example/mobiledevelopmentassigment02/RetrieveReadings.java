package com.example.mobiledevelopmentassigment02;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ListView;
import android.widget.Spinner;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class RetrieveReadings extends AppCompatActivity {

    ListView lvReadings;
    List<Reading> readingList;
    Spinner mySpinner;
    DatabaseReference databaseReadings;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_retrieve_readings);

        lvReadings = findViewById(R.id.lvReadings);
        mySpinner= findViewById(R.id.spinner);
        readingList = new ArrayList<Reading>();
        String member = mySpinner.getSelectedItem().toString().trim();
        databaseReadings = FirebaseDatabase.getInstance().getReference("students");
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
                    readingList.add(reading);
                }

                ReadingListAdapter adapter = new ReadingListAdapter(RetrieveReadings.this, readingList);
                lvReadings.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) { }
        });
    }

}