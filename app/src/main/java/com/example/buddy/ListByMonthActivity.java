package com.example.buddy;

import android.os.Bundle;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;

public class ListByMonthActivity extends AppCompatActivity {

    EditText monthInput;
    Button searchBtn;
    ListView listView;
    DatabaseHelper db;
    int userId; // <-- New field to store userId

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_by_month);

        db = new DatabaseHelper(this);
        monthInput = findViewById(R.id.editTextMonth);
        searchBtn = findViewById(R.id.buttonSearch);
        listView = findViewById(R.id.listViewResults);

        // Get userId from intent
        userId = getIntent().getIntExtra("userId", -1);
        if (userId == -1) {
            Toast.makeText(this, "User ID is missing!", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        searchBtn.setOnClickListener(v -> {
            String month = monthInput.getText().toString().trim();

            // Validate month format
            if (month.length() != 2 || !month.matches("\\d{2}")) {
                Toast.makeText(this, "Please enter a valid 2-digit month (e.g., 01 for January)", Toast.LENGTH_SHORT).show();
                return;
            }

            ArrayList<String> list = db.getFriendsByMonth(String.valueOf(userId), month);
            ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, list);
            listView.setAdapter(adapter);
        });
    }
}
