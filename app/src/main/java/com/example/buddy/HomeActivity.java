package com.example.buddy;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;

public class HomeActivity extends AppCompatActivity {
    Button btnAddFriend, btnListFriendsByMonth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        btnAddFriend = findViewById(R.id.btnAddFriend);
        btnListFriendsByMonth = findViewById(R.id.btnListByMonth);

        btnAddFriend.setOnClickListener(v -> {
            Intent intent = new Intent(HomeActivity.this, AddFriendActivity.class);
            startActivity(intent);
        });

        btnListFriendsByMonth.setOnClickListener(v -> {
            Intent intent = new Intent(HomeActivity.this, ListByMonthActivity.class);
            startActivity(intent);
        });
    }
}
