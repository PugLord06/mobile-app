package com.tripbuddy.app.activities;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.tripbuddy.app.R;
import com.tripbuddy.app.utils.AnimationUtil;

import java.util.Locale;

public class BudgetSummaryActivity extends AppCompatActivity {
    private static final String TAG = "BudgetSummaryActivity";

    private TextView tvDestination, tvDates, tvSubtotal, tvDiscount, tvTotal, tvTripCount;
    private CardView cardSubtotal, cardDiscount, cardTotal;
    private Button btnDone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_budget_summary);

        Log.d(TAG, "BudgetSummaryActivity started");

        // Initialize views
        initializeViews();

        // Get data from intent
        String destination = getIntent().getStringExtra("destination");
        String startDate = getIntent().getStringExtra("startDate");
        String endDate = getIntent().getStringExtra("endDate");
        double totalCost = getIntent().getDoubleExtra("totalCost", 0.0);
        double discount = getIntent().getDoubleExtra("discount", 0.0);
        int tripCount = getIntent().getIntExtra("tripCount", 0);

        // Display data
        tvDestination.setText("Destination: " + destination);
        tvDates.setText("Dates: " + startDate + " - " + endDate);
        tvSubtotal.setText(String.format(Locale.US, "$%.2f", totalCost));
        
        if (discount > 0) {
            double discountAmount = totalCost * discount / 100;
            tvDiscount.setText(String.format(Locale.US, "%.0f%% ($%.2f saved)", discount, discountAmount));
            cardDiscount.setVisibility(View.VISIBLE);
        } else {
            tvDiscount.setText("No discount");
            cardDiscount.setVisibility(View.GONE);
        }
        
        double finalTotal = totalCost - (totalCost * discount / 100);
        tvTotal.setText(String.format(Locale.US, "$%.2f", finalTotal));
        
        if (tripCount >= 3) {
            tvTripCount.setText("Loyalty Member! (" + tripCount + " trips)");
            tvTripCount.setVisibility(View.VISIBLE);
        } else {
            tvTripCount.setVisibility(View.GONE);
        }

        // Apply animations
        AnimationUtil.fadeIn(findViewById(R.id.layout_summary), 300);
        AnimationUtil.fadeIn(cardSubtotal, 600);
        AnimationUtil.fadeIn(cardDiscount, 900);
        AnimationUtil.fadeIn(cardTotal, 1200);
        AnimationUtil.scaleAnimation(cardTotal);

        // Set button listener
        btnDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AnimationUtil.scaleAnimation(v);
                finish();
                overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
            }
        });
    }

    private void initializeViews() {
        tvDestination = findViewById(R.id.tv_destination);
        tvDates = findViewById(R.id.tv_dates);
        tvSubtotal = findViewById(R.id.tv_subtotal);
        tvDiscount = findViewById(R.id.tv_discount);
        tvTotal = findViewById(R.id.tv_total);
        tvTripCount = findViewById(R.id.tv_trip_count);
        cardSubtotal = findViewById(R.id.card_subtotal);
        cardDiscount = findViewById(R.id.card_discount);
        cardTotal = findViewById(R.id.card_total);
        btnDone = findViewById(R.id.btn_done);
    }
}