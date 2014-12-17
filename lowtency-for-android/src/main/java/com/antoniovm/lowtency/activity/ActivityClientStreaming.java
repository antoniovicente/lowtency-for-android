package com.antoniovm.lowtency.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.antoniovm.lowtency.R;
import com.antoniovm.lowtency.core.IncomingStream;
import com.antoniovm.lowtency.graphic.WaveCanvas;

public class ActivityClientStreaming extends Activity {

    private IncomingStream incomingStream;
    private String ip;
    private int port;

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
        ip = incomingIntent.getStringExtra("IP");
        port = incomingIntent.getIntExtra("PORT", 0);

        this.incomingStream = new IncomingStream(ip, port);
        incomingStream.startThread();
    }

    /**
     *
     */
    private void initViews() {

        TextView tvIp = (TextView) findViewById(R.id.tvIPField);
        tvIp.setText(this.ip);
        TextView tvPort = (TextView) findViewById(R.id.tvPortField);
        tvPort.setText(port + "");

        Button bDisconnect = (Button) findViewById(R.id.bDisconnect);

        bDisconnect.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                startActivity(new Intent(ActivityClientStreaming.this, ActivityMain.class));
                finish();
            }
        });

        WaveCanvas vAudioWave = (WaveCanvas) findViewById(R.id.vAudioWave);

        incomingStream.addDataAvailableListeners(vAudioWave);

        vAudioWave.setNormalizedBuffer(new double[incomingStream.getNumberOfSamplesPerBuffer()]);
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

    /*
     * (non-Javadoc)
     *
     * @see android.app.Activity#onSaveInstanceState(android.os.Bundle)
     */
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable("InputStream", incomingStream);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        incomingStream = (IncomingStream) savedInstanceState.getParcelable("InputStream");
    }
}
