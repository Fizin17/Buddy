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
    int userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friends);

        // Get userId from intent
        userId = getIntent().getIntExtra("userId", -1);
        if (userId == -1) {
            Toast.makeText(this, "User ID not found", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        // Initialize views
        addFriend = findViewById(R.id.btnAddFriend);
        friendRecyclerView = findViewById(R.id.friendRecyclerView);
        friendRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Initialize database and fetch data
        dbHelper = new DatabaseHelper(this);
        friendList = dbHelper.getAllFriends(userId); // PASS userId

        // Create and set adapter — pass context and list
        friendAdapter = new FriendAdapter(Friends.this, friendList);
        friendRecyclerView.setAdapter(friendAdapter);

        // Add friend button click
        addFriend.setOnClickListener(v -> {
            Intent intent = new Intent(Friends.this, AddFriendActivity.class);
            intent.putExtra("userId", userId); // PASS userId to AddFriendActivity
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
            intent.putExtra("userId", userId); // optional if Report needs userId
            startActivity(intent);
        });
    }
}
