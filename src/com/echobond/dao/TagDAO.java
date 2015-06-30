package com.echobond.dao;

import com.echobond.db.TagDB;

import android.content.ContentProvider;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.net.Uri;

public class TagDAO extends ContentProvider{
	
	public static final String PROVIDER_NAME = "com.echobond.contentprovider.tag";
	//content://authority/path/id
	public static final Uri CONTENT_URI_TAG = Uri.parse("content://"+PROVIDER_NAME+"/tag");
	public static final Uri CONTENT_URI_SELF = Uri.parse("content://"+PROVIDER_NAME+"/self");
	public static final Uri CONTENT_URI_LIKE = Uri.parse("content://"+PROVIDER_NAME+"/like");
	public static final Uri CONTENT_URI_SELF_LIST = Uri.parse("content://"+PROVIDER_NAME+"/selflist");
	public static final Uri CONTENT_URI_LIKE_LIST = Uri.parse("content://"+PROVIDER_NAME+"/likelist");
	private static final int TAG = 1;
	private static final int TAG_SELF = 2;
	private static final int TAG_SELF_LIST = 3;
	private static final int TAG_LIKE = 4;
	private static final int TAG_LIKE_LIST = 5;
	private static final UriMatcher uriMatcher;
	static {
		uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
		uriMatcher.addURI(PROVIDER_NAME, "tag", TAG);
		uriMatcher.addURI(PROVIDER_NAME, "self", TAG_SELF);
		uriMatcher.addURI(PROVIDER_NAME, "like", TAG_LIKE);
		uriMatcher.addURI(PROVIDER_NAME, "selflist", TAG_SELF_LIST);
		uriMatcher.addURI(PROVIDER_NAME, "likelist", TAG_LIKE_LIST);
	}

	public void close(){
		if(null != TagDB.getInstance()){
			TagDB.getInstance().close();
		}
	}
	@Override
	public int delete(Uri uri, String selection, String[] selectionArgs) {
		switch(uriMatcher.match(uri)){
		case TAG:
			break;
		case TAG_LIKE:
			return TagDB.getInstance().removeLikedTags(selection, selectionArgs);
		case TAG_SELF:
			return TagDB.getInstance().removeSelfTags(selection, selectionArgs);
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
		switch (uriMatcher.match(uri)) {
		case TAG:
			TagDB.getInstance().addTag(values);			
			break;
		case TAG_LIKE:
			TagDB.getInstance().addTagLike(values);
			break;
		case TAG_SELF:
			TagDB.getInstance().addTagSelf(values);
			break;
		default:
			break;
		}
		return uri;
	}
	
	@Override
	public int bulkInsert(Uri uri, ContentValues[] values) {
		switch(uriMatcher.match(uri)){
		case TAG:
			for (ContentValues contentValues : values) {
				TagDB.getInstance().addTag(contentValues);
			}
			break;
		case TAG_LIKE:
			for (ContentValues contentValues : values) {
				TagDB.getInstance().addTagLike(contentValues);
			}
			break;
		case TAG_SELF:
			for (ContentValues contentValues : values) {
				TagDB.getInstance().addTagSelf(contentValues);
			}
			break;
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
		case TAG:
			cursor = TagDB.getInstance().loadTags(selectionArgs);
			break;
		case TAG_LIKE:
			cursor = TagDB.getInstance().loadTagsLike(selectionArgs);
			break;
		case TAG_SELF:
			cursor = TagDB.getInstance().loadTagsSelf(selectionArgs);
			break;
		case TAG_SELF_LIST:
			cursor = TagDB.getInstance().loadTagsWithSelf(selectionArgs);
			break;
		case TAG_LIKE_LIST:
			cursor = TagDB.getInstance().loadTagsWithLike(selectionArgs);
			break;
		}
		cursor.setNotificationUri(getContext().getContentResolver(), uri);
		return cursor;
	}
	@Override
	public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
		if(uriMatcher.match(uri) == TAG){
			TagDB.getInstance().updateTag(values, selection, selectionArgs);
			ContentResolver resolver = getContext().getContentResolver();
			resolver.notifyChange(uri, null);
		}
		return 0;
	}
	
	public Cursor loadTagsByHomeThought(String args[]){
		return TagDB.getInstance().loadTagsHome(args);
	}
	
	public Cursor loadTagsByHotThought(String args[]){
		return TagDB.getInstance().loadTagsHot(args);
	}

	public Cursor loadTagsByThought(String args[]){
		return TagDB.getInstance().loadTagsByThought(args);
	}
}
