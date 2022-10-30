package com.example.mad_project;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Reviews extends AppCompatActivity {

    private RecyclerView recyclerView;
    private FloatingActionButton afab;

    private DatabaseReference reference;

    private ProgressDialog loader;

    private String key = "";
    private String uName;
    private String uReview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_review);

        recyclerView = findViewById(R.id.medTools);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setReverseLayout(true);
        layoutManager.setStackFromEnd(true);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(layoutManager);

        loader = new ProgressDialog(this);

        reference = FirebaseDatabase.getInstance().getReference().child("reviews");

        afab = findViewById(R.id.afab);
        afab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addReview();
            }
        });
    }

    private void addReview() {
        AlertDialog.Builder myDialog = new AlertDialog.Builder(this);
        LayoutInflater inflater = LayoutInflater.from(this);

        View myView = inflater.inflate(R.layout.add_review, null);
        myDialog.setView(myView);

        final AlertDialog dialog = myDialog.create();
        dialog.setCancelable(false);

        final EditText rName = myView.findViewById(R.id.aName);
        final EditText rReview = myView.findViewById(R.id.aReview);
        Button save = myView.findViewById(R.id.addBtn);
        Button cancel = myView.findViewById(R.id.aCancelBtn);


        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = rName.getText().toString().trim();
                String review = rReview.getText().toString().trim();
                String id = reference.push().getKey();


                if (name.isEmpty()) {
                    rName.setError("Required Field..");
                    return;
                }

                if (review.isEmpty()) {
                    rReview.setError("Required Field..");
                    return;
                } else {
                    loader.setMessage("Adding your Review");
                    loader.setCanceledOnTouchOutside(false);
                    loader.show();

                    ReviewModel model = new ReviewModel(name, review);
                    reference.child(id).setValue(model).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()) {
                                Toast.makeText(Reviews.this, "Review Added", Toast.LENGTH_SHORT).show();
                                loader.dismiss();
                            } else {
                                String error = task.getException().toString();
                                Toast.makeText(Reviews.this, "Failed" + error, Toast.LENGTH_SHORT).show();
                                loader.dismiss();
                            }
                        }
                    });
                }
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        View mView;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            mView = itemView;
        }

        public void setUname(String name) {
            TextView medicationName = mView.findViewById(R.id.rName);
            medicationName.setText(name);
        }

        public void setReview(String cmt) {
            TextView medicationTime = mView.findViewById(R.id.rCmnt);
            medicationTime.setText(cmt);
        }
    }


    @Override
    protected void onStart() {
        super.onStart();

        FirebaseRecyclerOptions<ReviewModel> options = new FirebaseRecyclerOptions.Builder<ReviewModel>()
                .setQuery(reference, ReviewModel.class)
                .build();

        FirebaseRecyclerAdapter<ReviewModel, MyViewHolder> adapter = new FirebaseRecyclerAdapter<ReviewModel, MyViewHolder>(options) {

            @Override
            protected void onBindViewHolder(@NonNull MyViewHolder holder, @SuppressLint("RecyclerView") final int position, @NonNull final ReviewModel model) {
                holder.setUname(model.getName());
                holder.setReview(model.getReview());

                holder.mView.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        key = getRef(position).getKey();
                        uName = model.getName();
                        uReview = model.getReview();

                        updateReview();
                    }
                });

            }

            @NonNull
            @Override
            public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_item_review, parent, false);
                return new MyViewHolder(view);
            }
        };

        recyclerView.setAdapter(adapter);
        adapter.startListening();
    }

    private void updateReview() {
        AlertDialog.Builder myDialog = new AlertDialog.Builder(this);
        LayoutInflater inflater = LayoutInflater.from(this);
        View view = inflater.inflate(R.layout.update_review, null);
        myDialog.setView(view);

        final AlertDialog dialog = myDialog.create();

        final EditText upName = view.findViewById(R.id.uName);
        final EditText upReview = view.findViewById(R.id.uReview);


        upName.setText(uName);
        upName.setSelection(uName.length());

        upReview.setText(uReview);
        upReview.setSelection(uReview.length());

        Button delButton = view.findViewById(R.id.uDelBtn);
        Button updateButton = view.findViewById(R.id.updateBtn);

        updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uName = upName.getText().toString().trim();
                uReview = upReview.getText().toString().trim();

                ReviewModel model = new ReviewModel(uName, uReview);

                reference.child(key).setValue(model).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                        if (task.isSuccessful()) {
                            Toast.makeText(Reviews.this, "Data has been updated successfully", Toast.LENGTH_SHORT).show();
                        } else {
                            String err = task.getException().toString();
                            Toast.makeText(Reviews.this, "update failed " + err, Toast.LENGTH_SHORT).show();
                        }

                    }
                });

                dialog.dismiss();

            }
        });

        delButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reference.child(key).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()){
                            Toast.makeText(Reviews.this, "Review deleted successfully", Toast.LENGTH_SHORT).show();
                        }else {
                            String err = task.getException().toString();
                            Toast.makeText(Reviews.this, "Failed to delete Medicine "+ err, Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }



}
