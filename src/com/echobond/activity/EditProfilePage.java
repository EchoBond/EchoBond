package com.echobond.activity;

import com.echobond.R;
import com.echobond.connector.UpdateUserProfileService;
import com.echobond.entity.User;
import com.echobond.fragment.DrawingIconFragment;
import com.echobond.fragment.EditProfileFragment;
import com.echobond.intf.EditProfileSwitchCallback;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.NavUtils;
import android.support.v4.app.TaskStackBuilder;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
/**
 * 
 * @author aohuijun
 *
 */
public class EditProfilePage extends ActionBarActivity implements EditProfileSwitchCallback{

	private ImageView backButton, doneButton;
	private TextView titleView;
	private EditProfileFragment mainFragment;
	private DrawingIconFragment picFragment;
	
	private ProgressBar progressBar;
	
	private User user;
	private String[] selfTags, likedTags, followedGroups;
	
	private boolean isDrawing = false;
	
	private BroadcastReceiver receiver = new BroadcastReceiver() {
		
		@SuppressLint("NewApi")
		@Override
		public void onReceive(Context context, Intent intent) {
			progressBar.setVisibility(View.INVISIBLE);
			if(intent.hasExtra("duplicateUserName")){
				Toast.makeText(getApplicationContext(), "duplicate user name", Toast.LENGTH_SHORT).show();
				return;
			}
			/*
			EditText avatarText = picFragment.getAvatarText();
			avatarText.setBackground(null);
			RelativeLayout avatarLayout = picFragment.getAvatarLayout();
			Bitmap avatar = ImageUtil.generateBitmap(avatarLayout);
			Time time = new Time();
			time.setToNow();*/
//			ImageUtil.saveBitmap(avatar, "avatar_" + time.year + time.month + time.monthDay + time.hour + time.minute + time.second);
			closeEditorActivity();
			Toast.makeText(getApplicationContext(), getString(R.string.hint_edit_profile_saved), Toast.LENGTH_SHORT).show();
		}
	};
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_edit_profile_page);
		initTitleBar();
		initContent();
		LocalBroadcastManager.getInstance(this).registerReceiver(receiver, new IntentFilter("updateUserProfile"));
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		LocalBroadcastManager.getInstance(this).unregisterReceiver(receiver);
	}

	private void initTitleBar() {
		Toolbar edittingToolbar = (Toolbar)findViewById(R.id.toolbar_edit_profile);
		setSupportActionBar(edittingToolbar);
		getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
		getSupportActionBar().setCustomView(R.layout.title_bar_setting);
		
		progressBar = (ProgressBar) findViewById(R.id.edit_profile_progress);
		
		backButton = (ImageView)findViewById(R.id.button_left_side);
		backButton.setImageDrawable(getResources().getDrawable(R.drawable.button_back));
		backButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if (!isDrawing) {
					closeEditorActivity();
				} else {
					isDrawing = false;
					initContent();
					titleView.setText(getResources().getString(R.string.edit_profile_activity_title));
				}
			}
		});
		
		doneButton = (ImageView)findViewById(R.id.button_right_side);
		doneButton.setImageDrawable(getResources().getDrawable(R.drawable.button_done));
		doneButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if (!isDrawing) {
					progressBar.bringToFront();
					progressBar.setVisibility(View.VISIBLE);
					user = mainFragment.getUser();
					selfTags = mainFragment.getSelfTags();
					likedTags = mainFragment.getLikedTags();
					followedGroups = mainFragment.getFollowedGroups();
					Intent intent = new Intent();
					intent.putExtra("user", user);
					intent.putExtra("selfTags", selfTags);
					intent.putExtra("likedTags", likedTags);
					intent.putExtra("followedGroups", followedGroups);
					intent.setClass(EditProfilePage.this, UpdateUserProfileService.class);
					startService(intent);					
				} else {
					isDrawing = false;
					initContent();
					titleView.setText(getResources().getString(R.string.edit_profile_activity_title));
				}
			}
		});
		
		Typeface tf = Typeface.createFromAsset(getAssets(), "fonts/Skia.ttf");
		titleView = (TextView)findViewById(R.id.title_name);
		titleView.setText(getResources().getString(R.string.edit_profile_activity_title));
		titleView.setTypeface(tf);

	}
	
	private void initContent() {
		FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
		if (null == mainFragment || null == picFragment) {
			mainFragment = new EditProfileFragment();
			picFragment = new DrawingIconFragment();
			transaction.add(R.id.edit_profile_content, mainFragment);
			transaction.add(R.id.edit_profile_content, picFragment);
		}
		transaction.show(mainFragment).hide(picFragment).commit();
	}
	
	@Override
	public void setPoster(boolean isDrawing) {
		this.isDrawing = isDrawing;
		FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
		transaction.show(picFragment).hide(mainFragment).commit();
		titleView.setText(getResources().getString(R.string.edit_profile_set_avatar));
	}
	
	private void closeEditorActivity() {
		Intent upIntent = NavUtils.getParentActivityIntent(EditProfilePage.this);
		if (NavUtils.shouldUpRecreateTask(EditProfilePage.this, upIntent)) {
			TaskStackBuilder.create(EditProfilePage.this).addNextIntentWithParentStack(upIntent).startActivities();
		}else {
			upIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			NavUtils.navigateUpTo(EditProfilePage.this, upIntent);
		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
			if (!isDrawing) {
				finish();
				return true;
			} else {
				isDrawing = false;
				initContent();
				titleView.setText(getResources().getString(R.string.edit_profile_activity_title));
			}
		}
		return true;
	}

}
