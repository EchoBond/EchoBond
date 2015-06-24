package com.echobond.connector;

import java.util.ArrayList;

import org.json.JSONObject;

import com.echobond.entity.Group;
import com.echobond.entity.User;
import com.echobond.intf.GroupCallback;
import com.echobond.util.HTTPUtil;
import com.echobond.util.JSONUtil;

import android.os.AsyncTask;

/**
 * 
 * This task is to handle group loading or update in server's DB.<br>
 * Params (Object): action(int), url(String), activity(PostThoughtAsyncTaskCallback), groups(ArrayList<Group>) / user(User) / null<br>
 * Progress (Integer)<br>
 * Result (JSONObject)<br>
 * @version 1.0
 * @author Luck
 *
 */
public class GroupAsyncTask extends AsyncTask<Object, Integer, JSONObject> {

	private GroupCallback activity;
	public static final int GROUP_UPDATE = 1;
	public static final int GROUP_LOAD = 2;
	public static final int GROUP_LOAD_ALL = 3;
	@SuppressWarnings("unchecked")
	@Override
	protected JSONObject doInBackground(Object... params) {
		int action = (Integer) params[0];
		String baseUrl = (String) params[1];
		activity = (GroupCallback) params[2];
		ArrayList<Group> groups = null;
		JSONObject body = new JSONObject();
		String url = baseUrl;
		if(action == GROUP_UPDATE){
			groups = (ArrayList<Group>) params[3];
			body = JSONUtil.fromObjectToJSON(groups);
		} else if(action == GROUP_LOAD){
			User user = (User) params[3];
			body = JSONUtil.fromObjectToJSON(user);
		} else if(action == GROUP_LOAD_ALL){
			
		}
		return HTTPUtil.getInstance().sendRequest(url, body, true);
	}

	@Override
	protected void onPostExecute(JSONObject result) {
		super.onPostExecute(result);
		activity.onGroupResult(result);
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
