package com.echobond.activity;

import java.io.File;
import java.io.IOException;

import com.echobond.R;

import com.echobond.application.MyApp;
import com.echobond.util.HTTPUtil;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.nostra13.universalimageloader.utils.DiskCacheUtils;
import com.nostra13.universalimageloader.utils.MemoryCacheUtils;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;

public class AvatarPage extends Activity {

	private String userId;
	private ImageView avatarView, backButton;
	private ProgressBar progressBar;
	private String url;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_avatar_page);
		
		userId = getIntent().getStringExtra("userId");
		avatarView = (ImageView)findViewById(R.id.avatar_image);
		backButton = (ImageView)findViewById(R.id.avatar_back);
		backButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				finish();
			}
		});
		
		progressBar = (ProgressBar)findViewById(R.id.avatar_progress);
		progressBar.bringToFront();
		url = HTTPUtil.getInstance().composePreURL(this) + getResources().getString(R.string.url_down_img) 
				+ "?path=" + userId;
		Bitmap image = BitmapFactory.decodeResource(getResources(), R.drawable.default_avatar);
		
		//load image
		if (MemoryCacheUtils.findCachedBitmapsForImageUri(url, ImageLoader.getInstance().getMemoryCache()) != null) {
			MemoryCacheUtils.removeFromCache(url, ImageLoader.getInstance().getMemoryCache());
		}
		if (DiskCacheUtils.findInCache(url, ImageLoader.getInstance().getDiskCache()) != null) {
			File file = DiskCacheUtils.findInCache(url, ImageLoader.getInstance().getDiskCache());
			image = BitmapFactory.decodeFile(file.getAbsolutePath());
			try {
				ImageLoader.getInstance().getDiskCache().save(url+"_", image);
			} catch (IOException e) {
				e.printStackTrace();
			}
			DiskCacheUtils.removeFromCache(url, ImageLoader.getInstance().getDiskCache());
		}
		
		//displaying
		MyApp.getDefaultDisplayImageOptions(new DisplayImageOptions.Builder()).showImageOnLoading(new BitmapDrawable(getResources(), image));
		ImageLoader.getInstance().displayImage(url, avatarView, new ImageLoadingListener() {
			
			@Override
			public void onLoadingStarted(String imageUri, View view) {}
			
			@Override
			public void onLoadingFailed(String imageUri, View view,
					FailReason failReason) {
				recoverDiskCache(imageUri);
				progressBar.setVisibility(View.INVISIBLE);
			}
			
			@Override
			public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
				if(null != DiskCacheUtils.findInCache(imageUri+"_", ImageLoader.getInstance().getDiskCache())){
					DiskCacheUtils.removeFromCache(imageUri+"_", ImageLoader.getInstance().getDiskCache());
				}
				progressBar.setVisibility(View.INVISIBLE);
			}
			
			@Override
			public void onLoadingCancelled(String imageUri, View view) {
				recoverDiskCache(imageUri);
				progressBar.setVisibility(View.INVISIBLE);
			}
			
			private void recoverDiskCache(String imageUri){
				if (null != DiskCacheUtils.findInCache(imageUri+"_", ImageLoader.getInstance().getDiskCache())) {
					try {
						File file = DiskCacheUtils.findInCache(imageUri+"_", ImageLoader.getInstance().getDiskCache());
						Bitmap image = BitmapFactory.decodeFile(file.getAbsolutePath());
						ImageLoader.getInstance().getDiskCache().save(imageUri, image);
						DiskCacheUtils.removeFromCache(imageUri+"_", ImageLoader.getInstance().getDiskCache());
						avatarView.setImageBitmap(image);
					} catch (IOException e) {
						e.printStackTrace();
					}
				}				
			}

		});
	}
}
