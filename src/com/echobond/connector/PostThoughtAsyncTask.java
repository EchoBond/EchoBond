package com.echobond.connector;

import java.net.ConnectException;
import java.net.SocketTimeoutException;

import org.json.JSONException;
import org.json.JSONObject;

import com.echobond.entity.RawHttpRequest;
import com.echobond.entity.RawHttpResponse;
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
		String method = RawHttpRequest.HTTP_METHOD_POST;
		JSONObject body = JSONUtil.fromObjectToJSON(t);
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
		activity.onPostThoughtResult(result);
	}
	
	@Override
	protected void onProgressUpdate(Integer... values) {
		super.onProgressUpdate(values);
	}
}
