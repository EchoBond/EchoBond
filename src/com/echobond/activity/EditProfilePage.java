package com.echobond.activity;

import com.echobond.R;

import android.app.ActionBar;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;
/**
 * 
 * @author aohuijun
 *
 */
public class EditProfilePage extends ActionBarActivity {

	private TextView titleView;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_edit_profile_page);
		initTitleBar();
		
	}

	private void initTitleBar() {
		Toolbar edittingToolbar = (Toolbar)findViewById(R.id.toolbar_edit_profile);
		setSupportActionBar(edittingToolbar);
		getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
		getSupportActionBar().setCustomView(R.layout.title_bar_setting);
		
		Typeface tf = Typeface.createFromAsset(getAssets(), "fonts/Skia.ttf");
		titleView = (TextView)findViewById(R.id.title_name);
		titleView.setText("Edit Profile");
		titleView.setTypeface(tf);

	}
}
