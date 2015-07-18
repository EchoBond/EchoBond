package com.echobond.fragment;

import java.util.ArrayList;

import org.json.JSONException;
import org.json.JSONObject;

import com.echobond.R;
import com.echobond.activity.ChatPage;
import com.echobond.activity.CommentPage;
import com.echobond.activity.ImagePage;
import com.echobond.activity.MainPage;
import com.echobond.application.MyApp;
import com.echobond.connector.BoostAsyncTask;
import com.echobond.connector.LoadThoughtAsyncTask;
import com.echobond.dao.CommentDAO;
import com.echobond.dao.HomeThoughtDAO;
import com.echobond.dao.HotThoughtDAO;
import com.echobond.dao.ThoughtTagDAO;
import com.echobond.dao.UserDAO;
import com.echobond.entity.Comment;
import com.echobond.entity.Tag;
import com.echobond.entity.Thought;
import com.echobond.entity.User;
import com.echobond.intf.BoostCallback;
import com.echobond.intf.LoadThoughtCallback;
import com.echobond.util.HTTPUtil;
import com.echobond.util.JSONUtil;
import com.echobond.util.SPUtil;
import com.echobond.widget.XListView;
import com.echobond.widget.XListView.IXListViewListener;
import com.google.gson.reflect.TypeToken;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingProgressListener;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.widget.CursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
/**
 * 
 * @author Luck
 *
 */
public class HomeThoughtFragment extends Fragment implements AdapterView.OnItemClickListener, IXListViewListener, LoadThoughtCallback, BoostCallback, LoaderCallbacks<Cursor>{

	private ThoughtAdapter adapter;
	private XListView mListView;

	private MainPage mActivity;
	
	private int currentLimit;
	private long lastLoadTime;
	private BroadcastReceiver boostReceiver = new BroadcastReceiver() {
		
		@Override
		public void onReceive(Context context, Intent intent) {
			if(null != intent && null != intent.getExtras()){
				if(intent.getBooleanExtra(MyApp.INTENT_MSG_UPDATE_HOME, false)){
					updateListView();
				}
			}
		}
	};
	
	private BroadcastReceiver commentReceiver = new BroadcastReceiver() {
		
		@Override
		public void onReceive(Context context, Intent intent) {
			updateListView();
		}
	};
	
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		
		View thoughtView = inflater.inflate(R.layout.fragment_main_thoughts, container, false);
		mListView = (XListView)thoughtView.findViewById(R.id.list_thoughts);
		adapter = new ThoughtAdapter(getActivity(), R.layout.item_thought, null, 0);
		mListView.setAdapter(adapter);
		mListView.setOverScrollMode(View.OVER_SCROLL_NEVER);
		mListView.setOnItemClickListener(this);
		mListView.setXListViewListener(this);
		mListView.setPullLoadEnable(true);
		
		currentLimit = MyApp.LIMIT_INIT;
		lastLoadTime = 0;
		
		LocalBroadcastManager.getInstance(getActivity()).registerReceiver(boostReceiver, new IntentFilter(MyApp.BROADCAST_BOOST));
		LocalBroadcastManager.getInstance(getActivity()).registerReceiver(commentReceiver, new IntentFilter(MyApp.BROADCAST_COMMENT));
		
		getLoaderManager().initLoader(MyApp.LOADER_HOME, null, this);
		
