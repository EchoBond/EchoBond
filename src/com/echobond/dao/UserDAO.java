package com.echobond.dao;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.net.Uri;

import com.echobond.db.UserDB;
import com.echobond.entity.User;

public class UserDAO extends ContentProvider{
	
	public static final String PROVIDER_NAME = "com.echobond.contentprovider.user";
	//content://authority/path/id
	public static final Uri CONTENT_URI_USER = Uri.parse("content://"+PROVIDER_NAME+"/user");
	public static final Uri CONTENT_URI_GRP = Uri.parse("content://"+PROVIDER_NAME+"/grp");
	public static final Uri CONTENT_URI_SELF_TAG = Uri.parse("content://"+PROVIDER_NAME+"/selfTag");
	public static final Uri CONTENT_URI_LIKE_TAG = Uri.parse("content://"+PROVIDER_NAME+"/likeTag");
	public static final Uri CONTENT_URI_KEYWORD = Uri.parse("content://"+PROVIDER_NAME+"/keyword");
	
	private static final int USER = 1;
	private static final int GRP = 2;
	private static final int SELF_TAG = 3;
	private static final int LIKE_TAG = 4;
	private static final int KEYWORD = 5;
	private static final UriMatcher uriMatcher;
	static {
		uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
		uriMatcher.addURI(PROVIDER_NAME, "user", USER);
		uriMatcher.addURI(PROVIDER_NAME, "grp", GRP);
		uriMatcher.addURI(PROVIDER_NAME, "selfTag", SELF_TAG);
		uriMatcher.addURI(PROVIDER_NAME, "likeTag", LIKE_TAG);
		uriMatcher.addURI(PROVIDER_NAME, "keyword", KEYWORD);
	}
	
	public void addUser(User user){
		UserDB.getInstance().addUser(user.putValues());
	}
	public Cursor loadUserById(String id){
		return UserDB.getInstance().loadUserById(id);
	}
	public Cursor loadUserByUserName(String userName){
		return UserDB.getInstance().loadUserByUserName(userName);
	}
	public void close(){
		if(null != UserDB.getInstance()){
			UserDB.getInstance().close();
		}
	}
	@Override
	public int delete(Uri arg0, String arg1, String[] arg2) {
		// TODO Auto-generated method stub
		return 0;
	}
	@Override
	public String getType(Uri arg0) {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public Uri insert(Uri uri, ContentValues values) {
		if(uriMatcher.match(uri) == USER){
			UserDB.getInstance().addUser(values);
		}
		return uri;
	}
	@Override
	public boolean onCreate() {
		return false;
	}
	@Override
	public Cursor query(Uri uri, String[] projection, String selection,
			String[] selectionArgs, String sortOrder) {
		Cursor cursor = null;
		switch(uriMatcher.match(uri)){
		case USER:
			cursor = UserDB.getInstance().loadUserById(selectionArgs);
			break;
		case GRP:
			cursor = UserDB.getInstance().loadUserByGroup(selectionArgs);
			break;
		case SELF_TAG:
			cursor = UserDB.getInstance().loadUserBySelfTag(selectionArgs);
			break;
		case LIKE_TAG:
			cursor = UserDB.getInstance().loadUserByLikeTag(selectionArgs);
			break;
		case KEYWORD:
			cursor = UserDB.getInstance().loadUserByKeyword(selectionArgs);
			break;
		}
		cursor.setNotificationUri(getContext().getContentResolver(), uri);
		return cursor;
	}
	@Override
	public int update(Uri arg0, ContentValues arg1, String arg2, String[] arg3) {
		// TODO Auto-generated method stub
		return 0;
	}
}
