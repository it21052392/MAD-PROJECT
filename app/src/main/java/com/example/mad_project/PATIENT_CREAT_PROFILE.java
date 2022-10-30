package com.example.mad_project;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class PATIENT_CREAT_PROFILE extends AppCompatActivity {

    DatabaseReference DatabaseReference = FirebaseDatabase.getInstance().getReferenceFromUrl("https://mad-project-662d9-default-rtdb.firebaseio.com/");

    private EditText pname,pphoneno,paddress,pemail,ppassword;
    private Button pbtnsave;

    private FirebaseAuth mAuth;


    private DatabaseReference databaseReference;
    private ProgressDialog loader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_creat_profile);


        mAuth = FirebaseAuth.getInstance();


        loader = new ProgressDialog(this);

        pname = findViewById(R.id.D_I_name);
        pphoneno = findViewById(R.id.D_I_phoneno);
        paddress = findViewById(R.id.D_I_address);
        pemail = findViewById(R.id.D_I_email);
        ppassword = findViewById(R.id.D_I_password);
        pbtnsave = findViewById(R.id.todoc);

        pbtnsave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = pname.getText().toString().trim();
                String phoneno = pphoneno.getText().toString().trim();
                String address = paddress.getText().toString().trim();
                String email = pemail.getText().toString().trim();
                String password = ppassword.getText().toString().trim();

                if(name.isEmpty()||phoneno.isEmpty()||address.isEmpty()||email.isEmpty()||password.isEmpty()){
                    Toast.makeText(PATIENT_CREAT_PROFILE.this,"Please Fill All fields",Toast.LENGTH_SHORT).show();
                }
                else{
                    DatabaseReference.child("users").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if(snapshot.hasChild(phoneno)){
                                Toast.makeText(PATIENT_CREAT_PROFILE.this,"Phone is already registered",Toast.LENGTH_SHORT).show();
                            }
                            else{
                                DatabaseReference.child("users").child(phoneno).child("pname").setValue(name);
                                DatabaseReference.child("users").child(phoneno).child("pphoneno").setValue(phoneno);
                                DatabaseReference.child("users").child(phoneno).child("paddress").setValue(address);
                                DatabaseReference.child("users").child(phoneno).child("pemail").setValue(email);
                                DatabaseReference.child("users").child(phoneno).child("ppassword").setValue(password);

                                Toast.makeText(PATIENT_CREAT_PROFILE.this,"User Registered Successfully",Toast.LENGTH_SHORT).show();
                                finish();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });

                    DatabaseReference.child("users").child(phoneno).child("pname").setValue(name);
                    DatabaseReference.child("users").child(phoneno).child("pphoneno").setValue(phoneno);
                    DatabaseReference.child("users").child(phoneno).child("paddress").setValue(address);
                    DatabaseReference.child("users").child(phoneno).child("pemail").setValue(email);
                    DatabaseReference.child("users").child(phoneno).child("ppassword").setValue(password);

                    Toast.makeText(PATIENT_CREAT_PROFILE.this,"User Registered Successfully",Toast.LENGTH_SHORT).show();
                    finish();
                }
            }
        });
    }
}