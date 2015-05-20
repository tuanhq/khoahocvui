package com.ligerdev.appbase.utils;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class CheckAlivePort extends Thread {
	
	private ServerSocket serverSocket;
	private boolean running = false;

	public CheckAlivePort(int port) throws IOException {
		this.serverSocket = new ServerSocket(port);
		this.serverSocket.setSoTimeout(10000);
	}

	public void run() {
		this.running = true;
		while (this.running) {
			try {
				Socket server = this.serverSocket.accept();
				server.close();
			} catch (Exception e) {
			}  
		}
	}

	public void kill() {
		this.running = false;
	}
} 