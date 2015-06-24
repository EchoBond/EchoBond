package com.echobond.activity;

import com.echobond.R;
import com.echobond.fragment.DrawingIconFragment;
import com.echobond.fragment.EditProfileFragment;
import com.echobond.intf.EditProfileSwitchCallback;
import com.echobond.util.ImageUtil;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.NavUtils;
import android.support.v4.app.TaskStackBuilder;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.text.format.Time;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
/**
 * 
 * @author aohuijun
 *
 */
public class EditProfilePage extends ActionBarActivity implements EditProfileSwitchCallback {

	private ImageView backButton, doneButton;
	private TextView titleView;
	private EditProfileFragment mainFragment;
	private DrawingIconFragment picFragment;
	
	private boolean isDrawing = false;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_edit_profile_page);
		initTitleBar();
		initContent();
	}

	private void initTitleBar() {
		Toolbar edittingToolbar = (Toolbar)findViewById(R.id.toolbar_edit_profile);
		setSupportActionBar(edittingToolbar);
		getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
		getSupportActionBar().setCustomView(R.layout.title_bar_setting);
		
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
			
			@SuppressLint("NewApi") 
			@Override
			public void onClick(View v) {
				if (!isDrawing) {
					// TODO Submit the edit behaviors
					EditText avatarText = picFragment.getAvatarText();
					avatarText.setBackground(null);
					RelativeLayout avatarLayout = picFragment.getAvatarLayout();
					Bitmap post = ImageUtil.generateBitmap(avatarLayout);
					Time time = new Time();
					time.setToNow();
					ImageUtil.saveBitmap(post, "avatar_" + time.year + time.month + time.monthDay + time.hour + time.minute + time.second);
					closeEditorActivity();
					Toast.makeText(getApplicationContext(), getString(R.string.hint_edit_profile_saved), Toast.LENGTH_SHORT).show();
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
