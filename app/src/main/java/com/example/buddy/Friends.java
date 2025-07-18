package com.example.buddy;

import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
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
    ActivityResultLauncher<Intent> friendDetailsLauncher;

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

        dbHelper = new DatabaseHelper(this);
        friendList = dbHelper.getAllFriends(userId);

        // Initialize adapter
        friendAdapter = new FriendAdapter(this, friendList, friend -> {
            Intent intent = new Intent(Friends.this, FriendDetails.class);
            intent.putExtra("friend", friend);
            friendDetailsLauncher.launch(intent);
        });

        friendRecyclerView.setAdapter(friendAdapter);

        // Search functionality
        EditText etSearch = findViewById(R.id.etSearch);
        ImageView imgFind = findViewById(R.id.imgFind);

        imgFind.setOnClickListener(v -> {
            String keyword = etSearch.getText().toString().trim().toLowerCase();
            if (keyword.isEmpty()) {
                loadFriendList();
            } else {
                ArrayList<Friend> filteredList = new ArrayList<>();
                for (Friend f : friendList) {
                    if (f.getName().toLowerCase().contains(keyword)) {
                        filteredList.add(f);
                    }
                }
                friendAdapter.updateList(filteredList);
            }
        });

        // Add new friend
        addFriend.setOnClickListener(v -> {
            Intent intent = new Intent(Friends.this, AddFriendActivity.class);
            intent.putExtra("userId", userId);
            startActivity(intent);
        });

        // Bottom navigation
        findViewById(R.id.btnHome).setOnClickListener(v -> finish());
        findViewById(R.id.btnFriend).setOnClickListener(v -> {
            Toast.makeText(this, "Already In Friends Page", Toast.LENGTH_SHORT).show();
        });
        findViewById(R.id.btnReport).setOnClickListener(v -> {
            Intent intent = new Intent(Friends.this, Report.class);
            intent.putExtra("userId", userId);
            startActivity(intent);
        });

        // FriendDetails launcher result
        friendDetailsLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK) {
                        loadFriendList();
                    }
                }
        );
    }

    // Reload list from DB
    private void loadFriendList() {
        friendList = dbHelper.getAllFriends(userId);
        if (friendAdapter == null) {
            friendAdapter = new FriendAdapter(this, friendList, friend -> {
                Intent intent = new Intent(Friends.this, FriendDetails.class);
                intent.putExtra("friend", friend);
                friendDetailsLauncher.launch(intent);
            });
            friendRecyclerView.setAdapter(friendAdapter);
        } else {
            // update the list
            friendAdapter.updateList(friendList);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadFriendList();
    }


}
