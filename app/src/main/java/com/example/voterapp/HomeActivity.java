package com.example.voterapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.hardware.biometrics.BiometricPrompt;
import android.os.Bundle;
import android.os.CancellationSignal;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class HomeActivity extends AppCompatActivity {
    String poll;
    String voter;
    ProgressBar loadingBar;
    TextView homeText;
    DatabaseReference mref;
    Intent resultIntent;
    Intent voteIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        final Executor executor = Executors.newSingleThreadExecutor();
        final BiometricPrompt biometricPrompt = new BiometricPrompt.Builder(this)
                .setTitle("Use Fingerprint")
                .setDescription("Confirm your vote")
                .setNegativeButton("Opt out", executor, (dialogInterface, i) -> {

                }).build();

        final HomeActivity activity=this;

        Intent intent = getIntent();
        poll = intent.getStringExtra("poll");
        voter = intent.getStringExtra("phone");
        loadingBar = findViewById(R.id.homeProgressBar);
        homeText = findViewById(R.id.homeText);
        mref = FirebaseDatabase.getInstance("https://voterapp-b835f-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference();
        loadingBar.setProgress(50, true);

        mref.child("Users").child(voter).get().addOnSuccessListener(dataSnapshot -> {
            if(dataSnapshot.child("votes").hasChild(poll)){
                homeText.setText("Redirecting to results...");
                loadingBar.setProgress(100, true);
                resultIntent = new Intent( HomeActivity.this, ResultActivity.class);
                resultIntent.putExtra("poll", poll);
                startActivity(resultIntent);
                finish();
            }
            else {
                homeText.setText("Redirecting to votes...");
                loadingBar.setProgress(100, true);
                biometricPrompt.authenticate(new CancellationSignal(), executor, new BiometricPrompt.AuthenticationCallback() {
                    @Override
                    public void onAuthenticationSucceeded(BiometricPrompt.AuthenticationResult result) {
                        super.onAuthenticationSucceeded(result);
                        activity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                voteIntent = new Intent(HomeActivity.this, VoteActivity.class);
                                voteIntent.putExtra("voter", voter);
                                voteIntent.putExtra("poll", poll);
                                startActivity(voteIntent);
                                finish();
                            }
                        });

                    }
                });

            }
        });



    }
}