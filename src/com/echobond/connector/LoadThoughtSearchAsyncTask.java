package com.echobond.connector;

import org.json.JSONException;
import org.json.JSONObject;

import com.echobond.intf.LoadSearchThoughtCallback;
import com.echobond.util.HTTPUtil;

import android.os.AsyncTask;

/**
 * This task is to load thought search brief info from server's DB.<br>
 * Params (Object): url(String), activity(LoadThoughtSearchCallback), random (int)<br>
 * @version 1.0
 * @author Luck
 * 
 */
public class LoadThoughtSearchAsyncTask extends AsyncTask<Object, Integer, JSONObject> {

	private LoadSearchThoughtCallback activity;
	@Override
	protected JSONObject doInBackground(Object... params) {
		String url = (String) params[0];
		activity =  (LoadSearchThoughtCallback) params[1];
		int random = (Integer)params[2];
		JSONObject body = new JSONObject();
		try {
			body.put("random", random);
		} catch (JSONException e1) {
			e1.printStackTrace();
		}
		return HTTPUtil.getInstance().sendRequest(url, body, true);
	}

	@Override
	protected void onPostExecute(JSONObject result) {
		super.onPostExecute(result);
		activity.onLoadSearchThoughtResult(result);
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
