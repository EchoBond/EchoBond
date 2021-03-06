package com.echobond.dao;

import com.echobond.db.CommentDB;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.net.Uri;

public class CommentDAO extends ContentProvider{

	public static final String PROVIDER_NAME = "com.echobond.contentprovider.comment";
	public static final Uri CONTENT_URI = Uri.parse("content://"+PROVIDER_NAME+"/comment");

	private static final int COMMENT = 1;
	private static final UriMatcher uriMatcher;

	static {
		uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
		uriMatcher.addURI(PROVIDER_NAME, "comment", COMMENT);
	}

	public void close(){
		if(null != CommentDB.getInstance()){
			CommentDB.getInstance().close();
		}
	}
	@Override
	public int delete(Uri uri, String selection, String[] selectionArgs) {
		// TODO Auto-generated method stub
		return 0;
	}
	@Override
	public String getType(Uri uri) {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public Uri insert(Uri uri, ContentValues values) {
		if(uriMatcher.match(uri) == COMMENT){
			CommentDB.getInstance().addComment(values);
			getContext().getContentResolver().notifyChange(uri, null);
		}
		return uri;
	}
	@Override
	public int bulkInsert(Uri uri, ContentValues[] values) {
		if(uriMatcher.match(uri) == COMMENT){
			for(ContentValues value: values){
				CommentDB.getInstance().addComment(value);
			}
			getContext().getContentResolver().notifyChange(uri, null);
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
		if(uriMatcher.match(uri) == COMMENT){
			int id;
			if(null == selection || selection.isEmpty())
				id = 0;
			else id = Integer.parseInt(selection);
			cursor = CommentDB.getInstance().loadComments(id);
			cursor.setNotificationUri(getContext().getContentResolver(), uri);
		}
		return cursor;
	}
	@Override
	public int update(Uri uri, ContentValues values, String selection,
			String[] selectionArgs) {
		// TODO Auto-generated method stub
		return 0;
	}
	
}
