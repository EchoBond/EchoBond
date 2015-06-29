package com.echobond.connector;

import org.json.JSONException;
import org.json.JSONObject;
import com.echobond.entity.User;
import com.echobond.intf.UserAsyncTaskCallback;
import com.echobond.util.HTTPUtil;
import com.echobond.util.JSONUtil;

import android.os.AsyncTask;

/**
 * This task is to handle loading or updates of users in server's DB.<br>
 * Params (Object): url(String), activity(UserAsyncTaskCallback), action(int), user(User)<br>
 * 		null/offset(int), limit(int), condition(JSONObject)<br>
 * @version 1.0
 * @author Luck
 * 
 */
public class UsersAsyncTask extends AsyncTask<Object, Integer, JSONObject> {

	public static final Integer USER_LOAD_BY_ID = 1;
	public static final Integer USER_LOAD_BY_EMAIL = 2;
	public static final Integer USER_LOAD_BY_USERNAME = 3;
	public static final Integer USER_LOAD_BY_CONDITIONS = 4;
	public static final Integer USER_UPDATE = 5;
	private UserAsyncTaskCallback activity;
	private Integer action;
	@Override
	protected JSONObject doInBackground(Object... params) {
		String url = (String) params[0];
		activity =  (UserAsyncTaskCallback) params[1];
		action = (Integer) params[2];
		User user = (User) params[3];
		JSONObject body = new JSONObject();
		try {
			body.put("action", action);
			body.put("user", JSONUtil.fromObjectToJSON(user));
			if(USER_LOAD_BY_ID == action){
			} else if(USER_LOAD_BY_CONDITIONS == action){
				Integer offset = (Integer) params[4];
				Integer limit = (Integer) params[5];
				JSONObject condition = (JSONObject) params[6];
				body.put("offset", offset);
				body.put("limit", limit);
				body.put("condition", condition);
			} else {
			}
		} catch (JSONException e1) {
			e1.printStackTrace();
		}
		return HTTPUtil.getInstance().sendRequest(url, body, true);
	}

	@Override
	protected void onPostExecute(JSONObject result) {
		super.onPostExecute(result);
		if(action == USER_UPDATE){
			activity.onUpdateUserResult(result);
		} else {
			activity.onLoadUsersResult(result);
		}

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
