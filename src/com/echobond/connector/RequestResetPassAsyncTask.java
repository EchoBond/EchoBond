package com.echobond.connector;

import org.json.JSONException;
import org.json.JSONObject;

import com.echobond.intf.RequestResetPassCallback;
import com.echobond.util.HTTPUtil;

import android.os.AsyncTask;

/**
 * 
 * This task is to handle pass reset request in server's DB.<br>
 * Params (Object): id(String), url(String), activity(RequestResetPassCallback), oldPass(String), newPass(String)<br>
 * Progress (Integer)<br>
 * Result (JSONObject) <br>
 * @author Luck
 *
 */
public class RequestResetPassAsyncTask extends AsyncTask<Object, Integer, JSONObject> {
	private RequestResetPassCallback activity;
	@Override
	protected JSONObject doInBackground(Object... params) {
		String id = (String) params[0];
		String url = (String) params[1];
		activity = (RequestResetPassCallback) params[2];
		String oldPass = (String) params[3];
		String newPass = (String) params[4];
		JSONObject body = new JSONObject();
		try {
			body.put("id", id);
			body.put("oldPass", oldPass);
			body.put("newPass", newPass);
		} catch (JSONException e1) {
			e1.printStackTrace();
		}
		return HTTPUtil.getInstance().sendRequest(url, body, true);
	}
	
	@Override
	protected void onPostExecute(JSONObject result) {
		super.onPostExecute(result);
		activity.onRequestResetPassFinished(result);
	}
}
