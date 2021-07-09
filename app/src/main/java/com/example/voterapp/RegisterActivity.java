package com.example.voterapp;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity {
    EditText nameText;
    EditText phoneText;
    EditText passwordText;
    Button registerBtn;
    DatabaseReference mref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        nameText = findViewById(R.id.nameentry);
        phoneText = findViewById(R.id.phoneentry);
        passwordText = findViewById(R.id.passwordentry);
        registerBtn = findViewById(R.id.registerLayoutBtn);
        mref = FirebaseDatabase.getInstance("https://voterapp-b835f-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference();

        registerBtn.setOnClickListener((View v) -> {

            if(TextUtils.isEmpty(nameText.getText().toString()))
            {
                Toast.makeText(v.getContext(),"Please enter your name first..", Toast.LENGTH_SHORT).show();
            }
            else if(TextUtils.isEmpty(phoneText.getText().toString()))
            {
                Toast.makeText(v.getContext(),"Please enter your Phone Number first..", Toast.LENGTH_SHORT).show();
            }
            else if(TextUtils.isEmpty(passwordText.getText().toString()))
            {
                Toast.makeText(v.getContext(),"Please enter your password first..", Toast.LENGTH_SHORT).show();
            }
            else if(phoneText.getText().toString().length()<10)
            {
                Toast.makeText(v.getContext(),"Please enter correct Phone Number..", Toast.LENGTH_SHORT).show();
            }

            else
            {
                mref.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        if(!(dataSnapshot.child("Users").child("+91"+phoneText.getText().toString()).exists()))
                        {
                            CreateAccount(phoneText,nameText,passwordText);
                        }
                        else
                        {
                            Toast.makeText(RegisterActivity.this, "Account with this number already exist..", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
        });

    }


    private void CreateAccount(EditText phone, EditText name, EditText password) {
        Map<String,Object> details=new HashMap<String,Object>();
        details.put("Phone","+91"+phone.getText().toString());
        details.put("Name",name.getText().toString());
        details.put("Password",password.getText().toString());
        details.put("Vote",new ArrayList<String>());
        mref.child("Users").child("+91"+phone.getText().toString()).updateChildren(details)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                        if(task.isSuccessful())
                        {
                            Toast.makeText(RegisterActivity.this, "Account created successfully..", Toast.LENGTH_SHORT).show();
                            Intent loginIntent =new Intent(RegisterActivity.this,LoginActivity.class);
                            loginIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(loginIntent);
                        }
                        else
                        {
                            Toast.makeText(RegisterActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}