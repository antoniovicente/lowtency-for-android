package com.avm.audio;

import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioTrack;
import android.util.Log;

import com.avm.util.SyncronizedRingQueueByteBuffer;

public class AudioOutputManager implements Runnable {

	private AudioTrack audioTrack;
	private SyncronizedRingQueueByteBuffer syncronizedRingBuffer;

	public AudioOutputManager() {
		this(44100, AudioFormat.CHANNEL_CONFIGURATION_MONO, AudioFormat.ENCODING_PCM_16BIT, null);
	}

	public AudioOutputManager(int sampleRate, int channelFormat, int encodingFormat,
			SyncronizedRingQueueByteBuffer syncronizedRingBuffer) {
		int tamanoMinimoDeBuffer = AudioTrack.getMinBufferSize(sampleRate, channelFormat, encodingFormat);

		this.audioTrack = new AudioTrack(AudioManager.STREAM_MUSIC, sampleRate, channelFormat, encodingFormat,
				tamanoMinimoDeBuffer, AudioTrack.MODE_STREAM);
		this.syncronizedRingBuffer = syncronizedRingBuffer;

	}

	public void writeSamples(byte[] samples) {
		audioTrack.write(samples, 0, samples.length);
	}

	public void play() {
		audioTrack.play();
	}

	public void stop() {
		audioTrack.stop();
	}

	private void playSamples() {
		play();

		while (true) {
			writeSamples(syncronizedRingBuffer.pop().array());
			Log.v("Buffer", "Size: " + syncronizedRingBuffer.size());
		}
	}

	@Override
	public void run() {
		playSamples();
	}

	public void start() {
		(new Thread(this)).start();
	}

	public void setSyncronizedRingBuffer(SyncronizedRingQueueByteBuffer syncronizedRingBuffer) {
		this.syncronizedRingBuffer = syncronizedRingBuffer;
	}

	public SyncronizedRingQueueByteBuffer getSyncronizedRingBuffer() {
		return syncronizedRingBuffer;
	}

	public int getMinBufferSize() {
		return AudioTrack.getMinBufferSize(audioTrack.getSampleRate(), audioTrack.getChannelConfiguration(),
				audioTrack.getAudioFormat());
	}
}
