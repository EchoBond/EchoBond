package com.echobond.activity;

import com.echobond.R;

import android.app.ActionBar;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;
/**
 * 
 * @author aohuijun
 *
 */
public class ViewMorePage extends ActionBarActivity {

	private TextView titleView;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_view_more_page);
		initToolBar();
		
	}

	private void initToolBar() {
		Toolbar viewMoreToolbar = (Toolbar)findViewById(R.id.toolbar_view_more);
		setSupportActionBar(viewMoreToolbar);
		getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
		getSupportActionBar().setCustomView(R.layout.title_bar_setting);
		
		titleView = (TextView)findViewById(R.id.title_name);
		titleView.setText(getIntent().getStringExtra("title"));
	}
}