		return thoughtView;
	}
	
	public class ThoughtAdapter extends CursorAdapter {

		private LayoutInflater inflater;
		private int layout;
		public ThoughtAdapter(Context context, int layout, Cursor c, int flags) {
			super(context, c, flags);
			this.layout = layout;
			this.inflater = LayoutInflater.from(context);
		}
		
		@Override
		public boolean isEnabled(int position) {
			return false;
		}
		
		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return super.getCount();
		}

		@Override
		public View newView(Context context, Cursor cursor, ViewGroup parent) {
			View view = inflater.inflate(layout, parent, false);
			return view;
		}
		
		@Override
		public void bindView(View convertView, Context ctx, Cursor c) {
			String id = c.getString(c.getColumnIndex("_id"));
			String title = c.getString(c.getColumnIndex("username"));
			String content = c.getString(c.getColumnIndex("content"));
			String path = c.getString(c.getColumnIndex("image"));
			String userId = c.getString(c.getColumnIndex("user_id"));
			int boost = c.getInt(c.getColumnIndex("boost"));
			int cmt = c.getInt(c.getColumnIndex("num_of_cmt"));
			int isUserBoost = c.getInt(c.getColumnIndex("isUserBoost")); 
			
			TextView titleView = (TextView)convertView.findViewById(R.id.thought_list_title);
			TextView contentView = (TextView)convertView.findViewById(R.id.thought_list_content);
			TextView boostsNum = (TextView)convertView.findViewById(R.id.thought_list_boostsnum);
			TextView commentsNum = (TextView)convertView.findViewById(R.id.thought_list_commentsnum);
			TextView thoughtIdView = (TextView)convertView.findViewById(R.id.thought_list_id);
			TextView imagePathView = (TextView) convertView.findViewById(R.id.thought_list_image);
			TextView isUserBoostView = (TextView) convertView.findViewById(R.id.thought_list_isUserBoost);
			TextView userIdView = (TextView) convertView.findViewById(R.id.thought_list_poster_id);
			
			titleView.setText(title);
			contentView.setText(content);
			boostsNum.setText(boost+"");
			commentsNum.setText(cmt+"");
			thoughtIdView.setText(id);
			imagePathView.setText(path);
			isUserBoostView.setText(isUserBoost+"");
			userIdView.setText(userId);
			
			ImageView postFigure = (ImageView)convertView.findViewById(R.id.thought_list_pic);
			ImageView messageButton = (ImageView)convertView.findViewById(R.id.thought_list_message);
			ImageView boostButton = (ImageView)convertView.findViewById(R.id.thought_list_boost);
			ImageView commentButton = (ImageView)convertView.findViewById(R.id.thought_list_comment);
			ImageView shareButton = (ImageView)convertView.findViewById(R.id.thought_list_share);
			
			postFigure.setOnClickListener(new FunctionOnClickListener(MyApp.THOUGHT_POST));
			messageButton.setOnClickListener(new FunctionOnClickListener(MyApp.THOUGHT_MESSAGE));
			boostButton.setOnClickListener(new FunctionOnClickListener(MyApp.THOUGHT_BOOST));
			commentButton.setOnClickListener(new FunctionOnClickListener(MyApp.THOUGHT_COMMENT));
			shareButton.setOnClickListener(new FunctionOnClickListener(MyApp.THOUGHT_SHARE));
			
			if(isUserBoost == 1){
				boostButton.setImageResource(R.drawable.thoughts_rocket_up_boost);
			} else {
				boostButton.setImageResource(R.drawable.thoughts_rocket_up_normal);
			}
			
			String fileName;
			fileName = imagePathView.getText().toString();
			String url = HTTPUtil.getInstance().composePreURL(getActivity()) 
					+ getResources().getString(R.string.url_down_img)
					+ "?path=" + fileName;
			DisplayImageOptions opt = new DisplayImageOptions.Builder().showImageOnFail(R.drawable.no_image).cacheInMemory(true).cacheOnDisk(true).build();
			ImageLoader.getInstance().displayImage(url, postFigure, opt, new SimpleImageLoadingListener(){
				@Override
				public void onLoadingStarted(String imageUri, View view) {
					RelativeLayout layout = (RelativeLayout) view.getParent();
					ProgressBar spinner = (ProgressBar) layout.findViewById(R.id.thought_list_spinner);
					spinner.setVisibility(View.VISIBLE);
					spinner.setProgress(10);
				}

				@Override
				public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
					RelativeLayout layout = (RelativeLayout) view.getParent();
					ProgressBar spinner = (ProgressBar) layout.findViewById(R.id.thought_list_spinner);
					spinner.setVisibility(View.INVISIBLE);
				}

				@Override
				public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
					RelativeLayout layout = (RelativeLayout) view.getParent();
					ProgressBar spinner = (ProgressBar) layout.findViewById(R.id.thought_list_spinner);
					spinner.setVisibility(View.INVISIBLE);
				}
			}, new ImageLoadingProgressListener() {
				
				/**
				 * Will only be called when cacheInDisk is enabled
				 */
				@Override
				public void onProgressUpdate(String imageUri, View view, int current,
						int total) {
					RelativeLayout layout = (RelativeLayout) view.getParent();
					ProgressBar spinner = (ProgressBar) layout.findViewById(R.id.thought_list_spinner);
					spinner.setProgress(Math.round(100.0f * current / total));
				}
			});
		}

	}

	public class FunctionOnClickListener implements OnClickListener {

		private int buttonIndex = 1;
		public FunctionOnClickListener(int i) {	
			buttonIndex = i;
		}

		@Override
		public void onClick(View v) {
			RelativeLayout root;
			if(null != v.getParent().getParent() && v.getParent().getParent() instanceof RelativeLayout)
				root = (RelativeLayout) v.getParent().getParent();
			else root = (RelativeLayout) v.getParent();
			TextView idView = (TextView) root.findViewById(R.id.thought_list_id);			
			Integer id = Integer.parseInt(idView.getText().toString());
			TextView imageView = (TextView) root.findViewById(R.id.thought_list_image);
			String postPath = imageView.getText().toString();
			TextView titleView = (TextView) root.findViewById(R.id.thought_list_title);
			String userName = titleView.getText().toString();
			TextView contentView = (TextView) root.findViewById(R.id.thought_list_content);
			String content = contentView.getText().toString();
			TextView userIdView = (TextView) root.findViewById(R.id.thought_list_poster_id);
			String userId = userIdView.getText().toString();
			switch (buttonIndex) {
			case MyApp.THOUGHT_POST:
				Intent imageIntent = new Intent();
				imageIntent.setClass(HomeThoughtFragment.this.getActivity(), ImagePage.class);
				String imageUrl = HTTPUtil.getInstance().composePreURL(getActivity())
						+ getResources().getString(R.string.url_down_img)
						+ "?path=" + postPath;
				imageIntent.putExtra("url", imageUrl);
				imageIntent.putExtra("id", id);
				imageIntent.putExtra("type", "home");
				startActivity(imageIntent);
				break;
			case MyApp.THOUGHT_MESSAGE:
				if(SPUtil.get(HomeThoughtFragment.this.getActivity(), MyApp.PREF_TYPE_LOGIN, MyApp.LOGIN_VERIFIED, 0, Integer.class).equals(0)){
					Toast.makeText(HomeThoughtFragment.this.getActivity(), getResources().getString(R.string.hint_user_unverified), Toast.LENGTH_SHORT).show();
					return;
				}
				String localId = (String) SPUtil.get(getActivity(), MyApp.PREF_TYPE_LOGIN, MyApp.LOGIN_ID, "", String.class);
				if(localId.equals(userId)){
					Toast.makeText(getActivity(), getResources().getString(R.string.hint_talk_self), Toast.LENGTH_SHORT).show();
				} else if("".equals(SPUtil.get(HomeThoughtFragment.this.getActivity(), MyApp.PREF_TYPE_LOGIN, MyApp.LOGIN_USERNAME, "", String.class))){
					Toast.makeText(HomeThoughtFragment.this.getActivity(), getResources().getString(R.string.hint_empty_username), Toast.LENGTH_SHORT).show();
					return;
				} else {
					Intent chatIntent = new Intent();
					chatIntent.setClass(HomeThoughtFragment.this.getActivity(), ChatPage.class);
					chatIntent.putExtra("guestId", userId);
					chatIntent.putExtra("userName", userName);
					startActivity(chatIntent);
				}
				break;
			case MyApp.THOUGHT_BOOST:
				if(SPUtil.get(HomeThoughtFragment.this.getActivity(), MyApp.PREF_TYPE_LOGIN, MyApp.LOGIN_VERIFIED, 0, Integer.class).equals(0)){
					Toast.makeText(HomeThoughtFragment.this.getActivity(), getResources().getString(R.string.hint_user_unverified), Toast.LENGTH_SHORT).show();
					return;
				}
				new BoostAsyncTask().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, 
						HTTPUtil.getInstance().composePreURL(getActivity()) + getResources().getString(R.string.url_boost_thought), 
						HomeThoughtFragment.this, id, SPUtil.get(getActivity(), MyApp.PREF_TYPE_LOGIN, MyApp.LOGIN_ID, null, String.class));
				break;
			case MyApp.THOUGHT_COMMENT:
				Intent commentIntent = new Intent();
				commentIntent.setClass(HomeThoughtFragment.this.getActivity(), CommentPage.class);
				String posterAvatar = userId;
				String avatarUrl = HTTPUtil.getInstance().composePreURL(getActivity())
						+ getResources().getString(R.string.url_down_img)
						+ "?path=" + posterAvatar;
				String postUrl = HTTPUtil.getInstance().composePreURL(getActivity())
						+ getResources().getString(R.string.url_down_img)
						+ "?path=" + postPath;
				commentIntent.putExtra("avatarUrl", avatarUrl);
				commentIntent.putExtra("postUrl", postUrl);
				commentIntent.putExtra("id", id);
				commentIntent.putExtra("userName", userName);
				commentIntent.putExtra("content", content);
				startActivity(commentIntent);
				break;
			case MyApp.THOUGHT_SHARE:
				Toast.makeText(getActivity().getApplicationContext(), getString(R.string.hint_under_development), Toast.LENGTH_SHORT).show();
				break;
			default:
				break;
			}
		}
	}
	
	@Override
	public void onActivityCreated(@Nullable Bundle savedInstanceState) {
		
		super.onActivityCreated(savedInstanceState);
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		
	}
	
	public void onLoadFinished() {
		mListView.stopRefresh();
		mListView.stopLoadMore();
	}

	@Override
	public void onRefresh() {
		if(System.currentTimeMillis() - lastLoadTime > MyApp.LOAD_INTERVAL){
			lastLoadTime = System.currentTimeMillis();
			User user = new User();
			user.setId((String) SPUtil.get(getActivity(), MyApp.PREF_TYPE_LOGIN, MyApp.LOGIN_ID, null, String.class));
			new LoadThoughtAsyncTask().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, 
					HTTPUtil.getInstance().composePreURL(getActivity()) + getResources().getString(R.string.url_load_thoughts), 
					LoadThoughtAsyncTask.LOAD_T_HOME, this, MyApp.DEFAULT_OFFSET, currentLimit, user);
		} else {
			onLoadFinished();
		}
	}

	@Override
	public void onLoadMore() {
		if(System.currentTimeMillis() - lastLoadTime > MyApp.LOAD_INTERVAL){
			lastLoadTime = System.currentTimeMillis();
			User user = new User();
			user.setId((String) SPUtil.get(getActivity(), MyApp.PREF_TYPE_LOGIN, MyApp.LOGIN_ID, null, String.class));
			currentLimit += MyApp.LIMIT_INCREMENT;
			new LoadThoughtAsyncTask().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, 
					HTTPUtil.getInstance().composePreURL(getActivity())+getResources().getString(R.string.url_load_thoughts), 
					LoadThoughtAsyncTask.LOAD_T_HOME, this, MyApp.DEFAULT_OFFSET, currentLimit, user);
		} else {
			onLoadFinished();
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public void onLoadThoughtResult(JSONObject result) {
		if(null != result){
			getActivity().getContentResolver().delete(HomeThoughtDAO.CONTENT_URI, null, null);
			TypeToken<ArrayList<Thought>> token = new TypeToken<ArrayList<Thought>>(){};
			ArrayList<Thought> thoughts = null;
			try {
				thoughts = (ArrayList<Thought>) JSONUtil.fromJSONArrayToList(result.getJSONArray("thoughts"), token);
			} catch (JSONException e) {
				e.printStackTrace();
			}
			ContentValues[] contentValues = new ContentValues[thoughts.size()];
			int i = 0;
			/* Loading thoughts */
			for (Thought thought : thoughts) {
				/* thoughts */
				contentValues[i++] = thought.putValues();
				/* author */
				getActivity().getContentResolver().insert(UserDAO.CONTENT_URI_USER, thought.getUser().putValues());
				/* comments */
				if(null != thought.getComments()){
					int x = 0;
					ContentValues[] cmtValues = new ContentValues[thought.getComments().size()]; 
					for(Comment cmt: thought.getComments()){
						cmtValues[x++] = cmt.putValues();
					}
					getActivity().getContentResolver().bulkInsert(CommentDAO.CONTENT_URI, cmtValues);
				}
				/* tags */
				if(null != thought.getTags()){
					int x = 0;
					ContentValues[] thoughtTagValues = new ContentValues[thought.getTags().size()];
					for(Tag t: thought.getTags()){
						ContentValues value = new ContentValues();
						value.put("thought_id", thought.getId());
						value.put("tag_id", t.getId());
						thoughtTagValues[x++] = value;
					}
					getActivity().getContentResolver().bulkInsert(ThoughtTagDAO.CONTENT_URI, thoughtTagValues);
				}
			}
			/* Inserting thoughts */
			getActivity().getContentResolver().bulkInsert(HomeThoughtDAO.CONTENT_URI, contentValues);
			/* Update UI */
			updateListView();
			onLoadFinished();
		} else {
			onLoadFinished();
			Toast.makeText(getActivity().getApplicationContext(), getResources().getString(R.string.hint_network_issue), Toast.LENGTH_LONG).show();
		}
	}

	@Override
	public void onBoostResult(JSONObject result) {
		if(null != result){			
			try {
				Integer action =  result.getInt("action");
				Integer id = result.getInt("id");
				Integer totalBoost = result.getInt("total");
				ContentValues values = new ContentValues();
				values.put("isUserBoost", action);
				values.put("boost", totalBoost);
				String where = "_id="+id;
				ContentResolver resolver = getActivity().getContentResolver();
				resolver.update(HomeThoughtDAO.CONTENT_URI, values, where, null);
				updateListView();
				/* Update HotThought if this thought is also there */
				resolver.update(HotThoughtDAO.CONTENT_URI, values, where, null);
				Intent intent = new Intent(MyApp.BROADCAST_BOOST);
				intent.putExtra(MyApp.INTENT_MSG_UPDATE_HOT, true);
				LocalBroadcastManager.getInstance(getActivity()).sendBroadcast(intent);
			} catch (JSONException e) {
				e.printStackTrace();
			}
		} else {
			Toast.makeText(getActivity().getApplicationContext(), getResources().getString(R.string.hint_network_issue), Toast.LENGTH_LONG).show();			
		}
	}
	
	/**
	 * will auto query once upon creation!
	 */
	@Override
	public Loader<Cursor> onCreateLoader(int loader, Bundle arg1) {
		switch(loader){
		case MyApp.LOADER_HOME:
			Uri uri = HomeThoughtDAO.CONTENT_URI;
			String[] args = new String[]{currentLimit+"", MyApp.DEFAULT_OFFSET+""};
			return new CursorLoader(getActivity(), uri, null, null, args, null);
		}
		return null;
	}
	
	@Override
	public void onLoadFinished(Loader<Cursor> arg0, Cursor cursor) {
		adapter.swapCursor(cursor);
	}

	@Override
	public void onLoaderReset(Loader<Cursor> arg0) {
		adapter.swapCursor(null);
	}
	
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		mActivity = (MainPage) activity;
	}
	
	private void updateListView(){
		Cursor cursor = mActivity.getContentResolver().query(HomeThoughtDAO.CONTENT_URI, null, null, new String[]{currentLimit+"", MyApp.DEFAULT_OFFSET+""}, null);
		adapter.swapCursor(cursor);
		adapter.notifyDataSetChanged();
	}
	
}
