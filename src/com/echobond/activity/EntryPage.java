package com.echobond.activity;

import com.echobond.R;
import com.echobond.application.MyApp;
import com.echobond.connector.InitFetchService;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;

public class EntryPage extends Activity {
	
	private BroadcastReceiver receiver = new BroadcastReceiver() {
		
		@Override
		public void onReceive(Context context, Intent intent) {
			Intent newIntent = new Intent();
			newIntent.setClass(EntryPage.this, StartPage.class);
			newIntent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
			startActivity(newIntent);
			finish();
		}
	};
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_entry);
		LocalBroadcastManager.getInstance(this).registerReceiver(receiver, new IntentFilter(MyApp.BROADCAST_STARTUP));
		Intent initFetch = new Intent(this, InitFetchService.class);
		startService(initFetch);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		LocalBroadcastManager.getInstance(this).unregisterReceiver(receiver);
	}
	
}