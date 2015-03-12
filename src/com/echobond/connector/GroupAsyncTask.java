package com.echobond.connector;

import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.util.ArrayList;

import org.json.JSONException;
import org.json.JSONObject;

import com.echobond.entity.Group;
import com.echobond.entity.RawHttpRequest;
import com.echobond.entity.RawHttpResponse;
import com.echobond.entity.User;
import com.echobond.intf.GroupCallback;
import com.echobond.util.HTTPUtil;
import com.echobond.util.JSONUtil;

import android.os.AsyncTask;

/**
 * @version 1.0
 * @author Luck
 * This task is to handle group loading or update in server's DB.
 * Params (Object): action(int), url(String), activity(PostThoughtAsyncTaskCallback), groups(ArrayList<Group>) / user(User) / null
 * Progress (Integer)
 * Result (JSONObject)
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
		String method = RawHttpRequest.HTTP_METHOD_POST;
		if(action == GROUP_UPDATE){
			groups = (ArrayList<Group>) params[3];
			body = JSONUtil.fromObjectToJSON(groups);
		} else if(action == GROUP_LOAD){
			User user = (User) params[3];
			body = JSONUtil.fromObjectToJSON(user);
		} else if(action == GROUP_LOAD_ALL){
			
		}
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
