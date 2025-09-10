package com.tripbuddy.app.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.tripbuddy.app.R;
import com.tripbuddy.app.adapters.GalleryAdapter;
import com.tripbuddy.app.database.DatabaseHelper;
import com.tripbuddy.app.models.Memory;
import com.tripbuddy.app.utils.AnimationUtil;

import java.util.ArrayList;
import java.util.List;

public class GalleryActivity extends AppCompatActivity implements GalleryAdapter.OnImageClickListener {
    private static final String TAG = "GalleryActivity";
    private static final String PREFS_NAME = "TripBuddyPrefs";
    private static final String KEY_USER_ID = "userId";

    private RecyclerView rvGallery;
    private TextView tvEmptyGallery;
    private Button btnSimulateUpload;
    
    private DatabaseHelper dbHelper;
    private GalleryAdapter galleryAdapter;
    private List<Memory> memories;
    private GestureDetector gestureDetector;
    private int userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery);

        Log.d(TAG, "GalleryActivity started");

        // Initialize database
        dbHelper = new DatabaseHelper(this);
        memories = new ArrayList<>();

        // Get user ID
        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        userId = prefs.getInt(KEY_USER_ID, -1);

        // Initialize views
        initializeViews();
        setupGestureDetector();
        loadGallery();

        // Apply animation
        AnimationUtil.fadeIn(rvGallery, 500);
    }

    private void initializeViews() {
        rvGallery = findViewById(R.id.rv_gallery);
        tvEmptyGallery = findViewById(R.id.tv_empty_gallery);
        btnSimulateUpload = findViewById(R.id.btn_simulate_upload);

        // Setup RecyclerView with GridLayoutManager
        GridLayoutManager layoutManager = new GridLayoutManager(this, 2); // 2 columns
        rvGallery.setLayoutManager(layoutManager);
        rvGallery.setItemAnimator(new DefaultItemAnimator());
        rvGallery.setHasFixedSize(true);

        btnSimulateUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AnimationUtil.scaleAnimation(v);
                simulateImageUpload();
            }
        });
    }

    private void setupGestureDetector() {
        gestureDetector = new GestureDetector(this, new GestureDetector.SimpleOnGestureListener() {
            @Override
            public boolean onSingleTapConfirmed(MotionEvent e) {
                return super.onSingleTapConfirmed(e);
            }
        });
    }

    private void loadGallery() {
        // Load memories from database
        memories = dbHelper.getAllMemories(userId);
        
        // Add some dummy memories for demo if empty
        if (memories.isEmpty()) {
            addDummyMemories();
            memories = dbHelper.getAllMemories(userId);
        }

        if (memories.isEmpty()) {
            tvEmptyGallery.setVisibility(View.VISIBLE);
            rvGallery.setVisibility(View.GONE);
        } else {
            tvEmptyGallery.setVisibility(View.GONE);
            rvGallery.setVisibility(View.VISIBLE);
            
            galleryAdapter = new GalleryAdapter(this, memories, this);
            rvGallery.setAdapter(galleryAdapter);
            
            // Apply scroll animation
            rvGallery.scheduleLayoutAnimation();
        }

        Log.d(TAG, "Loaded " + memories.size() + " memories");
    }

    private void addDummyMemories() {
        // Add some dummy memories for demo
        String[] locations = {"Beach Paradise", "Mountain View", "City Lights", "Forest Trail"};
        String[] moods = {"Happy", "Excited", "Relaxed", "Adventurous"};
        String[] images = {"beach", "mountain", "city", "forest"};
        
        for (int i = 0; i < 4; i++) {
            Memory memory = new Memory();
            memory.setUserId(userId);
            memory.setLocation(locations[i]);
            memory.setMood(moods[i]);
            memory.setImagePath(images[i]);
            memory.setMusicPath("relax");
            memory.setDate("2023-12-" + (i + 1));
            
            dbHelper.addMemory(memory);
        }
    }

    private void simulateImageUpload() {
        // Simulate adding a new memory
        Memory newMemory = new Memory();
        newMemory.setUserId(userId);
        newMemory.setLocation("New Adventure");
        newMemory.setMood("Excited");
        newMemory.setImagePath("adventure");
        newMemory.setMusicPath("upbeat");
        newMemory.setDate("2023-12-15");
        
        long id = dbHelper.addMemory(newMemory);
        if (id > 0) {
            Toast.makeText(this, "New memory added!", Toast.LENGTH_SHORT).show();
            loadGallery(); // Reload gallery
            AnimationUtil.pulseAnimation(btnSimulateUpload);
        }
    }

    @Override
    public void onImageClick(Memory memory, int position) {
        // Navigate to full screen view
        Intent intent = new Intent(GalleryActivity.this, FullScreenImageActivity.class);
        intent.putExtra("imagePath", memory.getImagePath());
        intent.putExtra("location", memory.getLocation());
        intent.putExtra("date", memory.getDate());
        intent.putExtra("mood", memory.getMood());
        intent.putExtra("musicPath", memory.getMusicPath());
        
        startActivity(intent);
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        
        Log.d(TAG, "Opening memory: " + memory.getLocation());
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return gestureDetector.onTouchEvent(event) || super.onTouchEvent(event);
    }
}