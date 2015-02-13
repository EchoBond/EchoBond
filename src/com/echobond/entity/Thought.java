package com.echobond.entity;

import java.sql.Blob;
import java.util.ArrayList;

/**
 * 
 * @author Luck
 *
 */
public class Thought {
	private int id;
	private int echoerId;
	private int langId;
	private int groupId;
	private String content;
	private Blob image;
	private String time;

	private User echoer;
	private Language lang;
	private Group group;
	private ArrayList<Tag> tags;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getEchoerId() {
		return echoerId;
	}
	public void setEchoerId(int echoerId) {
		this.echoerId = echoerId;
	}
	public int getLangId() {
		return langId;
	}
	public void setLangId(int langId) {
		this.langId = langId;
	}
	public int getGroupId() {
		return groupId;
	}
	public void setGroupId(int groupId) {
		this.groupId = groupId;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public Blob getImage() {
		return image;
	}
	public void setImage(Blob image) {
		this.image = image;
	}
	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = time;
	}
	public User getEchoer() {
		return echoer;
	}
	public void setEchoer(User echoer) {
		this.echoer = echoer;
	}
	public Language getLang() {
		return lang;
	}
	public void setLang(Language lang) {
		this.lang = lang;
	}
	public Group getGroup() {
		return group;
	}
	public void setGroup(Group group) {
		this.group = group;
	}
	public ArrayList<Tag> getTags() {
		return tags;
	}
	public void setTags(ArrayList<Tag> tags) {
		this.tags = tags;
	}

	
}
