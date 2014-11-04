package com.antoniovm.lowtency.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Button;

import com.antoniovm.lowtency.R;
import com.antoniovm.lowtency.core.IncomingStream;

public class ActivityClientStreaming extends Activity {
	
	private IncomingStream incomingStream;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.a_client_streaming);

		init();
	}

	/**
	 * 
	 */
	private void init() {
		initData();
		initViews();

	}

	/**
	 * 
	 */
	private void initData() {
		Intent incomingIntent = getIntent();
		String ip = incomingIntent.getStringExtra("IP");
		int port = incomingIntent.getIntExtra("PORT", 0);

		this.incomingStream = new IncomingStream(ip, port);
		incomingStream.startThread();
	}

	/**
	 * 
	 */
	private void initViews() {

		Button bDisconnect = (Button) findViewById(R.id.bDisconnect);

		bDisconnect.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				startActivity(new Intent(ActivityClientStreaming.this, ActivityMain.class));
				finish();
			}
		});
	}


	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Activity#onBackPressed()
	 */
	@Override
	public void onBackPressed() {
		// Avoid the activity to be destroyed
		moveTaskToBack(true);
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

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Activity#onDestroy()
	 */
	@Override
	protected void onDestroy() {
		super.onDestroy();
		this.incomingStream.stop();
	}

}
