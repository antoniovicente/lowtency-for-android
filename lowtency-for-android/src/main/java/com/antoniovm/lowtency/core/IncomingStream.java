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

    private static final int SAMPLES_PER_CHUNK = 256;

    private BlockingQueue blockingQueue;
    private NetworkClient receiver;
    private AudioOutputManager audioOutputManager;
    private Thread thread;
    private boolean running;
    private String host;
    private int port;
    private ArrayList<DataAvailableListener> dataAvailableListeners;

    public IncomingStream(String host, int port) {
        this.audioOutputManager = new AudioOutputManager(SAMPLES_PER_CHUNK);
        this.receiver = new NetworkClient(audioOutputManager.getBufferLength());
        this.running = false;
        this.host = host;
        this.port = port;
        this.dataAvailableListeners = new ArrayList<DataAvailableListener>();
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

    /*
     * (non-Javadoc)
     *
     * @see java.lang.Runnable#run()
     */
    @Override
    public void run() {

        if (!connect()) {
            tearDown();
        }

        StreamHeader streamHeader = receiver.receiveHeader();

        audioOutputManager.play();

        running = true;

        byte[] data = null;

        while (running) {
            data = receiver.receiveUDP();
            fireOnDataAvailable(data, audioOutputManager.getBytesPerSample());
            audioOutputManager.writeSamples(data);
        }

        tearDown();

    }

    private void tearDown() {
        this.audioOutputManager.stop();
        this.thread = null;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    /**
     * @return the dataAvailableListeners
     */
    public void addDataAvailableListeners(DataAvailableListener dataAvailableListener) {
        this.dataAvailableListeners.add(dataAvailableListener);
    }

    /**
     *
     */
    private void fireOnDataAvailable(byte[] data, int sampleSize) {
        for (int i = 0; i < dataAvailableListeners.size(); i++) {
            dataAvailableListeners.get(i).onDataAvailableListener(data, sampleSize);
        }
    }

    /**
     * @return
     */
    public int getNumberOfSamplesPerBuffer() {
        return audioOutputManager.getBufferLength() / audioOutputManager.getBytesPerSample();
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
