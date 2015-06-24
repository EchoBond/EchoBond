package com.echobond.connector;

import org.json.JSONObject;

import com.echobond.activity.StartPage;
import com.echobond.entity.User;
import com.echobond.util.HTTPUtil;
import com.echobond.util.JSONUtil;

import android.os.AsyncTask;

/**
 * 
 * This task will be executed in signing up.<br>
 * Params (Object): user(User), url(String), activity(StartPage)<br>
 * Progress (Integer)<br>
 * Result (JSONObject) <br>
 * @version 1.0
 * @author Luck
 *
 */
public class SignUpAsyncTask extends AsyncTask<Object, Integer, JSONObject> {
	
	private StartPage activity;

	@Override
	protected JSONObject doInBackground(Object... params) {
		User user = (User) params[0];
		String url = (String) params[1];
		activity = (StartPage) params[2];
		JSONObject body = JSONUtil.fromObjectToJSON(user);
		return HTTPUtil.getInstance().sendRequest(url, body, true);
	}

	@Override
	protected void onPostExecute(JSONObject result) {
		activity.onSignUpResult(result);
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
