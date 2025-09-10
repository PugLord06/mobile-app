package com.tripbuddy.app.activities;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import com.tripbuddy.app.R;
import com.tripbuddy.app.utils.AnimationUtil;

public class SettingsActivity extends AppCompatActivity {
    private static final String TAG = "SettingsActivity";
    private static final String PREFS_NAME = "TripBuddyPrefs";
    private static final String KEY_DARK_MODE = "darkMode";
    private static final String KEY_MUSIC_ENABLED = "musicEnabled";
    private static final String KEY_LANGUAGE = "language";
    private static final String KEY_USERNAME = "username";

    private Switch switchDarkMode, switchMusic;
    private Spinner spinnerLanguage;
    private TextView tvUsername;
    
    private SharedPreferences prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        Log.d(TAG, "SettingsActivity started");

        prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);

        // Initialize views
        initializeViews();
        loadSettings();
        setupListeners();

        // Apply animations
        AnimationUtil.fadeIn(findViewById(R.id.card_appearance), 500);
        AnimationUtil.fadeIn(findViewById(R.id.card_preferences), 800);
        AnimationUtil.fadeIn(findViewById(R.id.card_account), 1000);
    }

    private void initializeViews() {
        switchDarkMode = findViewById(R.id.switch_dark_mode);
        switchMusic = findViewById(R.id.switch_music);
        spinnerLanguage = findViewById(R.id.spinner_language);
        tvUsername = findViewById(R.id.tv_username);

        // Setup language spinner
        String[] languages = {"English", "Spanish", "French", "German", "Chinese"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, languages);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerLanguage.setAdapter(adapter);
    }

    private void loadSettings() {
        // Load saved preferences
        boolean isDarkMode = prefs.getBoolean(KEY_DARK_MODE, false);
        boolean isMusicEnabled = prefs.getBoolean(KEY_MUSIC_ENABLED, true);
        String language = prefs.getString(KEY_LANGUAGE, "English");
        String username = prefs.getString(KEY_USERNAME, "User");

        // Set current values
        switchDarkMode.setChecked(isDarkMode);
        switchMusic.setChecked(isMusicEnabled);
        
        // Set language spinner
        ArrayAdapter<String> adapter = (ArrayAdapter<String>) spinnerLanguage.getAdapter();
        int position = adapter.getPosition(language);
        spinnerLanguage.setSelection(position);
        
        tvUsername.setText("Logged in as: " + username);
    }

    private void setupListeners() {
        switchDarkMode.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                SharedPreferences.Editor editor = prefs.edit();
                editor.putBoolean(KEY_DARK_MODE, isChecked);
                editor.apply();

                // Apply theme change
                if (isChecked) {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                } else {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                }

                AnimationUtil.pulseAnimation(buttonView);
                Toast.makeText(SettingsActivity.this, 
                    isChecked ? "Dark mode enabled" : "Light mode enabled", 
                    Toast.LENGTH_SHORT).show();

                // Recreate activity to apply theme
                recreate();
            }
        });

        switchMusic.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                SharedPreferences.Editor editor = prefs.edit();
                editor.putBoolean(KEY_MUSIC_ENABLED, isChecked);
                editor.apply();

                AnimationUtil.pulseAnimation(buttonView);
                Toast.makeText(SettingsActivity.this, 
                    isChecked ? "Music enabled" : "Music disabled", 
                    Toast.LENGTH_SHORT).show();

                Log.d(TAG, "Music setting changed to: " + isChecked);
            }
        });

        spinnerLanguage.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedLanguage = parent.getItemAtPosition(position).toString();
                String currentLanguage = prefs.getString(KEY_LANGUAGE, "English");
                
                if (!selectedLanguage.equals(currentLanguage)) {
                    SharedPreferences.Editor editor = prefs.edit();
                    editor.putString(KEY_LANGUAGE, selectedLanguage);
                    editor.apply();

                    Toast.makeText(SettingsActivity.this, 
                        "Language changed to " + selectedLanguage, 
                        Toast.LENGTH_SHORT).show();

                    Log.d(TAG, "Language changed to: " + selectedLanguage);
                    
                    // In a real app, would apply language change here
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Do nothing
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
    }
}