package com.echobond.connector;

import java.net.ConnectException;
import java.net.SocketTimeoutException;

import org.json.JSONException;
import org.json.JSONObject;

import com.echobond.activity.MainPage;
import com.echobond.entity.RawHttpRequest;
import com.echobond.entity.RawHttpResponse;
import com.echobond.intf.CategoryAsyncTaskCallback;
import com.echobond.util.HTTPUtil;

import android.os.AsyncTask;

/**
 * @version 1.0
 * @author Luck
 * This task is to handle category update in server's DB.
 * This task will be executed whenever a tag is handled.
 * The three specified generic type refers to Params, Progress and Result.
 *
 */
public class CategoryAsyncTask extends AsyncTask<Object, Integer, JSONObject> {

	private CategoryAsyncTaskCallback activity;
	public static final int CATEGORY_UPDATE = 1;
	public static final int CATEGORY_LOAD = 2;
	@Override
	protected JSONObject doInBackground(Object... params) {
		int action = (Integer) params[0];
		String baseUrl = (String) params[1];
		activity =  (CategoryAsyncTaskCallback) params[2];
		JSONObject body = new JSONObject();
		String url = baseUrl;
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
