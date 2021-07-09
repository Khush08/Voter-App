package com.example.voterapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;

public class ResultActivity extends AppCompatActivity {
    DatabaseReference mref;
    TextView description;
    TextView item1;
    TextView item2;
    TextView item3;
    ProgressBar p1;
    ProgressBar p2;
    ProgressBar p3;
    Intent intent;
    String poll;
    int vote1, vote2, vote3, votes;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);
        intent = getIntent();
        poll = intent.getStringExtra("poll");
        mref = FirebaseDatabase.getInstance("https://voterapp-b835f-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference();
        description = findViewById(R.id.descriptionText);
        item1 = findViewById(R.id.item1Text);
        item2 = findViewById(R.id.item2Text);
        item3 = findViewById(R.id.item3Text);
        p1 = findViewById(R.id.item1Progress);
        p2 = findViewById(R.id.item2Progress);
        p3 = findViewById(R.id.item3Progress);

        mref.child("Polls").child(poll).get().addOnSuccessListener(dataSnapshot -> {
            description.setText(dataSnapshot.child("Description").getValue(String.class));
            item1.setText(dataSnapshot.child("Item1").child("value").getValue(String.class));
            item2.setText(dataSnapshot.child("Item2").child("value").getValue(String.class));
            item3.setText(dataSnapshot.child("Item3").child("value").getValue(String.class));
            vote1 = dataSnapshot.child("Item1").child("vote").getValue(Integer.class);
            vote2 = dataSnapshot.child("Item2").child("vote").getValue(Integer.class);
            vote3 = dataSnapshot.child("Item3").child("vote").getValue(Integer.class);
            votes = vote1 + vote2 + vote3;
            p1.setMax(votes);
            p2.setMax(votes);
            p3.setMax(votes);
            p1.setProgress(vote1, true);
            p2.setProgress(vote2, true);
            p3.setProgress(vote3, true);
        });

        mref.child("Polls").child(poll).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull @NotNull DataSnapshot snapshot, @Nullable @org.jetbrains.annotations.Nullable String previousChildName) {

            }

            @Override
            public void onChildChanged(@NonNull @NotNull DataSnapshot snapshot, @Nullable @org.jetbrains.annotations.Nullable String previousChildName) {
                vote1 = snapshot.child("Item1").child("vote").getValue(Integer.class);
                vote2 = snapshot.child("Item2").child("vote").getValue(Integer.class);
                vote3 = snapshot.child("Item3").child("vote").getValue(Integer.class);
                votes = vote1 + vote2 + vote3;
                p1.setMax(votes);
                p2.setMax(votes);
                p3.setMax(votes);
                p1.setProgress(vote1, true);
                p2.setProgress(vote2, true);
                p3.setProgress(vote3, true);
            }

            @Override
            public void onChildRemoved(@NonNull @NotNull DataSnapshot snapshot) {

            }

            @Override
            public void onChildMoved(@NonNull @NotNull DataSnapshot snapshot, @Nullable @org.jetbrains.annotations.Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });



    }
}