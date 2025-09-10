package com.tripbuddy.app.activities;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.tripbuddy.app.R;
import com.tripbuddy.app.database.DatabaseHelper;
import com.tripbuddy.app.models.Memory;
import com.tripbuddy.app.utils.AnimationUtil;
import com.tripbuddy.app.utils.MusicPlayerUtil;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class MemoryActivity extends AppCompatActivity {
    private static final String TAG = "MemoryActivity";
    private static final String PREFS_NAME = "TripBuddyPrefs";
    private static final String KEY_USER_ID = "userId";
    private static final String KEY_MUSIC_ENABLED = "musicEnabled";

    private EditText etLocation;
    private Spinner spinnerMood;
    private ImageView ivPreview;
    private TextView tvMusicStatus;
    private Button btnPlay, btnPause, btnStop, btnSaveMemory;
    
    private DatabaseHelper dbHelper;
    private int userId;
    private String currentImagePath = "beach"; // Default image
    private String currentMusicPath = "relax"; // Default music
    private boolean isMusicEnabled = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_memory);

        Log.d(TAG, "MemoryActivity started");

        // Initialize database
        dbHelper = new DatabaseHelper(this);

        // Get user preferences
        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        userId = prefs.getInt(KEY_USER_ID, -1);
        isMusicEnabled = prefs.getBoolean(KEY_MUSIC_ENABLED, true);

        // Initialize views
        initializeViews();
        setupMoodSpinner();
        setupMusicControls();
        
        // Apply animations
        AnimationUtil.fadeIn(findViewById(R.id.card_memory_details), 500);
        AnimationUtil.fadeIn(findViewById(R.id.card_music_controls), 800);
        AnimationUtil.fadeIn(findViewById(R.id.card_preview), 1000);
    }

    private void initializeViews() {
        etLocation = findViewById(R.id.et_location);
        spinnerMood = findViewById(R.id.spinner_mood);
        ivPreview = findViewById(R.id.iv_preview);
        tvMusicStatus = findViewById(R.id.tv_music_status);
        btnPlay = findViewById(R.id.btn_play);
        btnPause = findViewById(R.id.btn_pause);
        btnStop = findViewById(R.id.btn_stop);
        btnSaveMemory = findViewById(R.id.btn_save_memory);

        // Set default preview image (simulated)
        // In real app, this would load from resources
        ivPreview.setImageResource(android.R.drawable.ic_menu_gallery);
        
        btnSaveMemory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AnimationUtil.scaleAnimation(v);
                saveMemory();
            }
        });
    }

    private void setupMoodSpinner() {
        String[] moods = {"Happy", "Sad", "Excited", "Relaxed", "Adventurous"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, moods);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerMood.setAdapter(adapter);
    }

    private void setupMusicControls() {
        btnPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AnimationUtil.scaleAnimation(v);
                if (isMusicEnabled) {
                    playMusic();
                } else {
                    Toast.makeText(MemoryActivity.this, "Music is disabled in settings", Toast.LENGTH_SHORT).show();
                }
            }
        });

        btnPause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AnimationUtil.scaleAnimation(v);
                pauseMusic();
            }
        });

        btnStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AnimationUtil.scaleAnimation(v);
                stopMusic();
            }
        });

        updateMusicStatus();
    }

    private void playMusic() {
        // In real app, this would play actual music file from res/raw
        // For demo, we simulate with placeholder
        try {
            // MusicPlayerUtil.playMusic(this, R.raw.relax);
            tvMusicStatus.setText("Music: Playing - " + currentMusicPath + ".mp3");
            AnimationUtil.pulseAnimation(tvMusicStatus);
            Log.d(TAG, "Music playback started: " + currentMusicPath);
            Toast.makeText(this, "Playing relaxing music", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Log.e(TAG, "Error playing music", e);
            Toast.makeText(this, "Unable to play music", Toast.LENGTH_SHORT).show();
        }
    }

    private void pauseMusic() {
        MusicPlayerUtil.pauseMusic();
        tvMusicStatus.setText("Music: Paused");
        Log.d(TAG, "Music paused");
    }

    private void stopMusic() {
        MusicPlayerUtil.stopMusic();
        tvMusicStatus.setText("Music: Stopped");
        Log.d(TAG, "Music stopped");
    }

    private void updateMusicStatus() {
        if (MusicPlayerUtil.isPlaying()) {
            tvMusicStatus.setText("Music: Playing");
        } else if (MusicPlayerUtil.isPaused()) {
            tvMusicStatus.setText("Music: Paused");
        } else {
            tvMusicStatus.setText("Music: Stopped");
        }
    }

    private void saveMemory() {
        String location = etLocation.getText().toString().trim();
        String mood = spinnerMood.getSelectedItem().toString();
        
        if (location.isEmpty()) {
            Toast.makeText(this, "Please enter a location", Toast.LENGTH_SHORT).show();
            return;
        }

        // Create memory object
        Memory memory = new Memory();
        memory.setUserId(userId);
        memory.setLocation(location);
        memory.setMood(mood);
        memory.setImagePath(currentImagePath);
        memory.setMusicPath(currentMusicPath);
        memory.setDate(new SimpleDateFormat("yyyy-MM-dd", Locale.US).format(new Date()));

        // Save to database
        long memoryId = dbHelper.addMemory(memory);
        
        if (memoryId > 0) {
            Toast.makeText(this, "Memory saved successfully!", Toast.LENGTH_SHORT).show();
            Log.d(TAG, "Memory saved with ID: " + memoryId);
            
            // Clear form
            etLocation.setText("");
            spinnerMood.setSelection(0);
            
            // Stop music if playing
            stopMusic();
            
            finish();
            overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
        } else {
            Toast.makeText(this, "Failed to save memory", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        // Stop music when leaving activity
        stopMusic();
    }

    @Override
    protected void onStop() {
        super.onStop();
        // Ensure music is stopped
        MusicPlayerUtil.stopMusic();
    }
}