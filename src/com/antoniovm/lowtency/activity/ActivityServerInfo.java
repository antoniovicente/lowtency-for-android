package com.antoniovm.lowtency.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.antoniovm.lowtency.R;
import com.antoniovm.lowtency.util.QRManager;

public class ActivityServerInfo extends Activity {
	
	private int DEFAULT_QR_SIZE = 400;

	private String ip;
	private int port;
	private QRManager qrManager;

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

		String qrString = getURI(ip, port);
		ImageView ivQR = (ImageView) findViewById(R.id.ivQR);
		ivQR.setImageBitmap(qrManager.encode(qrString, DEFAULT_QR_SIZE, DEFAULT_QR_SIZE));


	}

	/**
	 * 
	 */
	private void initData() {
		Intent incomingIntent = getIntent();
		ip = incomingIntent.getStringExtra("IP");
		port = incomingIntent.getIntExtra("PORT", 0);

		this.qrManager = new QRManager();
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

	/**
	 * 
	 */
	private String getURI(String ip, int port) {
		return getString(R.string.l_lowtency) + "://" + ip + ":" + port;
	}

}
