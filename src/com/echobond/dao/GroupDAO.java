package com.echobond.dao;

import android.content.ContentProvider;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.net.Uri;

import com.echobond.db.GroupDB;

public class GroupDAO extends ContentProvider{
	
	public static final String PROVIDER_NAME = "com.echobond.contentprovider.group";
	//content://authority/path/id
	public static final Uri CONTENT_URI_GROUP = Uri.parse("content://"+PROVIDER_NAME+"/group");
	public static final Uri CONTENT_URI_GROUP_ID = Uri.parse("content://"+PROVIDER_NAME+"/id");
	public static final Uri CONTENT_URI_FOLLOW = Uri.parse("content://"+PROVIDER_NAME+"/follow");
	private static final int GROUP = 1;
	private static final int GROUP_ID = 2;
	private static final int GROUP_FOLLOW = 3;
	private static final UriMatcher uriMatcher;
	static {
		uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
		uriMatcher.addURI(PROVIDER_NAME, "group", GROUP);
		uriMatcher.addURI(PROVIDER_NAME, "id", GROUP_ID);
		uriMatcher.addURI(PROVIDER_NAME, "follow", GROUP_FOLLOW);
	}

	public void close(){
		if(null != GroupDB.getInstance()){
			GroupDB.getInstance().close();
		}
	}
	@Override
	public int delete(Uri uri, String selection, String[] selectionArgs) {
		switch(uriMatcher.match(uri)){
		case GROUP:
			break;
		case GROUP_FOLLOW:
			return GroupDB.getInstance().removeFollowedGroups(selection, selectionArgs);
		}
		return 0;
	}
	@Override
	public String getType(Uri uri) {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public Uri insert(Uri uri, ContentValues values) {
		switch(uriMatcher.match(uri)){
		case GROUP:
			GroupDB.getInstance().addGroup(values);
			break;
		case GROUP_FOLLOW:
			GroupDB.getInstance().addGroupFollow(values);
		}
		return uri;
	}
	
	@Override
	public int bulkInsert(Uri uri, ContentValues[] values) {
		for (ContentValues contentValues : values) {
			insert(uri, contentValues);
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
		switch(uriMatcher.match(uri)){
		case GROUP:
			cursor = GroupDB.getInstance().loadGroups(selectionArgs);
			break;
		case GROUP_FOLLOW:
			cursor = GroupDB.getInstance().loadGroupsFollow(selectionArgs);
		}
		cursor.setNotificationUri(getContext().getContentResolver(), uri);
		return cursor;
	}
	@Override
	public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
		if(uriMatcher.match(uri) == GROUP){
			GroupDB.getInstance().updateGroup(values, selection, selectionArgs);
			ContentResolver resolver = getContext().getContentResolver();
			resolver.notifyChange(uri, null);
		}
		return 0;
	}
	
	public Cursor loadGroupByHomeThought(String[] args){
		return GroupDB.getInstance().loadGroupHome(args);
	}
	
	public Cursor loadGroupByHotThought(String[] args){
		return GroupDB.getInstance().loadGroupHot(args);
	}

	public Cursor loadGroupById(String[] args){
		return GroupDB.getInstance().loadGroupById(args);
	}
}
