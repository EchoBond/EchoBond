package com.echobond.activity;

import com.echobond.R;

import android.app.ActionBar;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v4.app.TaskStackBuilder;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
/**
 * 
 * @author aohuijun
 *
 */
public class EditProfilePage extends ActionBarActivity {

	private ImageView backButton, doneButton;
	private TextView titleView, moreTagsView, moreGroupsView;
	private EditText userName, userGender, userBio, userAge, userOrigin, 
					userDNA, userTrophy, userTodo, userPhilo, userDesc, userInterest, userSec, userLang, 
					userTags, userGroups;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_edit_profile_page);
		initTitleBar();
		initInput();
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
				closeEditorActivity();
			}
		});
		
		doneButton = (ImageView)findViewById(R.id.button_right_side);
		doneButton.setImageDrawable(getResources().getDrawable(R.drawable.button_done));
		doneButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Submit the edit behaviors
				closeEditorActivity();
				Toast.makeText(getApplicationContext(), getString(R.string.hint_edit_profile_saved), Toast.LENGTH_SHORT).show();
			}
		});
		
		Typeface tf = Typeface.createFromAsset(getAssets(), "fonts/Skia.ttf");
		titleView = (TextView)findViewById(R.id.title_name);
		titleView.setText("Edit Profile");
		titleView.setTypeface(tf);

	}
	
	private void initInput() {
		userName = (EditText)findViewById(R.id.edit_profile_title_text);
		userGender = (EditText)findViewById(R.id.edit_profile_gender_text);
		userBio = (EditText)findViewById(R.id.edit_profile_bio_text);
		userAge = (EditText)findViewById(R.id.edit_profile_age_text);
		userOrigin = (EditText)findViewById(R.id.edit_profile_earth_text);
		
		userDNA = (EditText)findViewById(R.id.edit_profile_dna_text);
		userTrophy = (EditText)findViewById(R.id.edit_profile_trophy_text);
		userTodo = (EditText)findViewById(R.id.edit_profile_todo_text);
		userPhilo = (EditText)findViewById(R.id.edit_profile_philo_text);
		userDesc = (EditText)findViewById(R.id.edit_profile_desc_text);
		userInterest = (EditText)findViewById(R.id.edit_profile_heart_text);
		userSec = (EditText)findViewById(R.id.edit_profile_sec_text);
		userLang = (EditText)findViewById(R.id.edit_profile_lang_text);
		
		userTags = (EditText)findViewById(R.id.edit_profile_tag_text);
		userGroups = (EditText)findViewById(R.id.edit_profile_group_text);
		
		moreTagsView = (TextView)findViewById(R.id.edit_profile_tag_more);
		moreGroupsView = (TextView)findViewById(R.id.edit_profile_group_more);
		moreTagsView.setOnClickListener(new ViewMoreClickListener(NewPostPage.TAG));
		moreGroupsView.setOnClickListener(new ViewMoreClickListener(NewPostPage.GROUP));
		
	}
	
	public class ViewMoreClickListener implements OnClickListener {

		private String typeString;
		
		public ViewMoreClickListener(String tag) {
			this.typeString = tag;
		}

		@Override
		public void onClick(View v) {
			Intent intent = new Intent();
			intent.putExtra("title", typeString);
			intent.setClass(EditProfilePage.this, ViewMorePage.class);
			startActivity(intent);
		}
		
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
}
