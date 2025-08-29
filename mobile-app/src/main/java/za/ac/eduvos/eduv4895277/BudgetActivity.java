package za.ac.eduvos.eduv4895277;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.LinkedHashMap;
import java.util.Map;

public class BudgetActivity extends Activity {
    private EditText destinationInput;
    private EditText notesInput;
    private EditText customInput;
    private EditText mealsInput;
    private Spinner activitySpinner;
    private TextView subtotalText;
    private Button saveBtn;
    private Button confirmBtn;

    private final Map<String, Double> activityCosts = new LinkedHashMap<>();
    private double subtotal = 0.0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_budget);
        setTitle(getString(R.string.budget));

        destinationInput = findViewById(R.id.input_destination);
        notesInput = findViewById(R.id.input_notes);
        customInput = findViewById(R.id.input_custom);
        mealsInput = findViewById(R.id.input_meals);
        activitySpinner = findViewById(R.id.spinner_activity);
        subtotalText = findViewById(R.id.budget_subtotal);
        saveBtn = findViewById(R.id.btn_save_trip);
        confirmBtn = findViewById(R.id.btn_confirm_trip);

        seedActivities();
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, activityCosts.keySet().toArray(new String[0]));
        activitySpinner.setAdapter(adapter);

        TextWatcher watcher = new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override public void onTextChanged(CharSequence s, int start, int before, int count) { recalc(); }
            @Override public void afterTextChanged(Editable s) { recalc(); }
        };
        customInput.addTextChangedListener(watcher);
        mealsInput.addTextChangedListener(watcher);

        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                recalc();
                TripStore.addTrip(BudgetActivity.this);
                // Persist trip to SQLite and preload later
                TripModel t = new TripModel();
                t.destination = destinationInput.getText() == null ? null : destinationInput.getText().toString();
                t.notes = notesInput.getText() == null ? null : notesInput.getText().toString();
                String selected = (String) activitySpinner.getSelectedItem();
                if (selected != null) t.activities.add(selected);
                t.customExpense = safeDouble(customInput.getText());
                t.mealsExpense = safeDouble(mealsInput.getText());
                new DatabaseHelper(BudgetActivity.this).insertTrip(t);
            }
        });

        confirmBtn.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                double custom = safeDouble(customInput.getText());
                double meals = safeDouble(mealsInput.getText());
                String selected = (String) activitySpinner.getSelectedItem();
                double activityCost = selected == null ? 0 : activityCosts.get(selected);

                double baseSubtotal = custom + meals + activityCost;
                double discount = 0.0;
                if (TripStore.getTripCount(BudgetActivity.this) >= 3) {
                    discount = baseSubtotal * 0.10; // 10%
                }
                double total = baseSubtotal - discount;

                Intent i = new Intent(BudgetActivity.this, BudgetSummaryActivity.class);
                i.putExtra("subtotal", baseSubtotal);
                i.putExtra("discount", discount);
                i.putExtra("total", total);
                startActivity(i);
            }
        });

        // Preload last trip if available
        TripModel last = new DatabaseHelper(this).getLastTrip();
        if (last != null) {
            if (last.destination != null) destinationInput.setText(last.destination);
            if (last.notes != null) notesInput.setText(last.notes);
            if (!last.activities.isEmpty()) {
                String act = last.activities.get(0);
                int pos = ((ArrayAdapter<String>) activitySpinner.getAdapter()).getPosition(act);
                if (pos >= 0) activitySpinner.setSelection(pos);
            }
            if (last.customExpense > 0) customInput.setText(String.valueOf(last.customExpense));
            if (last.mealsExpense > 0) mealsInput.setText(String.valueOf(last.mealsExpense));
        }

        recalc();
    }

    private void seedActivities() {
        activityCosts.put("Sightseeing", 1700.0);
        activityCosts.put("Hiking", 2000.0);
        activityCosts.put("Beach Day", 2600.0);
        activityCosts.put("Museum Visit", 1500.0);
        activityCosts.put("Dining", 1200.0);
    }

    private double safeDouble(Editable text) {
        if (text == null) return 0.0;
        try {
            String s = text.toString().trim();
            if (s.length() == 0) return 0.0;
            double v = Double.parseDouble(s);
            if (v < 0) return 0.0;
            return v;
        } catch (Exception e) {
            return 0.0;
        }
    }

    private void recalc() {
        String selected = (String) activitySpinner.getSelectedItem();
        double activityCost = selected == null ? 0 : activityCosts.get(selected);
        double custom = safeDouble(customInput.getText());
        double meals = safeDouble(mealsInput.getText());
        subtotal = activityCost + custom + meals;
        subtotalText.setText(getString(R.string.subtotal_fmt, String.format("R%.2f", subtotal)));
    }
} 