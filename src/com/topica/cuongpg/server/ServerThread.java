package com.topica.cuongpg.server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.topica.cuongpg.init.Message;
import com.topica.cuongpg.init.User;

public class ServerThread extends Thread {
	public static final String LOG_IN = "log";
	public static final String LOG_ERROR = "logerror";
	public static final String LOG_SUCCES = "logsucces";
	public static final String CHAT = "chat";
	public static final String AGRE = "agre";
	public static final String NOT_AGRE = "not agre";
	public static final String WANT_CHAT = "want chat";
	public static final String BLOCK = "block";

	public static ObjectOutputStream os = null;
	public static ObjectOutputStream os1 = null;
	public static ObjectInputStream is = null;
	public static Map<String, ArrayList<String>> userBlock = new HashMap<String, ArrayList<String>>();

	User user;
	public static Message messServerRecive;
	public static Message messServerSend;
	public Socket serverSocket;

	public ServerThread(Socket serverSocket) {
		this.serverSocket = serverSocket;
		ServerThread.os = null;
		ServerThread.is = null;
	}

	@Override
	public void run() {
		try {
			os = new ObjectOutputStream(serverSocket.getOutputStream());
			is = new ObjectInputStream(serverSocket.getInputStream());
			while (true) {
				// System.out.println("aaaa");
				messServerRecive = (Message) is.readObject();
				System.out
						.println(messServerRecive.head + " " + messServerRecive.nameid + " " + messServerRecive.friend);
				String head = messServerRecive.head;
				switch (head) {
				case LOG_IN:
					User check = new User(messServerRecive.nameid);
					if (Server.listUser.containsKey(messServerRecive.nameid)) {
						messServerSend = new Message(LOG_ERROR, null, null, null);
						os.writeObject(messServerSend);
					} else {
						user = new User(messServerRecive.nameid);
						Server.listUser.put(messServerRecive.nameid, serverSocket);
						System.out.println(Server.listUser.get(user));
						messServerSend = new Message(LOG_SUCCES, null, null, null);
						os.writeObject(messServerSend);
					}
					break;

				case WANT_CHAT:
					System.out.println(messServerRecive.friend);
					for (String i : userBlock.get(messServerRecive.friend)) {
						if (i == messServerRecive.nameid) {
							messServerSend = new Message(BLOCK, messServerRecive.friend, messServerRecive.nameid, null);
							os.writeObject(messServerSend);
						}
					}
					Socket friendSocket = Server.listUser.get(messServerRecive.friend);
					System.out.println(friendSocket);
					os1 = new ObjectOutputStream(friendSocket.getOutputStream());
					messServerSend = new Message(WANT_CHAT, null, messServerRecive.nameid, null);
					os1.writeObject(messServerSend);
					System.out.println(messServerSend.head + " " + messServerSend.nameid + " " + messServerSend.friend);

				case AGRE:
					messServerSend = new Message(AGRE, null, messServerRecive.nameid, null);
					os.writeObject(messServerSend);
					break;
				case NOT_AGRE:
					messServerSend = new Message(NOT_AGRE, null, messServerRecive.nameid, null);
					os.writeObject(messServerSend);
					break;

				case CHAT:
					friendSocket = Server.listUser.get(messServerRecive.friend);
					os1 = new ObjectOutputStream(friendSocket.getOutputStream());
					messServerSend = new Message(WANT_CHAT, messServerRecive.nameid, messServerRecive.nameid,
							messServerRecive.body);
					os1.writeObject(messServerSend);
					break;
				case BLOCK:
					userBlock.get(messServerRecive.nameid).add(messServerRecive.friend);
					messServerSend = new Message(LOG_SUCCES, null, null, null);
					os.writeObject(messServerSend);
					break;
				default:
					break;
				}
			}

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	// public boolean checkUser(String name) {
	// return listUser.containsKey(name);
	// }
}
