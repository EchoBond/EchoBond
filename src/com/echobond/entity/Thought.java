package com.echobond.entity;

import java.util.ArrayList;

import com.echobond.util.ImageUtil;

import android.content.ContentValues;
import android.graphics.Bitmap;


/**
 * 
 * @author Luck
 *
 */
public class Thought {
	private int id;
	private String userId;
	private int langId;
	private int groupId;
	private int categoryId;
	private String content;
	private String image;
	private String time;
	private int boost;

	private int isUserBoost;
	private User user;
	private Language lang;
	private Group group;
	private Category category;
	private ArrayList<Integer> tagIds;
	private ArrayList<Tag> tags;
	
	private ArrayList<Comment> comments;
	
	public Bitmap getImgBitmap(){
		return ImageUtil.getBitmapByPath(image);
	}
	
	public ContentValues putValues(){
		ContentValues values = new ContentValues();
		values.put("_id", id);
		values.put("user_id", userId);
		values.put("lang_id", langId);
		values.put("group_id", groupId);
		values.put("category_id", categoryId);
		values.put("content", content);
		values.put("image", image);
		values.put("time", time);
		values.put("boost", boost);
		values.put("isUserBoost", isUserBoost);
		return values;
	}
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
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
	public String getImage() {
		return image;
	}
	public void setImage(String image) {
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
	public int getCategoryId() {
		return categoryId;
	}
	public void setCategoryId(int categoryId) {
		this.categoryId = categoryId;
	}
	public Category getCategory() {
		return category;
	}
	public void setCategory(Category category) {
		this.category = category;
	}
	public int getBoost() {
		return boost;
	}
	public void setBoost(int boost) {
		this.boost = boost;
	}
	public ArrayList<Comment> getComments() {
		return comments;
	}
	public void setComments(ArrayList<Comment> comments) {
		this.comments = comments;
	}
	public int getIsUserBoost() {
		return isUserBoost;
	}
	public void setIsUserBoost(int isUserBoost) {
		this.isUserBoost = isUserBoost;
	}

}
