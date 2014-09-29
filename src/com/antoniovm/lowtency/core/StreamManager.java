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
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Runnable#run()
	 */
	@Override
	public void run() {
		// TODO Auto-generated method stub

	}

}
