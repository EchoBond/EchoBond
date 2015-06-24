package com.echobond.connector;

import java.util.ArrayList;

import org.json.JSONException;
import org.json.JSONObject;

import com.echobond.R;
import com.echobond.application.MyApp;
import com.echobond.dao.GroupDAO;
import com.echobond.dao.TagDAO;
import com.echobond.entity.Category;
import com.echobond.entity.Group;
import com.echobond.entity.Tag;
import com.echobond.util.HTTPUtil;
import com.echobond.util.JSONUtil;
import com.google.gson.reflect.TypeToken;

import android.annotation.SuppressLint;
import android.app.IntentService;
import android.content.ContentValues;
import android.content.Intent;

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
		try {
			body.put("limit", MyApp.LIMIT_INIT_FETCH);
			body.put("offset", MyApp.DEFAULT_OFFSET);
		} catch (JSONException e1) {
			e1.printStackTrace();
		}
		JSONObject result = HTTPUtil.getInstance().sendRequest(url, body, true);
		ArrayList<Group> groups = null;
		ArrayList<Tag> tags = null;
		ArrayList<Category> categories = null;

		TypeToken<ArrayList<Group>> gToken = new TypeToken<ArrayList<Group>>(){};
		TypeToken<ArrayList<Tag>> tToken = new TypeToken<ArrayList<Tag>>(){};
		TypeToken<ArrayList<Category>> cToken = new TypeToken<ArrayList<Category>>(){};
		groups = (ArrayList<Group>) JSONUtil.fromJSONToList(result, "groups", gToken);
		tags = (ArrayList<Tag>) JSONUtil.fromJSONToList(result, "tags", tToken);
		categories = (ArrayList<Category>) JSONUtil.fromJSONToList(result, "categories", cToken);
			
		ContentValues values[] = new ContentValues[groups.size()];
		int i = 0;
		for(Group g: groups){
			values[i++] = g.putValues();
		}
		getContentResolver().bulkInsert(GroupDAO.CONTENT_URI, values);
		
		values = new ContentValues[tags.size()];
		i = 0;
		for(Tag t: tags){
			values[i++] = t.putValues();
		}
		getContentResolver().bulkInsert(TagDAO.CONTENT_URI, values);

		values = new ContentValues[categories.size()];
		i = 0;
		for(Category c: categories){
			values[i++] = c.putValues();
		}
		stopSelf();
    }
    
}