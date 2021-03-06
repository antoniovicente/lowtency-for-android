/**
 *
 */
package com.antoniovm.lowtency.core;

import android.os.Parcel;
import android.os.Parcelable;

import com.antoniovm.lowtency.audio.AudioOutputManager;
import com.antoniovm.lowtency.event.DataAvailableListener;
import com.antoniovm.lowtency.net.NetworkClient;
import com.antoniovm.util.raw.BlockingQueue;

import java.util.ArrayList;

/**
 * This class handles the incoming stream from internet, and routes it to the
 * audio output manager.
 * <p/>
 * To start a new connection, startThread() method must be called
 *
 * @author Antonio Vicente Martin
 */
public class IncomingStream implements Runnable, Parcelable {

    private BlockingQueue blockingQueue;
    private NetworkClient receiver;
    private Thread thread;
    private boolean running;
    private String host;
    private int port;
    private ArrayList<DataAvailableListener> dataAvailableListeners;
    private int bufferLengthInSamples;

    /**
     * Builds a new IncomingString
     * @param host The remote host's IP
     * @param port The remote's aplication port
     */
    public IncomingStream(String host, int port) {
        this.receiver = new NetworkClient();
        this.running = false;
        this.host = host;
        this.port = port;
        this.dataAvailableListeners = new ArrayList<>();
        this.bufferLengthInSamples = -1;
    }

    /**
     * Creates a new connection to the host and port specified in the
     * constructor
     *
     * @return true if there was a successful connection, false otherwise
     */
    public boolean connect() {
        return receiver.connect(host, port);
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

    /**
     * Makes a request to the thread to stop
     */
    public void stop() {
        running = false;
    }

    /**
     * Main thread to read from the UDP socket and store into a concurrent queue to let the
     * playback thread, read the data to send it to the audio device
     */
    @Override
    public void run() {

        if (!connect()) {
            tearDown();
        }

        StreamHeader streamHeader = receiver.receiveHeader();

        byte[] data;

        AudioOutputManager audioOutputManager = new AudioOutputManager(streamHeader.getBufferSize());
        this.blockingQueue = audioOutputManager.getBlockingQueue();
        audioOutputManager.play();
        this.bufferLengthInSamples = audioOutputManager.getBufferLengthInSamples();

        running = true;

        while (running) {
            data = receiver.receiveUDP();
            blockingQueue.push(data);
            fireOnDataAvailable(data, audioOutputManager.getBytesPerSample());
        }

        audioOutputManager.stop();

        tearDown();

    }

    /**
     * Stops the playback
     */
    private void tearDown() {
        this.thread = null;
        this.receiver.close();
        this.bufferLengthInSamples = -1;
    }

    /**
     * Add a new DataAvailableListener to the list
     * @param dataAvailableListener The DataAvailableListener object
     */
    public void addDataAvailableListeners(DataAvailableListener dataAvailableListener) {
        this.dataAvailableListeners.add(dataAvailableListener);
    }

    /**
     * Fires the OnDataAvailable event
     * @param data The new data available
     * @param sampleSize The size of each sample in bytes
     */
    private void fireOnDataAvailable(byte[] data, int sampleSize) {
        for (int i = 0; i < dataAvailableListeners.size(); i++) {
            dataAvailableListeners.get(i).onDataAvailableListener(data, sampleSize);
        }
    }

    /**
     * Returns the buffer length in samples
     * @return bufferLength The buffer's length in samples
     */
    public int getBufferLengthInSamples() {
        return bufferLengthInSamples;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        Object[] os = {this};
        dest.writeArray(os);
    }
}
