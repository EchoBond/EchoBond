package com.echobond.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.util.Base64;

public class ImageUtil {
	
	/**
	 * compress bitmap
	 * @param src
	 * @param quality
	 * @return
	 */
	public static Bitmap shrinkBitmap(Bitmap src, int quality){
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		ByteArrayInputStream in = new ByteArrayInputStream(out.toByteArray());
		src.compress(CompressFormat.JPEG, quality, out);
		Bitmap dest = BitmapFactory.decodeStream(in);
		try {
			if(null != out)
				out.close();
			if(null != in)
				in.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return dest;
	}
	
	/**
	 * resample bitmap
	 * @param src
	 * @param times
	 * @return
	 */
	public static Bitmap resampleBitmap(Bitmap src, int times){
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		ByteArrayInputStream in = new ByteArrayInputStream(out.toByteArray());
		src.compress(CompressFormat.PNG, 100, out);
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inSampleSize = times;
		Bitmap dest = BitmapFactory.decodeStream(in, null, options);
		try {
			if(null != out)
				out.close();
			if(null != in)
				in.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return dest;
	}
	
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
