package com.echobond.util;

import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.util.Base64;
import android.view.View;

public class ImageUtil {
	
	/**
	 * generate bitmap from view
	 * @param v
	 * @return
	 */
	public static Bitmap generateBitmap(View v) {
		v.setDrawingCacheEnabled(true);
		v.buildDrawingCache();
		Bitmap b = v.getDrawingCache();
		v.destroyDrawingCache();
		return b;
	}
	
	/**
	 * save the generated bitmap
	 * @param bitmap
	 * @param fileName
	 * @return
	 */
	public static boolean saveBitmap(Bitmap bitmap, String fileName){
		File file = new File(Environment.MEDIA_MOUNTED+"/testbitmap/"+fileName+".jpg");
		FileOutputStream fos = null;
		try{
			fos = new FileOutputStream(file);
			BufferedOutputStream bos = new BufferedOutputStream(fos);
			bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
			bos.flush();
			bos.close();
		} catch (FileNotFoundException e){
			e.printStackTrace();
			return false;
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
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
	public static String bmToStr(Bitmap bit){
		
		ByteArrayOutputStream bos=new ByteArrayOutputStream();  
		bit.compress(CompressFormat.JPEG, 100, bos); //100=no compresssion
		byte[] bytes=bos.toByteArray();
		return Base64.encodeToString(bytes, Base64.DEFAULT);
	}
	
	/**
	 * Base64 String->Bitmap via Base32
	 * @param src
	 * @return
	 */
	public static Bitmap strToBm(String src){
		/* String->bytes */
		byte[] imgSrc = Base64.decode(src, Base64.DEFAULT);
		/* bytes->Bitmap */
		return BitmapFactory.decodeByteArray(imgSrc, 0, imgSrc.length);
	}
	
	/**
	 * get bitmap from path
	 * @param path
	 * @return
	 */
	public static Bitmap getBitmapByPath(String path){
		File file = new File(path);
		BitmapFactory.Options options = new BitmapFactory.Options();
		Bitmap bitmap = BitmapFactory.decodeFile(file.getAbsolutePath(), options);
		return bitmap;
	}
}
