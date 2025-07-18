package com.example.buddy;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class FriendDetails extends AppCompatActivity {

    TextView friendName, friendDob, friendPhone, friendGender, friendEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend_details);

        friendName = findViewById(R.id.friendName);
        friendDob = findViewById(R.id.friendDob);
        friendPhone = findViewById(R.id.friendPhone);
        friendGender = findViewById(R.id.friendGender);
        friendEmail = findViewById(R.id.friendEmail);

        // Get data from Intent
        Intent intent = getIntent();
        friendName.setText(intent.getStringExtra("name"));
        friendDob.setText("Born at " + intent.getStringExtra("dob"));
        friendPhone.setText(intent.getStringExtra("phone"));
        friendGender.setText(intent.getStringExtra("gender"));
        friendEmail.setText(intent.getStringExtra("email"));

        // Back button logic
        findViewById(R.id.backButton).setOnClickListener(v -> finish());
    }
}