package com.example.buddy;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class HomeActivity extends AppCompatActivity {
    LinearLayout btnAddFriend, btnVisualGraph, btnFriend, btnHome,btnReport ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        btnAddFriend = findViewById(R.id.btnFriends);
        btnVisualGraph = findViewById(R.id.btnVisualGraph);
        btnFriend = findViewById(R.id.btnFriend);
        btnReport = findViewById(R.id.btnReport);
        btnHome = findViewById(R.id.btnHome);

        btnAddFriend.setOnClickListener(v -> {
            int userId = getIntent().getIntExtra("userId", -1);
            Intent intent = new Intent(HomeActivity.this, Friends.class);
            intent.putExtra("userId", userId);
            startActivity(intent);
        });

        btnFriend.setOnClickListener(v -> {
            int userId = getIntent().getIntExtra("userId", -1);
            Intent intent = new Intent(HomeActivity.this, Friends.class);
            intent.putExtra("userId", userId);
            startActivity(intent);
        });

        btnVisualGraph.setOnClickListener(v -> {
            int userId = getIntent().getIntExtra("userId", -1);
            Intent intent = new Intent(HomeActivity.this, Report.class);
            intent.putExtra("userId", userId);
            startActivity(intent);
        });

        btnHome.setOnClickListener(v -> {
            Toast.makeText(this, "Already In Home Page", Toast.LENGTH_SHORT).show();
        });

        btnReport.setOnClickListener(v ->{
            int userId = getIntent().getIntExtra("userId", -1);
            Intent intent = new Intent(HomeActivity.this, ListByMonthActivity.class);
            intent.putExtra("userId", userId);
            startActivity(intent);
        });
    }
}
