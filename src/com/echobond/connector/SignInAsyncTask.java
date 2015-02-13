package com.echobond.connector;

import org.json.JSONException;
import org.json.JSONObject;

import com.echobond.activity.StartPage;
import com.echobond.entity.RawHttpRequest;
import com.echobond.entity.RawHttpResponse;
import com.echobond.entity.User;
import com.echobond.fragment.LoginPageFragment;
import com.echobond.util.HTTPUtil;

import android.os.AsyncTask;

/**
 * @version 1.0
 * @author Luck
 * This task is to fetch data from server's DB.
 * This task will be executed in signing in with an existed account.
 * The three specified generic type refers to Params, Progress and Result.
 *
 */
public class SignInAsyncTask extends AsyncTask<Object, Integer, JSONObject> {

	private StartPage activity;
	@Override
	protected JSONObject doInBackground(Object... params) {
		User user = (User) params[0];
		String baseUrl = (String) params[1];
		activity = (StartPage) params[2];
		String method = RawHttpRequest.HTTP_METHOD_POST;
		String body = "email="+user.getEmail()+"&password="+user.getPassword();
		RawHttpRequest request = new RawHttpRequest(baseUrl, method, null, body);
		RawHttpResponse response = HTTPUtil.getInstance().send(request);
		String msg = response.getMsg();
		JSONObject result = new JSONObject();
		try{
			if("exists=0".equals(msg)){
				result.put("exists", 0);
			} else if("exists=1,passMatch=0".equals(msg)){
				result.put("exists", 1);
				result.put("passMatch", 0);
			} else if("exists=1,passMatch=1".equals(msg)){
				result.put("exists", 1);
				result.put("passMatch", 1);
			}
		} catch (JSONException e){
			e.printStackTrace();
		}
		return result;
	}

	@Override
	protected void onPostExecute(JSONObject result) {
		activity.onSignInResult(result);
		super.onPostExecute(result);
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
