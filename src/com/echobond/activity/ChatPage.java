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
import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.app.NavUtils;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.widget.CursorAdapter;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
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
	
	private Toolbar toolbar;
	private TextView titleView;
	private XListView chatListView;
	private EditText msgInputText;
	private ImageView msgSendView;
	private String userId;
	private String guestId;
	private String userName;
	private ChatListAdapter adapter;
	private int currentLimit;
	private static final int DEFAULT_OFFSET = 0;
	private static final int LIMIT_INIT = 10;
	private static final int LIMIT_INCREMENT = 10;
	private static final long LOAD_INTERVAL = 2000;
	private long lastLoadTime;
	private long sendTime = 0;
	
	private BroadcastReceiver receiver = new BroadcastReceiver() {
		
		@Override
		public void onReceive(Context context, Intent intent) {
			ContentValues values = new ContentValues();
			values.put("is_read", 1);
			String where = "_id=?";
			String[] selectionArgs = new String[]{intent.getExtras().getInt("id")+""};
			getContentResolver().update(ChatDAO.CONTENT_URI, values, where, selectionArgs);
			updateUI();
		}
	};
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_chat_page);
		initActionBar();
		initSendingMsg();
		toolbar = (Toolbar) findViewById(R.id.toolbar_chat_page);
		titleView = (TextView) toolbar.findViewById(R.id.title_name);
		adapter = new ChatListAdapter(this, R.layout.item_chat, null, 0);
		chatListView = (XListView)findViewById(R.id.chat_list);
		chatListView.setOverScrollMode(View.OVER_SCROLL_NEVER);
		chatListView.setXListViewListener(this);
		chatListView.setPullLoadEnable(false);
		chatListView.setPullRefreshEnable(true);
		chatListView.setAdapter(adapter);
		userId = (String) SPUtil.get(this, MyApp.PREF_TYPE_LOGIN, MyApp.LOGIN_ID, "", String.class);
		
		guestId = getIntent().getStringExtra("guestId");
		userName = getIntent().getStringExtra("userName");
		
		titleView.setText("Talking with "+userName);
		
		currentLimit = LIMIT_INIT;
		
		ContentValues values = new ContentValues();
		values.put("is_read", 1);
		String where = "recver_id=? OR sender_id=?";
		String[] selectionArgs = new String[]{guestId, guestId};
		getContentResolver().update(ChatDAO.CONTENT_URI, values, where, selectionArgs);
		
		selectionArgs = new String[]{userId,""};
		Cursor c = getContentResolver().query(ChatDAO.CONTENT_URI, null, null, selectionArgs, null);
		if(c.moveToFirst()){
			int count = c.getInt(c.getColumnIndex("count"));
			if(count == 0){
				Intent notifyIntent = new Intent("newNotification");
				notifyIntent.putExtra("new", false);
				LocalBroadcastManager.getInstance(this).sendBroadcast(notifyIntent);
				sendBroadcast(notifyIntent);
			}
			c.close();
		}
		
		getSupportLoaderManager().initLoader(MyApp.LOADER_CHAT, null, this);
		LocalBroadcastManager.getInstance(this).registerReceiver(receiver, new IntentFilter("chatWith"+guestId));
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		LocalBroadcastManager.getInstance(this).unregisterReceiver(receiver);
	}
	
	@Override
	protected void onNewIntent(Intent intent) {
		setIntent(intent);
		guestId = getIntent().getStringExtra("guestId");
		userName = getIntent().getStringExtra("userName");
		titleView.setText(userName);
		updateUI();
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
		
		msgInputText.addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
					int arg3) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void afterTextChanged(Editable arg0) {
				msgSendView.setImageDrawable(getResources().getDrawable(R.drawable.button_send_ready));
				if ("".equals(msgInputText.getText().toString().trim())) {
					msgSendView.setImageDrawable(getResources().getDrawable(R.drawable.button_send_normal));
				}
			}
		});
		
		msgSendView.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				String msg = msgInputText.getText().toString().trim();
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
				msgSendView.setImageDrawable(getResources().getDrawable(R.drawable.button_send_normal));
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
		if(null != result){
			try {
				if("0".equals(result.getString("success"))){
					Toast.makeText(this, getResources().getString(R.string.network_issue), Toast.LENGTH_SHORT).show();
					return;
				}
				JSONObject msgJSON = result.getJSONObject("msg");
				UserMsg msg = (UserMsg) JSONUtil.fromJSONToObject(msgJSON, UserMsg.class);
				ContentValues values = msg.putValues();
				//always read for messages sent by myself
				values.put("is_read", 1);
				getContentResolver().insert(ChatDAO.CONTENT_URI, values);
				updateUI();
			} catch (JSONException e) {
				e.printStackTrace();
			}			
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public void onLoadResult(JSONObject result) {
		if(null != result){
			ArrayList<UserMsg> msgs = null;
			try {
				TypeToken<ArrayList<UserMsg>> token = new TypeToken<ArrayList<UserMsg>>(){};				
				msgs = (ArrayList<UserMsg>) JSONUtil.fromJSONArrayToList(result.getJSONArray("msgList"), token);
			} catch (JSONException e) {
				e.printStackTrace();
			}
			ContentValues[] values = new ContentValues[msgs.size()];
			int i = 0;
			for (UserMsg msg : msgs) {
				msg.setIsRead(1);
				values[i++] = msg.putValues();
			}
			getContentResolver().bulkInsert(ChatDAO.CONTENT_URI, values);
			updateUI();
			chatListView.smoothScrollToPositionFromTop(LIMIT_INCREMENT, 5);
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
			Intent upIntent = new Intent(this, MainPage.class);
			upIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			if (NavUtils.shouldUpRecreateTask(ChatPage.this, upIntent)) {
				startActivity(upIntent);
			} else {
				NavUtils.navigateUpTo(ChatPage.this, upIntent);
			}
			//Intent upIntent = NavUtils.getParentActivityIntent(ChatPage.this);
			/*
			if (NavUtils.shouldUpRecreateTask(ChatPage.this, upIntent)) {
				//TaskStackBuilder.create(ChatPage.this).addNextIntentWithParentStack(upIntent).startActivities();				
				startActivity(upIntent);
			}else {
				upIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				NavUtils.navigateUpTo(ChatPage.this, upIntent);
			}*/
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}
	
	private void updateUI(){
		Cursor cursor = getContentResolver().query(ChatDAO.CONTENT_URI, null, null, 
				new String[]{userId, guestId, guestId, userId, currentLimit+"", DEFAULT_OFFSET+""}, null);
		adapter.swapCursor(cursor);
		adapter.notifyDataSetChanged();		
	}

	public String getGuestId() {
		return guestId;
	}

	public String getUserName() {
		return userName;
	}
	
}
