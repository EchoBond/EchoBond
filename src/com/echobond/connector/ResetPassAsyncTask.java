package com.echobond.connector;

import org.json.JSONException;
import org.json.JSONObject;

import com.echobond.activity.StartPage;
import com.echobond.entity.RawHttpRequest;
import com.echobond.entity.RawHttpResponse;
import com.echobond.fragment.LoginPageFragment;
import com.echobond.util.HTTPUtil;

import android.os.AsyncTask;

public class ResetPassAsyncTask extends AsyncTask<Object, Integer, JSONObject> {
	private StartPage activity;
	@Override
	protected JSONObject doInBackground(Object... params) {
		String email = (String) params[0];
		String url = (String) params[1];
		activity = (StartPage) params[2];
		String method = RawHttpRequest.HTTP_METHOD_POST;
		String body = "email="+email;
		RawHttpRequest request = new RawHttpRequest(url, method, null, body);
		RawHttpResponse response = HTTPUtil.getInstance().send(request);
		String msg = response.getMsg();
		JSONObject result = new JSONObject();
		try{
			if("accExists=0".equals(msg)){
				result.put("accExists", 0);
			} else if("accExists=1,hadReset=0,extra=sendEmail".equals(msg)){
				result.put("accExists", 1);
				result.put("hadReset", 0);
			} else if("accExists=1,hadReset=1,reset=0,expire=1,extra=resendEmail".equals(msg)){
				result.put("accExists", 1);
				result.put("hadReset", 1);
				result.put("reset", 0);
				result.put("expire", 1);
			} else if("accExists=1,hadReset=1,reset=0,expire=0".equals(msg)){
				result.put("accExists", 1);
				result.put("hadReset", 1);
				result.put("reset", 0);
				result.put("expire", 0);
			} else if("accExists=1,hadReset=1,reset=1,extra=resendEmail".equals(msg)){
				result.put("accExists", 1);
				result.put("hadReset", 1);
				result.put("reset", 1);
			}
		} catch(JSONException e){
			e.printStackTrace();
		}
		return result;
	}
	
	@Override
	protected void onPostExecute(JSONObject result) {
		super.onPostExecute(result);
		activity.onResetPassResult(result);
	}
}
