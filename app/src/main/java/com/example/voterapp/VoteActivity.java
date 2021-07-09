package com.example.voterapp;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.hardware.biometrics.BiometricPrompt;
import android.os.Bundle;
import android.os.CancellationSignal;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class VoteActivity extends AppCompatActivity {
    Intent intent;
    Intent pollIntent;
    String voter;
    String poll;
    TextView pollText;
    RadioGroup radioGroup;
    RadioButton radioButton1;
    RadioButton radioButton2;
    RadioButton radioButton3;
    Button voteBtn;
    DatabaseReference mref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vote);



        intent = getIntent();
        voter = intent.getStringExtra("voter");
        poll = intent.getStringExtra("poll");
        pollText = findViewById(R.id.pollText);
        radioGroup = findViewById(R.id.voteGroup);
        radioButton1 = findViewById(R.id.radio1);
        radioButton2 = findViewById(R.id.radio2);
        radioButton3 = findViewById(R.id.radio3);
        voteBtn = findViewById(R.id.voteBtn);
        mref = FirebaseDatabase.getInstance("https://voterapp-b835f-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference();

        mref.child("Polls").child(poll).get().addOnSuccessListener(dataSnapshot -> {
            pollText.setText(dataSnapshot.child("Description").getValue(String.class));
            radioButton1.setText(dataSnapshot.child("Item1").child("value").getValue(String.class));
            radioButton2.setText(dataSnapshot.child("Item2").child("value").getValue(String.class));
            radioButton3.setText(dataSnapshot.child("Item3").child("value").getValue(String.class));
        });

        voteBtn.setOnClickListener((View v) ->{
            RadioButton rdBtn = findViewById(radioGroup.getCheckedRadioButtonId());
            if(rdBtn.isChecked()){
                 int id = rdBtn.getId();
                 String itemKey = (id==R.id.radio1)? "Item1":(id==R.id.radio2)? "Item2":"Item3";
                 mref.child("Users").child(voter).child("votes").child(poll).setValue(itemKey);
                 mref.child("Polls").child(poll).child(itemKey).get().addOnSuccessListener(dataSnapshot -> {
                    int vote = dataSnapshot.child("vote").getValue(Integer.class);
                    vote = vote+1;
                    mref.child("Polls").child(poll).child(itemKey).child("vote").setValue(vote);
                 });
                 Toast.makeText(v.getContext(), "Your vote has been recorded", Toast.LENGTH_SHORT).show();
                 pollIntent = new Intent(v.getContext(), PollActivity.class);
                 pollIntent.putExtra("phone", voter);
                 startActivity(pollIntent);
            }else{
                 Toast.makeText(v.getContext(), "Select an option", Toast.LENGTH_SHORT).show();
            }

        });
    }
    


    @SuppressLint("NonConstantResourceId")
    public void onRadioButtonClicked(View view) {
        boolean checked = ((RadioButton) view).isChecked();
        switch(view.getId()) {
            case R.id.radio1:
                if (checked)
                    break;
            case R.id.radio2:
                if (checked)
                    break;
            case R.id.radio3:
                if(checked)
                    break;
        }
    }
}