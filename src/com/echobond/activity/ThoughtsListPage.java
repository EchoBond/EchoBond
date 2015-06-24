package com.echobond.activity;

import java.util.ArrayList;

import org.json.JSONException;
import org.json.JSONObject;

import com.echobond.R;
import com.echobond.application.MyApp;
import com.echobond.connector.BoostAsyncTask;
import com.echobond.connector.LoadThoughtAsyncTask;
import com.echobond.dao.CommentDAO;
import com.echobond.dao.HomeThoughtDAO;
import com.echobond.dao.HotThoughtDAO;
import com.echobond.dao.ThoughtTagDAO;
import com.echobond.dao.UserDAO;
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

import android.app.ActionBar;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.widget.CursorAdapter;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
/**
 * 
 * @author aohuijun
 *
 */
public class ThoughtsListPage extends ActionBarActivity implements IXListViewListener,LoadThoughtCallback, BoostCallback, LoaderCallbacks<Cursor> {

	private XListView thoughtsListView;
	private ThoughtListAdapter adapter;
	private TextView titleView;
	private ImageView backButton;
	private UserDAO userDAO;
	private CommentDAO commentDAO;
	private ThoughtTagDAO thoughtTagDAO;
	private static final int POST = 0;
	private static final int MESSAGE = 1;
	private static final int BOOST = 2;
	private static final int COMMENT = 3;
	private static final int SHARE = 4;
	private int currentLimit;
	private static final int DEFAULT_OFFSET = 0;
	private static final int LIMIT_INIT = 10;
	private static final int LIMIT_INCREMENT = 10;
	private static final long LOAD_INTERVAL = 2000;
	private long lastLoadTime;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_thoughts_list_page);
		initActionBar();
		initThoughtsList();
		
	}

	private void initActionBar() {
		Toolbar thoughtsListToolbar = (Toolbar)findViewById(R.id.toolbar_thoughts_list);
		setSupportActionBar(thoughtsListToolbar);
		getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
		getSupportActionBar().setCustomView(R.layout.title_bar_setting);
		
		backButton = (ImageView)findViewById(R.id.button_left_side);
		backButton.setImageDrawable(getResources().getDrawable(R.drawable.button_back));
		backButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				finish();
			}
		});
		
		titleView = (TextView)findViewById(R.id.title_name);
		titleView.setText(getIntent().getStringExtra("userName") + "Thoughts");
	}

	private void initThoughtsList() {
		adapter = new ThoughtListAdapter(this, R.layout.item_thought, null, 0);
		thoughtsListView = (XListView)findViewById(R.id.thoughts_list);
		thoughtsListView.setAdapter(adapter);
		thoughtsListView.setOverScrollMode(View.OVER_SCROLL_NEVER);
		thoughtsListView.setPullRefreshEnable(true);
		thoughtsListView.setPullLoadEnable(true);
		thoughtsListView.setXListViewListener(this);
		
		userDAO = new UserDAO(this);
		currentLimit = LIMIT_INIT;
		lastLoadTime = 0;
		commentDAO = new CommentDAO();
		thoughtTagDAO = new ThoughtTagDAO(this);
		getSupportLoaderManager().initLoader(MyApp.LOADER_HOME, null, this);		
	}
	
	public class ThoughtListAdapter extends CursorAdapter {

		private LayoutInflater inflater;
		private int layout;
		
		public ThoughtListAdapter(Context context, int layout, Cursor c, int flags) {
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
			
			postFigure.setOnClickListener(new FunctionOnClickListener(POST));
			messageButton.setOnClickListener(new FunctionOnClickListener(MESSAGE));
			boostButton.setOnClickListener(new FunctionOnClickListener(BOOST));
			commentButton.setOnClickListener(new FunctionOnClickListener(COMMENT));
			shareButton.setOnClickListener(new FunctionOnClickListener(SHARE));
			
			if(isUserBoost == 1){
				boostButton.setImageResource(R.drawable.thoughts_rocket_up_boost);
			} else {
				boostButton.setImageResource(R.drawable.thoughts_rocket_up_normal);
			}
			
			String fileName;
			fileName = imagePathView.getText().toString();
			String url = HTTPUtil.getInstance().composePreURL(ThoughtsListPage.this) 
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

		@Override
		public View newView(Context context, Cursor cursor, ViewGroup parent) {
			View view = inflater.inflate(layout, parent, false);
			return view;
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
			case POST:
				Intent imageIntent = new Intent();
				imageIntent.setClass(ThoughtsListPage.this, ImagePage.class);
				String imageUrl = HTTPUtil.getInstance().composePreURL(ThoughtsListPage.this)
						+ getResources().getString(R.string.url_down_img)
						+ "?path=" + postPath;
				imageIntent.putExtra("url", imageUrl);
				imageIntent.putExtra("type", "home");
				imageIntent.putExtra("id", id);
				startActivity(imageIntent);
				break;
			case MESSAGE:
				/*String localId = (String) SPUtil.get(getActivity(), MyApp.PREF_TYPE_LOGIN, MyApp.LOGIN_ID, "", String.class);
				if(localId.equals(userId)){
					Toast.makeText(getActivity(), "Sorry but you can't talk to yourself!", Toast.LENGTH_SHORT).show();
				} else {*/
					Intent chatIntent = new Intent();
					chatIntent.setClass(ThoughtsListPage.this, ChatPage.class);
					chatIntent.putExtra("guestId", userId);
					chatIntent.putExtra("userName", userName);
					startActivity(chatIntent);
				//}
				break;
			case BOOST:				
				new BoostAsyncTask().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, 
						HTTPUtil.getInstance().composePreURL(ThoughtsListPage.this) + getResources().getString(R.string.url_boost_thought), 
						ThoughtsListPage.this, id, SPUtil.get(ThoughtsListPage.this, MyApp.PREF_TYPE_LOGIN, MyApp.LOGIN_ID, null, String.class));
				break;
			case COMMENT:
				Intent commentIntent = new Intent();
				commentIntent.setClass(ThoughtsListPage.this, CommentPage.class);
				String posterAvatar = userId;
				String avatarUrl = HTTPUtil.getInstance().composePreURL(ThoughtsListPage.this)
						+ getResources().getString(R.string.url_down_img)
						+ "?path=" + posterAvatar;
				String postUrl = HTTPUtil.getInstance().composePreURL(ThoughtsListPage.this)
						+ getResources().getString(R.string.url_down_img)
						+ "?path=" + postPath;
				commentIntent.putExtra("avatarUrl", avatarUrl);
				commentIntent.putExtra("postUrl", postUrl);
				commentIntent.putExtra("id", id);
				commentIntent.putExtra("userName", userName);
				commentIntent.putExtra("content", content);
				startActivity(commentIntent);
				break;
			case SHARE:				
				break;
			default:
				break;
			}
		}
	}	
	
	public void onLoadFinished() {
		thoughtsListView.stopRefresh();
		thoughtsListView.stopLoadMore();
	}

	@Override
	public void onRefresh() {
		if(System.currentTimeMillis() - lastLoadTime > LOAD_INTERVAL){
			lastLoadTime = System.currentTimeMillis();
			User user = new User();
			user.setId((String) SPUtil.get(this, MyApp.PREF_TYPE_LOGIN, MyApp.LOGIN_ID, null, String.class));
			new LoadThoughtAsyncTask().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, 
					HTTPUtil.getInstance().composePreURL(this) + getResources().getString(R.string.url_load_thoughts), 
					LoadThoughtAsyncTask.LOAD_T_HOME, this, DEFAULT_OFFSET, currentLimit, user);
		} else {
			onLoadFinished();
		}
	}

	@Override
	public void onLoadMore() {
		if(System.currentTimeMillis() - lastLoadTime > LOAD_INTERVAL){
			lastLoadTime = System.currentTimeMillis();
			User user = new User();
			user.setId((String) SPUtil.get(this, MyApp.PREF_TYPE_LOGIN, MyApp.LOGIN_ID, null, String.class));
			currentLimit += LIMIT_INCREMENT;
			new LoadThoughtAsyncTask().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, 
					HTTPUtil.getInstance().composePreURL(this)+getResources().getString(R.string.url_load_thoughts), 
					LoadThoughtAsyncTask.LOAD_T_HOME, this, DEFAULT_OFFSET, currentLimit, user);
		} else {
			onLoadFinished();
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public void onLoadThoughtResult(JSONObject result) {
		if(null != result){
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
				userDAO.addUser(thought.getUser());
				/* comments */
				commentDAO.addComments(thought.getComments());
				/* tags */
				thoughtTagDAO.addThoughtTags(thought.getId(), thought.getTags());
			}
			/* Inserting thoughts */
			getContentResolver().bulkInsert(HomeThoughtDAO.CONTENT_URI, contentValues);
			/* Update UI */
			updateListView();
			onLoadFinished();
		} else {
			onLoadFinished();
			Toast.makeText(getApplicationContext(), getResources().getString(R.string.network_issue), Toast.LENGTH_LONG).show();
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
				ContentResolver resolver = getContentResolver();
				resolver.update(HomeThoughtDAO.CONTENT_URI, values, where, null);
				updateListView();
				/* Update HotThought if this thought is also there */
				resolver.update(HotThoughtDAO.CONTENT_URI, values, where, null);
			} catch (JSONException e) {
				e.printStackTrace();
			}
		} else {
			Toast.makeText(this.getApplicationContext(), getResources().getString(R.string.network_issue), Toast.LENGTH_LONG).show();			
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
			String[] args = new String[]{(String) SPUtil.get(this, MyApp.PREF_TYPE_LOGIN, MyApp.LOGIN_ID, "", String.class), 
					currentLimit+"", DEFAULT_OFFSET+""};
			return new CursorLoader(this, uri, null, null, args, null);
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
	
	private void updateListView(){
		Cursor cursor = getContentResolver().query(HomeThoughtDAO.CONTENT_URI, null, null, new String[]{
				(String) SPUtil.get(this, MyApp.PREF_TYPE_LOGIN, MyApp.LOGIN_ID, "", String.class),
				currentLimit+"", DEFAULT_OFFSET+""}, null);
		adapter.getCursor().close();
		adapter.swapCursor(cursor);
		adapter.notifyDataSetChanged();
	}
}
