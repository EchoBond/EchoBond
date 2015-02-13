package com.echobond.entity;

/**
 * 
 * @author Luck
 *
 */
public class UserAction {
	public static final int SIGN_UP = 0;
	public static final int SIGN_IN = 1;
	public static final int RESET_PASS = 2;
	public static final int FB_SIGN_UP = 3;
	public static final int FB_SIGN_IN = 4;
	
	public static final int LIKE_TAG = 5;
	public static final int UNLIKE_TAG = 6;
	public static final int TAG_SELF = 7;
	public static final int UNTAG_SELF = 8;
	public static final int FOLLOW_GROUP = 9;
	public static final int UNFOLLOW_GROUP = 10;
	public static final int POST_THOUGHT = 9;
	public static final int BOOST_THOUGHT = 10;
	public static final int COMMENT_THOUGHT = 11;
	
	public static final int SEARCH_USER_TAG = 12;
	public static final int SEARCH_TAG = 13;
	public static final int SEARCH_GROUP = 14;
	
	private int id;
	private int actionType;
	private String userId;
	private int targetId;
	private String info;
	private String time;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getActionType() {
		return actionType;
	}
	public void setActionType(int actionType) {
		this.actionType = actionType;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public int getEchoId() {
		return targetId;
	}
	public void setEchoId(int echoId) {
		this.targetId = echoId;
	}	
	public String getInfo() {
		return info;
	}
	public void setInfo(String info) {
		this.info = info;
	}
	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = time;
	}
	
}
