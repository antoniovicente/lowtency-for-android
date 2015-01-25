package com.antoniovm.lowtency.audio;

import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioTrack;

import com.antoniovm.util.raw.BlockingQueue;

/**
 * This class handles the audio stream and sends it to the output audio device
 *
 * @author Antonio Vicente Martin
 */
public class AudioOutputManager extends AudioIOManger implements Runnable {

    private AudioTrack audioTrack;
    private byte[] samplesWritingBuffer;
    private BlockingQueue blockingQueue;
    private Thread thread;
    private boolean running;

    /**
     * Builds a new AudioOutputManager
     * @param proposedBufferSize The lowest buffer proposed by listener
     */
    public AudioOutputManager(int proposedBufferSize) {
        this(AudioIOManger.DEFAULT_SAMPLERATE, AudioFormat.CHANNEL_CONFIGURATION_MONO, AudioFormat.ENCODING_PCM_16BIT, proposedBufferSize);
    }

    /**
     * Builds a new AudioOutputManager
     * @param sampleRate The samplerate in samples per second
     * @param channelFormat The AudioFormat.CHANNEL_CONFIGURATION_XXXX configuration
     * @param encodingFormat The AudioFormat.ENCODING_XXX configuration
     * @param proposedBufferSize The lowest buffer proposed by listener
     */
    public AudioOutputManager(int sampleRate, int channelFormat, int encodingFormat, int proposedBufferSize) {
        int minimumBufferSize = AudioTrack.getMinBufferSize(sampleRate, channelFormat, encodingFormat);

        minimumBufferSize = (proposedBufferSize < minimumBufferSize)?proposedBufferSize*2:proposedBufferSize;

        //minimumBufferSize = Math.max(minimumBufferSize, proposedBufferSize);

        this.samplesWritingBuffer = new byte[minimumBufferSize];
        this.blockingQueue = new BlockingQueue(minimumBufferSize, BlockingQueue.PushPolicy.OVERWRITE_OLD_DATA,minimumBufferSize);
        this.audioTrack = new AudioTrack(AudioManager.STREAM_MUSIC, sampleRate, channelFormat, encodingFormat,
                minimumBufferSize, AudioTrack.MODE_STREAM);

    }

    /**
     * Writes the samples to the audio device for playback
     * @param samples The raw bytes
     */
    public void writeSamples(byte[] samples) {
        writeSamples(samples, 0, samples.length);
    }

    /**
     * Writes the samples to the audio device for playback
     * @param samples The raw bytes
     * @param i The intial index
     * @param j The final index
     */
    public void writeSamples(byte[] samples, int i, int j) {
        audioTrack.write(samples, i, j);
    }

    /**
     * Requests the audio device to start the playback
     */
    public void play() {
        audioTrack.play();
        startThread();
    }

    /**
     * Requests the audio device to stop the playback
     */
    public void stop() {
        audioTrack.stop();
        running = false;
    }

    /**
     * Returns the number of bytes per sample
     *
     * @return The number of bytes per sample
     */
    public int getBytesPerSample() {
        return getBytesPerSample(audioTrack.getAudioFormat());
    }

    /**
     * Returns the number of bytes per sample
     *
     * @param encodingFormat The encoding type
     * @return The number of bytes per sample
     */
    private static int getBytesPerSample(int encodingFormat) {
        switch (encodingFormat) {
            case AudioFormat.ENCODING_PCM_8BIT:
                return 1;
            case AudioFormat.ENCODING_PCM_16BIT:
                return 2;
            default:
                break;
        }

        return 0;
    }

    /**
     * Returns the buffer length in bytes
     * @return bufferLength The buffer's length in bytes
     */
    public int getBufferLength() {
        return samplesWritingBuffer.length;
    }

    /**
     * Returns the buffer length in samples
     * @return bufferLength The buffer's length in samples
     */
    public int getBufferLengthInSamples(){
        return getBufferLength()/getBytesPerSample();
    }

    /**
     * Main thread to get the latest data available
     */
    @Override
    public void run() {
        running = true;
        while(running){
            blockingQueue.pop(samplesWritingBuffer);
            writeSamples(samplesWritingBuffer);
        }
    }

    /**
     * Gets the BlockingQueue buffer
     * @return The BlockingQueue
     */
    public BlockingQueue getBlockingQueue() {
        return blockingQueue;
    }

    /**
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
}
