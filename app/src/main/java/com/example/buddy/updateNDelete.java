package com.example.buddy;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.RadioButton;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class updateNDelete extends AppCompatActivity {

    EditText etName, etDob, etPhone, etEmail;
    RadioGroup genderGroup;
    Button btnSave, btnDelete;
    int friendId;
    DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_ndelete);
        Friend friend = (Friend) getIntent().getSerializableExtra("friend");

        if (friend == null) {
            Toast.makeText(this, "Friend data not found", Toast.LENGTH_SHORT).show();
            finish();  // Prevent crash
            return;
        }


        etName = findViewById(R.id.editName);
        etDob = findViewById(R.id.editDob);
        etPhone = findViewById(R.id.editTel);
        etEmail = findViewById(R.id.editEmail);
        genderGroup = findViewById(R.id.genderGroup);
        btnSave = findViewById(R.id.btnUpdate);

        dbHelper = new DatabaseHelper(this);

        friendId = friend.getId();

        etName.setText(friend.getName());
        etDob.setText(friend.getDob());
        etPhone.setText(friend.getPhone());
        etEmail.setText(friend.getEmail());

        if ("Male".equals(friend.getGender())) {
            genderGroup.check(R.id.radioMale);
        } else if ("Female".equals(friend.getGender())) {
            genderGroup.check(R.id.radioFemale);
        }


        btnSave.setOnClickListener(v -> {
            String newName = etName.getText().toString().trim();
            String newDob = etDob.getText().toString().trim();
            String newPhone = etPhone.getText().toString().trim();
            String newEmail = etEmail.getText().toString().trim();

            int selectedId = genderGroup.getCheckedRadioButtonId();
            String newGender = ((RadioButton) findViewById(selectedId)).getText().toString();

            boolean success = dbHelper.updateFriend(friendId, newName, newGender, newDob, newPhone, newEmail);
            if (success) {
                Toast.makeText(this, "Friend updated", Toast.LENGTH_SHORT).show();
                Intent resultIntent = new Intent();
                setResult(RESULT_OK, resultIntent);
                finish();
            } else {
                Toast.makeText(this, "Update failed", Toast.LENGTH_SHORT).show();
            }
        });

        btnDelete = findViewById(R.id.btnDelete);
        btnDelete.setOnClickListener(v -> {
            boolean deleted = dbHelper.deleteFriend(friendId);
            if (deleted) {
                Toast.makeText(this, "Friend deleted", Toast.LENGTH_SHORT).show();
                Intent resultIntent = new Intent();
                resultIntent.putExtra("action", "deleted");
                setResult(RESULT_OK, resultIntent);
                finish();
            } else {
                Toast.makeText(this, "Delete failed", Toast.LENGTH_SHORT).show();
            }
        });


    }
}