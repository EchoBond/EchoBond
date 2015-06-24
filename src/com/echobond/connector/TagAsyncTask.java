package com.echobond.connector;

import java.util.ArrayList;

import org.json.JSONObject;

import com.echobond.entity.Tag;
import com.echobond.entity.User;
import com.echobond.intf.TagCallback;
import com.echobond.util.HTTPUtil;
import com.echobond.util.JSONUtil;

import android.os.AsyncTask;

/**
 * 
 * This task is to handle tag loading and update in server's DB.<br>
 * Params (Object): action(int), url(String), activity(TagAsyncTaskCallback), tags(ArrayList<Tags>) / user(User) / null<br>
 * Progress (Integer)<br>
 * Result (JSONObject)<br>
 * @version 1.0
 * @author Luck
 * 
 */
public class TagAsyncTask extends AsyncTask<Object, Integer, JSONObject> {

	private TagCallback activity;
	public static final int TAG_UPDATE = 1;
	public static final int TAG_LOAD = 2;
	public static final int TAG_LOAD_ALL = 3;
	@SuppressWarnings("unchecked")
	@Override
	protected JSONObject doInBackground(Object... params) {
		int action = (Integer) params[0];
		String url = (String) params[1];
		activity = (TagCallback) params[2];
		ArrayList<Tag> tags = null;
		User user = null;
		JSONObject body = null;
		if(action == TAG_UPDATE){
			tags = (ArrayList<Tag>) params[3];
			body = JSONUtil.fromObjectToJSON(tags);
		} else if(action == TAG_LOAD){
			user = (User) params[3];
			body = JSONUtil.fromObjectToJSON(user);
		} else if(action == TAG_LOAD_ALL){

		}
		return HTTPUtil.getInstance().sendRequest(url, body, true);
	}

	@Override
	protected void onPostExecute(JSONObject result) {
		super.onPostExecute(result);
		activity.onTagResult(result);
	}
	
	@Override
	protected void onProgressUpdate(Integer... values) {
		super.onProgressUpdate(values);
	}
	
	@Override
	protected void onPreExecute() {
		super.onPreExecute();
	}
	
	@Override
	protected void onCancelled() {
		super.onCancelled();
	}
	

}
