package com.example.buddy;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
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
        textViewStatus = findViewById(R.id.textViewStatus);

        DatabaseHelper dbHelper = new DatabaseHelper(this);

        registerBtn.setOnClickListener(v -> {
            String u = username.getText().toString().trim();
            String e = email.getText().toString().trim();
            String p = password.getText().toString().trim();

            // Validation
            if (u.isEmpty() || e.isEmpty() || p.isEmpty()) {
                textViewStatus.setTextColor(Color.RED);
                textViewStatus.setText("All fields are required");
            } else if (dbHelper.isUserExists(u, e)) {
                textViewStatus.setTextColor(Color.RED);
                textViewStatus.setText("Username or Email already registered");
            } else {
                boolean success = dbHelper.registerUser(u, e, p);
                if (success) {
                    textViewStatus.setTextColor(Color.GREEN);
                    textViewStatus.setText("Registered successfully!");
                    Toast.makeText(Register.this, "Registered successfully!", Toast.LENGTH_SHORT).show();

                    // Delay and then close the Register activity
                    new Handler().postDelayed(() -> finish(), 1500);
                } else {
                    textViewStatus.setTextColor(Color.RED);
                    textViewStatus.setText("Registration failed");
                }
            }
        });
    }
}
