package com.antoniovm.lowtency.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.antoniovm.lowtency.R;

public class ActivityServerInfo extends Activity {
	
	private String ip;
	private int port;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.a_server_connection_info);

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
	private void initViews() {
		
		EditText etIp = (EditText)findViewById(R.id.etIP);
		etIp.setText(this.ip);
		EditText etPort = (EditText)findViewById(R.id.etPort);
		etPort.setText(port + "");
		
		
		LinearLayout lServerInfoContainer = (LinearLayout) findViewById(R.id.lServerInfoContainer);
		lServerInfoContainer.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				finish();
			}
		});


	}

	/**
	 * 
	 */
	private void initData() {
		Intent incomingIntent = getIntent();
		ip = incomingIntent.getStringExtra("IP");
		port = incomingIntent.getIntExtra("PORT", 0);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Activity#onPause()
	 */
	@Override
	protected void onPause() {
		super.onPause();
		finish();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}
