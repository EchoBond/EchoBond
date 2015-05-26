package com.echobond;

import com.nostra13.universalimageloader.core.ImageLoader;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ImageView;

public class ImagePage extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_image_page);
		ImageView image = (ImageView) findViewById(R.id.image_page_image);
		ImageLoader loader = ImageLoader.getInstance();
		loader.displayImage(getIntent().getStringExtra("url"), image);
	}
}
