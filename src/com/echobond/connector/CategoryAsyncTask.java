package com.echobond.connector;

import java.net.ConnectException;
import java.net.SocketTimeoutException;

import org.json.JSONException;
import org.json.JSONObject;

import com.echobond.entity.RawHttpRequest;
import com.echobond.entity.RawHttpResponse;
import com.echobond.intf.CategoryCallback;
import com.echobond.util.HTTPUtil;

import android.os.AsyncTask;

/**
 * This task is to handle category loading or update in server's DB.<br>
 * Params (Object): action(int), user(User), url(String), activity(CategoryAsyncTaskCallback)<br>
 * @version 1.0
 * @author Luck
 * 
 */
public class CategoryAsyncTask extends AsyncTask<Object, Integer, JSONObject> {

	private CategoryCallback activity;
	public static final int CATEGORY_UPDATE = 1;
	public static final int CATEGORY_LOAD = 2;
	@Override
	protected JSONObject doInBackground(Object... params) {
		int action = (Integer) params[0];
		String url = (String) params[1];
		activity =  (CategoryCallback) params[2];
		JSONObject body = new JSONObject();
		if(action == CATEGORY_LOAD){
			
		} else if(action == CATEGORY_UPDATE){
			
		}
		String method = RawHttpRequest.HTTP_METHOD_POST;
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
		activity.onCategoryResult(result);
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
