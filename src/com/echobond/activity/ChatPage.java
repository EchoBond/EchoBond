package com.echobond.activity;

import java.util.ArrayList;

import org.json.JSONException;
import org.json.JSONObject;

import com.echobond.R;
import com.echobond.application.MyApp;
import com.echobond.connector.UserMsgAsyncTask;
import com.echobond.dao.ChatDAO;
import com.echobond.entity.User;
import com.echobond.entity.UserMsg;
import com.echobond.intf.UserMsgCallback;
import com.echobond.util.HTTPUtil;
import com.echobond.util.JSONUtil;
import com.echobond.util.SPUtil;
import com.echobond.widget.XListView;
import com.echobond.widget.XListView.IXListViewListener;
import com.google.gson.reflect.TypeToken;
import com.nostra13.universalimageloader.core.ImageLoader;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.app.NavUtils;
import android.support.v4.app.TaskStackBuilder;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.widget.CursorAdapter;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;
/**
 * 
 * @author aohuijun
 *
 */
public class ChatPage extends ActionBarActivity implements LoaderCallbacks<Cursor>, UserMsgCallback, IXListViewListener{
	
	private XListView chatListView;
	private EditText msgInputText;
	private ImageView msgSendView;
	private String userId;
	private String guestId;
	private ChatListAdapter adapter;
	private int currentLimit;
	private static final int DEFAULT_OFFSET = 0;
	private static final int LIMIT_INIT = 10;
	private static final int LIMIT_INCREMENT = 10;
	private static final long LOAD_INTERVAL = 2000;
	private long lastLoadTime;
	private long sendTime = 0;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_chat_page);
		initActionBar();
		initSendingMsg();
		
		adapter = new ChatListAdapter(this, R.layout.item_chat, null, 0);
		chatListView = (XListView)findViewById(R.id.chat_list);
		chatListView.setOverScrollMode(View.OVER_SCROLL_NEVER);
		chatListView.setXListViewListener(this);
		chatListView.setPullLoadEnable(false);
		chatListView.setPullRefreshEnable(true);
		chatListView.setAdapter(adapter);
		userId = (String) SPUtil.get(this, MyApp.PREF_TYPE_LOGIN, MyApp.LOGIN_ID, "", String.class);
		
		guestId = getIntent().getStringExtra("guestId");
		
		currentLimit = LIMIT_INIT;
		
		getSupportLoaderManager().initLoader(MyApp.LOADER_CHAT, null, this);
		
	}

	private void initActionBar() {
		Toolbar chatToolbar = (Toolbar)findViewById(R.id.toolbar_chat_page);
		setSupportActionBar(chatToolbar);
		getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
		getSupportActionBar().setCustomView(R.layout.title_bar_setting);
	}

	private void initSendingMsg() {
		msgInputText = (EditText)findViewById(R.id.chat_msg_input);
		msgSendView = (ImageView)findViewById(R.id.chat_msg_send);
		msgSendView.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				String msg = msgInputText.getText().toString();
				if (msg != null && !msg.equals("")) {
					String url = HTTPUtil.getInstance().composePreURL(ChatPage.this) + getResources().getString(R.string.url_send_msg);
					UserMsg msgObj = new UserMsg();
					msgObj.setSenderId(userId);
					msgObj.setRecverId(guestId);
					msgObj.setContent(msg);
					if ((System.currentTimeMillis() - sendTime) < 1000) {
						Toast.makeText(getApplicationContext(), "Too frequent to speak. ", Toast.LENGTH_SHORT).show();
					} else {
						new UserMsgAsyncTask().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, url, ChatPage.this, UserMsgAsyncTask.MSG_SEND, msgObj);
						msgInputText.setText(null);
					}
					sendTime = System.currentTimeMillis();
				}
			}
		});
	}
	
	public class ChatListAdapter extends CursorAdapter {

		private LayoutInflater inflater;
		private int layout;
		
		public ChatListAdapter(Context context, int layout, Cursor c, int flags) {
			super(context, c, flags);
			this.inflater = LayoutInflater.from(context);
			this.layout = layout;
		}

		@SuppressLint("NewApi") 
		@Override
		public void bindView(View convertView, Context ctx, Cursor c) {
			Integer id = c.getInt(c.getColumnIndex("_id"));
			String senderId = c.getString(c.getColumnIndex("sender_id"));
			String recverId = c.getString(c.getColumnIndex("recver_id"));
			String time = c.getString(c.getColumnIndex("time"));
			String content = c.getString(c.getColumnIndex("content"));
			
			LinearLayout contentLayout = (LinearLayout)convertView.findViewById(R.id.item_chat_content_layout);
			LinearLayout timeLayout = (LinearLayout)convertView.findViewById(R.id.item_chat_time_layout);
			ImageView hostImageView = (ImageView)convertView.findViewById(R.id.item_chat_hostpic);
			ImageView guestImageView = (ImageView)convertView.findViewById(R.id.item_chat_guestpic);
			TextView chatContentView = (TextView)convertView.findViewById(R.id.item_chat_content);
			TextView chatTimeView = (TextView)convertView.findViewById(R.id.item_chat_time);
			TextView idView = (TextView) convertView.findViewById(R.id.item_chat_id);
			TextView senderView = (TextView) convertView.findViewById(R.id.item_chat_sender_id);
			TextView recverView = (TextView) convertView.findViewById(R.id.item_chat_recver_id);
			
			chatContentView.setText(content);
			chatTimeView.setText(time);
			idView.setText(id+"");
			senderView.setText(senderId);
			recverView.setText(recverId);
			
			String url = HTTPUtil.getInstance().composePreURL(ChatPage.this)
					+ getResources().getString(R.string.url_down_img)
					+ "?path=" + senderId;
			if(userId.equals(senderId)){
				ImageLoader.getInstance().displayImage(url, hostImageView);
				guestImageView.setImageDrawable(null);
				contentLayout.setBackground(getResources().getDrawable(R.drawable.square_edittext_green));		// new API
				
				LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
				params.gravity = Gravity.END;
				contentLayout.setLayoutParams(params);
				timeLayout.setLayoutParams(params);
			} else {
				ImageLoader.getInstance().displayImage(url, guestImageView);
				hostImageView.setImageDrawable(null);
				contentLayout.setBackground(getResources().getDrawable(R.drawable.square_edittext_red));		// new API
				
				LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
				params.gravity = Gravity.START;
				contentLayout.setLayoutParams(params);
				timeLayout.setLayoutParams(params);
			}			
		}

		@Override
		public View newView(Context context, Cursor cursor, ViewGroup parent) {
			View view = inflater.inflate(layout, parent, false);
			return view;
		}
		
	}

	@Override
	public Loader<Cursor> onCreateLoader(int loader, Bundle arg1) {
		switch(loader){
		case MyApp.LOADER_CHAT:
			Uri uri = ChatDAO.CONTENT_URI;
			String[] args = new String[]{userId, guestId, guestId, userId, currentLimit+"", DEFAULT_OFFSET+""};
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

	@Override
	public void onSendResult(JSONObject result) {
				
	}

	@SuppressWarnings("unchecked")
	@Override
	public void onLoadResult(JSONObject result) {
		if(null != result){
			TypeToken<ArrayList<UserMsg>> token = new TypeToken<ArrayList<UserMsg>>(){};
			ArrayList<UserMsg> msgs = null;
			try {
				msgs = (ArrayList<UserMsg>) JSONUtil.fromJSONArrayToList(result.getJSONArray("msgList"), token);
			} catch (JSONException e) {
				e.printStackTrace();
			}
			ContentValues[] values = new ContentValues[msgs.size()];
			int i = 0;
			for (UserMsg msg : msgs) {
				values[i++] = msg.putValues();
			}
			getContentResolver().bulkInsert(ChatDAO.CONTENT_URI, values);
			Cursor cursor = getContentResolver().query(ChatDAO.CONTENT_URI, null, null, 
					new String[]{userId, guestId, guestId, userId, currentLimit+"", DEFAULT_OFFSET+""}, null);
			adapter.swapCursor(cursor);
			adapter.notifyDataSetChanged();
		}
		onLoadFinished();
	}

	@Override
	public void onRefresh() {
		if(System.currentTimeMillis() - lastLoadTime > LOAD_INTERVAL){
			lastLoadTime = System.currentTimeMillis();
			User me = new User();
			me.setId((String) SPUtil.get(this, MyApp.PREF_TYPE_LOGIN, MyApp.LOGIN_ID, null, String.class));
			User guest = new User();
			guest.setId(guestId);
			currentLimit += LIMIT_INCREMENT;
			new UserMsgAsyncTask().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, 
					HTTPUtil.getInstance().composePreURL(this) + getResources().getString(R.string.url_load_msg),
					this, UserMsgAsyncTask.MSG_LOAD, me, guest, currentLimit, DEFAULT_OFFSET);
		} else {
			onLoadFinished();
		}
	}

	@Override
	public void onLoadMore() {
		
	}
	
	public void onLoadFinished() {
		chatListView.stopRefresh();
		chatListView.stopLoadMore();
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
			Intent upIntent = NavUtils.getParentActivityIntent(ChatPage.this);
			if (NavUtils.shouldUpRecreateTask(ChatPage.this, upIntent)) {
				TaskStackBuilder.create(ChatPage.this).addNextIntentWithParentStack(upIntent).startActivities();
			}else {
				upIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				NavUtils.navigateUpTo(ChatPage.this, upIntent);
			}
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}
}
