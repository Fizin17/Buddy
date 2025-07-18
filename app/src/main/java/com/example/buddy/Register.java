package com.example.buddy;

import android.graphics.Color;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;


public class Register extends AppCompatActivity {
    EditText username, password, email;
    Button registerBtn;
    TextView textViewStatus;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        username = findViewById(R.id.etUsername);
        email = findViewById(R.id.etEmail);
        password = findViewById(R.id.etPassword);
        registerBtn = findViewById(R.id.btnRegister);

        DatabaseHelper dbHelper = new DatabaseHelper(this);

        registerBtn.setOnClickListener(v -> {
            String u = username.getText().toString().trim();
            String p = password.getText().toString().trim();

            if (u.isEmpty() || p.isEmpty()) {
                textViewStatus.setText("All fields are required");
            } else if (dbHelper.checkUser(u, p)) {
                textViewStatus.setText("User already registered");
            } else {
                boolean success = dbHelper.registerUser(u, p);
                if (success) {
                    textViewStatus.setTextColor(Color.GREEN);
                    textViewStatus.setText("Registered successfully!");
                } else {
                    textViewStatus.setText("Registration failed");
                }
            }
        });
    }
}