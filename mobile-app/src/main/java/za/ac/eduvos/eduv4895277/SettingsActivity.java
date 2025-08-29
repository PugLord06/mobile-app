package za.ac.eduvos.eduv4895277;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Switch;

public class SettingsActivity extends Activity {
    private Switch darkSwitch;
    private Switch musicSwitch;
    private Button logoutBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        ThemeHelper.apply(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        setTitle("Trip Buddy");

        darkSwitch = findViewById(R.id.switch_dark);
        musicSwitch = findViewById(R.id.switch_music);
        logoutBtn = findViewById(R.id.btn_logout);

        darkSwitch.setChecked(Preferences.isDarkMode(this));
        musicSwitch.setChecked(Preferences.isMusicEnabled(this));

        darkSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            Preferences.setDarkMode(SettingsActivity.this, isChecked);
            recreate();
        });
        musicSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            Preferences.setMusicEnabled(SettingsActivity.this, isChecked);
        });
        logoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                Preferences.setLoggedIn(SettingsActivity.this, false);
                finish();
            }
        });
    }
} 