package com.example.mad_project;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ADMIN_PROFILE extends AppCompatActivity {
    FirebaseDatabase firebaseDatabase;
    DatabaseReference reference;

    private ProgressDialog loader;

   private EditText adminname;
   private EditText adminphoneno;
   private EditText adminaddress;
   private EditText adminemail;
   private EditText adminpassword;

   private Button updateBtn;
   private Button delBtn;
   private Button todocbtn;

   private String name;
   private  String email;
   private String address;
   private String phoneno;
   private String password;

    AdminModel newModel;

    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_profile);

        firebaseDatabase = FirebaseDatabase.getInstance();
        reference = firebaseDatabase.getReference("users").child("admin");

        adminname = findViewById(R.id.ad_name);
        adminphoneno = findViewById(R.id.ad_phoneno);
        adminaddress = findViewById(R.id.ad_address);
        adminemail = findViewById(R.id.ad_email);
        adminpassword = findViewById(R.id.ad_password);

        updateBtn = findViewById(R.id.U_btnsave);
        delBtn = findViewById(R.id.ad_delete);


        getphoneno();
    }


    private void getphoneno() {

        reference.addValueEventListener(new ValueEventListener() {

            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if(snapshot.exists()){
                    name = snapshot.child("name").getValue().toString();
                    email = snapshot.child("email").getValue().toString();
                    address = snapshot.child("address").getValue().toString();
                    phoneno = snapshot.child("phoneNo").getValue().toString();
                    password = snapshot.child("pass").getValue().toString();

                    newModel = new AdminModel(name, email, address, phoneno, password);

                    adminname.setText(name);
                    adminname.setSelection(name.length());

                    adminphoneno.setText(phoneno);
                    adminphoneno.setSelection(phoneno.length());

                    adminaddress.setText(address);
                    adminaddress.setSelection(address.length());

                    adminemail.setText(email);
                    adminemail.setSelection(email.length());

                    adminpassword.setText(password);
                    adminpassword.setSelection(password.length());
                }



            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(ADMIN_PROFILE.this, "Fail to get data.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        update();

        delBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                reference.removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(ADMIN_PROFILE.this, "deleted", Toast.LENGTH_SHORT);
                        }else{
                            String err = task.getException().toString();
                            Toast.makeText(ADMIN_PROFILE.this, "Error: " + err, Toast.LENGTH_SHORT);
                        }
                    }
                });
            }
        });

    }

    public void update() {
        updateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loader = new ProgressDialog(ADMIN_PROFILE.this);
                loader.setMessage("Updating");
                loader.setCanceledOnTouchOutside(false);
                loader.show();

                name = adminname.getText().toString().trim();
                email =  adminemail.getText().toString().trim();
                address =  adminaddress.getText().toString().trim();
                phoneno =  adminphoneno.getText().toString().trim();
                password =  adminpassword.getText().toString().trim();

                AdminModel model = new AdminModel(name, phoneno, address, email, password);

                reference.setValue(model).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()) {
                            loader.dismiss();
                            Toast.makeText(ADMIN_PROFILE.this,"Update Successfully",Toast.LENGTH_SHORT).show();

                        }else{
                            loader.dismiss();
                            Toast.makeText(ADMIN_PROFILE.this,"Failed to Update",Toast.LENGTH_SHORT).show();
                        }
                    }
                });

            }
        });
    }
}