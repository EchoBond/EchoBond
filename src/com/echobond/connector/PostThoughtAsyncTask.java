package com.echobond.connector;

import org.json.JSONObject;

import com.echobond.entity.Thought;
import com.echobond.intf.PostThoughtCallback;
import com.echobond.util.HTTPUtil;
import com.echobond.util.JSONUtil;

import android.os.AsyncTask;

/**
 * 
 * This task is to handle storing thought in server's DB.<br>
 * Params (Object): url(String), t(Thought), activity(PostThoughtCallback)<br>
 * Progress (Integer)<br>
 * Result (JSONObject)<br>
 * @author Luck
 *
 */
public class PostThoughtAsyncTask extends AsyncTask<Object, Integer, JSONObject> {

	private PostThoughtCallback activity;
	@Override
	protected JSONObject doInBackground(Object... params) {
		String url = (String) params[0];
		Thought t = (Thought) params[1];
		activity = (PostThoughtCallback) params[2];
		JSONObject body = JSONUtil.fromObjectToJSON(t);
		return HTTPUtil.getInstance().sendRequest(url, body, true);
	}
	
	@Override
	protected void onPostExecute(JSONObject result) {
		super.onPostExecute(result);
		activity.onPostThoughtResult(result);
	}
	
	@Override
	protected void onProgressUpdate(Integer... values) {
		super.onProgressUpdate(values);
	}
}
