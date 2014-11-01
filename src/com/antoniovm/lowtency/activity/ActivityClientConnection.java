package com.antoniovm.lowtency.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;

import com.antoniovm.lowtency.R;

public class ActivityClientConnection extends Activity {
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.a_server_streaming);

		init();
	}

	/**
	 * 
	 */
	private void init() {

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
