package com.tripbuddy.app.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.tripbuddy.app.R;
import com.tripbuddy.app.database.DatabaseHelper;
import com.tripbuddy.app.models.User;
import com.tripbuddy.app.utils.AnimationUtil;

public class LoginActivity extends AppCompatActivity {
    private static final String TAG = "LoginActivity";
    private static final String PREFS_NAME = "TripBuddyPrefs";
    private static final String KEY_IS_LOGGED_IN = "isLoggedIn";
    private static final String KEY_USER_ID = "userId";
    private static final String KEY_USERNAME = "username";

    private EditText etUsername, etEmail, etPassword;
    private Button btnLogin, btnRegister;
    private TextView tvToggle;
    private DatabaseHelper dbHelper;
    private boolean isLoginMode = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Log.d(TAG, "LoginActivity started");

        // Initialize database
        dbHelper = new DatabaseHelper(this);

        // Initialize views
        etUsername = findViewById(R.id.et_username);
        etEmail = findViewById(R.id.et_email);
        etPassword = findViewById(R.id.et_password);
        btnLogin = findViewById(R.id.btn_login);
        btnRegister = findViewById(R.id.btn_register);
        tvToggle = findViewById(R.id.tv_toggle);

        // Set initial state
        etEmail.setVisibility(View.GONE);
        btnRegister.setVisibility(View.GONE);

        // Set click listeners
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AnimationUtil.scaleAnimation(v);
                handleLogin();
            }
        });

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AnimationUtil.scaleAnimation(v);
                handleRegister();
            }
        });

        tvToggle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggleMode();
            }
        });
    }

    private void toggleMode() {
        isLoginMode = !isLoginMode;
        if (isLoginMode) {
            // Switch to login mode
            etEmail.setVisibility(View.GONE);
            btnLogin.setVisibility(View.VISIBLE);
            btnRegister.setVisibility(View.GONE);
            tvToggle.setText("Don't have an account? Register");
            AnimationUtil.fadeIn(btnLogin, 300);
        } else {
            // Switch to register mode
            etEmail.setVisibility(View.VISIBLE);
            btnLogin.setVisibility(View.GONE);
            btnRegister.setVisibility(View.VISIBLE);
            tvToggle.setText("Already have an account? Login");
            AnimationUtil.fadeIn(etEmail, 300);
            AnimationUtil.fadeIn(btnRegister, 300);
        }
    }

    private void handleLogin() {
        String username = etUsername.getText().toString().trim();
        String password = etPassword.getText().toString().trim();

        if (TextUtils.isEmpty(username) || TextUtils.isEmpty(password)) {
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        // Check credentials in database
        User user = dbHelper.getUser(username, password);
        if (user != null) {
            // Save login status
            SharedPreferences prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
            SharedPreferences.Editor editor = prefs.edit();
            editor.putBoolean(KEY_IS_LOGGED_IN, true);
            editor.putInt(KEY_USER_ID, user.getId());
            editor.putString(KEY_USERNAME, user.getUsername());
            editor.apply();

            Log.d(TAG, "Login successful for user: " + username);

            // Navigate to main activity
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(intent);
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            finish();
        } else {
            // For demo purposes, create a default user if login fails
            Toast.makeText(this, "Creating demo user...", Toast.LENGTH_SHORT).show();
            User demoUser = new User(username, username + "@tripbuddy.com", password);
            long userId = dbHelper.addUser(demoUser);
            
            if (userId > 0) {
                // Save login status
                SharedPreferences prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
                SharedPreferences.Editor editor = prefs.edit();
                editor.putBoolean(KEY_IS_LOGGED_IN, true);
                editor.putInt(KEY_USER_ID, (int) userId);
                editor.putString(KEY_USERNAME, username);
                editor.apply();

                // Navigate to main activity
                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                startActivity(intent);
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                finish();
            } else {
                Toast.makeText(this, "Login failed. Please try again.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void handleRegister() {
        String username = etUsername.getText().toString().trim();
        String email = etEmail.getText().toString().trim();
        String password = etPassword.getText().toString().trim();

        if (TextUtils.isEmpty(username) || TextUtils.isEmpty(email) || TextUtils.isEmpty(password)) {
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        // Create new user
        User newUser = new User(username, email, password);
        long userId = dbHelper.addUser(newUser);

        if (userId > 0) {
            Toast.makeText(this, "Registration successful! Please login.", Toast.LENGTH_SHORT).show();
            toggleMode();
            etUsername.setText("");
            etEmail.setText("");
            etPassword.setText("");
        } else {
            Toast.makeText(this, "Registration failed. Username may already exist.", Toast.LENGTH_SHORT).show();
        }
    }
}