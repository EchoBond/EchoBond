package com.echobond.activity;

import com.echobond.R;
import com.nostra13.universalimageloader.core.ImageLoader;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.widget.CursorAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class ImagePage extends Activity {

	private ImageView image, commentButton;
	private TextView titleText, contentText;
	private ListView commentsList;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_image_page);
		
		image = (ImageView)findViewById(R.id.image_page_image);
		titleText = (TextView)findViewById(R.id.image_page_title);
		contentText = (TextView)findViewById(R.id.image_page_content);
		commentButton = (ImageView)findViewById(R.id.image_page_comment);
		commentsList = (ListView)findViewById(R.id.image_page_comments_list);
		
		ImageLoader loader = ImageLoader.getInstance();
		loader.displayImage(getIntent().getStringExtra("url"), image);
		
//		commentsList.setAdapter(adapter);
		commentsList.setOverScrollMode(View.OVER_SCROLL_NEVER);
	}
	
	public class CommentsAdapter extends CursorAdapter {

		public CommentsAdapter(Context context, Cursor c, boolean autoRequery) {
			super(context, c, autoRequery);
			// TODO Auto-generated constructor stub
		}

		@Override
		public void bindView(View arg0, Context arg1, Cursor arg2) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public View newView(Context arg0, Cursor arg1, ViewGroup arg2) {
			// TODO Auto-generated method stub
			return null;
		}
		
	}
}
