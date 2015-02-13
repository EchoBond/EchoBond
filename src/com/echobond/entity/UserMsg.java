package com.echobond.entity;

/**
 * 
 * @author Luck
 *
 */
public class UserMsg {
	private int id;
	private String senderId;
	private String recverId;
	private String time;
	private String content;
	
	private User sender;
	private User recver;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getSenderId() {
		return senderId;
	}
	public void setSenderId(String senderId) {
		this.senderId = senderId;
	}
	public String getRecverId() {
		return recverId;
	}
	public void setRecverId(String recverId) {
		this.recverId = recverId;
	}
	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = time;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public User getSender() {
		return sender;
	}
	public void setSender(User sender) {
		this.sender = sender;
	}
	public User getRecver() {
		return recver;
	}
	public void setRecver(User recver) {
		this.recver = recver;
	}
	
}
