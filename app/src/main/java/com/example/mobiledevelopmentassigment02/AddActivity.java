package com.example.mobiledevelopmentassigment02;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

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
        TextView.OnEditorActionListener listener = new TextView.OnEditorActionListener(){
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                int systolic = Integer.parseInt(sys.getText().toString());
                int diastolic = Integer.parseInt(dia.getText().toString());
                if(systolic > 180 || diastolic > 120){
                    showWarningDialog();
                }
                return false;
            }
        };
        Button.OnClickListener listener2 = new Button.OnClickListener(){
            @Override
            public void onClick(View v) {
                addReading();
            }
        };
        dia.setOnEditorActionListener(listener);
        sys.setOnEditorActionListener(listener);
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
        Reading reading = new Reading(id, systolic, diastolic, familyMem);

        Task setValueTask = databaseReadings.child(id).setValue(reading);

        setValueTask.addOnSuccessListener(new OnSuccessListener() {
            @Override
            public void onSuccess(Object o) {
                Toast.makeText(AddActivity.this,"Student added.",Toast.LENGTH_LONG).show();
                finish();
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
}
