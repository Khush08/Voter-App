package com.example.voterapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;

public class welcomeActivity extends AppCompatActivity {

    Button loginBtn;
    Button registerBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        loginBtn = (Button)findViewById(R.id.loginBtn);
        registerBtn = (Button)findViewById(R.id.registerBtn);

        registerBtn.setOnClickListener((View v) -> {
            startActivity(new Intent(welcomeActivity.this, RegisterActivity.class));
        });

        loginBtn.setOnClickListener((View v) ->{
            startActivity(new Intent(welcomeActivity.this, LoginActivity.class));
        });

    }
}