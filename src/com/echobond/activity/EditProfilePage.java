package com.echobond.activity;

import org.json.JSONObject;

import com.echobond.R;
import com.echobond.application.MyApp;
import com.echobond.connector.ImageUploadAsyncTask;
import com.echobond.connector.UpdateUserProfileService;
import com.echobond.entity.User;
import com.echobond.fragment.CanvasFragment;
import com.echobond.fragment.PosterAvatarFragment;
import com.echobond.fragment.EditProfileFragment;
import com.echobond.fragment.PresetAvatarFragment;
import com.echobond.fragment.ResetPasswordDialogFragment;
import com.echobond.fragment.SelectAvatarDialogFragment;
import com.echobond.intf.EditProfileSwitchCallback;
import com.echobond.intf.ImageCallback;
import com.echobond.util.HTTPUtil;
import com.echobond.util.ImageUtil;
import com.echobond.util.SPUtil;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.NavUtils;
import android.support.v4.app.TaskStackBuilder;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.text.format.Time;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
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
public class EditProfilePage extends ActionBarActivity implements EditProfileSwitchCallback, ImageCallback {

	public ResetPasswordDialogFragment resetPasswordDialogFragment;
	private SelectAvatarDialogFragment selectAvatarDialogFragment;
	private PresetAvatarFragment presetAvatarFragment;
	private EditProfileFragment mainFragment;
	private PosterAvatarFragment picFragment;
	private CanvasFragment canvasFragment;
	
	private ImageView backButton, doneButton;
	private TextView titleView;
	
	private ProgressBar progressBar;
	private Bitmap avatar;
	
	private User user;
	private String[] selfTags, likedTags, followedGroups;
	
	private int pgIndex = 0;
	private int avatarType = PAGE_PROFILE;
	
	public static final int PAGE_PROFILE = 0;
	public static final int PAGE_PRESET = 1;
	public static final int PAGE_POSTER = 2;
	public static final int PAGE_CANVAS = 3;
	
