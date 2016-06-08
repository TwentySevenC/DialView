package com.android.leo.dialview;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class DialViewActivity extends AppCompatActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_dial_view);

		final DialView dialView = (DialView)findViewById(R.id.dial_view);
		final EditText progress = (EditText)findViewById(R.id.progress);
		Button start = (Button)findViewById(R.id.start);

		start.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				float progressValue = Float.valueOf(progress.getText().toString());
				dialView.setProgressWithAnimation(progressValue);
			}
		});
	}
}
