/**
 * 
 */
package com.antoniovm.lowtency.core;

import com.antoniovm.lowtency.audio.AudioInputManager;
import com.antoniovm.lowtency.net.StreamSender;

/**
 * @author Antonio Vicente Martin
 *
 */
public class StreamManager implements Runnable {

	private AudioInputManager audioInputManager;
	private StreamSender sender;
	private Thread thread;
	private boolean running;
	
	/**
	 * 
	 */
	public StreamManager() {
		this.audioInputManager = new AudioInputManager();
		this.sender = new StreamSender(audioInputManager.getData());
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
			sender.sendUDP();
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
	 * 
	 */
	public void startStreaming() {
		audioInputManager.startRecording();
	}

	/**
	 * 
	 */
	public void stopStreaming() {
		audioInputManager.stopRecording();
	}

}
