package com.echobond.connector;

import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.util.ArrayList;

import org.json.JSONException;
import org.json.JSONObject;

import com.echobond.entity.RawHttpRequest;
import com.echobond.entity.RawHttpResponse;
import com.echobond.entity.User;
import com.echobond.intf.FollowTagsCallback;
import com.echobond.util.HTTPUtil;
import com.echobond.util.JSONUtil;
import com.google.gson.reflect.TypeToken;

import android.os.AsyncTask;

/**
 * This task is to handle following of tags updates in server's DB.<br>
 * Params (Object): url(String), activity(FollowTagsCallback), user(User), tagIds(ArrayList<Integer>)<br>
 * @version 1.0
 * @author Luck
 * 
 */
public class FollowTagsAsyncTask extends AsyncTask<Object, Integer, JSONObject> {

	private FollowTagsCallback activity;
	@Override
	protected JSONObject doInBackground(Object... params) {
		String url = (String) params[0];
		activity =  (FollowTagsCallback) params[1];
		User u = (User) params[2];
		@SuppressWarnings("unchecked")
		ArrayList<Integer> tagIds = (ArrayList<Integer>) params[3];
		JSONObject body = new JSONObject();
		try {
			body.put("user", JSONUtil.fromObjectToJSON(u));
			body.put("tagIds", JSONUtil.fromListToJSONArray(tagIds, new TypeToken<ArrayList<Integer>>(){}));
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
		activity.onFollowTagsFinished(result);
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
