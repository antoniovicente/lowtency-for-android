/**
 * 
 */
package com.avm.thread;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketException;
import java.nio.ByteBuffer;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

/**
 * @author Antonio Vicente Martin
 *
 */
public class StreamReceiver extends IntentService{

	private Socket s;
	private DatagramSocket datagramSocket;
	private DatagramPacket datagramPacket;
	
	private ByteBuffer data;
	
	/**
	 * @param name
	 */
	public StreamReceiver() {
		super("StreamReceiver");	
	}

	/* (non-Javadoc)
	 * @see android.app.IntentService#onHandleIntent(android.content.Intent)
	 */
	@Override
	protected void onHandleIntent(Intent intent) {

		Log.v("UDP","Inside onHandleIntent");
		android.os.Debug.waitForDebugger();
		data = ByteBuffer.allocate(100);
		
		s = new Socket();
			
		
		
		try {
			s.connect(new InetSocketAddress("192.168.3.132", 3333));
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		
		
		try {
			datagramSocket = new DatagramSocket();
		} catch (SocketException e) {
			e.printStackTrace();
		}
		
		int port = datagramSocket.getLocalPort();
		data.putInt(port);
		
		datagramPacket = new DatagramPacket(data.array(), data.capacity());
		
		try {
			// Send udp client information
			s.getOutputStream().write(data.array());
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		
		try {
			datagramSocket.receive(datagramPacket);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		Log.v("UDP",new String(datagramPacket.getData()));
		
		//tv.setText(new String(datagramPacket.getData()));
	}

}
