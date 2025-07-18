package com.example.buddy;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;

public class Friends extends AppCompatActivity {

    ImageButton addFriend;
    RecyclerView friendRecyclerView;
    FriendAdapter friendAdapter;
    ArrayList<Friend> friendList;
    DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friends);

        // Initialize views
        addFriend = findViewById(R.id.btnAddFriend);
        friendRecyclerView = findViewById(R.id.friendRecyclerView);
        friendRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Initialize database and fetch data
        dbHelper = new DatabaseHelper(this);
        friendList = dbHelper.getAllFriends();

        // Create and set adapter — pass context and list
        friendAdapter = new FriendAdapter(Friends.this, friendList);
        friendRecyclerView.setAdapter(friendAdapter);

        // Add friend button click
        addFriend.setOnClickListener(v -> {
            Intent intent = new Intent(Friends.this, AddFriendActivity.class);
            startActivity(intent);
        });

        // Bottom navigation
        findViewById(R.id.btnHome).setOnClickListener(v -> {
            finish(); // go back to home (if you started Friends with startActivity)
        });

        findViewById(R.id.btnFriend).setOnClickListener(v -> {
            Toast.makeText(this, "Already In Friends Page", Toast.LENGTH_SHORT).show();
        });

        findViewById(R.id.btnReport).setOnClickListener(v -> {
            Intent intent = new Intent(Friends.this, Report.class);
            startActivity(intent);
        });
    }



}
