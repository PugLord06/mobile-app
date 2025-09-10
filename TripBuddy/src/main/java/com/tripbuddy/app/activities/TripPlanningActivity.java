package com.tripbuddy.app.activities;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.tripbuddy.app.R;
import com.tripbuddy.app.adapters.ActivityAdapter;
import com.tripbuddy.app.database.DatabaseHelper;
import com.tripbuddy.app.models.Activity;
import com.tripbuddy.app.models.Trip;
import com.tripbuddy.app.utils.AnimationUtil;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class TripPlanningActivity extends AppCompatActivity implements ActivityAdapter.OnActivitySelectedListener {
    private static final String TAG = "TripPlanningActivity";
    private static final String PREFS_NAME = "TripBuddyPrefs";
    private static final String KEY_USER_ID = "userId";
    private static final String KEY_TRIP_COUNT = "tripCount";

    private EditText etDestination, etStartDate, etEndDate, etNotes;
    private Spinner spinnerCategory;
    private RecyclerView rvActivities;
    private TextView tvTotalCost;
    private Button btnAddCustomExpense, btnCalculateBudget;
    
    private DatabaseHelper dbHelper;
    private ActivityAdapter activityAdapter;
    private List<Activity> activities;
    private List<Trip.CustomExpense> customExpenses;
    private Trip currentTrip;
    private int userId;
    private double totalCost = 0.0;
    private Calendar startCalendar, endCalendar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trip_planning);

        Log.d(TAG, "TripPlanningActivity started");

        // Initialize database and data
        dbHelper = new DatabaseHelper(this);
        activities = new ArrayList<>();
        customExpenses = new ArrayList<>();
        currentTrip = new Trip();
        startCalendar = Calendar.getInstance();
        endCalendar = Calendar.getInstance();

        // Get user ID
        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        userId = prefs.getInt(KEY_USER_ID, -1);
        currentTrip.setUserId(userId);

        // Initialize views
        initializeViews();
        setupDatePickers();
        setupSpinner();
        loadPredefinedActivities();
        setupListeners();

        // Apply animations
        AnimationUtil.fadeIn(findViewById(R.id.card_trip_details), 500);
        AnimationUtil.fadeIn(findViewById(R.id.card_activities), 800);
        AnimationUtil.fadeIn(findViewById(R.id.card_budget), 1000);
    }

    private void initializeViews() {
        etDestination = findViewById(R.id.et_destination);
        etStartDate = findViewById(R.id.et_start_date);
        etEndDate = findViewById(R.id.et_end_date);
        etNotes = findViewById(R.id.et_notes);
        spinnerCategory = findViewById(R.id.spinner_category);
        rvActivities = findViewById(R.id.rv_activities);
        tvTotalCost = findViewById(R.id.tv_total_cost);
        btnAddCustomExpense = findViewById(R.id.btn_add_custom_expense);
        btnCalculateBudget = findViewById(R.id.btn_calculate_budget);

        // Setup RecyclerView
        rvActivities.setLayoutManager(new LinearLayoutManager(this));
        rvActivities.setHasFixedSize(true);
    }

    private void setupDatePickers() {
        DatePickerDialog.OnDateSetListener startDateListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                startCalendar.set(Calendar.YEAR, year);
                startCalendar.set(Calendar.MONTH, month);
                startCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateDateEditText(etStartDate, startCalendar);
            }
        };

        DatePickerDialog.OnDateSetListener endDateListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                endCalendar.set(Calendar.YEAR, year);
                endCalendar.set(Calendar.MONTH, month);
                endCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateDateEditText(etEndDate, endCalendar);
            }
        };

        etStartDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(TripPlanningActivity.this, startDateListener,
                        startCalendar.get(Calendar.YEAR),
                        startCalendar.get(Calendar.MONTH),
                        startCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        etEndDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(TripPlanningActivity.this, endDateListener,
                        endCalendar.get(Calendar.YEAR),
                        endCalendar.get(Calendar.MONTH),
                        endCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });
    }

    private void updateDateEditText(EditText editText, Calendar calendar) {
        String format = "MM/dd/yyyy";
        SimpleDateFormat sdf = new SimpleDateFormat(format, Locale.US);
        editText.setText(sdf.format(calendar.getTime()));
    }

    private void setupSpinner() {
        String[] categories = {"Transport", "Accommodation", "Food", "Entertainment", "Other"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, categories);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCategory.setAdapter(adapter);
    }

    private void loadPredefinedActivities() {
        activities = dbHelper.getAllActivities();
        activityAdapter = new ActivityAdapter(this, activities, this);
        rvActivities.setAdapter(activityAdapter);
    }

    private void setupListeners() {
        btnAddCustomExpense.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AnimationUtil.scaleAnimation(v);
                showAddCustomExpenseDialog();
            }
        });

        btnCalculateBudget.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AnimationUtil.scaleAnimation(v);
                calculateAndSaveTrip();
            }
        });

        // Add text watcher for real-time validation
        TextWatcher costValidator = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                updateTotalCost();
            }
        };
    }

    private void showAddCustomExpenseDialog() {
        View dialogView = getLayoutInflater().inflate(R.layout.dialog_add_expense, null);
        EditText etExpenseName = dialogView.findViewById(R.id.et_expense_name);
        EditText etExpenseCost = dialogView.findViewById(R.id.et_expense_cost);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Add Custom Expense")
                .setView(dialogView)
                .setPositiveButton("Add", (dialog, which) -> {
                    String name = etExpenseName.getText().toString().trim();
                    String costStr = etExpenseCost.getText().toString().trim();
                    
                    if (!name.isEmpty() && !costStr.isEmpty()) {
                        try {
                            double cost = Double.parseDouble(costStr);
                            if (cost < 0) {
                                Toast.makeText(this, "Cost cannot be negative", Toast.LENGTH_SHORT).show();
                                return;
                            }
                            Trip.CustomExpense expense = new Trip.CustomExpense(name, cost);
                            customExpenses.add(expense);
                            updateTotalCost();
                            Toast.makeText(this, "Custom expense added", Toast.LENGTH_SHORT).show();
                        } catch (NumberFormatException e) {
                            Toast.makeText(this, "Invalid cost format", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("Cancel", null)
                .show();
    }

    @Override
    public void onActivitySelected() {
        updateTotalCost();
    }

    private void updateTotalCost() {
        totalCost = 0.0;
        
        // Add selected activities cost
        for (Activity activity : activities) {
            if (activity.isSelected()) {
                totalCost += activity.getCost();
            }
        }
        
        // Add custom expenses
        for (Trip.CustomExpense expense : customExpenses) {
            totalCost += expense.getCost();
        }
        
        tvTotalCost.setText(String.format(Locale.US, "Total: $%.2f", totalCost));
        AnimationUtil.pulseAnimation(tvTotalCost);
    }

    private void calculateAndSaveTrip() {
        // Validate inputs
        String destination = etDestination.getText().toString().trim();
        String startDate = etStartDate.getText().toString().trim();
        String endDate = etEndDate.getText().toString().trim();
        
        if (destination.isEmpty() || startDate.isEmpty() || endDate.isEmpty()) {
            Toast.makeText(this, "Please fill all required fields", Toast.LENGTH_SHORT).show();
            return;
        }

        // Update trip object
        currentTrip.setDestination(destination);
        currentTrip.setStartDate(startDate);
        currentTrip.setEndDate(endDate);
        currentTrip.setNotes(etNotes.getText().toString());
        currentTrip.setActivities(activities);
        currentTrip.setCustomExpenses(customExpenses);
        
        // Check for loyalty discount
        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        int tripCount = dbHelper.getTripCount(userId);
        
        if (tripCount >= 3) {
            currentTrip.setDiscount(10.0); // 10% discount
            Log.d(TAG, "Loyalty discount applied: 10%");
        }
        
        currentTrip.setTotalCost(totalCost);
        
        // Navigate to budget summary
        Intent intent = new Intent(TripPlanningActivity.this, BudgetSummaryActivity.class);
        intent.putExtra("destination", destination);
        intent.putExtra("startDate", startDate);
        intent.putExtra("endDate", endDate);
        intent.putExtra("totalCost", totalCost);
        intent.putExtra("discount", currentTrip.getDiscount());
        intent.putExtra("tripCount", tripCount);
        
        // Pass activities and expenses as JSON strings for simplicity
        intent.putExtra("activities", activities.size());
        intent.putExtra("expenses", customExpenses.size());
        
        startActivity(intent);
        overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
        
        // Save trip to database
        long tripId = dbHelper.addTrip(currentTrip);
        if (tripId > 0) {
            // Update trip count
            SharedPreferences.Editor editor = prefs.edit();
            editor.putInt(KEY_TRIP_COUNT, tripCount + 1);
            editor.apply();
            
            Log.d(TAG, "Trip saved successfully with ID: " + tripId);
        }
    }
}