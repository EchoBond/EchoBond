package com.echobond.util;

import java.io.ByteArrayOutputStream;

import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.util.Base64;

public class ImageUtil {
	
	/** 
	 * Bitmap->Base64 String via Base32 
	 * @param bit 
	 * @return 
	 */  
	public static String Bitmap2StrByBase64(Bitmap bit){  
	   ByteArrayOutputStream bos=new ByteArrayOutputStream();  
	   bit.compress(CompressFormat.JPEG, 40, bos);//100=no compresssion
	   byte[] bytes=bos.toByteArray();  
	   return Base64.encodeToString(bytes, Base64.DEFAULT);  
	}
	
	/**
	 * Base64 String->Bitmap via Base32
	 * @param src
	 * @return
	 */
	public static Bitmap Str2Bitmap(String src){
		byte[] imgSrc = Base64.decode(src, Base64.DEFAULT);
		return BitmapFactory.decodeByteArray(imgSrc, 0, imgSrc.length);
	}
	
}
