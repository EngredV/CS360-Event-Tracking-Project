package com.example.EventTrackingOptionTwoEngredVanegas;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class SignUpActivity extends AppCompatActivity {

    private DatabaseHelper databaseHelper;
    private EditText editTextEmail;
    private EditText editTextPassword;
    private EditText editTextConfirmPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        databaseHelper = new DatabaseHelper(this);
        editTextEmail = findViewById(R.id.text_field_signin_email);
        editTextPassword = findViewById(R.id.text_field_signup_password);
        editTextConfirmPassword = findViewById(R.id.text_field_signup_confirm_password);

        Button buttonSignUp = findViewById(R.id.button_signup);
        buttonSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signUp();
            }
        });

        ImageButton imageButtonBack = findViewById(R.id.image_button_signin_back);
        imageButtonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish(); // Close the signup activity on back button press
            }
        });
    }

    private void signUp() {
        String email = editTextEmail.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();
        String confirmPassword = editTextConfirmPassword.getText().toString().trim();

        // Validate input
        if (TextUtils.isEmpty(email)) {
            editTextEmail.setError("Email or Username is required");
            return;
        }

        if (TextUtils.isEmpty(password)) {
            editTextPassword.setError("Password is required");
            return;
        }

        if (!password.equals(confirmPassword)) {
            editTextConfirmPassword.setError("Passwords do not match");
            return;
        }

        // Check if the user already exists
        boolean isUserExists = databaseHelper.checkUserExists(email);
        if (isUserExists) {
            Toast.makeText(this, "User already exists. Please use a different email or username.", Toast.LENGTH_SHORT).show();
        } else {
            // Create new user
            boolean isInserted = databaseHelper.insertUsers(email, password);
            if (isInserted) {
                Toast.makeText(this, "Sign up successful. Please login.", Toast.LENGTH_SHORT).show();
                finish(); // Close the signup activity after successful signup
            } else {
                Toast.makeText(this, "Sign up failed. Please try again later.", Toast.LENGTH_SHORT).show();
            }
        }
    }
}