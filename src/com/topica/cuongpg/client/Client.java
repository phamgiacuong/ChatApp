package com.topica.cuongpg.client;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.Scanner;

import com.topica.cuongpg.init.Message;

public class Client {

	public static final String LOG_IN = "log";
	public static final String LOG_ERROR = "logerror";
	public static final String LOG_SUCCES = "logsucces";
	public static final String CHAT = "chat";
	public static final String AGRE = "agre";
	public static final String NOT_AGRE = "notagre";
	public static final String CONNECT_CHAT = "connect";
	public static final String WANT_CHAT = "want chat";
	public static final String CHAT_RECIVE = "chat recive";
	public static final String BLOCK = "block";

	private static int port = 8080;
	public static String nameid;
	public static String friend;
	public static Message messClientRecive;
	public static Message messClientSend;
	public static ObjectOutputStream os = null;
	public static ObjectInputStream is = null;

	public static void main(String[] args) {
		Socket clientSocket = null;
		try {
			clientSocket = new Socket("127.0.0.1", port);

			os = new ObjectOutputStream(clientSocket.getOutputStream());
			is = new ObjectInputStream(clientSocket.getInputStream());
			System.out.println("nhap vao ten cua ban");
			Scanner scan = new Scanner(System.in);
			nameid = scan.nextLine();
			messClientSend = new Message(LOG_IN, nameid, null, null);
			os.writeObject(messClientSend);
			while (true) {
				// is = new ObjectInputStream(new
				// ObjectInputStream(clientSocket.getInputStream()));
				messClientRecive = (Message) is.readObject();
				System.out
						.println(messClientRecive.head + " " + messClientRecive.nameid + " " + messClientRecive.friend);
				String head = messClientRecive.head;
				switch (head) {
				case LOG_ERROR:
					System.out.println("Ten nay da co nguoi dung");
					break;
				case LOG_SUCCES:
//					System.out.println("dang nhap thanh cong moi chon chuc nang");
					System.out.println("1.Chat");
					System.out.println("2.Chan");
					System.out.println("3.Thoat");
					scan = new Scanner(System.in);
					int a = scan.nextInt();
					switch (a) {
					case 1:
						System.out.println("Moi nhap ten ban chat");
						scan = new Scanner(System.in);
						friend = scan.nextLine();
						messClientSend = new Message(WANT_CHAT, nameid, friend, null);
						os.writeObject(messClientSend);
						break;

					case 2:
						System.out.println("Moi nhap ten ban chan");
						scan = new Scanner(System.in);
						String block = scan.nextLine();
						messClientSend = new Message(BLOCK, nameid, block, null);
						os.writeObject(messClientSend);
						break;

					default:
						break;
					}
					break;

				case WANT_CHAT:
					System.out.println(messClientRecive.friend + " muon chat voi ban");
					System.out.println("ban co dong y");
					System.out.println("1.co");
					System.out.println("2.khong");
					scan = new Scanner(System.in);
					int b = scan.nextInt();
					switch (b) {
					case 1:
						messClientSend = new Message(AGRE, nameid, messClientRecive.friend, null);
						os.writeObject(messClientSend);
						break;
					case 2:
						messClientSend = new Message(NOT_AGRE, nameid, messClientRecive.friend, null);
						os.writeObject(messClientSend);
						break;
					default:
						break;
					}
					break;
				case CHAT:
					System.out.println(messClientRecive.friend + ": " + messClientRecive.body);
					messClientSend = connectChat(friend);
					os.writeObject(messClientSend);
					break;

				case AGRE:
					System.out.println(messClientRecive.friend + " dong y chat voi ban");
					System.out.println("moi nhap van ban");
					messClientSend = connectChat(friend);
					os.writeObject(messClientSend);
					break;

				case NOT_AGRE:
					System.out.println(messClientRecive.friend + " khong dong y chat voi ban");
					break;
				case BLOCK:
					System.out.println("ban da bi " + messClientRecive.nameid + " block khong the gui tin nhan");
					break;

				default:
					break;
				}
			}
		} catch (Exception e) {
			// TODO: handle exception
		}

	}

	public static Message connectChat(String friend) throws IOException {
		Scanner scan = new Scanner(System.in);
		String text = scan.nextLine();
		System.out.println("[" + nameid + "]:" + text);
		return new Message(CHAT, nameid, friend, text);
	}
}
