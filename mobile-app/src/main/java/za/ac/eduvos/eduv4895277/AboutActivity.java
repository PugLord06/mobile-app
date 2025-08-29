package za.ac.eduvos.eduv4895277;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

public class AboutActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        setTitle(getString(R.string.about));

        TextView tv = findViewById(R.id.about_text);
        tv.setText(getString(R.string.app_name) + "\n" + getString(R.string.author));
    }
} 