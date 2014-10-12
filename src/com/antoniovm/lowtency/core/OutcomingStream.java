/**
 * 
 */
package com.antoniovm.lowtency.core;

import com.antoniovm.lowtency.audio.AudioInputManager;
import com.antoniovm.lowtency.net.NetworkServer;

/**
 * This class handles the outcoming stream read from audio input device, ands
 * sends it to the clients connected
 * 
 * @author Antonio Vicente Martin
 * 
 */
public class OutcomingStream implements Runnable {

	private static int DEFAULT_PORT = 3333;

	private AudioInputManager audioInputManager;
	private NetworkServer sender;
	private Thread thread;
	private boolean running;
	
	/**
	 * 
	 */
	public OutcomingStream() {
		this.audioInputManager = new AudioInputManager();
		this.sender = new NetworkServer(DEFAULT_PORT);
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
		running = true;
		
		while (running) {
			audioInputManager.read1Synchronized6BitMono();
			sender.sendBroadcast(audioInputManager.getData());
		}

		this.thread = null;

	}

	/**
	 * 
	 */
	public boolean isStreaming() {
		return audioInputManager.isRecording();
	}

	/**
	 * Starts recording from input device
	 */
	public void startStreaming() {
		audioInputManager.startRecording();
	}

	/**
	 * Stops recording fron input device
	 */
	public void stopStreaming() {
		audioInputManager.stopRecording();
	}

}
