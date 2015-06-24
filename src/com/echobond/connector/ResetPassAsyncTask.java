package com.echobond.connector;

import org.json.JSONException;
import org.json.JSONObject;

import com.echobond.activity.StartPage;
import com.echobond.util.HTTPUtil;

import android.os.AsyncTask;

/**
 * 
 * This task is to handle pass reset in server's DB.<br>
 * Params (Object): email(String), url(String), activity(StartActivity)<br>
 * Progress (Integer)<br>
 * Result (JSONObject) <br>
 * @author Luck
 *
 */
public class ResetPassAsyncTask extends AsyncTask<Object, Integer, JSONObject> {
	private StartPage activity;
	@Override
	protected JSONObject doInBackground(Object... params) {
		String email = (String) params[0];
		String url = (String) params[1];
		activity = (StartPage) params[2];
		JSONObject body = new JSONObject();
		try {
			body.put("email", email);
		} catch (JSONException e1) {
			e1.printStackTrace();
		}
		return HTTPUtil.getInstance().sendRequest(url, body, true);
	}
	
	@Override
	protected void onPostExecute(JSONObject result) {
		super.onPostExecute(result);
		activity.onResetPassResult(result);
	}
}
