/**
 * 
 */
package com.antoniovm.lowtency.core;

import com.antoniovm.lowtency.audio.AudioOutputManager;
import com.antoniovm.lowtency.net.NetworkClient;

/**
 * @author Antonio Vicente Martin
 *
 */
public class IncomingStream {

	private NetworkClient networkClient;
	private AudioOutputManager audioOutputManager;

	public IncomingStream() {
		this.networkClient = new NetworkClient();
		this.audioOutputManager = new AudioOutputManager();
	}

}
