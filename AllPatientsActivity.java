package com.example.hospitalmanagementsystem;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.events.EventHandler;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class AllPatientsActivity extends AppCompatActivity {

    Button addNewPatientButton;
    RecyclerView recyclerView;
    PatentsAdapter patentsAdapter;
    ArrayList<PatientModel> patients;
    FirebaseFirestore db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_patients);
        db = FirebaseFirestore.getInstance();
        patients = new ArrayList<PatientModel>();
        addNewPatientButton = findViewById(R.id.addNewPatientButton);
        recyclerView = findViewById(R.id.patientsListView);
        patentsAdapter = new PatentsAdapter(this,patients);
        recyclerView.setAdapter(patentsAdapter);

        addNewPatientButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AllPatientsActivity.this,AddEditPatientActivity.class);
                startActivity(intent);
            }
        });

        EventHandler();

    }

    void EventHandler(){

        db.collection("patients").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                if(error == null){
                    for(DocumentSnapshot snapshot:value){
                        PatientModel patientModel = new PatientModel(
                          snapshot.getId(),
                          snapshot.getString("name"),
                                snapshot.getString("roomNo"),
                                snapshot.getString("ward"),"",""
                        );
                        patients.add(patientModel);
                        patentsAdapter.notifyDataSetChanged();
                    }
                }else{
                    Toast.makeText(AllPatientsActivity.this, "Get Patients Failed " + error.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
        System.out.println(patients.size());
    }
}