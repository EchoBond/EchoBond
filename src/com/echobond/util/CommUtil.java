package com.echobond.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Patterns;

public class CommUtil {
	
	public static boolean isValidEmail(String email){
		return Patterns.EMAIL_ADDRESS.matcher(email).matches();
	}
	
	public static boolean isValidPassword(String password){
		if(password.length() < 6){
			return false;
		} else return true;
	}

	public static boolean isNetworkOnline(Context ctx) {
		boolean status=false;
	    try{
	        ConnectivityManager cm = (ConnectivityManager) ctx.getSystemService(Context.CONNECTIVITY_SERVICE);
	        NetworkInfo netInfo = cm.getNetworkInfo(0);
	        if (netInfo != null && netInfo.getState()==NetworkInfo.State.CONNECTED) {
	            status= true;
	        }else {
	            netInfo = cm.getNetworkInfo(1);
	            if(netInfo!=null && netInfo.getState()==NetworkInfo.State.CONNECTED)
	                status= true;
	        }
	    }catch(Exception e){
	        e.printStackTrace();  
	        return false;
	    }
	    return status;
	    }
}
