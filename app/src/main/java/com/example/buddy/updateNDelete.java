package com.example.buddy;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.RadioButton;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import java.util.Calendar;
import java.util.Locale;

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
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
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

        etDob.setOnClickListener(v -> {
            final Calendar calendar = Calendar.getInstance();

            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH);
            int day = calendar.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog datePickerDialog = new DatePickerDialog(
                    this,
                    (view, selectedYear, selectedMonth, selectedDay) -> {
                        // Format to dd/MM/yyyy
                        String formattedDate = String.format(Locale.getDefault(), "%02d/%02d/%04d",
                                selectedDay, selectedMonth + 1, selectedYear);
                        etDob.setText(formattedDate);
                    },
                    year, month, day
            );
            datePickerDialog.show();
        });


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

        //Navigation Button
        findViewById(R.id.btnHome).setOnClickListener(v -> {
            Intent intent2 = new Intent(updateNDelete.this, HomeActivity.class);
            intent2.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intent2);
            finish();
        });

        findViewById(R.id.btnFriend).setOnClickListener(v -> {
            Intent intent2 = new Intent(updateNDelete.this, Friends.class);
            intent2.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intent2);
            finish();
        });

        findViewById(R.id.btnReport).setOnClickListener(v -> {
            int userId = getIntent().getIntExtra("userId", -1);
            Intent intent = new Intent(updateNDelete.this, ListByMonthActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            intent.putExtra("userId", userId);
            startActivity(intent);
        });

    }
}