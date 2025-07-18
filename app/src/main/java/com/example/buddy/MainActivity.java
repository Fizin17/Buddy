package com.example.buddy;

import android.content.Intent;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_main);

            findViewById(R.id.btnLogin).setOnClickListener(v ->{
                Intent intent = new Intent (MainActivity.this, LoginActivity.class);
                startActivity(intent);
            });

            findViewById(R.id.btnRegister).setOnClickListener(v ->{
                Intent intent = new Intent (MainActivity.this, Register.class);
                startActivity(intent);
            });
    }
}