	private BroadcastReceiver receiver = new BroadcastReceiver() {
		
		@Override
		public void onReceive(Context context, Intent intent) {
			progressBar.setVisibility(View.INVISIBLE);
			if(intent.hasExtra(MyApp.INTENT_MSG_DUP_USERNAME)){
				Toast.makeText(getApplicationContext(), "duplicate user name", Toast.LENGTH_SHORT).show();
				return;
			} else if(intent.hasExtra(MyApp.INTENT_MSG_UPLOAD_IMG)){
				
			} else {
				closeEditorActivity();
				Toast.makeText(getApplicationContext(), getString(R.string.hint_edit_profile_saved), Toast.LENGTH_SHORT).show();
			}
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
				if (canvasFragment.isSelected()) {
					canvasFragment.setMoreFunctions(true);
				} else if (pgIndex == PAGE_PROFILE) {
					closeEditorActivity();
				} else if (pgIndex == PAGE_PRESET) {
					initContent();
				} else if (pgIndex == PAGE_POSTER) {
					initContent();
				} else if (pgIndex == PAGE_CANVAS) {
					initContent();
				}
			}
		});
		
		doneButton = (ImageView)findViewById(R.id.button_right_side);
		doneButton.setImageDrawable(getResources().getDrawable(R.drawable.button_done));
		doneButton.setOnClickListener(new View.OnClickListener() {
			
			@SuppressLint("NewApi") 
			@Override
			public void onClick(View v) {
				progressBar.bringToFront();
				progressBar.setVisibility(View.VISIBLE);
				if (pgIndex == PAGE_PROFILE) {					
					user = mainFragment.getUser();
					
					if(null == user.getUserName() || user.getUserName().trim().equals("")){
						Toast.makeText(EditProfilePage.this, getResources().getString(R.string.hint_empty_username), 
								Toast.LENGTH_SHORT).show();
						progressBar.setVisibility(View.INVISIBLE);
						return;
					}
					
					selfTags = mainFragment.getSelfTags();
					
					if(null != selfTags && selfTags.length > MyApp.MAX_SELF_TAG){
						Toast.makeText(EditProfilePage.this, getResources().getString(R.string.hint_edit_profile_many_tags), 
								Toast.LENGTH_SHORT).show();
						progressBar.setVisibility(View.INVISIBLE);
						return;
					}
					
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
					if (pgIndex == PAGE_PRESET) {
						initContent();
						avatarType = PAGE_PRESET;
						avatar = BitmapFactory.decodeResource(getResources(), presetAvatarFragment.getSelectedAvatar());
						
					} else if (pgIndex == PAGE_POSTER) {
						initContent();
						avatarType = PAGE_POSTER;
						/* GENERATE AVATAR */
						EditText avatarText = picFragment.getAvatarText();
						avatarText.setBackground(null);
						avatarText.setHint("");
						RelativeLayout avatarLayout = picFragment.getAvatarLayout();
						avatar = ImageUtil.generateBitmap(avatarLayout);
						
					} else if (pgIndex == PAGE_CANVAS) {
						initContent();
						avatarType = PAGE_CANVAS;
						/* GENERATE CANVAS */
						ImageView canvasView = canvasFragment.getDrawBoard();
						avatar = ImageUtil.generateBitmap(canvasView);
												
					}
					Time time = new Time();
					time.setToNow();
					ImageUtil.saveBitmap(avatar, avatarType + "_avatar_" + time.year + time.month + time.monthDay + time.hour + time.minute + time.second);
					String userId = (String) SPUtil.get(EditProfilePage.this, MyApp.PREF_TYPE_LOGIN, MyApp.LOGIN_ID, null, String.class);
					String email = (String) SPUtil.get(EditProfilePage.this, MyApp.PREF_TYPE_LOGIN, MyApp.LOGIN_EMAIL, null, String.class);
					new ImageUploadAsyncTask().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, 
							HTTPUtil.getInstance().composePreURL(EditProfilePage.this) + getResources().getString(R.string.url_up_img), 
							ImageUtil.bmToStr(avatar), EditProfilePage.this, ImageUploadAsyncTask.IMAGE_TYPE_USER, userId, email);
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
		if (null == mainFragment || null == presetAvatarFragment || null == picFragment || null == canvasFragment) {
			mainFragment = new EditProfileFragment();
			presetAvatarFragment = new PresetAvatarFragment();
			picFragment = new PosterAvatarFragment();
			canvasFragment = new CanvasFragment();
			transaction.add(R.id.edit_profile_content, mainFragment);
			transaction.add(R.id.edit_profile_content, presetAvatarFragment);
			transaction.add(R.id.edit_profile_content, picFragment);
			transaction.add(R.id.edit_profile_content, canvasFragment);
		}
		transaction.show(mainFragment).hide(presetAvatarFragment).hide(picFragment).hide(canvasFragment).commit();
		pgIndex = PAGE_PROFILE;
		titleView.setText(getResources().getString(R.string.edit_profile_activity_title));
	}
	
	@Override
	public void resetPassword() {
		resetPasswordDialogFragment = new ResetPasswordDialogFragment();
		resetPasswordDialogFragment.show(getSupportFragmentManager(), "reset_password");
	}
	
	@Override
	public void setAvatarSelection() {
		Bundle bundle = new Bundle();
		bundle.putString("title", getString(R.string.edit_profile_set_avatar));
		selectAvatarDialogFragment = new SelectAvatarDialogFragment();
		selectAvatarDialogFragment.setArguments(bundle);
		selectAvatarDialogFragment.show(getSupportFragmentManager(), "select_avatar_type");
	}
	
	@Override
	public void setAvatarType(int index) {
		this.pgIndex = index;
		FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
		switch (pgIndex) {
		case PAGE_PRESET:
			transaction.show(presetAvatarFragment).hide(mainFragment).commit();
			break;
		case PAGE_POSTER:
			transaction.show(picFragment).hide(mainFragment).commit();
			break;
		case PAGE_CANVAS:
			transaction.show(canvasFragment).hide(mainFragment).commit();
			break;
		default:
			break;
		}
		titleView.setText(getResources().getString(R.string.edit_profile_set_avatar));
	}
	
	private void closeEditorActivity() {
		Intent upIntent = NavUtils.getParentActivityIntent(EditProfilePage.this);
		if (NavUtils.shouldUpRecreateTask(EditProfilePage.this, upIntent)) {
			TaskStackBuilder.create(EditProfilePage.this).addNextIntentWithParentStack(upIntent).startActivities();
		} else {
			upIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			NavUtils.navigateUpTo(EditProfilePage.this, upIntent);
		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
			if (canvasFragment.isSelected()) {
				canvasFragment.setMoreFunctions(true);
			} else if (pgIndex == PAGE_PROFILE) {
				finish();
				return true;
			} else if (pgIndex == PAGE_PRESET) {
				initContent();
			} else if (pgIndex == PAGE_POSTER) {
				initContent();
			} else if (pgIndex == PAGE_CANVAS) {
				initContent();
			}
		}
		return true;
	}

	@Override
	public void onUploadImage(JSONObject result) {
		if(null != result){
			Toast.makeText(this, getResources().getString(R.string.hint_edit_profile_saved), Toast.LENGTH_SHORT).show();
		} else {
			Toast.makeText(this, getResources().getString(R.string.hint_network_issue), Toast.LENGTH_SHORT).show();
		}
		getSupportFragmentManager().beginTransaction().hide(canvasFragment).show(mainFragment).commit();
		progressBar.setVisibility(View.INVISIBLE);
		Intent intent = new Intent(MyApp.BROADCAST_UPDATE_PROFILE);
		intent.putExtra(MyApp.INTENT_MSG_UPLOAD_IMG, true);
		LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
	}

	@Override
	public void onDownloadImage(byte[] result) {
		
	}

}
