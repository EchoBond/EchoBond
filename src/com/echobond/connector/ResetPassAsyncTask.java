package com.echobond.connector;

import java.net.ConnectException;
import java.net.SocketTimeoutException;

import org.json.JSONException;
import org.json.JSONObject;

import com.echobond.activity.StartPage;
import com.echobond.entity.RawHttpRequest;
import com.echobond.entity.RawHttpResponse;
import com.echobond.util.HTTPUtil;

import android.os.AsyncTask;

/**
 * 
 * @author Luck
 * This task is to handle pass reset in server's DB.
 * Params (Object): email(String), url(String), activity(StartActivity)
 * Progress (Integer)
 * Result (JSONObject)
 *
 */
public class ResetPassAsyncTask extends AsyncTask<Object, Integer, JSONObject> {
	private StartPage activity;
	@Override
	protected JSONObject doInBackground(Object... params) {
		String email = (String) params[0];
		String url = (String) params[1];
		activity = (StartPage) params[2];
		String method = RawHttpRequest.HTTP_METHOD_POST;
		JSONObject body = new JSONObject();
		try {
			body.put("email", email);
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
		} catch (ConnectException e) {
			e.printStackTrace();
		}
		if(null != response){
			try{
				result = new JSONObject(response.getMsg());
			} catch(JSONException e){
				e.printStackTrace();
			}
		}
		return result;
	}
	
	@Override
	protected void onPostExecute(JSONObject result) {
		super.onPostExecute(result);
		activity.onResetPassResult(result);
	}
}
