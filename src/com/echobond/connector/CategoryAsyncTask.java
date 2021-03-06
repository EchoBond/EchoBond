package com.echobond.connector;

import org.json.JSONObject;

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
		return HTTPUtil.getInstance().sendRequest(url, body, true);
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
