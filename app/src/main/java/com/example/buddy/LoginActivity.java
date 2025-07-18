package com.example.buddy;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class LoginActivity extends AppCompatActivity {
    EditText usernameInput, passwordInput;
    Button loginBtn,GotoRegisterbtn;
    DatabaseHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        db = new DatabaseHelper(this);

        usernameInput = findViewById(R.id.editTextUsername);
        passwordInput = findViewById(R.id.editTextPassword);
        loginBtn = findViewById(R.id.buttonLogin);
        GotoRegisterbtn = findViewById(R.id.buttonGotoRegister);


        loginBtn.setOnClickListener(v -> {
            String username = usernameInput.getText().toString().trim();
            String password = passwordInput.getText().toString().trim();

            if (username.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
            } else if (db.checkUser(username, password)) {
                Toast.makeText(this, "Login successful!", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(LoginActivity.this, HomeActivity.class));
                finish(); // Optional: prevent going back to login
            } else {
                Toast.makeText(this, "Invalid credentials", Toast.LENGTH_SHORT).show();
            }
        });
        GotoRegisterbtn.setOnClickListener(v -> {
            Intent intent = new Intent(LoginActivity.this, Register.class);
            startActivity(intent);
        });

    }
}
