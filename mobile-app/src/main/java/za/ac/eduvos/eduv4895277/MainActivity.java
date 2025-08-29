package za.ac.eduvos.eduv4895277;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TextView welcome = findViewById(R.id.welcome_text);
        String name = Preferences.getUserName(this);
        if (name != null) {
            welcome.setText(getString(R.string.welcome_name, name));
        } else {
            welcome.setText(getString(R.string.welcome));
        }

        Button btnMem = findViewById(R.id.btn_memories);
        Button btnGal = findViewById(R.id.btn_gallery);
        Button btnBud = findViewById(R.id.btn_budget);
        Button btnAbout = findViewById(R.id.btn_about);
        Button btnSettings = findViewById(R.id.btn_settings);

        btnMem.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, MemoriesActivity.class));
            }
        });
        btnGal.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, GalleryActivity.class));
            }
        });
        btnBud.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, BudgetActivity.class));
            }
        });
        btnAbout.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, AboutActivity.class));
            }
        });
        btnSettings.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, SettingsActivity.class));
            }
        });
    }
} 