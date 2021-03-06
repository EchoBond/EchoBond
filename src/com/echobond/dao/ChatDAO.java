package com.echobond.dao;

import android.content.ContentProvider;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.net.Uri;

import com.echobond.db.ChatDB;

public class ChatDAO extends ContentProvider{
	
	public static final String PROVIDER_NAME = "com.echobond.contentprovider.chat";
	//content://authority/path/id
	public static final Uri CONTENT_URI = Uri.parse("content://"+PROVIDER_NAME+"/chat");
	private static final int CHAT = 1;
	private static final UriMatcher uriMatcher;
	static {
		uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
		uriMatcher.addURI(PROVIDER_NAME, "chat", CHAT);
	}

	public void close(){
		if(null != ChatDB.getInstance()){
			ChatDB.getInstance().close();
		}
	}
	@Override
	public int delete(Uri uri, String selection, String[] selectionArgs) {
		
		return 0;
	}
	@Override
	public String getType(Uri uri) {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public Uri insert(Uri uri, ContentValues values) {
		if(uriMatcher.match(uri) == CHAT){
			ChatDB.getInstance().addMsg(values);
		}
		return uri;
	}
	
	@Override
	public int bulkInsert(Uri uri, ContentValues[] values) {
		if(uriMatcher.match(uri) == CHAT){
			for (ContentValues contentValues : values) {
				ChatDB.getInstance().addMsg(contentValues);
			}
		}
		return 0;
	}
	
	@Override
	public boolean onCreate() {
		return false;
	}
	@Override
	public Cursor query(Uri uri, String[] projection, String selection,
			String[] selectionArgs, String sortOrder) {
		Cursor cursor = null;
		if(uriMatcher.match(uri) == CHAT){
			//load message list
			if(selectionArgs.length == 1){
				cursor = ChatDB.getInstance().loadMsgList(selectionArgs);	
			}
			//load messages
			else if(selectionArgs.length == 6){
				cursor = ChatDB.getInstance().loadMsg(selectionArgs);
			}
			//count unread
			else {
				String userId = selectionArgs[0];
				cursor = ChatDB.getInstance().countUnreadMsg(new String[]{userId});
			}
			cursor.setNotificationUri(getContext().getContentResolver(), uri);
		}
		return cursor;
	}
	@Override
	public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
		if(uriMatcher.match(uri) == CHAT){
			ChatDB.getInstance().updateMsg(values, selection, selectionArgs);
			ContentResolver resolver = getContext().getContentResolver();
			resolver.notifyChange(uri, null);
		}
		return 0;
	}

}
