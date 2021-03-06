package com.echobond.activity;

import org.json.JSONException;
import org.json.JSONObject;

import com.echobond.R;
import com.echobond.application.MyApp;
import com.echobond.connector.CommentAsyncTask;
import com.echobond.dao.CommentDAO;
import com.echobond.entity.Comment;
import com.echobond.fragment.CommentDialogFragment;
import com.echobond.intf.CommentCallback;
import com.echobond.util.HTTPUtil;
import com.echobond.util.JSONUtil;
import com.echobond.util.SPUtil;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.widget.CursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class CommentPage extends FragmentActivity implements LoaderCallbacks<Cursor>, CommentCallback {

	private CommentDialogFragment dialogFragment;
	private ImageView image, commentButton, avatar, backButton;
	private TextView titleText, contentText;
	private ListView commentsList;
	private CommentsAdapter adapter;
	private Integer id;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_comment_page);
		
		dialogFragment = new CommentDialogFragment();
		image = (ImageView)findViewById(R.id.comment_page_image);
		avatar = (ImageView)findViewById(R.id.comment_page_avatar);
		backButton = (ImageView)findViewById(R.id.comment_page_back);
		titleText = (TextView)findViewById(R.id.comment_page_title);
		contentText = (TextView)findViewById(R.id.comment_page_content);
		commentButton = (ImageView)findViewById(R.id.comment_page_comment);
		commentsList = (ListView)findViewById(R.id.comment_page_comments_list);

		id = getIntent().getIntExtra("id", 0);
		titleText.setText(getIntent().getStringExtra("userName"));
		contentText.setText(getIntent().getStringExtra("content"));
		adapter = new CommentsAdapter(this, R.layout.item_comment, null, 0);
		
		getSupportLoaderManager().initLoader(MyApp.LOADER_COMMENT, null, this);
		
		String postUrl = getIntent().getStringExtra("postUrl");
		DisplayImageOptions opt = new DisplayImageOptions.Builder().showImageOnFail(R.drawable.no_image).build();		
		ImageLoader.getInstance().displayImage(postUrl, image, opt);
		
		String avatarUrl = getIntent().getStringExtra("avatarUrl");
		opt = new DisplayImageOptions.Builder().showImageOnFail(R.drawable.default_avatar).imageScaleType(ImageScaleType.EXACTLY).build();		
		ImageLoader.getInstance().displayImage(avatarUrl, avatar, opt);
		
		backButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				finish();
			}
		});
		commentButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(SPUtil.get(CommentPage.this, MyApp.PREF_TYPE_LOGIN, MyApp.LOGIN_VERIFIED, 0, Integer.class).equals(0)){
					Toast.makeText(CommentPage.this, getResources().getString(R.string.hint_user_unverified), Toast.LENGTH_SHORT).show();
					return;
				} else if("".equals(SPUtil.get(CommentPage.this, MyApp.PREF_TYPE_LOGIN, MyApp.LOGIN_USERNAME, "", String.class))){
					Toast.makeText(CommentPage.this, getResources().getString(R.string.hint_empty_username), Toast.LENGTH_SHORT).show();
					return;
				}
				dialogFragment.show(getFragmentManager(), "comment_dialog");
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
			TextView timeView = (TextView) view.findViewById(R.id.comment_time);
			ImageView userAvatar= (ImageView) view.findViewById(R.id.comment_icon);
			
			String userId = cursor.getString(cursor.getColumnIndex("user_id"));
			String content = cursor.getString(cursor.getColumnIndex("content"));
			String userName = cursor.getString(cursor.getColumnIndex("username"));
			String time = cursor.getString(cursor.getColumnIndex("time"));
			
			contentView.setText(content);
			userNameView.setText(userName);
			timeView.setText(time);
			
			String uri = HTTPUtil.getInstance().composePreURL(CommentPage.this) + 
					getResources().getString(R.string.url_down_img) + "?path=" + userId;
			DisplayImageOptions opt = new DisplayImageOptions.Builder().showImageOnFail(R.drawable.default_avatar).build();
			ImageLoader.getInstance().displayImage(uri, userAvatar, opt);
		}

		@Override
		public View newView(Context ctx, Cursor c, ViewGroup parent) {
			return inflater.inflate(layout, parent, false);
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

	@Override
	public void onDialogConfirmed(String comment) {
		if(comment.length() > MyApp.MAX_COMMENT){
			Toast.makeText(this, getResources().getString(R.string.hint_long_comment), Toast.LENGTH_SHORT).show();
			return;
		}
		Comment cmt = new Comment();
		cmt.setContent(comment);
		cmt.setThoughtId(id);
		cmt.setUserId((String) SPUtil.get(this, MyApp.PREF_TYPE_LOGIN, MyApp.LOGIN_ID, "", String.class));
		new CommentAsyncTask().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, CommentAsyncTask.SEND_COMMENT,
				HTTPUtil.getInstance().composePreURL(this)+getResources().getString(R.string.url_comment_thought),
				this, cmt);
		
	}
	
	@Override
	public void onCommentResult(JSONObject result) {
		if(null != result){
			try {
				JSONObject cmt = result.getJSONObject("comment");
				if(null != cmt){
					Comment comment = (Comment) JSONUtil.fromJSONToObject(cmt, Comment.class);
					ContentValues values = comment.putValues();
					getContentResolver().insert(CommentDAO.CONTENT_URI, values);					
					//for insert, auto requery, no need to manually query
					//for update, need to requery
					LocalBroadcastManager.getInstance(this).sendBroadcast(new Intent(MyApp.BROADCAST_COMMENT));
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
	}
}
