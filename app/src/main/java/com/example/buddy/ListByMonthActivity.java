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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_by_month);

        db = new DatabaseHelper(this);
        monthInput = findViewById(R.id.editTextMonth);
        searchBtn = findViewById(R.id.buttonSearch);
        listView = findViewById(R.id.listViewResults);

        searchBtn.setOnClickListener(v -> {
            String month = monthInput.getText().toString().trim();
            ArrayList<String> list = db.getFriendsByMonth(month);
            ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, list);
            listView.setAdapter(adapter);
        });
    }
}
