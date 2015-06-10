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
import android.view.View;
import android.view.ViewGroup;

public class ChatPage extends ActionBarActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_chat_page);
		initActionBar();
	}
	
	private void initActionBar() {
		Toolbar chatToolbar = (Toolbar)findViewById(R.id.toolbar_chat_page);
		setSupportActionBar(chatToolbar);
		getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
		getSupportActionBar().setCustomView(R.layout.title_bar_setting);
	}

	public class ChatListAdapter extends CursorAdapter {

		public ChatListAdapter(Context context, Cursor c, int flags) {
			super(context, c, flags);
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
