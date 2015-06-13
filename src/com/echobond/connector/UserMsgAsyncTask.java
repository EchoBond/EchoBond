package com.echobond.connector;

import java.net.ConnectException;
import java.net.SocketTimeoutException;

import org.json.JSONException;
import org.json.JSONObject;

import com.echobond.entity.RawHttpRequest;
import com.echobond.entity.RawHttpResponse;
import com.echobond.entity.User;
import com.echobond.entity.UserMsg;
import com.echobond.intf.UserMsgCallback;
import com.echobond.util.HTTPUtil;
import com.echobond.util.JSONUtil;

import android.os.AsyncTask;

/**
 * This task is to handle msg load or updates in server's DB.<br>
 * Params (Object): url(String), activity(UserMsgCallback), action(int), <br>
 * user(User), guest(User), limit(int), offset(int) / msg(UserMsg) / user(User), limit(int), offset(int)
 * @version 1.0
 * @author Luck
 * 
 */
public class UserMsgAsyncTask extends AsyncTask<Object, Integer, JSONObject> {

	private UserMsgCallback activity;
	public static final Integer MSG_LOAD = 1;
	public static final Integer MSG_SEND = 2;
	public static final Integer MSG_LOAD_ALL = 3;
	private Integer action = 0;
	@Override
	protected JSONObject doInBackground(Object... params) {
		String url = (String) params[0];
		activity =  (UserMsgCallback) params[1];
		action = (Integer) params[2];
		JSONObject body = new JSONObject();
		try{
			if(action.equals(MSG_LOAD)){
				User user = (User) params[3];
				User guest = (User) params[4];
				Integer limit = (Integer) params[5];
				Integer offset = (Integer) params[6];
				body.put("user", JSONUtil.fromObjectToJSON(user));
				body.put("guest", JSONUtil.fromObjectToJSON(guest));
				body.put("limit", limit);
				body.put("offset", offset);
			} else if(action.equals(MSG_SEND)){
				UserMsg msg = (UserMsg) params[3];
				body.put("msg", msg);
			} else {
				User user = (User) params[3];
				Integer limit = (Integer) params[4];
				Integer offset = (Integer) params[5];
				body.put("user", JSONUtil.fromObjectToJSON(user));
				body.put("limit", limit);
				body.put("offset", offset);				
			}
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
		if(action.equals(MSG_SEND))
			activity.onSendResult(result);
		else activity.onLoadResult(result);
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
