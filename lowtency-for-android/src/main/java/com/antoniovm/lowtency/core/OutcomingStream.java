/**
 *
 */
package com.antoniovm.lowtency.core;

import com.antoniovm.lowtency.audio.AudioInputManager;
import com.antoniovm.lowtency.event.ConnectionListener;
import com.antoniovm.lowtency.event.DataAvailableListener;
import com.antoniovm.lowtency.net.NetworkServer;
import com.antoniovm.util.raw.Queue;

import java.util.ArrayList;

/**
 * This class handles the outcoming network stream, reads from audio input device, ands
 * sends it to the connected clients
 *
 * @author Antonio Vicente Martin
 */
public class OutcomingStream implements Runnable, ConnectionListener {

    private byte[] chunk;
    private Queue queue;
    private AudioInputManager audioInputManager;
    private NetworkServer sender;
    private StreamHeader streamHeader;
    private Thread thread;
    private boolean running;
    private ArrayList<DataAvailableListener> dataAvailableListeners;

    /**
     *
     */
    public OutcomingStream() {
        this.audioInputManager = new AudioInputManager();
        this.streamHeader = new StreamHeader(audioInputManager.getBufferSize());
        this.queue = new Queue(audioInputManager.getBufferSize());
        this.sender = new NetworkServer(streamHeader);
        this.dataAvailableListeners = new ArrayList<>();
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
        running = true;

        sender.addConnectionListener(this);
        sender.startThread();

        while (running) {
            audioInputManager.read1Synchronized6BitMono();
            fireOnDataAvailable(audioInputManager.getData(), audioInputManager.getBytesPerSample());
            sender.sendBroadcast(audioInputManager.getData());
            /*
            queue.push(audioInputManager.getData());

            // Send chunks to clients
            while (!queue.isEmpty()){
                queue.pop(chunk);
                sender.sendBroadcast(chunk);
            }*/

        }

        this.thread = null;

    }

    /**
     *
     */
    public int getPort() {
        return sender.getPort();

    }

    /**
     * @return
     */
    public String getHost() {
        return sender.getIp();
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * com.antoniovm.lowtency.event.ConnectionListener#onFirstClientConnection()
     */
    @Override
    public void onFirstClientConnection() {
        audioInputManager.startRecording();

    }

    /*
     * (non-Javadoc)
     *
     * @see
     * com.antoniovm.lowtency.event.ConnectionListener#onFirstClientDisconnection
     * ()
     */
    @Override
    public void onLastClientDisconnection() {
        audioInputManager.stopRecording();
    }

    /**
     *
     */
    public int getNumberOfSamplesPerBuffer() {
        return audioInputManager.getNumberOfSamplesPerBuffer();
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

}
