package com.example.hospitalmanagementsystem;

import androidx.appcompat.app.AppCompatActivity;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;


public class MainActivity extends AppCompatActivity {

    Button myProfileButton,doctorProfilesButton,createAppointmentButton,myAppointmentsButton,buMedicinesButton,patientsButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        myProfileButton = findViewById(R.id.myProfileButton);
        doctorProfilesButton = findViewById(R.id.doctorProfileButton);
        createAppointmentButton = findViewById(R.id.createAppointmentButton);
        myAppointmentsButton = findViewById(R.id.myAppointmentButton);
        buMedicinesButton = findViewById(R.id.buyMedicineButton);
        patientsButton = findViewById(R.id.patientsButton);

        createAppointmentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent= new Intent(MainActivity.this,CreateAppointment.class);
                startActivity(intent);
            }
        });

        patientsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent= new Intent(MainActivity.this,AllPatientsActivity.class);
                startActivity(intent);
            }
        });



    }



}