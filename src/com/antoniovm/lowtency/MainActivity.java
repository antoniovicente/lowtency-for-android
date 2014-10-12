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
import com.antoniovm.lowtency.core.IncomingStream;
import com.antoniovm.lowtency.core.OutcomingStream;
import com.antoniovm.lowtency.net.NetworkClient;

public class MainActivity extends Activity {
	
	private OutcomingStream outcomingStream;
	private IncomingStream incomingStream;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		outcomingStream = new OutcomingStream();
		outcomingStream.startThread();
		incomingStream = new IncomingStream("localhost", 3333);

		final TextView ip = (TextView) findViewById(R.id.tvIp);

		final String ipregex = "^([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\."
				+ "([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\."
				+ "([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\."
				+ "([01]?\\d\\d?|2[0-4]\\d|25[0-5])$";
		;



		final AudioInputManager audioInputManager = new AudioInputManager();

		Button record = (Button) findViewById(R.id.bReadFromMic);
		record.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				if (!outcomingStream.isStreaming()) {
					outcomingStream.startStreaming();
					incomingStream.startThread();
				} else {
					outcomingStream.stopStreaming();
				}

			};
		});

		Button start = (Button) findViewById(R.id.bStart);
		start.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				if (Pattern.matches(ipregex, ip.getText())) {
					Intent intent = new Intent(MainActivity.this, NetworkClient.class);
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
