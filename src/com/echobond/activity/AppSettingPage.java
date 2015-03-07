package com.echobond.activity;

import com.echobond.R;

import android.app.ActionBar;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class AppSettingPage extends ActionBarActivity {
	
	private ImageView backButton;
	private TextView titleView;
	private String[] appSettingTitles;
	private ListView appSettingListView;
	private ArrayAdapter<String> appSettingArrayAdapter;
	Intent intent = new Intent();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_app_setting_page);
		initTitleBar();
		initSettingList();
		
	}
	
	private void initTitleBar() {
		Toolbar searchToolbar = (Toolbar)findViewById(R.id.setting_toolbar);
		setSupportActionBar(searchToolbar);
		getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
		getSupportActionBar().setCustomView(R.layout.title_bar_setting);
		
		backButton = (ImageView)findViewById(R.id.leftBtn);
		backButton.setImageResource(R.drawable.back_button);
		
		titleView = (TextView)findViewById(R.id.titleName);
		titleView.setText("App Setting");
	}

	private void initSettingList() {
		appSettingTitles = getResources().getStringArray(R.array.app_setting_array);
		appSettingArrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, appSettingTitles);	//Need to be fixed. 
		appSettingListView = (ListView)findViewById(R.id.appSettingList);
		appSettingListView.setAdapter(appSettingArrayAdapter);
		appSettingListView.setOnItemClickListener(new SettingItemClickListener());
		
	}

	public class SettingItemClickListener implements AdapterView.OnItemClickListener {

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			switch (position) {
			case 0:
				Toast.makeText(getApplicationContext(), appSettingTitles[0], Toast.LENGTH_SHORT).show();
				break;
			case 1:
				Toast.makeText(getApplicationContext(), appSettingTitles[1], Toast.LENGTH_SHORT).show();
				break;
			case 2:
				Toast.makeText(getApplicationContext(), appSettingTitles[2], Toast.LENGTH_SHORT).show();
				break;
			case 3:
				Toast.makeText(getApplicationContext(), appSettingTitles[3], Toast.LENGTH_SHORT).show();
				break;
			case 4:
				Toast.makeText(getApplicationContext(), appSettingTitles[4], Toast.LENGTH_SHORT).show();
				break;
			case 5:
				intent.setClass(AppSettingPage.this, StartPage.class);
				//clear activity stack
				intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
				startActivity(intent);
				break;
			case 6:
				Toast.makeText(getApplicationContext(), appSettingTitles[6], Toast.LENGTH_SHORT).show();
				break;
			case 7:
				Toast.makeText(getApplicationContext(), appSettingTitles[7], Toast.LENGTH_SHORT).show();
				break;
			default:
				break;
			}
		}
		
	}
}
