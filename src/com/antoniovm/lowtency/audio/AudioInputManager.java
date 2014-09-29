/**
 * 
 */
package com.antoniovm.lowtency.audio;

import java.util.concurrent.Semaphore;

import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;

/**
 * This class handles the audio input stream through an AudioRecorder device.
 * 
 * @author Antonio Vicente Martin
 * 
 */
public class AudioInputManager {

	private AudioRecord recorder;
	private byte[] buffer;
	private Semaphore semaphore;

	public AudioInputManager() {
		int minBufferSize = AudioRecord.getMinBufferSize(AudioIOManger.DEFAULT_SAMPLERATE, AudioFormat.CHANNEL_IN_MONO,
				AudioFormat.ENCODING_PCM_16BIT);
		this.recorder = new AudioRecord(MediaRecorder.AudioSource.DEFAULT, AudioIOManger.DEFAULT_SAMPLERATE,
				AudioFormat.CHANNEL_IN_MONO, AudioFormat.ENCODING_PCM_16BIT, minBufferSize);

		this.semaphore = new Semaphore(1);
	}

	/**
	 * If the audio recorder is not recording yet, it starts the recorder and
	 * releases the Semaphore to guarantee a correct read
	 * 
	 * @return startSuccess
	 */
	public synchronized boolean startRecording() {
		if (!isRecording()) {
			recorder.startRecording();
			semaphore.release();
			return true;
		}

		return false;
	}

	/**
	 * Returns true if the recorder is recording, and false otherwise
	 * 
	 * @return isRecording
	 */
	public boolean isRecording() {
		return recorder.getRecordingState() == AudioRecord.RECORDSTATE_RECORDING;
	}

	/**
	 * If the audio recorder is alredy recording, it acquires the Semaphore to
	 * avoid reading issues and, stops the recorder
	 * 
	 * @return stopSuccess
	 */
	public synchronized boolean stopRecording() {
		if (isRecording()) {
			waitForStart();
			recorder.stop();
			return true;
		}

		return false;
	}

	/**
	 * Reads 16 bit mono channel
	 * 
	 * @param buffer
	 */
	private synchronized void read16BitMono(byte[] buffer) {
		recorder.read(buffer, 0, buffer.length);
	}

	/**
	 * Reads 16 bit mono channel. It blocks if the Semaphore does not permit it
	 * 
	 * @param buffer
	 */
	public void read1Synchronized6BitMono(byte[] buffer) {
		waitForStart();
		read16BitMono(buffer);
		semaphore.release();
	}

	/**
	 * Reads 16 bit mono channel. It blocks if the Semaphore does not permit it
	 * 
	 * @param buffer
	 * @return theBuffer
	 */
	public byte[] read1Synchronized6BitMono() {
		read1Synchronized6BitMono(buffer);
		return buffer;
	}

	/**
	 * It aquires the Semaphore
	 */
	private void waitForStart() {
		try {
			semaphore.acquire();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	/**
	 * @return
	 */
	public byte[] getData() {
		return buffer;
	}

	/**
	 * 
	 */
	public void printBuffer() {
		System.out.print("[");
		for (int i = 0; i < buffer.length; i = i+2) {
			System.out.print(buffer[i] + buffer[i + 1] * 256);
		}
		System.out.println("]");

	}
}
