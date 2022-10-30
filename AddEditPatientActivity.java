package com.example.hospitalmanagementsystem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class AddEditPatientActivity extends AppCompatActivity {

    Button backButton,saveButton;

    TextInputEditText nameField,roomNoField,wardField,allocationDateField,profileImageField;

    String name,roomNo,ward,allocationDate,image;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_edit_patient);



        backButton = findViewById(R.id.backButton);
        saveButton = findViewById(R.id.saveButton);

        nameField = findViewById(R.id.patientNameInputField);
        roomNoField = findViewById(R.id.patientRoomNo);
        wardField = findViewById(R.id.ward);
        allocationDateField = findViewById(R.id.allocationDate);
        profileImageField = findViewById(R.id.profileUrl);

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent= new Intent(AddEditPatientActivity.this,AllPatientsActivity.class);
                startActivity(intent);
            }
        });

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseFirestore db = FirebaseFirestore.getInstance();
                name = String.valueOf(nameField.getText());
                ward = String.valueOf(wardField.getText());
                roomNo = String.valueOf(roomNoField.getText());

                if(getIntent().getStringExtra("PATIENT_NAME") !=null){
                    boolean validator =  validator(name,"Name")
                            && validator(ward,"Ward")  && validator(roomNo,"Room Number");
                    if(validator){
                        Map data =new HashMap();
                        data.put("name",name);
                        data.put("roomNo",roomNo);
                        data.put("ward",ward);
                        db.collection("patients").document(getIntent().getStringExtra("PATIENT_ID")).update(data).addOnCompleteListener(new OnCompleteListener() {
                            @Override
                            public void onComplete(@NonNull Task task) {
                                if(task.isSuccessful()){
                                    Toast.makeText(AddEditPatientActivity.this, "Patient updating successfully", Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(AddEditPatientActivity.this, AllPatientsActivity.class);
                                    startActivity(intent);
                                } else {
                                    Toast.makeText(AddEditPatientActivity.this, "Patient updated failed", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }
                    return;
                }

                allocationDate = String.valueOf(allocationDateField.getText());
                image = String.valueOf(profileImageField.getText());

                boolean validator = validator(name,"Name")
                        && validator(ward,"Ward")  && validator(roomNo,"Room Number") &&validator(allocationDate,"Allocation Date") ;


                if(validator){
                    PatientModel patientModel = new PatientModel(
                            "",
                            name,
                            roomNo,
                            ward,
                            allocationDate,
                            image
                    );
                    db.collection("patients").document().set(patientModel).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(AddEditPatientActivity.this, "Patient added successfully", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(AddEditPatientActivity.this, AllPatientsActivity.class);
                                startActivity(intent);
                            } else {
                                Toast.makeText(AddEditPatientActivity.this, "Patient adding failed", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        });
        if(getIntent().getStringExtra("PATIENT_NAME") !=null){
            name = getIntent().getStringExtra("PATIENT_NAME");
            roomNo = getIntent().getStringExtra("PATIENT_ROOM_NUMBER");
            ward = getIntent().getStringExtra("PATIENT_WARD");
            nameField.setText(name);
            roomNoField.setText(roomNo);
            wardField.setText(ward);
            saveButton.setText("EDIT");
            allocationDateField.setVisibility(View.INVISIBLE);
            profileImageField.setVisibility(View.INVISIBLE);
        }
    }

    boolean validator(String value,String message){
        if(value.isEmpty()){
            Toast.makeText(this, "Please enter valid " +message, Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }
}