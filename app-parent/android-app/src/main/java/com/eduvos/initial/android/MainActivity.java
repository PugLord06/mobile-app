package com.eduvos.initial.android;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

import com.eduvos.initial.core.GreetingService;

public class MainActivity extends Activity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		GreetingService service = new GreetingService();
		TextView textView = new TextView(this);
		textView.setTextSize(20);
		textView.setText(service.greet("Android"));
		setContentView(textView);
	}
}
