package com.example.buddy;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;

import java.util.Calendar;
import java.util.Locale;

public class AddFriendActivity extends AppCompatActivity {

    EditText nameInput, editTextDob, phoneInput, emailInput;
    Button addFriendBtn;
    RadioGroup radioGroupGender;
    RadioButton radioMale, radioFemale;
    DatabaseHelper db;
    int userId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_friends);

        // Get userId from Intent
        userId = getIntent().getIntExtra("userId", -1);
        if (userId == -1) {
            Toast.makeText(this, "User ID is missing!", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        // Initialize views
        db = new DatabaseHelper(this);
        nameInput = findViewById(R.id.editTextName);
        editTextDob = findViewById(R.id.editTextDob);
        phoneInput = findViewById(R.id.editTextPhone);
        emailInput = findViewById(R.id.editTextEmail);
        addFriendBtn = findViewById(R.id.buttonAdd);
        radioGroupGender = findViewById(R.id.radioGroupGender);
        radioMale = findViewById(R.id.radioMale);
        radioFemale = findViewById(R.id.radioFemale);

        // Date picker dialog
        editTextDob.setOnClickListener(v -> {
            final Calendar calendar = Calendar.getInstance();
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH);
            int day = calendar.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog datePickerDialog = new DatePickerDialog(
                    AddFriendActivity.this,
                    (view, year1, month1, dayOfMonth) -> {
                        String selectedDate = String.format(Locale.getDefault(), "%02d/%02d/%04d", dayOfMonth, month1 + 1, year1);
                        editTextDob.setText(selectedDate);
                    },
                    year, month, day
            );
            datePickerDialog.show();
        });

        // Add friend button click
        addFriendBtn.setOnClickListener(v -> {
            String name = nameInput.getText().toString().trim();
            String dob = editTextDob.getText().toString().trim();
            String phone = phoneInput.getText().toString().trim();
            String email = emailInput.getText().toString().trim();

            // Get selected gender
            int selectedGenderId = radioGroupGender.getCheckedRadioButtonId();
            String gender = "";
            if (selectedGenderId == R.id.radioMale) {
                gender = "Male";
            } else if (selectedGenderId == R.id.radioFemale) {
                gender = "Female";
            }

            if (name.isEmpty() || gender.isEmpty() || dob.isEmpty() || phone.isEmpty() || email.isEmpty()) {
                Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
                return;
            }

            // Insert using userId
            if (db.insertFriend(userId, name, gender, dob, phone, email)) {
                Toast.makeText(this, "Friend added successfully", Toast.LENGTH_SHORT).show();
                finish();
            } else {
                Toast.makeText(this, "Error adding friend", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
