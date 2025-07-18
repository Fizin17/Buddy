package com.example.buddy;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

public class FriendDetails extends AppCompatActivity {

    TextView friendName, friendDob, friendPhone, friendGender, friendEmail;
    Button btnEdit;
    Friend friend;
    ActivityResultLauncher<Intent> editFriendLauncher;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend_details);

        // Initialize views
        friendName = findViewById(R.id.friendName);
        friendDob = findViewById(R.id.friendDob);
        friendPhone = findViewById(R.id.friendPhone);
        friendGender = findViewById(R.id.friendGender);
        friendEmail = findViewById(R.id.friendEmail);
        btnEdit = findViewById(R.id.btnEdit);

        // Get the Friend object from Intent
        Intent intent = getIntent();
        friend = (Friend) intent.getSerializableExtra("friend");

        // Null check
        if (friend == null) {
            finish(); // don't continue
            return;
        }

        showFriendDetails(friend);

        // Back button
        findViewById(R.id.backButton).setOnClickListener(v -> finish());

        editFriendLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                        String action = result.getData().getStringExtra("action");
                        if ("deleted".equals(action)) {
                            Intent resultIntent = new Intent();
                            resultIntent.putExtra("action", "deleted");
                            setResult(RESULT_OK, resultIntent);
                            finish();
                            return;
                        }
                        // Friend was updated — reload from DB
                        DatabaseHelper db = new DatabaseHelper(FriendDetails.this);
                        friend = db.getFriendById(friend.getId());
                        if (friend != null) {
                            showFriendDetails(friend);
                        }
                    }
                }
        );



        btnEdit.setOnClickListener(v -> {
            Intent editIntent = new Intent(FriendDetails.this, updateNDelete.class);
            editIntent.putExtra("friend", friend);
            editFriendLauncher.launch(editIntent);
        });
    }

    private void showFriendDetails(Friend f) {
        friendName.setText(f.getName());
        friendDob.setText("Born at " + f.getDob());
        friendPhone.setText(f.getPhone());
        friendGender.setText(f.getGender());
        friendEmail.setText(f.getEmail());
    }

}
