package com.antoniovm.lowtency.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Button;

import com.antoniovm.lowtency.R;

public class ActivityMain extends Activity {
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.a_main_menu);

		initButtonsActions();
	}

	/**
	 * Innit the buttons to init each activity flow
	 */
	private void initButtonsActions() {

		Button bServer = (Button) findViewById(R.id.bServer);
		Button bClient = (Button) findViewById(R.id.bClient);

		// Server activity flow
		bServer.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				startActivity(new Intent(ActivityMain.this, ActivityServerStreaming.class));
				finish();
			}
		});

		// Client activity flow
		bClient.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				startActivity(new Intent(ActivityMain.this, ActivityClientConnection.class));
				finish();
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
