package com.echobond.connector;

import org.json.JSONException;
import org.json.JSONObject;

import com.echobond.activity.StartPage;
import com.echobond.entity.RawHttpRequest;
import com.echobond.entity.RawHttpResponse;
import com.echobond.entity.User;
import com.echobond.fragment.LoginPageFragment;
import com.echobond.fragment.SignUpPageFragment;
import com.echobond.util.HTTPUtil;

import android.os.AsyncTask;

/**
 * @version 1.0
 * @author Luck
 * This task is to connect to and update server's DB.
 * This task will be executed in signing up.
 * The three specified generic type refers to Params, Progress and Result.
 *
 */
public class SignUpAsyncTask extends AsyncTask<Object, Integer, JSONObject> {
	
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
			if("exists=0,extra=sendEmail".equals(msg)){
				result.put("exists", 0);
			} else if("exists=1,verified=1".equals(msg)){
				result.put("exists", 1);
				result.put("verified", 1);
			} else if("exists=1,verified=0,email=0,extra=resendEmail".equals(msg)){
				result.put("exists", 1);
				result.put("verified", 0);
				result.put("email", 0);
			} else if("exists=1,verified=0,email=1,expired=0".equals(msg)){
				result.put("exists", 1);
				result.put("verified", 0);				
				result.put("email", 1);
				result.put("expired", 0);
			} else if("exists=1,verified=0,email=1,expired=1,extra=resendEmail".equals(msg)){
				result.put("exists", 1);
				result.put("verified", 0);
				result.put("email", 1);
				result.put("expired", 1);
			}
		} catch (JSONException e){
			e.printStackTrace();
		}
		return result;
	}

	@Override
	protected void onPostExecute(JSONObject result) {
		activity.onSignUpResult(result);
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
