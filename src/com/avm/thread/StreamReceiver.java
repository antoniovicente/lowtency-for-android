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
public class StreamReceiver extends IntentService {

	public static final int DEFAULT_BUFFER_SIZE = 512;

	private Socket s;
	private DatagramSocket datagramSocket;
	private DatagramPacket datagramPacket;

	private ByteBuffer data;

	/**
	 * @param name
	 */
	public StreamReceiver() {
		super("StreamReceiver");

		try {
			datagramSocket = new DatagramSocket();
		} catch (SocketException e) {
			e.printStackTrace();
		}

		data = ByteBuffer.allocate(4);

		s = new Socket();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.IntentService#onHandleIntent(android.content.Intent)
	 */
	@Override
	protected void onHandleIntent(Intent intent) {

		android.os.Debug.waitForDebugger();

		try {
			s.connect(new InetSocketAddress("192.168.3.135", 3333));
		} catch (IOException e1) {
			e1.printStackTrace();
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
		
		// Receive stream data
		while (true) {
			try {
				datagramSocket.receive(datagramPacket);
			} catch (IOException e) {
				e.printStackTrace();
			}

			Log.v("UDP", datagramPacket.getData().length+"");
		}


	}

}
