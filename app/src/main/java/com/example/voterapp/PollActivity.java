package com.example.voterapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class PollActivity extends AppCompatActivity {
    ListView pollList;
    ArrayList<Items> itemList;
    DatabaseReference mref;
    String myPhone;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_poll);
        Intent intent = getIntent();
        myPhone = intent.getStringExtra("phone");
        pollList = findViewById(R.id.pollList);
        mref = FirebaseDatabase.getInstance("https://voterapp-b835f-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference();
        loadDataInListview();
    }
    private void  loadDataInListview(){
        mref.child("Polls").get().addOnSuccessListener(dataSnapshot -> {
            itemList = new ArrayList<>();
            if(dataSnapshot.hasChildren()){
                for(DataSnapshot child:dataSnapshot.getChildren()){
                    itemList.add(new Items(child.getKey(), myPhone));
                }
                adapter adapterObj = new adapter(PollActivity.this, itemList);
                pollList.setAdapter(adapterObj);
            }

        });
    }
}