package com.echobond.connector;

import java.util.ArrayList;

import org.json.JSONException;
import org.json.JSONObject;

import com.echobond.R;
import com.echobond.application.MyApp;
import com.echobond.dao.GroupDAO;
import com.echobond.dao.TagDAO;
import com.echobond.dao.UserDAO;
import com.echobond.db.CategoryDB;
import com.echobond.db.CountryDB;
import com.echobond.db.LanguageDB;
import com.echobond.entity.Category;
import com.echobond.entity.Country;
import com.echobond.entity.Group;
import com.echobond.entity.Language;
import com.echobond.entity.Tag;
import com.echobond.entity.User;
import com.echobond.util.HTTPUtil;
import com.echobond.util.JSONUtil;
import com.echobond.util.SPUtil;
import com.google.gson.reflect.TypeToken;

import android.annotation.SuppressLint;
import android.app.IntentService;
import android.content.ContentValues;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;

/**
 * Intent service to fetch data
 * @author Luck
 *
 */
public class InitFetchService extends IntentService {
    public InitFetchService() {
        super("InitFetchIntentService");
    }

    @Override
    public void onCreate() {
    	super.onCreate();
    }
    
    @SuppressWarnings("unchecked")
	@SuppressLint("NewApi")
	@Override
    protected void onHandleIntent(Intent intent) {
    	/* request */
		String url = HTTPUtil.getInstance().composePreURL(this) + getResources().getString(R.string.url_init_fetch);
		JSONObject body = new JSONObject();
		String userId = "";
		try {
			body.put("limit", MyApp.LIMIT_INIT_FETCH);
			body.put("offset", MyApp.DEFAULT_OFFSET);
			User user = new User();
			userId = (String) SPUtil.get(this, MyApp.PREF_TYPE_LOGIN, MyApp.LOGIN_ID, "", String.class);
			if("".equals(userId))
				user.setId("0");
			else user.setId(userId);
			body.put("user", JSONUtil.fromObjectToJSON(user));
		} catch (JSONException e1) {
			e1.printStackTrace();
		}
		JSONObject result = HTTPUtil.getInstance().sendRequest(url, body, true);
		ArrayList<Group> groups = null, followedGroups = null;
		ArrayList<Tag> tags = null, likedTags = null, selfTags = null;
		ArrayList<Category> categories = null;
		ArrayList<Country> countries = null;
		ArrayList<Language> languages = null;
		User user = null;

		TypeToken<ArrayList<Group>> gToken = new TypeToken<ArrayList<Group>>(){};
		TypeToken<ArrayList<Tag>> tToken = new TypeToken<ArrayList<Tag>>(){};
		TypeToken<ArrayList<Category>> cToken = new TypeToken<ArrayList<Category>>(){};
		TypeToken<ArrayList<Country>> countryToken = new TypeToken<ArrayList<Country>>(){};
		TypeToken<ArrayList<Language>> lToken = new TypeToken<ArrayList<Language>>(){};
		
		if(null != result){
			try{
				groups = (ArrayList<Group>) JSONUtil.fromJSONToList(result, "groups", gToken);
				tags = (ArrayList<Tag>) JSONUtil.fromJSONToList(result, "tags", tToken);
				categories = (ArrayList<Category>) JSONUtil.fromJSONToList(result, "categories", cToken);
				countries = (ArrayList<Country>) JSONUtil.fromJSONToList(result, "countries", countryToken);
				languages = (ArrayList<Language>) JSONUtil.fromJSONToList(result, "languages", lToken);
				//groups
				ContentValues values[] = new ContentValues[groups.size()];
				int i = 0;
				for(Group g: groups){
					values[i++] = g.putValues();
				}
				getContentResolver().bulkInsert(GroupDAO.CONTENT_URI_GROUP, values);
				//tags
				values = new ContentValues[tags.size()];
				i = 0;
				for(Tag t: tags){
					values[i++] = t.putValues();
				}
				getContentResolver().bulkInsert(TagDAO.CONTENT_URI_TAG, values);
				//categories
				for(Category c: categories){
					ContentValues v = c.putValues();
					CategoryDB.getInstance().addCategory(v);
				}
				//countries
				for(Country c: countries){
					ContentValues v = c.putValues();
					CountryDB.getInstance().addCountry(v);
				}
				//languages
				for(Language l: languages){
					ContentValues v = l.putValues();
					LanguageDB.getInstance().addLanguage(v);
				}
				//user
				if(!"".equals(userId)){
					user = (User) JSONUtil.fromJSONToObject(result.getJSONObject("user"), User.class);
					getContentResolver().insert(UserDAO.CONTENT_URI_USER, user.putValues());
					//user meta
					JSONObject userMeta = result.getJSONObject("userMeta");
					//liked tags
					likedTags = (ArrayList<Tag>) JSONUtil.fromJSONToList(userMeta, "likedTags", tToken);
					i = 0;
					values = new ContentValues[likedTags.size()];
					for(Tag t: likedTags){
						values[i++] = t.putValues();
					}
					getContentResolver().bulkInsert(TagDAO.CONTENT_URI_TAG, values);
					i = 0;
					values = new ContentValues[likedTags.size()];
					for(Tag t: likedTags){
						ContentValues value = new ContentValues();
						value.put("tag_id", t.getId());
						value.put("user_id", user.getId());
						values[i++] = value;
					}
					getContentResolver().bulkInsert(TagDAO.CONTENT_URI_LIKE, values);
					//followed groups
					followedGroups = (ArrayList<Group>) JSONUtil.fromJSONToList(userMeta, "followedGroups", gToken);
					i = 0;
					values = new ContentValues[followedGroups.size()];
					for(Group g: followedGroups){
						values[i++] = g.putValues();
					}
					getContentResolver().bulkInsert(GroupDAO.CONTENT_URI_GROUP, values);
					i = 0;
					values = new ContentValues[followedGroups.size()];
					for(Group g: followedGroups){
						ContentValues value = new ContentValues();
						value.put("group_id", g.getId());
						value.put("user_id", user.getId());
						values[i++] = value;
					}
					getContentResolver().bulkInsert(GroupDAO.CONTENT_URI_FOLLOW, values);
					//self tags
					selfTags = (ArrayList<Tag>) JSONUtil.fromJSONToList(userMeta, "selfTags", tToken);
					i = 0;
					values = new ContentValues[selfTags.size()];
					for(Tag t: selfTags){
						values[i++] = t.putValues();
					}
					getContentResolver().bulkInsert(TagDAO.CONTENT_URI_TAG, values);
					i = 0;
					values = new ContentValues[selfTags.size()];
					for(Tag t: selfTags){
						ContentValues value = new ContentValues();
						value.put("tag_id", t.getId());
						value.put("user_id", user.getId());
						values[i++] = value;
					}
					getContentResolver().bulkInsert(TagDAO.CONTENT_URI_SELF, values);
				}
			} catch (JSONException e){
				e.printStackTrace();
			}
			LocalBroadcastManager.getInstance(this).sendBroadcast(new Intent(MyApp.BROADCAST_STARTUP));
			stopSelf();
		} else {
			new Thread(){
				@Override
				public void run() {
					try {
						sleep(2000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					LocalBroadcastManager.getInstance(InitFetchService.this).sendBroadcast(new Intent(MyApp.BROADCAST_STARTUP));
					stopSelf();
					
				}
			}.start();
		}
		
    }
    
}