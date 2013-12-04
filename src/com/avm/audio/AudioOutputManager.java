package com.avm.audio;

import java.nio.ByteBuffer;

import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioTrack;
import android.util.Log;

import com.avm.util.SyncronizedRingBuffer;

public class AudioOutputManager implements Runnable{
	
	private AudioTrack audioTrack;
	private SyncronizedRingBuffer<ByteBuffer> syncronizedRingBuffer;

	public AudioOutputManager(SyncronizedRingBuffer<ByteBuffer> syncronizedRingBuffer) {
		this(44100,AudioFormat.CHANNEL_CONFIGURATION_MONO,AudioFormat.ENCODING_PCM_16BIT,syncronizedRingBuffer);
		
	}

	public AudioOutputManager(int sampleRate, int channelFormat,
			int encodingFormat, SyncronizedRingBuffer<ByteBuffer> syncronizedRingBuffer) {
		int tamanoMinimoDeBuffer = AudioTrack.getMinBufferSize(sampleRate,
				channelFormat, encodingFormat);

		this.audioTrack = new AudioTrack(AudioManager.STREAM_MUSIC, sampleRate,
				channelFormat,
				encodingFormat, tamanoMinimoDeBuffer,
				AudioTrack.MODE_STREAM);
		this.syncronizedRingBuffer = syncronizedRingBuffer;

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
	
	private void playSamples() {
		play();
		
		while (true) {
			writeSamples(syncronizedRingBuffer.pop().array());
			Log.v("Buffer","Size: " + syncronizedRingBuffer.size());
		}
	}

	@Override
	public void run() {
		playSamples();
	}

	public void start() {
		(new Thread(this)).start();
	}
}
