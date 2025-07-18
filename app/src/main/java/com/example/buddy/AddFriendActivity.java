package com.example.buddy;

import android.os.Bundle;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;

public class AddFriendActivity extends AppCompatActivity {
    EditText nameInput, genderInput, dobInput, phoneInput, emailInput;
    Button addFriendBtn;
    DatabaseHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_friends);

        db = new DatabaseHelper(this);
        nameInput = findViewById(R.id.editTextName);
        genderInput = findViewById(R.id.editTextGender);
        dobInput = findViewById(R.id.editTextDob);
        phoneInput = findViewById(R.id.editTextPhone);
        emailInput = findViewById(R.id.editTextEmail);
        addFriendBtn = findViewById(R.id.buttonAdd);

        addFriendBtn.setOnClickListener(v -> {
            String name = nameInput.getText().toString();
            String gender = genderInput.getText().toString();
            String dob = dobInput.getText().toString();
            String phone = phoneInput.getText().toString();
            String email = emailInput.getText().toString();

            if (db.insertFriend(name, gender, dob, phone, email)) {
                Toast.makeText(this, "Friend added successfully", Toast.LENGTH_SHORT).show();
                finish();
            } else {
                Toast.makeText(this, "Error adding friend", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
