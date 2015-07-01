package com.echobond.connector;

import java.util.ArrayList;
import java.util.Arrays;

import org.json.JSONException;
import org.json.JSONObject;

import com.echobond.R;
import com.echobond.application.MyApp;
import com.echobond.dao.GroupDAO;
import com.echobond.dao.TagDAO;
import com.echobond.dao.UserDAO;
import com.echobond.entity.Group;
import com.echobond.entity.Tag;
import com.echobond.entity.User;
import com.echobond.util.HTTPUtil;
import com.echobond.util.JSONUtil;
import com.google.gson.reflect.TypeToken;

import android.app.IntentService;
import android.content.ContentValues;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;

public class UpdateUserProfileService extends IntentService {

	public UpdateUserProfileService() {
		super("UpdateUserProfileService");
	}

	@Override
	public void onCreate() {
		super.onCreate();
	}
	
	@SuppressWarnings("unchecked")
	@Override
	protected void onHandleIntent(Intent intent) {
		try{
			/* extraction */
			Bundle bundle = intent.getExtras();
			User user = (User) bundle.getSerializable("user");
			String[] selfTags = (String[]) bundle.getStringArray("selfTags");
			String[] likedTags = (String[])bundle.getStringArray("likedTags");
			String[] followedGroups = (String[]) bundle.getStringArray("followedGroups");
			boolean isSelfTag = (null != selfTags) && (selfTags.length > 0);
			boolean isLikedTag = (null != likedTags) && (likedTags.length > 0);
			boolean isFollowedGroup = (null != followedGroups) && (followedGroups.length > 0);
			ContentValues[] contentValues = null;
			int i = 0;
			ArrayList<Tag> newTags = null;
			ArrayList<Group> newGroups = null;
			
			/* update user */
			String url = HTTPUtil.getInstance().composePreURL(this) + getResources().getString(R.string.url_load_users);
			JSONObject body = new JSONObject();
			User check = new User();
			check.setUserName(user.getUserName());
			body.put("user", JSONUtil.fromObjectToJSON(check));
			body.put("action", UsersAsyncTask.USER_LOAD_BY_USERNAME);
			JSONObject result = HTTPUtil.getInstance().sendRequest(url, body, true);
			check = (User) JSONUtil.fromJSONToObject(result.getJSONObject("user"), User.class);
			//username in use by other user
			if(check.getId() != null && !check.getId().isEmpty() && !check.getId().equals(user.getId())){
				Intent backIntent = new Intent(MyApp.BROADCAST_UPDATE_PROFILE);
				backIntent.putExtra("duplicateUserName", true);
				LocalBroadcastManager.getInstance(this).sendBroadcast(backIntent);
				stopSelf();
				return;
			}
			body = new JSONObject();
			body.put("user", JSONUtil.fromObjectToJSON(user));
			body.put("action", UsersAsyncTask.USER_UPDATE);
			result = HTTPUtil.getInstance().sendRequest(url, body, true);
			getContentResolver().insert(UserDAO.CONTENT_URI_USER, user.putValues());
			
			/* update tags */
			ArrayList<String> tags = new ArrayList<String>();
			if(isSelfTag)
				tags.addAll(Arrays.asList(selfTags));
			if(isLikedTag){
				for(String t: likedTags){
					if(!tags.contains(t))
						tags.add(t);
				}
			}
			url = HTTPUtil.getInstance().composePreURL(this) + getResources().getString(R.string.url_upd_tags);
			body = new JSONObject();
			body.put("tags", JSONUtil.fromListToJSONArray(tags, new TypeToken<ArrayList<String>>(){}));
			result = HTTPUtil.getInstance().sendRequest(url, body, true);
			newTags = (ArrayList<Tag>) JSONUtil.fromJSONToList(result, "tags", new TypeToken<ArrayList<Tag>>(){});
			if(null != newTags && newTags.size() > 0){
				contentValues = new ContentValues[newTags.size()];
				i = 0;
				for(Tag t: newTags){
					contentValues[i++] = t.putValues();
				}
				getContentResolver().bulkInsert(TagDAO.CONTENT_URI_TAG, contentValues);
			}
			
			/* update groups */
			ArrayList<String> groups = new ArrayList<String>();
			if(null != followedGroups && followedGroups.length != 0)
				groups.addAll(Arrays.asList(followedGroups));
			url = HTTPUtil.getInstance().composePreURL(this) + getResources().getString(R.string.url_upd_group);
			body = new JSONObject();
			body.put("groups", JSONUtil.fromListToJSONArray(groups, new TypeToken<ArrayList<String>>(){}));
			result = HTTPUtil.getInstance().sendRequest(url, body, true);
			newGroups = (ArrayList<Group>) JSONUtil.fromJSONToList(result, "groups", new TypeToken<ArrayList<Group>>(){});
			if(null != newGroups && newGroups.size() > 0){
				i = 0;
				contentValues = new ContentValues[newGroups.size()];
				for(Group g: newGroups){
					contentValues[i++] = g.putValues();
				}
				getContentResolver().bulkInsert(GroupDAO.CONTENT_URI_GROUP, contentValues);
			}
			
			/* update user tag self */
			//clear previous tags
			String selection = "user_id = ?";
			String selectionArgs[] = new String[]{user.getId()};
			getContentResolver().delete(TagDAO.CONTENT_URI_SELF, selection, selectionArgs);
			ArrayList<Integer> selfTagIds = new ArrayList<Integer>();
			if(isSelfTag){
				for(Tag t: newTags){
					if(Arrays.asList(selfTags).contains(t.getName())){
						selfTagIds.add(t.getId());
					}
				}
			}
			url = HTTPUtil.getInstance().composePreURL(this) + getResources().getString(R.string.url_tag_self);
			body = new JSONObject();
			body.put("user", JSONUtil.fromObjectToJSON(user));
			body.put("tagIds", JSONUtil.fromListToJSONArray(selfTagIds, new TypeToken<ArrayList<Integer>>(){}));
			result = HTTPUtil.getInstance().sendRequest(url, body, true);
			i = 0;
			contentValues = new ContentValues[selfTagIds.size()];
			for(Integer id: selfTagIds){
				ContentValues values = new ContentValues();
				values.put("user_id", user.getId());
				values.put("tag_id", id);
				contentValues[i++] = values;
			}
			getContentResolver().bulkInsert(TagDAO.CONTENT_URI_SELF, contentValues);

			/* update user like tag */
			//clear previous tags
			selection = "user_id = ?";
			selectionArgs = new String[]{user.getId()};
			getContentResolver().delete(TagDAO.CONTENT_URI_LIKE, selection, selectionArgs);			
			ArrayList<Integer> likedTagIds = new ArrayList<Integer>();
			if(isLikedTag){
				for(Tag t: newTags){
					if(Arrays.asList(likedTags).contains(t.getName())){
						likedTagIds.add(t.getId());
					}
				}
			}
			url = HTTPUtil.getInstance().composePreURL(this) + getResources().getString(R.string.url_follow_tag);
			body = new JSONObject();
			body.put("user", JSONUtil.fromObjectToJSON(user));
			body.put("tagIds", JSONUtil.fromListToJSONArray(likedTagIds, new TypeToken<ArrayList<Integer>>(){}));
			result = HTTPUtil.getInstance().sendRequest(url, body, true);
			i = 0;
			contentValues = new ContentValues[likedTagIds.size()];
			for(Integer id: likedTagIds){
				ContentValues values = new ContentValues();
				values.put("user_id", user.getId());
				values.put("tag_id", id);
				contentValues[i++] = values;
			}
			getContentResolver().bulkInsert(TagDAO.CONTENT_URI_LIKE, contentValues);				
			
			/* update user follow group */
			//clear previous groups
			selection = "user_id = ?";
			selectionArgs = new String[]{user.getId()};
			getContentResolver().delete(GroupDAO.CONTENT_URI_FOLLOW, selection, selectionArgs);			
			ArrayList<Integer> followedGroupIds = new ArrayList<Integer>();
			if(isFollowedGroup){
				for(Group g: newGroups){
					if(Arrays.asList(followedGroups).contains(g.getName())){
						followedGroupIds.add(g.getId());
					}
				}
			}
			url = HTTPUtil.getInstance().composePreURL(this) + getResources().getString(R.string.url_follow_group);
			body = new JSONObject();
			body.put("user", JSONUtil.fromObjectToJSON(user));
			body.put("groupIds", JSONUtil.fromListToJSONArray(followedGroupIds, new TypeToken<ArrayList<Integer>>(){}));
			result = HTTPUtil.getInstance().sendRequest(url, body, true);
			i = 0;
			contentValues = new ContentValues[followedGroupIds.size()];
			for(Integer id: followedGroupIds){
				ContentValues values = new ContentValues();
				values.put("user_id", user.getId());
				values.put("group_id", id);
				contentValues[i++] = values;
			}
			getContentResolver().bulkInsert(GroupDAO.CONTENT_URI_FOLLOW, contentValues);
			LocalBroadcastManager.getInstance(this).sendBroadcast(new Intent(MyApp.BROADCAST_UPDATE_PROFILE));
			
		} catch (JSONException e){
			e.printStackTrace();
		}
	}

}
