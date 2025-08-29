package za.ac.eduvos.eduv4895277;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

import java.text.NumberFormat;

public class BudgetSummaryActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_budget_summary);
        setTitle(getString(R.string.budget_summary));

        double subtotal = getIntent().getDoubleExtra("subtotal", 0);
        double discount = getIntent().getDoubleExtra("discount", 0);
        double total = getIntent().getDoubleExtra("total", 0);

        NumberFormat currency = NumberFormat.getCurrencyInstance();
        ((TextView) findViewById(R.id.summary_subtotal)).setText(getString(R.string.subtotal_fmt, currency.format(subtotal)));
        ((TextView) findViewById(R.id.summary_discount)).setText(getString(R.string.discount_fmt, currency.format(discount)));
        ((TextView) findViewById(R.id.summary_total)).setText(getString(R.string.final_total_fmt, currency.format(total)));
    }
} 