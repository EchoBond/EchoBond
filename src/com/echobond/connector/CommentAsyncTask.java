package com.echobond.connector;

import org.json.JSONException;
import org.json.JSONObject;

import com.echobond.entity.Comment;
import com.echobond.intf.CommentCallback;
import com.echobond.util.HTTPUtil;
import com.echobond.util.JSONUtil;

import android.os.AsyncTask;

/**
 * This task is to handle comment of thought updates in server's DB.<br>
 * Params (Object): action(int), url(String), activity(CommentAsyncTaskCallback), null/comment (Comment)<br>
 * @version 1.0
 * @author Luck
 * 
 */
public class CommentAsyncTask extends AsyncTask<Object, Integer, JSONObject> {

	private CommentCallback activity;
	public static final Integer LOAD_COMMENT = 0;
	public static final Integer SEND_COMMENT = 1;
	@Override
	protected JSONObject doInBackground(Object... params) {
		Integer action = (Integer) params[0];
		String url = (String) params[1];
		activity =  (CommentCallback) params[2];
		JSONObject body = new JSONObject();
		try {
			if(action == SEND_COMMENT){
				Comment cmt = (Comment) params[3];
				body.put("comment", JSONUtil.fromObjectToJSON(cmt));
			} else {
				
			}
		} catch (JSONException e1) {
			e1.printStackTrace();
		}
		return HTTPUtil.getInstance().sendRequest(url, body, true);
	}

	@Override
	protected void onPostExecute(JSONObject result) {
		super.onPostExecute(result);
		activity.onCommentResult(result);
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
