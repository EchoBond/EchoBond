package com.echobond.connector;

import org.json.JSONException;
import org.json.JSONObject;

import com.echobond.activity.StartPage;
import com.echobond.entity.RawHttpRequest;
import com.echobond.entity.RawHttpResponse;
import com.echobond.entity.User;
import com.echobond.fragment.StartPageFragment;
import com.echobond.util.HTTPUtil;

import android.os.AsyncTask;

/**
 * @version 1.0
 * @author Luck
 * This task is to connect to, fetch from and update server's DB.
 * This task will be executed in signing up or signing in with an existed FB associated account.
 * The three specified generic type refers to Params, Progress and Result.
 *
 */
public class FBSignInAsyncTask extends AsyncTask<Object, Integer, JSONObject> {

	private StartPage activity;
	@Override
	protected JSONObject doInBackground(Object... params) {
		User fBUser = (User) params[0];
		String baseUrl = (String) params[1];
		activity = (StartPage) params[2];
		String url = baseUrl;
		String method = RawHttpRequest.HTTP_METHOD_POST;
		String body = "email="+fBUser.getEmail();
		body += "&FBId="+fBUser.getFBId();
		body += "&firstName="+fBUser.getFirstName();
		body += "&lastName="+fBUser.getLastName();
		body += "&name="+fBUser.getName();
		body += "&timezone="+fBUser.getTimeZone();
		body += "&locale="+fBUser.getLocale();
		body += "&gender="+fBUser.getGender().getName();
		RawHttpRequest request = new RawHttpRequest(url, method, null, body);
		RawHttpResponse response = HTTPUtil.getInstance().send(request);
		String msg = response.getMsg();
		JSONObject result = new JSONObject();
		try{
			if("new=0".equals(msg)){
				result.put("new", 0);
			} else if("new=1".equals(msg)){
				result.put("new", 1);
			}
		} catch (JSONException e){
			e.printStackTrace();
		}
		return result;
	}

	@Override
	protected void onPostExecute(JSONObject result) {
		super.onPostExecute(result);
		activity.onFBSignInResult(result);
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
