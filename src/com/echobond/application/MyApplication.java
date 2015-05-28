package com.echobond.application;

import android.app.Application;

public class MyApplication extends Application {
	@Override
	public void onCreate() {
		//super.onCreate();
	}
	@Override
	public void onTerminate() {
		super.onTerminate();
	}
	
	@Override
	public void onLowMemory() {
		// TODO Auto-generated method stub
		super.onLowMemory();
	}
	
}
