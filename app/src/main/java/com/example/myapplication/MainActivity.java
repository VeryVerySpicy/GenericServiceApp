package com.example.myapplication;

import android.os.Bundle;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        EditText username = findViewById(R.id.username);
        EditText password = findViewById(R.id.password);
        Button loginButton = findViewById(R.id.login_button);

        Intent intent = new Intent(MainActivity.this, HomeActivity.class);
        startActivity(intent);/*
        loginButton.setOnClickListener(v -> {
            String user = username.getText().toString();
            String pass = password.getText().toString();

            // Add your authentication logic here
            if (authenticate(user, pass)) {
                // Proceed to the next activity
                Intent intent = new Intent(MainActivity.this, HomeActivity.class);
                startActivity(intent);
                finish(); // Close login activity
            } else {
                // Show error message
                Toast.makeText(MainActivity.this, "Invalid credentials", Toast.LENGTH_SHORT).show();
            }
        })*/;
    }

    private boolean authenticate(String username, String password) {
        // Replace this with your actual authentication logic
        return username.equals("admin") && password.equals("admin");
    }
}
