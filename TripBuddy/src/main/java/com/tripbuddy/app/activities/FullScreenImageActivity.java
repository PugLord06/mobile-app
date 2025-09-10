package com.tripbuddy.app.activities;

import android.content.SharedPreferences;
import android.media.AudioManager;
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
    private static final String PREFS_NAME = "TripBuddyPrefs";
    private static final String KEY_MUSIC_ENABLED = "musicEnabled";

    private ImageView ivFullScreen;
    private TextView tvLocation, tvDate, tvMood;
    private Button btnPlayMusic, btnClose;
    
    private String imagePath;
    private String musicPath;
    private boolean isMusicPlaying = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_screen_image);

        // Route hardware volume buttons to control media volume
        setVolumeControlStream(AudioManager.STREAM_MUSIC);

        Log.d(TAG, "FullScreenImageActivity started");

        // Get data from intent
        imagePath = getIntent().getStringExtra("imagePath");
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

        // Set image from local drawable based on key
        ivFullScreen.setImageResource(getDrawableForKey(imagePath));

        // Apply fade-in animation
        AnimationUtil.fadeIn(ivFullScreen, 500);
        AnimationUtil.fadeIn(findViewById(R.id.overlay_info), 800);

        // Auto-play music if enabled
        autoPlayIfEnabled();
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
                if (isMusicPlaying) {
                    MusicPlayerUtil.pauseMusic();
                    isMusicPlaying = false;
                    btnPlayMusic.setText(R.string.play_music);
                } else {
                    playMemoryMusic();
                }
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

    private void autoPlayIfEnabled() {
        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        boolean isMusicEnabled = prefs.getBoolean(KEY_MUSIC_ENABLED, true);
        if (isMusicEnabled) {
            playMemoryMusic();
        } else {
            btnPlayMusic.setText(R.string.play_music);
        }
    }

    private void playMemoryMusic() {
        // Respect user preference for music
        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        boolean isMusicEnabled = prefs.getBoolean(KEY_MUSIC_ENABLED, true);
        if (!isMusicEnabled) {
            Toast.makeText(this, "Music is disabled in Settings", Toast.LENGTH_SHORT).show();
            return;
        }

        // Warn if volume is zero
        AudioManager am = (AudioManager) getSystemService(AUDIO_SERVICE);
        if (am != null && am.getStreamVolume(AudioManager.STREAM_MUSIC) == 0) {
            Toast.makeText(this, "Media volume is 0 — increase volume to hear audio", Toast.LENGTH_LONG).show();
        }

        try {
            int resId = getRawForKey(musicPath);
            if (resId != 0) {
                MusicPlayerUtil.playMusic(this, resId);
                isMusicPlaying = true;
                btnPlayMusic.setText(R.string.pause_music);
                Toast.makeText(this, "Playing: " + musicPath + ".mp3", Toast.LENGTH_SHORT).show();
                Log.d(TAG, "Playing music: " + musicPath);
            } else {
                Toast.makeText(this, "Music not found: " + musicPath, Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            Log.e(TAG, "Error playing music", e);
            Toast.makeText(this, "Unable to play music", Toast.LENGTH_SHORT).show();
        }
    }

    private int getDrawableForKey(String key) {
        if (key == null) return R.drawable.adventure;
        switch (key) {
            case "beach":
                return R.drawable.beach;
            case "mountain":
                return R.drawable.mountain;
            case "city":
                return R.drawable.city;
            case "forest":
                return R.drawable.forest;
            case "adventure":
                return R.drawable.adventure;
            default:
                return R.drawable.adventure;
        }
    }

    private int getRawForKey(String key) {
        if (key == null) return 0;
        switch (key) {
            case "relax":
                return R.raw.relax;
            case "upbeat":
                return R.raw.upbeat;
            case "ambient":
                return R.raw.ambient;
            default:
                return 0;
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        // Stop music when leaving
        MusicPlayerUtil.stopMusic();
        isMusicPlaying = false;
        btnPlayMusic.setText(R.string.play_music);
    }
}