package za.ac.eduvos.eduv4895277;

import android.app.Activity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class LoginActivity extends Activity {
    private EditText usernameInput;
    private Button loginBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        ThemeHelper.apply(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        setTitle("Trip Buddy");

        usernameInput = findViewById(R.id.input_username);
        loginBtn = findViewById(R.id.btn_login);
        loginBtn.setEnabled(false);

        usernameInput.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override public void onTextChanged(CharSequence s, int start, int before, int count) {}
            @Override public void afterTextChanged(Editable s) {
                loginBtn.setEnabled(s != null && s.toString().trim().length() > 0);
            }
        });

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                String name = usernameInput.getText() == null ? "" : usernameInput.getText().toString().trim();
                Preferences.setUserName(LoginActivity.this, name);
                Preferences.setLoggedIn(LoginActivity.this, true);
                finish();
            }
        });
    }
} 