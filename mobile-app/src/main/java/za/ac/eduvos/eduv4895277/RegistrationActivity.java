package za.ac.eduvos.eduv4895277;

import android.app.Activity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class RegistrationActivity extends Activity {
    private EditText nameInput;
    private Button continueBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        setTitle(getString(R.string.registration));

        nameInput = findViewById(R.id.input_name);
        continueBtn = findViewById(R.id.btn_continue);
        continueBtn.setEnabled(false);

        nameInput.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override public void onTextChanged(CharSequence s, int start, int before, int count) {}
            @Override public void afterTextChanged(Editable s) {
                continueBtn.setEnabled(s != null && s.toString().trim().length() > 0);
            }
        });

        continueBtn.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                String name = nameInput.getText() == null ? "" : nameInput.getText().toString().trim();
                Preferences.setUserName(RegistrationActivity.this, name);
                finish();
            }
        });
    }
} 