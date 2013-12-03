package com.avm;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketException;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.widget.TextView;

public class MainActivity extends Activity {
	
	private Socket s;
	private DatagramSocket datagramSocket;
	private DatagramPacket datagramPacket;
	
	private byte [] data;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		data = new byte [100];
		
		TextView tv = (TextView)findViewById(R.id.hello);
		
		s = new Socket();
			

		
		try {
			s.connect(new InetSocketAddress("192.168.3.132", 3333));
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		
		
		try {
			datagramSocket = new DatagramSocket(0);
		} catch (SocketException e) {
			e.printStackTrace();
		}
		
		datagramPacket = new DatagramPacket(data, data.length);
		
		try {
			datagramSocket.receive(datagramPacket);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		tv.setText(new String(datagramPacket.getData()));
		
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}
