package com.antoniovm.lowtency.audio;

import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioTrack;

/**
 * This class handles the audio stream and sends it to the output device
 * 
 * @author Antonio Vicente Martin
 * 
 */
public class AudioOutputManager  {

	private AudioTrack audioTrack;
	private byte [] samplesBuffer;

	public AudioOutputManager(byte [] samplesBuffer) {
		this(AudioIOManger.DEFAULT_SAMPLERATE, AudioFormat.CHANNEL_CONFIGURATION_MONO, AudioFormat.ENCODING_PCM_16BIT,
				samplesBuffer);
	}

	public AudioOutputManager(int sampleRate, int channelFormat, int encodingFormat,
 byte[] samplesBuffer) {
		int tamanoMinimoDeBuffer = AudioTrack.getMinBufferSize(sampleRate, channelFormat, encodingFormat);

		this.audioTrack = new AudioTrack(AudioManager.STREAM_MUSIC, sampleRate, channelFormat, encodingFormat,
				tamanoMinimoDeBuffer, AudioTrack.MODE_STREAM);
		this.samplesBuffer = samplesBuffer;

	}

	public void writeSamples(byte[] samples) {
		audioTrack.write(samples, 0, samples.length);
	}
	
	public void writeSamples(byte[] samples, int i, int j) {
		audioTrack.write(samples, i, j);
	}

	public void play() {
		audioTrack.play();
	}

	public void stop() {
		audioTrack.stop();
	}

	public void playSamples() {
		writeSamples(samplesBuffer);

	}

	public int getMinBufferSize() {
		return AudioTrack.getMinBufferSize(audioTrack.getSampleRate(), audioTrack.getChannelConfiguration(),
				audioTrack.getAudioFormat());
	}
}
