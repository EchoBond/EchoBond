package com.echobond.connector;

import org.json.JSONException;
import org.json.JSONObject;

import com.echobond.entity.User;
import com.echobond.intf.LoadThoughtCallback;
import com.echobond.util.HTTPUtil;
import com.echobond.util.JSONUtil;

import android.os.AsyncTask;

/**
 * 
 * This task is to handle loading thoughts from server's DB.<br>
 * Params (Object): url(String), type(int), activity(LoadThoughtCallback), offset(int), limit(int), user(User),<br>
 * 		null/null/condition(JSONObject)<br>
 * Progress (Integer)<br>
 * Result (JSONObject)<br>
 * @author Luck
 *
 */
public class LoadThoughtAsyncTask extends AsyncTask<Object, Integer, JSONObject> {
	
	public final static int LOAD_T_HOT = 0;
	public final static int LOAD_T_HOME = 1;
	public final static int LOAD_T_SEARCH = 2;

	private LoadThoughtCallback activity;
	@Override
	protected JSONObject doInBackground(Object... params) {
		String url = (String) params[0];
		int type = (Integer) params[1];
		activity = (LoadThoughtCallback) params[2];
		int offset = (Integer) params[3];
		int limit = (Integer)params[4];
		User user = (User) params[5];
		JSONObject body = new JSONObject();
		try {
			body.put("type", type);
			body.put("offset", offset);
			body.put("limit", limit);
			body.put("user", JSONUtil.fromObjectToJSON(user));
			if(type == LOAD_T_SEARCH){
				JSONObject condition = (JSONObject) params[6];
				body.put("condition", condition);
			}
		} catch (JSONException e1) {
			e1.printStackTrace();
		}
		return HTTPUtil.getInstance().sendRequest(url, body, true);
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
