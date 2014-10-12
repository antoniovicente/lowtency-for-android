/**
 * 
 */
package com.antoniovm.lowtency.core;

import com.antoniovm.lowtency.audio.AudioOutputManager;
import com.antoniovm.lowtency.net.NetworkClient;

/**
 * This class handles the incoming stream from internet, and routes it to the
 * audio output manager
 * 
 * @author Antonio Vicente Martin
 * 
 */
public class IncomingStream implements Runnable {

	private NetworkClient receiver;
	private AudioOutputManager audioOutputManager;
	private Thread thread;
	private boolean running;
	private String host;
	private int port;

	public IncomingStream(String host, int port) {
		this.receiver = new NetworkClient();
		this.audioOutputManager = new AudioOutputManager();
		this.running = false;
		this.host = host;
		this.port = port;
	}

	/**
	 * Creates a new connection to the host and port specified in the
	 * constructor
	 * 
	 * @return true if there was a successful connection, false otherwise
	 */
	public boolean connect() {
		return receiver.connect(host, port);
	}

	/**
	 * 
	 * Starts a new Thread
	 * 
	 * @return true if it could be started, false otherwise
	 */
	public boolean startThread() {
		if (this.thread == null) {
			this.thread = new Thread(this);
			this.thread.start();

			return true;
		}

		return false;
	}

	/**
	 * Makes a request to the thread to stop
	 */
	public void stop() {
		running = false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Runnable#run()
	 */
	@Override
	public void run() {

		if (!connect()) {
			tearDown();
		}

		StreamHeader streamHeader = receiver.receiveHeader();

		audioOutputManager.play();

		running = true;

		byte[] data = null;

		while (running) {
			data = receiver.receiveUDP();
			audioOutputManager.writeSamples(data);
		}

		tearDown();

	}

	private void tearDown() {
		this.audioOutputManager.stop();
		this.thread = null;
	}

}
