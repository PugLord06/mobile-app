package com.tripbuddy.app.activities;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.tripbuddy.app.R;
import com.tripbuddy.app.utils.AnimationUtil;
import com.tripbuddy.app.utils.MusicPlayerUtil;

public class FullScreenImageActivity extends AppCompatActivity {
    private static final String TAG = "FullScreenImageActivity";

    private ImageView ivFullScreen;
    private TextView tvLocation, tvDate, tvMood;
    private Button btnPlayMusic, btnClose;
    
    private String musicPath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_screen_image);

        Log.d(TAG, "FullScreenImageActivity started");

        // Get data from intent
        String imagePath = getIntent().getStringExtra("imagePath");
        String location = getIntent().getStringExtra("location");
        String date = getIntent().getStringExtra("date");
        String mood = getIntent().getStringExtra("mood");
        musicPath = getIntent().getStringExtra("musicPath");

        // Initialize views
        initializeViews();

        // Display data
        tvLocation.setText("Location: " + location);
        tvDate.setText("Date: " + date);
        tvMood.setText("Mood: " + mood);

        // Set image (simulated with placeholder)
        ivFullScreen.setImageResource(android.R.drawable.ic_menu_gallery);

        // Apply fade-in animation
        AnimationUtil.fadeIn(ivFullScreen, 500);
        AnimationUtil.fadeIn(findViewById(R.id.overlay_info), 800);
    }

    private void initializeViews() {
        ivFullScreen = findViewById(R.id.iv_full_screen);
        tvLocation = findViewById(R.id.tv_location);
        tvDate = findViewById(R.id.tv_date);
        tvMood = findViewById(R.id.tv_mood);
        btnPlayMusic = findViewById(R.id.btn_play_music);
        btnClose = findViewById(R.id.btn_close);

        btnPlayMusic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AnimationUtil.scaleAnimation(v);
                playMemoryMusic();
            }
        });

        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AnimationUtil.scaleAnimation(v);
                finish();
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            }
        });

        // Make fullscreen clickable to toggle info
        ivFullScreen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                View overlay = findViewById(R.id.overlay_info);
                if (overlay.getVisibility() == View.VISIBLE) {
                    AnimationUtil.fadeOut(overlay, 300);
                } else {
                    AnimationUtil.fadeIn(overlay, 300);
                }
            }
        });
    }

    private void playMemoryMusic() {
        try {
            // In real app, would play actual music file
            // MusicPlayerUtil.playMusic(this, R.raw.relax);
            Toast.makeText(this, "Playing memory music: " + musicPath + ".mp3", Toast.LENGTH_SHORT).show();
            Log.d(TAG, "Playing music: " + musicPath);
        } catch (Exception e) {
            Log.e(TAG, "Error playing music", e);
            Toast.makeText(this, "Unable to play music", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        // Stop music when leaving
        MusicPlayerUtil.stopMusic();
    }
}