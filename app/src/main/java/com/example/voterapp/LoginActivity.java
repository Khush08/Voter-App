package com.example.voterapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

public class LoginActivity extends AppCompatActivity {
    EditText phoneText;
    EditText passwordText;
    Button loginBtn;
    TextView newUserText;
    DatabaseReference mref;
    String myphone, mypassword;
    ProgressBar loadingBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        newUserText = findViewById(R.id.homeText);
        phoneText = findViewById(R.id.phoneLoginText);
        passwordText = findViewById(R.id.passwordLoginText);
        loginBtn = findViewById(R.id.loginLayoutBtn);
        mref = FirebaseDatabase.getInstance("https://voterapp-b835f-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference();
        loadingBar = findViewById(R.id.loginProgressBar);
        newUserText.setOnClickListener((View v) -> {
            startActivity(new Intent(v.getContext(),RegisterActivity.class));
        });

        loginBtn.setOnClickListener((View v) -> {
            if(TextUtils.isEmpty(phoneText.getText().toString()))
            {
                Toast.makeText(LoginActivity.this, "Please enter your phone number..", Toast.LENGTH_SHORT).show();
            }
            else if(TextUtils.isEmpty(passwordText.getText().toString()))
            {
                Toast.makeText(LoginActivity.this, "Please enter your password...", Toast.LENGTH_SHORT).show();
            }
            else if(phoneText.getText().toString().length() <10)
            {
                Toast.makeText(LoginActivity.this, "Please enter correct phone number..", Toast.LENGTH_SHORT).show();
            }
            else
            {
                myphone = phoneText.getText().toString();
                mypassword = passwordText.getText().toString();
                loginUser();
            }
        });
    }


    private void loginUser() {
        loadingBar.setProgress(35, true);
        loadingBar.setProgress(50, true);
        loadingBar.setProgress(65, true);
        loadingBar.setProgress(80, true);
        AllowAccessToUser(myphone,mypassword);
    }
    private void AllowAccessToUser(String myphone, String mypassword) {

        mref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if(dataSnapshot.child("Users").child("+91"+myphone).exists())
                {
                    final Users userdata=dataSnapshot.child("Users").child("+91"+myphone).getValue(Users.class);
                    if(userdata.getPhone().equals("+91"+myphone))
                    {

                        if(userdata.getPassword().equals(mypassword))
                        {

                            loadingBar.setProgress(100, true);
                            Toast.makeText(LoginActivity.this, "Logged in Successfully..", Toast.LENGTH_SHORT).show();
                            Intent i=new Intent(LoginActivity.this,PollActivity.class);
                            Prevalent.currentOnlineUser=userdata;
                            i.putExtra("phone","+91"+myphone);
                            startActivity(i);
                            LoginActivity.this.finish();
                        }
                        else
                        {
                            loadingBar.setProgress(20, false);
                            Toast.makeText(LoginActivity.this, "please enter correct password..", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
                else
                {
                    loadingBar.setProgress(0, false);
                    Toast.makeText(LoginActivity.this, "please create your account first with this number ..", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

}