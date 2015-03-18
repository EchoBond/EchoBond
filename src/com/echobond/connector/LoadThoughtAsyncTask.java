package com.echobond.connector;

import java.net.ConnectException;
import java.net.SocketTimeoutException;

import org.json.JSONException;
import org.json.JSONObject;

import com.echobond.entity.RawHttpRequest;
import com.echobond.entity.RawHttpResponse;
import com.echobond.entity.User;
import com.echobond.intf.LoadThoughtCallback;
import com.echobond.util.HTTPUtil;
import com.echobond.util.JSONUtil;

import android.os.AsyncTask;

/**
 * 
 * This task is to handle loading thoughts from server's DB.<br>
 * Params (Object): url(String), type(int), activity(LoadThoughtCallback), offset(int), limit(int), null / user(User)<br>
 * Progress (Integer)<br>
 * Result (JSONObject)<br>
 * @author Luck
 *
 */
public class LoadThoughtAsyncTask extends AsyncTask<Object, Integer, JSONObject> {
	
	public final static int LOAD_T_HOT = 0;
	public final static int LOAD_T_HOME = 1;

	private LoadThoughtCallback activity;
	@Override
	protected JSONObject doInBackground(Object... params) {
		String url = (String) params[0];
		int type = (Integer) params[1];
		activity = (LoadThoughtCallback) params[2];
		int offset = (Integer) params[3];
		int limit = (Integer)params[4];
		String method = RawHttpRequest.HTTP_METHOD_POST;
		JSONObject body = new JSONObject();
		try {
			body.put("type", type);
			body.put("offset", offset);
			body.put("limit", limit);
			if(type == LOAD_T_HOME){
				User user = (User) params[5];
				body.put("user", JSONUtil.fromObjectToJSON(user));
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
		activity.onLoadThoughtResult(result);
	}
	
	@Override
	protected void onProgressUpdate(Integer... values) {
		super.onProgressUpdate(values);
	}
}
