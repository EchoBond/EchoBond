package com.echobond.util;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.Set;

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

	public static boolean isThreadRunning(String name){
		Thread thread = getThreadByName(name);
		if(null == thread || !thread.isAlive())
			return false;
		return true;
	}
	
	public static Thread getThreadByName(String name){
		Set<Thread> tSet = Thread.getAllStackTraces().keySet();
		Iterator<Thread> t = tSet.iterator();
		Thread thread = null;
		while(t.hasNext()){
			Thread _thread = t.next();
			if(_thread.getName().equals(name) && _thread.isAlive()){
				thread = _thread;
			}
		}
		return thread;
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
	
	public static String getDateAsString(String pattern){
		if(null == pattern || pattern.isEmpty()){
			pattern = "yyyy-MM-dd";
		}
		return dateToString(new Date(), pattern);
	}
	public static String getTimeAsString(String pattern){
		if(null == pattern || pattern.isEmpty()){
			pattern = "HH:mm:ss";
		}
		return dateToString(new Date(), pattern);		
	}
	public static String getDateTimeAsString(String pattern){
		if(null == pattern || pattern.isEmpty()){
			pattern = "yyyy-MM-dd HH:mm:ss";
		}
		return dateToString(new Date(), pattern);
	}
	public static String dateToString(Date date, String pattern){
		SimpleDateFormat sdf = (SimpleDateFormat) SimpleDateFormat.getDateTimeInstance();
		sdf.applyPattern(pattern);
		return sdf.format(date);		
	}
	
	public static String[] parseString(String raw, String split){
		if(null == raw || 0 == raw.length()){
			return null;
		}
		String[] names = raw.split(",");
		String[] newNames = new String[names.length];
		int i = 0;
		for(String name: names){
			newNames[i++] = name.trim();
		}
		return newNames;
	}
	
	public static String arrayListToString(ArrayList<String> stringList, String split){
		StringBuilder builder = new StringBuilder();
		if(null != stringList){
			for(int i = 0; i < stringList.size(); i++){
				builder.append(stringList.get(i).trim());
				if(i < stringList.size()-1){
					builder.append(split);
				}
			}
		}
		return builder.toString();
	}
	
}
