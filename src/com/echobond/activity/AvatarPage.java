package com.echobond.activity;

import java.io.IOException;
import java.util.List;

import com.echobond.R;

import com.echobond.util.HTTPUtil;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.nostra13.universalimageloader.utils.DiskCacheUtils;
import com.nostra13.universalimageloader.utils.MemoryCacheUtils;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

public class AvatarPage extends Activity {

	private String userId;
	private ImageView avatarView;
	private String url;
	private List<Bitmap> cacheList;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_avatar_page);
		userId = getIntent().getStringExtra("userId");
		avatarView = (ImageView) findViewById(R.id.avatar_image);
		url = HTTPUtil.getInstance().composePreURL(this) + getResources().getString(R.string.url_down_img) 
				+ "?path=" + userId;
		if(MemoryCacheUtils.findCachedBitmapsForImageUri(url, ImageLoader.getInstance().getMemoryCache()) != null){
			cacheList = MemoryCacheUtils.findCachedBitmapsForImageUri(url, ImageLoader.getInstance().getMemoryCache());
			MemoryCacheUtils.removeFromCache(url, ImageLoader.getInstance().getMemoryCache());
		}
		if(DiskCacheUtils.findInCache(url, ImageLoader.getInstance().getDiskCache()) != null){
			DiskCacheUtils.removeFromCache(url, ImageLoader.getInstance().getDiskCache());
		}
		ImageLoader.getInstance().loadImage(url, new ImageLoadingListener() {
			
			@Override
			public void onLoadingStarted(String imageUri, View view) {}
			
			@Override
			public void onLoadingFailed(String imageUri, View view,
					FailReason failReason) {
				try {
					if(null != cacheList && cacheList.size() > 0){
						Bitmap image = cacheList.get(0);
						ImageLoader.getInstance().getDiskCache().save(url, image); //deprecated
						avatarView.setImageBitmap(cacheList.get(0));
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			
			@Override
			public void onLoadingComplete(String imageUri, View view, Bitmap image) {
				avatarView.setImageBitmap(image);
			}
			
			@Override
			public void onLoadingCancelled(String imageUri, View view) {}
		});
		
	}
}
