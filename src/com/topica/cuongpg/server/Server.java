package com.topica.cuongpg.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

import com.topica.cuongpg.init.User;

public class Server {
	private static ServerSocket listener = null;
	private static Socket serverSocket = null;
	private static int port = 8080;
	private static ServerThread serverThread;
	public static Map<String, Socket> listUser = new HashMap<String, Socket>();

	public static void main(String[] args) {
		try {
			listener = new ServerSocket(port);
		} catch (IOException e) {
			System.out.println(e);
		}
		while (true) {
			try {
				serverSocket = listener.accept();
				System.out.println("Recieved connection from " + serverSocket.getInetAddress() + " on port "
						+ serverSocket.getPort());
				serverThread = new ServerThread(serverSocket);
				serverThread.start();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				System.out.println("sdsad");
				e.printStackTrace();
			}

		}
	}
}
