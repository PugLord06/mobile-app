package com.tripbuddy.app.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.tripbuddy.app.R;
import com.tripbuddy.app.database.DatabaseHelper;
import com.tripbuddy.app.utils.AnimationUtil;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    private static final String PREFS_NAME = "TripBuddyPrefs";
    private static final String KEY_IS_LOGGED_IN = "isLoggedIn";
    private static final String KEY_USER_ID = "userId";
    private static final String KEY_USERNAME = "username";

    private TextView tvWelcome, tvTesScore;
    private Button btnPlanTrip, btnCreateMemory, btnViewGallery, btnSettings, btnLogout;
    private ImageView ivLogo;
    private DatabaseHelper dbHelper;
    private int userId;
    private String username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Log.d(TAG, "MainActivity started");

        // Initialize database
        dbHelper = new DatabaseHelper(this);

        // Get user info from preferences
        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        userId = prefs.getInt(KEY_USER_ID, -1);
        username = prefs.getString(KEY_USERNAME, "User");

        // Initialize views
        tvWelcome = findViewById(R.id.tv_welcome);
        tvTesScore = findViewById(R.id.tv_tes_score);
        btnPlanTrip = findViewById(R.id.btn_plan_trip);
        btnCreateMemory = findViewById(R.id.btn_create_memory);
        btnViewGallery = findViewById(R.id.btn_view_gallery);
        btnSettings = findViewById(R.id.btn_settings);
        btnLogout = findViewById(R.id.btn_logout);
        ivLogo = findViewById(R.id.iv_logo);

        // Set welcome message
        tvWelcome.setText("Welcome " + username + " to TripBuddy Dashboard");

        // Apply animations
        AnimationUtil.fadeIn(ivLogo, 500);
        AnimationUtil.fadeIn(tvWelcome, 800);
        AnimationUtil.fadeIn(btnPlanTrip, 1000);
        AnimationUtil.fadeIn(btnCreateMemory, 1200);
        AnimationUtil.fadeIn(btnViewGallery, 1400);
        AnimationUtil.fadeIn(btnSettings, 1600);
        AnimationUtil.fadeIn(btnLogout, 1800);

        // Set button click listeners
        btnPlanTrip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AnimationUtil.scaleAnimation(v);
                Intent intent = new Intent(MainActivity.this, TripPlanningActivity.class);
                startActivity(intent);
                overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
            }
        });

        btnCreateMemory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AnimationUtil.scaleAnimation(v);
                Intent intent = new Intent(MainActivity.this, MemoryActivity.class);
                startActivity(intent);
                overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
            }
        });

        btnViewGallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AnimationUtil.scaleAnimation(v);
                Intent intent = new Intent(MainActivity.this, GalleryActivity.class);
                startActivity(intent);
                overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
            }
        });

        btnSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AnimationUtil.scaleAnimation(v);
                Intent intent = new Intent(MainActivity.this, SettingsActivity.class);
                startActivity(intent);
                overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
            }
        });

        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AnimationUtil.scaleAnimation(v);
                handleLogout();
            }
        });

        // Calculate and display TES score
        calculateTesScore();
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Recalculate TES score when returning to dashboard
        calculateTesScore();
    }

    private void calculateTesScore() {
        // TES: Dummy calculation (memories *3 + gallery *1 + loyalty *4)
        int memoriesCount = dbHelper.getAllMemories(userId).size();
        int galleryCount = memoriesCount; // Same as memories for this demo
        int tripCount = dbHelper.getTripCount(userId);
        int loyaltyPoints = tripCount * 10; // 10 points per trip

        int tesScore = (memoriesCount * 3) + (galleryCount * 1) + (loyaltyPoints * 4);
        
        tvTesScore.setText("TES Score: " + tesScore);
        AnimationUtil.fadeIn(tvTesScore, 500);
        
        Log.d(TAG, "TES Score calculated: " + tesScore);
    }

    private void handleLogout() {
        // Clear login preferences
        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putBoolean(KEY_IS_LOGGED_IN, false);
        editor.putInt(KEY_USER_ID, -1);
        editor.putString(KEY_USERNAME, "");
        editor.apply();

        // Navigate to login
        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        finish();

        Toast.makeText(this, "Logged out successfully", Toast.LENGTH_SHORT).show();
    }
}