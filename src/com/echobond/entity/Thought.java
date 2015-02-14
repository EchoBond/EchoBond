package com.echobond.entity;

import java.util.ArrayList;

import android.graphics.Bitmap;


/**
 * 
 * @author Luck
 *
 */
public class Thought {
	private int id;
	private int userId;
	private int langId;
	private int groupId;
	private String content;
	private Bitmap image;
	private String time;

	private User user;
	private Language lang;
	private Group group;
	private ArrayList<Integer> tagIds;
	private ArrayList<Tag> tags;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getUserId() {
		return userId;
	}
	public void setUserId(int userId) {
		this.userId = userId;
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
	public Bitmap getImage() {
		return image;
	}
	public void setImage(Bitmap image) {
		this.image = image;
	}
	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = time;
	}
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
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
	public ArrayList<Integer> getTagIds() {
		return tagIds;
	}
	public void setTagIds(ArrayList<Integer> tagIds) {
		this.tagIds = tagIds;
	}
	public ArrayList<Tag> getTags() {
		return tags;
	}
	public void setTags(ArrayList<Tag> tags) {
		this.tags = tags;
	}

}
