package com.echobond.connector;

import java.net.ConnectException;
import java.net.SocketTimeoutException;

import org.json.JSONException;
import org.json.JSONObject;

import com.echobond.entity.RawHttpRequest;
import com.echobond.entity.RawHttpResponse;
import com.echobond.intf.ImageCallback;
import com.echobond.util.HTTPUtil;

import android.graphics.Bitmap;
import android.os.AsyncTask;

/**
 * This task is to handle image loading or update in server's DB.<br>
 * Params (Object): action(int), url(String), activity(ImageAsyncTaskCallback), bm(Bitmap) / path(String)<br>
 * @version 1.0
 * @author Luck
 * 
 */
public class ImageAsyncTask extends AsyncTask<Object, Integer, JSONObject> {

	private ImageCallback activity;
	private int action;
	public static final int IMAGE_UPLOAD = 1;
	public static final int IMAGE_DOWNLOAD = 2;
	@Override
	protected JSONObject doInBackground(Object... params) {
		this.action = (Integer) params[0];
		String url = (String) params[1];
		activity = (ImageCallback) params[2];
		JSONObject body = new JSONObject();
		try{
			switch (action) {
			case IMAGE_UPLOAD:
				Bitmap bm = (Bitmap) params[3];
				body.put("bitmap", bm);
				break;
			case IMAGE_DOWNLOAD:
				String path = (String) params[3];
				body.put("path", path);
			default:
				break;
			}
		} catch (JSONException e){
			e.printStackTrace();
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
		switch (action) {
		case IMAGE_DOWNLOAD:
			activity.onDownloadImage(result);
			break;
		case IMAGE_UPLOAD:
			activity.onUploadImage(result);
			break;
		default:
			break;
		}
		activity.onDownloadImage(result);
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
