package com.echobond.activity;

import com.echobond.R;
import com.echobond.dao.UserDAO;
import com.echobond.fragment.ProfileFragment;

import android.app.ActionBar;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;
/**
 * 
 * @author aohuijun
 *
 */
public class PeoplePage extends ActionBarActivity {

	private TextView titleView;
	private ProfileFragment profileFragment;
	private String userId, userName;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_people_page);
		initActionBar();
		initContent();
	}

	private void initActionBar() {
		Toolbar peopleToolbar = (Toolbar)findViewById(R.id.toolbar_people);
		setSupportActionBar(peopleToolbar);
		getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
		getSupportActionBar().setCustomView(R.layout.title_bar_setting);
		
		titleView = (TextView)findViewById(R.id.title_name);
		userId = getIntent().getStringExtra("userId");
		userName = "Somebody";
		Cursor uCursor = getContentResolver().query(UserDAO.CONTENT_URI_USER, null, null, new String[]{userId}, null);
		if(null != uCursor){
			if(uCursor.moveToNext()){
				userName = uCursor.getString(uCursor.getColumnIndex("username"));
			}
			uCursor.close();
		}
		titleView.setText(userName + "'s Profile");
	}
	
	private void initContent() {
		FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
		if (null == profileFragment) {
			profileFragment = new ProfileFragment();
		}
		Bundle bundle = new Bundle();
		bundle.putString("userId", userId);
		bundle.putString("userName", userName);
		profileFragment.setArguments(bundle);
		transaction.add(R.id.people_page_content, profileFragment).show(profileFragment).commit();
	}
	
}
