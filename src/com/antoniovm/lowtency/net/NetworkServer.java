/**
 * 
 */
package com.antoniovm.lowtency.net;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;

/**
 * @author Antonio Vicente Martin
 *
 */
public class NetworkServer {

	private ServerSocket serverSocket;
	private DatagramSocket datagramSocket;
	private DatagramPacket datagramPacket;
	private ArrayList<Socket> clients;

	/**
	 * 
	 */
	public NetworkServer(int port) {
		this.datagramPacket = new DatagramPacket(new byte[1], 1);
		this.clients = new ArrayList<Socket>();

		try {
			this.serverSocket = new ServerSocket();
			this.serverSocket.setReuseAddress(true);
			this.serverSocket.bind(new InetSocketAddress(port));
			this.datagramSocket = new DatagramSocket();
			this.serverSocket.setReuseAddress(true);
		} catch (SocketException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Sends UDP package to all clients
	 * 
	 * @param data
	 */
	public void sendBroadcast(byte[] data) {
		datagramPacket.setData(data);

		for (int i = 0; i < clients.size(); i++) {
			datagramPacket.setSocketAddress(clients.get(i).getRemoteSocketAddress());

			try {
				datagramSocket.send(datagramPacket);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * 
	 */
	public void accept() {
		try {
			Socket newClient = serverSocket.accept();
			clients.add(newClient);
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
}
