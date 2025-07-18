package com.example.buddy;

import android.os.Bundle;
import android.view.WindowManager;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;
import java.util.Collections;

public class ListByMonthActivity extends AppCompatActivity {

    ListView listViewResults;
    DatabaseHelper dbHelper;
    ArrayAdapter<String> adapter;
    ArrayList<Friend> friendList;
    ArrayList<String> displayList;
    String[] monthNames = {
            "January", "February", "March", "April", "May", "June",
            "July", "August", "September", "October", "November", "December"
    };
    int userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_by_month);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        listViewResults = findViewById(R.id.listViewResults);
        dbHelper = new DatabaseHelper(this);

        userId = getIntent().getIntExtra("userId", -1);
        friendList = dbHelper.getAllFriends(userId);

        Collections.sort(friendList, (f1, f2) -> {
            int m1 = Integer.parseInt(f1.getDob().split("/")[1]);
            int m2 = Integer.parseInt(f2.getDob().split("/")[1]);
            return Integer.compare(m1, m2);
        });

        displayList = new ArrayList<>();
        int lastMonth = -1;

        for (Friend f : friendList) {
            String[] parts = f.getDob().split("/");
            int month = Integer.parseInt(parts[1]);

            if (month != lastMonth) {
                // Add month header
                displayList.add(monthNames[month - 1]);
                lastMonth = month;
            }
            displayList.add(" • " + f.getName() + " (" + f.getDob() + ")");
        }
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, displayList);
        listViewResults.setAdapter(adapter);
    }
}
