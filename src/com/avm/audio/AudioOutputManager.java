package com.avm.audio;

import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioTrack;

public class AudioOutputManager implements Runnable{
	
	private AudioTrack audioTrack;

	public AudioOutputManager() {
		this(44100,AudioFormat.CHANNEL_CONFIGURATION_MONO,AudioFormat.ENCODING_PCM_16BIT);
	}

	public AudioOutputManager(int sampleRate, int channelFormat,
			int encodingFormat) {
		int tamanoMinimoDeBuffer = AudioTrack.getMinBufferSize(sampleRate,
				channelFormat, encodingFormat);

		this.audioTrack = new AudioTrack(AudioManager.STREAM_MUSIC, sampleRate,
				channelFormat,
				encodingFormat, tamanoMinimoDeBuffer,
				AudioTrack.MODE_STREAM);

	}

	public void writeSamples(byte [] samples) {
		audioTrack.write(samples, 0, samples.length);
	}


	public void play() {
		audioTrack.play();
	}
	
	public void stop() {
		audioTrack.stop();
	}

	@Override
	public void run() {
		
	}
}
