package com.echobond.activity;

import com.echobond.R;

import android.app.ActionBar;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v4.app.TaskStackBuilder;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
/**
 * 
 * @author aohuijun
 *
 */
public class ServicePage extends ActionBarActivity {

	private ImageView backButton;
	private TextView titleView;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_service_page);
		initToolBar();
		
	}

	private void initToolBar() {
		Toolbar serviceToolbar = (Toolbar)findViewById(R.id.toolbar_service);
		setSupportActionBar(serviceToolbar);
		getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
		getSupportActionBar().setCustomView(R.layout.title_bar_setting);
		
		backButton = (ImageView)findViewById(R.id.button_left_side);
		backButton.setImageDrawable(getResources().getDrawable(R.drawable.button_back));
		backButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent upIntent = NavUtils.getParentActivityIntent(ServicePage.this);
				if (NavUtils.shouldUpRecreateTask(ServicePage.this, upIntent)) {
					TaskStackBuilder.create(ServicePage.this).addNextIntentWithParentStack(upIntent).startActivities();
				}else {
					upIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
					NavUtils.navigateUpTo(ServicePage.this, upIntent);
				}
			}
		});
		
		int page = getIntent().getIntExtra("page", -1);
		titleView = (TextView)findViewById(R.id.title_name);
		titleView.setText(getResources().getStringArray(R.array.setting_list_array)[page]);
		
	}
}
