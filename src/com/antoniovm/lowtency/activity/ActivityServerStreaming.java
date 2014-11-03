package com.antoniovm.lowtency.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Button;

import com.antoniovm.lowtency.R;
import com.antoniovm.lowtency.core.OutcomingStream;

public class ActivityServerStreaming extends Activity {
	
	private OutcomingStream outcomingStream;
	private Intent intent;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.a_server_streaming);

		init();

		showServerInfoActivity();
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
		intent = new Intent(this, ActivityServerInfo.class);

		outcomingStream = new OutcomingStream();
		outcomingStream.startStreaming();
	}

	/**
	 * 
	 */
	private void initViews() {

		Button bShowInfo = (Button) findViewById(R.id.bInfo);

		bShowInfo.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				showServerInfoActivity();

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

	/**
	 * 
	 */
	private void showServerInfoActivity() {
		// First time this activity is created, the config activity must me
		// shown
		intent.putExtra("IP", outcomingStream.getHost());
		intent.putExtra("PORT", outcomingStream.getPort());
		startActivity(intent);

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
