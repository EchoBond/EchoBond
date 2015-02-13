package com.echobond.connector;

import android.content.res.XmlResourceParser;
import android.os.AsyncTask;

/**
 * @version 1.0
 * @author Luck
 * This task is to fetch data from server's DB.
 * This task will be executed in signing in with an existed account.
 * The three specified generic type refers to Params, Progress and Result.
 *
 */
public class SignInAsyncTask extends AsyncTask<String, Integer, String> {

	@Override
	protected String doInBackground(String... params) {
		
		return null;
	}

	@Override
	protected void onPostExecute(String result) {
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
