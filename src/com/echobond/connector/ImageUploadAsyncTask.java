package com.echobond.connector;

import org.json.JSONException;
import org.json.JSONObject;

import com.echobond.intf.ImageCallback;
import com.echobond.util.HTTPUtil;

import android.os.AsyncTask;

/**
 * This task is to handle image uploading to server's DB.<br>
 * Params (Object): url(String), activity(ImageAsyncTaskCallback), encoded image string (String), userId (String), email (String)<br>
 * @version 1.0
 * @author Luck
 * 
 */
public class ImageUploadAsyncTask extends AsyncTask<Object, Integer, JSONObject> {

	private ImageCallback activity;
	@Override
	protected JSONObject doInBackground(Object... params) {
		String url = (String) params[0];
		String data = (String) params[1];
		activity = (ImageCallback) params[2];
		String userId = (String) params[3];
		String email = (String)params[4];
		JSONObject body = new JSONObject();
		try{
			body.put("data", data);
			body.put("userId", userId);
			body.put("email", email);
		} catch (JSONException e){
			e.printStackTrace();
		}
		return HTTPUtil.getInstance().sendRequest(url, body, true);
	}

	@Override
	protected void onPostExecute(JSONObject result) {
		super.onPostExecute(result);
		activity.onUploadImage(result);
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
