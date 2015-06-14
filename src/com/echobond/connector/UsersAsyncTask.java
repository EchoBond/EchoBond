package com.echobond.connector;

import java.net.ConnectException;
import java.net.SocketTimeoutException;

import org.json.JSONException;
import org.json.JSONObject;

import com.echobond.entity.RawHttpRequest;
import com.echobond.entity.RawHttpResponse;
import com.echobond.entity.User;
import com.echobond.intf.UserAsyncTaskCallback;
import com.echobond.util.HTTPUtil;
import com.echobond.util.JSONUtil;

import android.os.AsyncTask;

/**
 * This task is to handle loading or updates of users in server's DB.<br>
 * Params (Object): url(String), activity(UserAsyncTaskCallback), action(int), / user(User)<br>
 * @version 1.0
 * @author Luck
 * 
 */
public class UsersAsyncTask extends AsyncTask<Object, Integer, JSONObject> {

	public static final Integer USER_LOAD_BY_ID = 1;
	public static final Integer USER_LOAD_BY_USERNAME = 2;
	public static final Integer USER_LOAD_BY_CONDITIONS = 3;
	public static final Integer USER_UPDATE = 4;
	private UserAsyncTaskCallback activity;
	private Integer action;
	@Override
	protected JSONObject doInBackground(Object... params) {
		String url = (String) params[0];
		activity =  (UserAsyncTaskCallback) params[1];
		action = (Integer) params[2];
		JSONObject body = new JSONObject();
		try {
			body.put("action", action);
			if(USER_LOAD_BY_ID == action){
				User user = (User) params[3];
				body.put("user", JSONUtil.fromObjectToJSON(user));
			} else if(USER_LOAD_BY_USERNAME == action){
				//TODO
			} else if(USER_LOAD_BY_CONDITIONS == action){
				//TODO
			} else {
				User user = (User) params[3];
				body.put("user", JSONUtil.fromObjectToJSON(user));
			}
		} catch (JSONException e1) {
			e1.printStackTrace();
		}
		String method = RawHttpRequest.HTTP_METHOD_POST;
		RawHttpRequest request = new RawHttpRequest(url, method, null, body, true);
		RawHttpResponse response = null;
		JSONObject result = null;
		try{
			response = HTTPUtil.getInstance().send(request);
		} catch (SocketTimeoutException e){
			e.printStackTrace();
		} catch (ConnectException e){
			e.printStackTrace();
		}
		if(null != response){
			try{
				result = new JSONObject(response.getMsg());
			} catch (JSONException e){
				e.printStackTrace();
			}
		}
		return result;
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
