package com.topica.cuongpg.init;

import java.io.Serializable;

public class Message implements Serializable {
	public String head;
	public String nameid;
	public String friend;
	public String body;

	public Message(String head, String nameid, String friend, String body) {

		this.head = head;
		this.nameid = nameid;
		this.friend = friend;
		this.body = body;
	}

}
