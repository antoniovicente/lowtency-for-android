package com.avm.audio;

import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioTrack;

public class AudioOutputManager {
	AudioTrack audioTrack;
	short[] buffer = null;

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

	public void writeSamples(float[] samples) {
		fillBuffer(samples);
		audioTrack.write(buffer, 0, samples.length);
	}

	private void fillBuffer(float[] samples) {
		if (buffer == null)
			buffer = new short[samples.length];

		for (int i = 0; i < samples.length; i++)
			buffer[i] = (short) (samples[i] * Short.MAX_VALUE);
	}

	public void play() {
		audioTrack.play();
	}
}
