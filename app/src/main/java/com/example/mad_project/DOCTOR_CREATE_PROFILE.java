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

public class DOCTOR_CREATE_PROFILE extends AppCompatActivity {

    DatabaseReference DatabaseReference = FirebaseDatabase.getInstance().getReferenceFromUrl("https://mad-project-662d9-default-rtdb.firebaseio.com/");

    private EditText dname,dphoneno,daddress,demail,dpassword;
    private Button dbtnsave;

    private FirebaseAuth mAuth;


    private DatabaseReference databaseReference;
    private ProgressDialog loader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_create_profile);


        mAuth = FirebaseAuth.getInstance();


        loader = new ProgressDialog(this);

        dname = findViewById(R.id.D_I_name);
        dphoneno = findViewById(R.id.D_I_phoneno);
        daddress = findViewById(R.id.D_I_address);
        demail = findViewById(R.id.D_I_email);
        dpassword = findViewById(R.id.D_I_password);
        dbtnsave = findViewById(R.id.todoc);

        dbtnsave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = dname.getText().toString().trim();
                String phoneno = dphoneno.getText().toString().trim();
                String address = daddress.getText().toString().trim();
                String email = demail.getText().toString().trim();
                String password = dpassword.getText().toString().trim();

                if(name.isEmpty()||phoneno.isEmpty()||address.isEmpty()||email.isEmpty()||password.isEmpty()){
                    Toast.makeText(DOCTOR_CREATE_PROFILE.this,"Please Fill All fields",Toast.LENGTH_SHORT).show();
                }
                else{
                    DatabaseReference.child("users").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if(snapshot.hasChild(email)){
                                Toast.makeText(DOCTOR_CREATE_PROFILE.this,"Phone is already registered",Toast.LENGTH_SHORT).show();
                            }
                            else{
                                DatabaseReference.child("users").child(email).child(phoneno).child("dname").setValue(name);
                                DatabaseReference.child("users").child(email).child(phoneno).child("dphoneno").setValue(phoneno);
                                DatabaseReference.child("users").child(email).child(phoneno).child("daddress").setValue(address);
                                DatabaseReference.child("users").child(email).child(phoneno).child("demail").setValue(email);
                                DatabaseReference.child("users").child(email).child(phoneno).child("dpassword").setValue(password);

                                Toast.makeText(DOCTOR_CREATE_PROFILE.this,"User Registered Successfully",Toast.LENGTH_SHORT).show();
                                finish();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });

                    DatabaseReference.child("users").child(email).child(phoneno).child("dname").setValue(name);
                    DatabaseReference.child("users").child(email).child(phoneno).child("dphoneno").setValue(phoneno);
                    DatabaseReference.child("users").child(email).child(phoneno).child("daddress").setValue(address);
                    DatabaseReference.child("users").child(email).child(phoneno).child("demail").setValue(email);
                    DatabaseReference.child("users").child(email).child(phoneno).child("dpassword").setValue(password);

                    Toast.makeText(DOCTOR_CREATE_PROFILE.this,"User Registered Successfully",Toast.LENGTH_SHORT).show();
                    finish();
                }
            }
        });
    }
}