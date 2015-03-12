package com.echobond.connector;

import java.net.ConnectException;
import java.net.SocketTimeoutException;

import org.json.JSONException;
import org.json.JSONObject;

import com.echobond.activity.StartPage;
import com.echobond.entity.RawHttpRequest;
import com.echobond.entity.RawHttpResponse;
import com.echobond.entity.User;
import com.echobond.util.HTTPUtil;
import com.echobond.util.JSONUtil;

import android.os.AsyncTask;

/**
 * 
 * This task will be executed in signing up or signing in with an existed FB associated account.<br>
 * Params (Object): user(User), url(String), activity(StartPage) <br>
 * Progress (Integer)<br>
 * Result (JSONObject) <br>
 * @version 1.0
 * @author Luck
 *
 */
public class FBSignInAsyncTask extends AsyncTask<Object, Integer, JSONObject> {

	private StartPage activity;
	@Override
	protected JSONObject doInBackground(Object... params) {
		User fBUser = (User) params[0];
		String url = (String) params[1];
		activity = (StartPage) params[2];
		String method = RawHttpRequest.HTTP_METHOD_POST;
		JSONObject body = JSONUtil.fromObjectToJSON(fBUser);
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
		activity.onFBSignInResult(result);
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
