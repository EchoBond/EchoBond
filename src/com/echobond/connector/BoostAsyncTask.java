package com.echobond.connector;

import org.json.JSONException;
import org.json.JSONObject;

import com.echobond.entity.Thought;
import com.echobond.entity.User;
import com.echobond.intf.BoostCallback;
import com.echobond.util.HTTPUtil;
import com.echobond.util.JSONUtil;

import android.os.AsyncTask;

/**
 * This task is to handle boost of thought updates in server's DB.<br>
 * Params (Object): url(String), activity(BoostAsyncTaskCallback), thoughtId, userId<br>
 * @version 1.0
 * @author Luck
 * 
 */
public class BoostAsyncTask extends AsyncTask<Object, Integer, JSONObject> {

	private BoostCallback activity;
	@Override
	protected JSONObject doInBackground(Object... params) {
		String url = (String) params[0];
		activity =  (BoostCallback) params[1];
		Thought t = new Thought();
		t.setId((Integer)params[2]);
		User u = new User();
		u.setId((String)params[3]);
		JSONObject body = new JSONObject();
		try {
			body.put("thought", JSONUtil.fromObjectToJSON(t));
			body.put("user", JSONUtil.fromObjectToJSON(u));
		} catch (JSONException e1) {
			e1.printStackTrace();
		}
		return HTTPUtil.getInstance().sendRequest(url, body, true);
	}

	@Override
	protected void onPostExecute(JSONObject result) {
		super.onPostExecute(result);
		activity.onBoostResult(result);
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
