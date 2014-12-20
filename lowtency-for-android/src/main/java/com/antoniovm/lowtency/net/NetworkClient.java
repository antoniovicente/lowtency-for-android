package com.antoniovm.lowtency.net;

import com.antoniovm.lowtency.core.StreamHeader;

import java.io.IOException;
import java.io.InputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.net.Socket;

/**
 * @author Antonio Vicente Martin
 */
public class NetworkClient {

    private Socket socket;
    private DatagramSocket datagramSocket;
    private DatagramPacket datagramPacket;
    private StreamHeader streamHeader;

    /**
     *
     */
    public NetworkClient(int bufferLength) {
        this.datagramPacket = new DatagramPacket(new byte[bufferLength], bufferLength);
        this.streamHeader = new StreamHeader();
        this.socket = new Socket();
    }

    /**
     * Receives a datagram socket from
     */
    public byte[] receiveUDP() {
        try {
            datagramSocket.receive(datagramPacket);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return datagramPacket.getData();
    }

    /**
     * Receives and returns the header information about the stream that is
     * going to be received
     *
     * @return streamHeader The header information of the stream
     */
    public StreamHeader receiveHeader() {
        byte[] serializedHeader = new byte[4];

        try {
            InputStream is = socket.getInputStream();
            is.read(serializedHeader);
            is.close();
            StreamHeader.buildFromSerialized(serializedHeader, streamHeader);

        } catch (IOException e) {
            e.printStackTrace();
        }

        //int bufferSize = streamHeader.getBufferSize();
        //this.datagramPacket = new DatagramPacket(new byte[bufferSize], bufferSize);

        return streamHeader;

    }

    /**
     * Makes a TCP conecction
     *
     * @param ip
     * @param port
     */
    public boolean connect(String host, int port) {
        if (socket.isConnected()) {
            return false;
        }

        try {
            this.socket.connect(new InetSocketAddress(host, port));
            this.datagramSocket = new DatagramSocket(socket.getLocalPort());
            this.datagramSocket.setReuseAddress(true);
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }

        return true;
    }

}
