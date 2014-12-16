/**
 *
 */
package com.antoniovm.lowtency.audio;

import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;

import com.antoniovm.lowtency.util.MathUtils;

import java.util.concurrent.Semaphore;

/**
 * This class handles the audio input stream through an AudioRecorder device.
 *
 * @author Antonio Vicente Martin
 */
public class AudioInputManager {

    private AudioRecord recorder;
    private byte[] buffer;
    private Semaphore semaphore;

    /**
     * @param chunkSize The buffer chunk size to handle in samples
     */
    public AudioInputManager(int chunkSize) {
        int minBufferSize = AudioRecord.getMinBufferSize(AudioIOManger.DEFAULT_SAMPLERATE, AudioFormat.CHANNEL_IN_MONO,
                AudioFormat.ENCODING_PCM_16BIT);
        this.recorder = new AudioRecord(MediaRecorder.AudioSource.DEFAULT, AudioIOManger.DEFAULT_SAMPLERATE,
                AudioFormat.CHANNEL_IN_MONO, AudioFormat.ENCODING_PCM_16BIT, minBufferSize);

        minBufferSize = MathUtils.getUpperClosestMultiple(minBufferSize, chunkSize * getBytesPerSample());

        this.buffer = new byte[minBufferSize];
        this.semaphore = new Semaphore(0);
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
            recorder.stop();
            waitForStart();
            return true;
        }

        return false;
    }

    /**
     * Reads 16 bit mono channel
     *
     * @param buffer The buffer to store read information
     */
    private synchronized void read16BitMono(byte[] buffer) {
        recorder.read(buffer, 0, buffer.length);
    }

    /**
     * Reads 16 bit mono channel. It blocks if the Semaphore does not permit it
     *
     * @param buffer The buffer to store read information
     */
    public void read1Synchronized6BitMono(byte[] buffer) {
        waitForStart();
        read16BitMono(buffer);
        semaphore.release();
    }

    /**
     * Reads 16 bit mono channel. It blocks if the Semaphore does not permit it
     *
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
     * Returns the buffer
     *
     * @return The buffer
     */
    public byte[] getData() {
        return buffer;
    }

    /**
     * Auxiliar method to print the buffer
     */
    public void printBuffer() {
        System.out.print("[");
        for (int i = 0; i < buffer.length; i = i + 2) {
            System.out.print((buffer[i] + buffer[i + 1] * 256) + " ");
        }
        System.out.println("]");

    }

    /**
     * Returns the buffer size
     *
     * @return bufferSize
     */
    public int getBufferSize() {
        return buffer.length;
    }

    /**
     * Returns the number of samples per buffer
     *
     * @return numberOfSamplesPerBuffer
     */
    public int getNumberOfSamplesPerBuffer() {
        // Buffer length / number of bytes per sample
        return buffer.length / getBytesPerSample();
    }

    /**
     * Returns the number of bytes per sample
     *
     * @return byetes per sample
     */
    public int getBytesPerSample() {
        switch (recorder.getAudioFormat()) {
            case AudioFormat.ENCODING_PCM_16BIT:
                return 2;
            case AudioFormat.ENCODING_PCM_8BIT:
                return 1;

            default:
                break;
        }
        return 0;

    }
}
