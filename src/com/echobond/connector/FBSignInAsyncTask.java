package com.echobond.connector;

import android.os.AsyncTask;

/**
 * @version 1.0
 * @author Luck
 * This task is to connect to, fetch from and update server's DB.
 * This task will be executed in signing up or signing in with an existed FB associated account.
 * The three specified generic type refers to Params, Progress and Result.
 *
 */
public class FBSignInAsyncTask extends AsyncTask<String, Integer, String> {

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
