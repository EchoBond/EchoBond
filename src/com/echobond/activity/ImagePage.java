package com.echobond.activity;

import com.echobond.R;
import com.nostra13.universalimageloader.core.ImageLoader;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ImageView;

public class ImagePage extends Activity {

	private ImageView image;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_image_page);
		
		image = (ImageView)findViewById(R.id.image_page_image);
		
		ImageLoader loader = ImageLoader.getInstance();
		loader.displayImage(getIntent().getStringExtra("url"), image);
	
	}
}
