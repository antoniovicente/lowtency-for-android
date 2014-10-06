/**
 * 
 */
package com.antoniovm.lowtency.net;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.SocketException;

/**
 * @author Antonio Vicente Martin
 *
 */
public class StreamSender {

	private Socket socket;
	private DatagramSocket datagramSocket;
	private DatagramPacket datagramPacket;
	private byte[] data;

	/**
	 * 
	 */
	public StreamSender(byte[] data) {
		this.data = data;
		this.socket = new Socket();
		this.datagramPacket = new DatagramPacket(data, data.length);

		try {
			this.datagramSocket = new DatagramSocket();
			this.socket.setReuseAddress(true);
		} catch (SocketException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 
	 * Makes a TCP conecction, and binds a new UDP socket
	 * 
	 * @param ip
	 * @param port
	 */
	public void connect(String ip, int port) {
		try {
			socket.connect(new InetSocketAddress(ip, port));
			SocketAddress address = socket.getRemoteSocketAddress();
			datagramPacket.setSocketAddress(address);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Sends UDP package
	 * 
	 * @param data
	 */
	public void sendUDP(byte[] data) {
		datagramPacket.setData(data);
		try {
			datagramSocket.send(datagramPacket);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Sends UDP package
	 */
	public void sendUDP() {
		try {
			datagramSocket.send(datagramPacket);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
