package com.echobond.activity;

import com.echobond.R;

import android.app.ActionBar;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v4.app.TaskStackBuilder;
import android.support.v4.widget.CursorAdapter;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
/**
 * 
 * @author aohuijun
 *
 */
public class ChatPage extends ActionBarActivity {
	
	private ListView chatListView;
	private EditText msgInputText;
	private ImageView msgSendView;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_chat_page);
		initActionBar();
		initSendingMsg();
		
		ChatListAdapter adapter = new ChatListAdapter(this, R.layout.item_chat, null, 0);
		chatListView = (ListView)findViewById(R.id.chat_list);
		chatListView.setOverScrollMode(View.OVER_SCROLL_NEVER);
		chatListView.setAdapter(adapter);
		
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
					Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
					msgInputText.setText(null);
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

		@Override
		public void bindView(View convertView, Context ctx, Cursor c) {
			ImageView hostImageView = (ImageView)convertView.findViewById(R.id.item_chat_hostpic);
			ImageView guestImageView = (ImageView)convertView.findViewById(R.id.item_chat_guestpic);
			TextView chatContentView = (TextView)convertView.findViewById(R.id.item_chat_content);
			TextView chatTimeView = (TextView)convertView.findViewById(R.id.item_chat_time);
			
			
		}

		@Override
		public View newView(Context context, Cursor cursor, ViewGroup parent) {
			View view = inflater.inflate(layout, parent, false);
			return view;
		}
		
	}
	
	public class testAdapter extends ArrayAdapter<Object> {

		public testAdapter(Context context, int resource) {
			super(context, resource);
			// TODO Auto-generated constructor stub
		}
		
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
