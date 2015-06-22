package com.echobond.connector;

import java.net.ConnectException;
import java.net.SocketTimeoutException;

import org.json.JSONException;
import org.json.JSONObject;

import com.echobond.entity.RawHttpRequest;
import com.echobond.entity.RawHttpResponse;
import com.echobond.intf.LoadSearchPeopleCallback;
import com.echobond.util.HTTPUtil;

import android.os.AsyncTask;

/**
 * This task is to load people search brief info from server's DB.<br>
 * Params (Object): url(String), activity(LoadPeopleSearchCallback), random (int)<br>
 * @version 1.0
 * @author Luck
 * 
 */
public class LoadPeopleSearchAsyncTask extends AsyncTask<Object, Integer, JSONObject> {

	private LoadSearchPeopleCallback activity;
	@Override
	protected JSONObject doInBackground(Object... params) {
		String url = (String) params[0];
		activity =  (LoadSearchPeopleCallback) params[1];
		int random = (Integer)params[2];
		JSONObject body = new JSONObject();
		try {
			body.put("random", random);
		} catch (JSONException e1) {
			e1.printStackTrace();
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
		activity.onLoadSearchPeopleResult(result);
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
