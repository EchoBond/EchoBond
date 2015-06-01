package com.echobond.activity;

import com.echobond.R;
import com.echobond.application.MyApp;
import com.echobond.dao.CommentDAO;
import com.echobond.widget.CommentDialog;
import com.nostra13.universalimageloader.core.ImageLoader;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.widget.CursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class CommentPage extends FragmentActivity implements LoaderCallbacks<Cursor>{

	private ImageView image, commentButton;
	private TextView titleText, contentText, idText;
	private ListView commentsList;
	private CommentsAdapter adapter;
	private Integer id;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_comment_page);
		
		idText = (TextView) findViewById(R.id.comment_page_thought_id);
		image = (ImageView)findViewById(R.id.comment_page_image);
		titleText = (TextView)findViewById(R.id.comment_page_title);
		contentText = (TextView)findViewById(R.id.comment_page_content);
		commentButton = (ImageView)findViewById(R.id.comment_page_comment);
		commentsList = (ListView)findViewById(R.id.comment_page_comments_list);

		id = getIntent().getIntExtra("id", 0);
		adapter = new CommentsAdapter(this, R.layout.item_comment, null, 0);
		
		getSupportLoaderManager().initLoader(MyApp.LOADER_COMMENT, null, this);
		
		ImageLoader loader = ImageLoader.getInstance();
		loader.displayImage(getIntent().getStringExtra("url"), image);
		
		commentButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				CommentDialog dialog = new CommentDialog(CommentPage.this);
				dialog.show();
			}
		});
		
		commentsList.setAdapter(adapter);
		commentsList.setOverScrollMode(View.OVER_SCROLL_NEVER);
	}
	
	public class CommentsAdapter extends CursorAdapter {

		private int layout;
		private LayoutInflater inflater;
		public CommentsAdapter(Context context, int layout, Cursor c, int flags) {
			super(context, c, flags);
			this.layout = layout;
			this.inflater = LayoutInflater.from(context);			
		}

		@Override
		public void bindView(View view, Context context, Cursor cursor) {
			TextView contentView = (TextView) view.findViewById(R.id.comment_content);
			TextView userNameView = (TextView) view.findViewById(R.id.comment_person_title);
			
			String content = cursor.getString(cursor.getColumnIndex("content"));
			String userName = cursor.getString(cursor.getColumnIndex("username"));
			
			contentView.setText(content);
			userNameView.setText(userName);
			
			contentView.setText(content);
		}

		@Override
		public View newView(Context arg0, Cursor arg1, ViewGroup parent) {
			View view = inflater.inflate(layout, parent, false);
			return view;
		}
		
	}

	@Override
	public Loader<Cursor> onCreateLoader(int loader, Bundle arg1) {
		switch(loader){
		case MyApp.LOADER_COMMENT:
			Uri uri = CommentDAO.CONTENT_URI;
			return new CursorLoader(this, uri, null, id+"", null, null);
		}
		return null;
	}

	@Override
	public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
		adapter.swapCursor(cursor);
	}

	@Override
	public void onLoaderReset(Loader<Cursor> arg0) {
		adapter.swapCursor(null);
	}
}
