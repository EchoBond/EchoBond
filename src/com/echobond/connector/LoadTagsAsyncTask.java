package com.echobond.connector;

import java.net.ConnectException;
import java.net.SocketTimeoutException;

import org.json.JSONException;
import org.json.JSONObject;

import com.echobond.entity.RawHttpRequest;
import com.echobond.entity.RawHttpResponse;
import com.echobond.entity.User;
import com.echobond.intf.LoadTagsCallback;
import com.echobond.util.HTTPUtil;
import com.echobond.util.JSONUtil;

import android.os.AsyncTask;

/**
 * 
 * This task is to handle loading thoughts from server's DB.<br>
 * Params (Object): url(String), activity(LoadThoughtCallback), offset(int), limit(int), user(User)/null<br>
 * Progress (Integer)<br>
 * Result (JSONObject)<br>
 * @author Luck
 *
 */
public class LoadTagsAsyncTask extends AsyncTask<Object, Integer, JSONObject> {

	private LoadTagsCallback activity;
	@Override
	protected JSONObject doInBackground(Object... params) {
		String url = (String) params[0];
		activity = (LoadTagsCallback) params[1];
		int offset = (Integer) params[2];
		int limit = (Integer)params[3];
		String method = RawHttpRequest.HTTP_METHOD_POST;
		JSONObject body = new JSONObject();
		try {
			body.put("offset", offset);
			body.put("limit", limit);
			if(params.length == 5){
				body.put("user", JSONUtil.fromObjectToJSON((User)params[4]));
			}
		} catch (JSONException e1) {
			e1.printStackTrace();
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
		activity.onLoadTagsResult(result);
	}
	
	@Override
	protected void onProgressUpdate(Integer... values) {
		super.onProgressUpdate(values);
	}
}
