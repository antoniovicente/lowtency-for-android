package com.antoniovm.lowtency;

import java.util.regex.Pattern;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.antoniovm.lowtency.audio.AudioInputManager;
import com.antoniovm.lowtency.core.StreamManager;
import com.antoniovm.lowtency.net.StreamReceiver;

public class MainActivity extends Activity {
	
	private StreamManager streamManager;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		streamManager = new StreamManager();

		final TextView ip = (TextView) findViewById(R.id.tvIp);

		final String ipregex = "^([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\."
				+ "([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\."
				+ "([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\."
				+ "([01]?\\d\\d?|2[0-4]\\d|25[0-5])$";
		;

		streamManager = new StreamManager();
		streamManager.startThread();

		final AudioInputManager audioInputManager = new AudioInputManager();

		Button record = (Button) findViewById(R.id.bReadFromMic);
		record.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				if (!streamManager.isStreaming()) {
					streamManager.startStreaming();
				} else {
					streamManager.stopStreaming();
				}

			};
		});

		Button start = (Button) findViewById(R.id.bStart);
		start.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				if (Pattern.matches(ipregex, ip.getText())) {
					Intent intent = new Intent(MainActivity.this, StreamReceiver.class);
					Bundle bundle = new Bundle();
					bundle.putString("host", ip.getText().toString());
					intent.putExtras(bundle);
					startService(intent);
				}else{
					Toast.makeText(MainActivity.this , "Invalid host", Toast.LENGTH_SHORT).show();
				}
				
			}
		});

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Activity#onResume()
	 */
	@Override
	protected void onResume() {
		super.onResume();

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}
