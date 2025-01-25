package com.example.myapplication.Activities;

import android.os.Bundle;

import android.content.Intent;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.R;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        Intent intent = new Intent(MainActivity.this, HomeActivity.class);
        startActivity(intent);
        finish(); // Close login activity
        /*

        EditText username = findViewById(R.id.username_input); // Updated ID
        EditText password = findViewById(R.id.password_input); // Updated ID
        Button loginButton = findViewById(R.id.login_button);

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
        });*/
    }

    private boolean authenticate(String username, String password) {
        // Replace this with your actual authentication logic
        return username.equals("admin") && password.equals("admin");
    }
}
