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
 * @version 1.0
 * @author Luck
 * This task will be executed in signing in with an existed account.
 * Params (Object): user(User), url(String), activity(StartPage)
 * Progress (Integer)
 * Result (JSONObject)
 * 
 */
public class SignInAsyncTask extends AsyncTask<Object, Integer, JSONObject> {

	private StartPage activity;
	@Override
	protected JSONObject doInBackground(Object... params) {
		User user = (User) params[0];
		String url = (String) params[1];
		activity = (StartPage) params[2];
		String method = RawHttpRequest.HTTP_METHOD_POST;
		JSONObject body = JSONUtil.fromObjectToJSON(user);
		RawHttpRequest request = new RawHttpRequest(url, method, null, body, true);
		RawHttpResponse response = null;
		JSONObject result = null;
		try{
			response = HTTPUtil.getInstance().send(request);
		} catch(SocketTimeoutException e){
			e.printStackTrace();
		} catch(ConnectException e){
			e.printStackTrace();
		}
		if(null != response){
			String msg = response.getMsg();
			try{
				result = new JSONObject(msg);
			} catch (JSONException e){
				e.printStackTrace();
			}
		}
		return result;
	}

	@Override
	protected void onPostExecute(JSONObject result) {
		activity.onSignInResult(result);
		super.onPostExecute(result);
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
