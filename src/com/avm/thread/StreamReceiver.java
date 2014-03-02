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

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

import com.avm.audio.AudioOutputManager;
import com.avm.util.ByteConverter;

/**
 * @author Antonio Vicente Martin
 * 
 */
public class StreamReceiver extends IntentService {

	private static int DEFAULT_BUFFER_SIZE = 8192;

	private Socket s;
	private DatagramSocket datagramSocket;
	private DatagramPacket datagramPacket, datagramPacketUDPIDResponse;

	private AudioOutputManager audioOutputManager;

	private byte[] udpPortdata;
	private byte[] data;

	/**
	 * @param name
	 */
	public StreamReceiver() {
		super("StreamReceiver");
		data = new byte[DEFAULT_BUFFER_SIZE * 2];

		audioOutputManager = new AudioOutputManager(data);

		DEFAULT_BUFFER_SIZE = audioOutputManager.getMinBufferSize();

		Log.v("Buffer", "MinBufferSize: " + DEFAULT_BUFFER_SIZE);

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
			Log.v("Intent", "Host: " + intent.getExtras().getString("host"));
			s.connect(new InetSocketAddress(intent.getExtras().getString("host"), 3333));
		} catch (IOException e1) {
			e1.printStackTrace();
		}

		int port = s.getLocalPort();

		try {
			datagramSocket = new DatagramSocket(port);
		} catch (SocketException e) {
			e.printStackTrace();
		}

		// Buffer to host UPD port value
		udpPortdata = ByteConverter.toBytesArray(port, true);

		datagramPacket = new DatagramPacket(udpPortdata, udpPortdata.length);
		datagramPacketUDPIDResponse = new DatagramPacket(new byte[4], 4);

		datagramPacket.setData(data);

		audioOutputManager.play();

		long before = 0;
		long after = System.currentTimeMillis();

		// Receive stream data
		while (true) {
			before = after;

			try {

				datagramSocket.receive(datagramPacket);

			} catch (IOException e) {
				e.printStackTrace();
			}

			try {
				datagramSocket.send(datagramPacketUDPIDResponse);
			} catch (IOException e) {
				e.printStackTrace();
			}

			after = System.currentTimeMillis();
			Log.v("UDP", "Time receive: " + (after - before));

			before = after;

			// TODO Descompress

			// syncronizedRingBuffer.dumpData(datagramPacket.getData());

			audioOutputManager.playSamples();

			after = System.currentTimeMillis();
			Log.v("UDP", "Time Play: " + (after - before));
			Log.v("UDP", "First byte: " + datagramPacket.getData()[0]);

			// TODO Process data stream

		}

	}
}
