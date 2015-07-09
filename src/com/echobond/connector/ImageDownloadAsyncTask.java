package com.echobond.connector;

import java.net.ConnectException;
import java.net.SocketTimeoutException;

import org.json.JSONObject;

import com.echobond.entity.RawHttpRequest;
import com.echobond.entity.RawHttpResponse;
import com.echobond.intf.ImageCallback;
import com.echobond.util.HTTPUtil;

import android.os.AsyncTask;

/**
 * This task is to handle image downloading from server's DB.<br>
 * Params (Object): url(String), activity(ImageAsyncTaskCallback)<br>
 * @version 1.0
 * @author Luck
 * 
 */
public class ImageDownloadAsyncTask extends AsyncTask<Object, Integer, byte[]> {

	private ImageCallback activity;
	@Override
	protected byte[] doInBackground(Object... params) {
		String url = (String) params[0];
		activity = (ImageCallback) params[1];
		RawHttpRequest request = new RawHttpRequest(url, RawHttpRequest.HTTP_METHOD_POST, null, new JSONObject());
		RawHttpResponse response = null;
		try {
			response = HTTPUtil.getInstance().send(request);
		} catch (SocketTimeoutException e) {
			e.printStackTrace();
		} catch (ConnectException e) {
			e.printStackTrace();
		}
		if(null != response){
			byte[] array = response.getMsg().getBytes();
			return array;
		}
		return null;
	}

	@Override
	protected void onPostExecute(byte[] result) {
		super.onPostExecute(result);
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
