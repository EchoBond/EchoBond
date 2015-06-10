package com.echobond.activity;

import com.echobond.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v4.app.TaskStackBuilder;
import android.view.KeyEvent;

public class ChatPage extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_chat_page);
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
