package com.echobond.connector;

import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.util.ArrayList;

import org.json.JSONException;
import org.json.JSONObject;

import com.echobond.entity.RawHttpRequest;
import com.echobond.entity.RawHttpResponse;
import com.echobond.entity.Tag;
import com.echobond.entity.User;
import com.echobond.intf.TagCallback;
import com.echobond.util.HTTPUtil;
import com.echobond.util.JSONUtil;

import android.os.AsyncTask;

/**
 * @version 1.0
 * @author Luck
 * This task is to handle tag loading and update in server's DB.
 * Params (Object): action(int), url(String), activity(TagAsyncTaskCallback), tags(ArrayList<Tags>) / user(User) / null
 * Progress (Integer)
 * Result (JSONObject)
 * 
 */
public class TagAsyncTask extends AsyncTask<Object, Integer, JSONObject> {

	private TagCallback activity;
	public static final int TAG_UPDATE = 1;
	public static final int TAG_LOAD = 2;
	public static final int TAG_LOAD_ALL = 3;
	@SuppressWarnings("unchecked")
	@Override
	protected JSONObject doInBackground(Object... params) {
		int action = (Integer) params[0];
		String url = (String) params[1];
		activity = (TagCallback) params[2];
		ArrayList<Tag> tags = null;
		User user = null;
		String method = RawHttpRequest.HTTP_METHOD_POST;
		JSONObject body = null;
		if(action == TAG_UPDATE){
			tags = (ArrayList<Tag>) params[3];
			body = JSONUtil.fromObjectToJSON(tags);
		} else if(action == TAG_LOAD){
			user = (User) params[3];
			body = JSONUtil.fromObjectToJSON(user);
		} else if(action == TAG_LOAD_ALL){

		}
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
		activity.onTagResult(result);
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
