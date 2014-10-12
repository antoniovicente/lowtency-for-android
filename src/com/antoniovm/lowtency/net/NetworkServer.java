/**
 * 
 */
package com.antoniovm.lowtency.net;

import java.io.IOException;
import java.io.OutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;

import com.antoniovm.lowtency.core.StreamHeader;

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
		this.datagramPacket = new DatagramPacket(new byte[100], 100);
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
	 * Waits for a new connection request, adds the new socket to the clients
	 * list and returns it
	 * 
	 * @return neClient
	 */
	public Socket accept() {
		Socket newClient = null;
		try {
			newClient = serverSocket.accept();
			clients.add(newClient);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return newClient;

	}

	/**
	 * Sends the stream header to the specified socket
	 * 
	 * @param streamHeader
	 */
	public void sendHeader(Socket socket, StreamHeader streamHeader) {
		OutputStream os;
		try {
			os = socket.getOutputStream();
			os.write(streamHeader.getSerialized());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Waits for a new client and sends the stream header to it
	 */
	public void waitForNewClent(StreamHeader streamHeader) {
		Socket newClient = accept();
		sendHeader(newClient, streamHeader);
	}
}
