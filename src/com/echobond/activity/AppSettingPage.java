package com.echobond.activity;

import com.echobond.R;
import com.echobond.application.MyApp;
import com.echobond.fragment.NotificationDialogFragment;
import com.echobond.intf.NotificationCallback;
import com.echobond.util.SPUtil;

import android.app.ActionBar;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v4.app.TaskStackBuilder;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
/**
 * 
 * @author aohuijun
 *
 */
public class AppSettingPage extends ActionBarActivity implements NotificationCallback {
	
	private ImageView backButton;
	private TextView titleView;
	private String[] appSettingTitles;
	private ListView appSettingListView;
	private ArrayAdapter<String> appSettingArrayAdapter;
	
	private NotificationDialogFragment notificationDialog;
	Intent intent = new Intent();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_app_setting_page);
		initTitleBar();
		initSettingList();
	}
	
	private void initTitleBar() {
		Toolbar settingToolbar = (Toolbar)findViewById(R.id.toolbar_setting);
		setSupportActionBar(settingToolbar);
		getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
		getSupportActionBar().setCustomView(R.layout.title_bar_setting);
		
		backButton = (ImageView)findViewById(R.id.button_left_side);
		backButton.setImageDrawable(getResources().getDrawable(R.drawable.button_back));
		backButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent upIntent = NavUtils.getParentActivityIntent(AppSettingPage.this);
				if (NavUtils.shouldUpRecreateTask(AppSettingPage.this, upIntent)) {
					TaskStackBuilder.create(AppSettingPage.this).addNextIntentWithParentStack(upIntent).startActivities();
				}else {
					upIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
					NavUtils.navigateUpTo(AppSettingPage.this, upIntent);
				}
			}
		});
		
		Typeface tf = Typeface.createFromAsset(getAssets(), "fonts/Skia.ttf");
		titleView = (TextView)findViewById(R.id.title_name);
		titleView.setText("App Setting");
		titleView.setTypeface(tf);
	}

	private void initSettingList() {
		appSettingTitles = getResources().getStringArray(R.array.app_setting_array);
		appSettingArrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, appSettingTitles); 
		appSettingListView = (ListView)findViewById(R.id.list_app_setting);
		appSettingListView.setAdapter(appSettingArrayAdapter);
		appSettingListView.setOverScrollMode(View.OVER_SCROLL_NEVER);
		appSettingListView.setOnItemClickListener(new SettingItemClickListener());
		
	}

	public class SettingItemClickListener implements AdapterView.OnItemClickListener {

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			switch (position) {
			case 0:	//	notification type
				Bundle bundle = new Bundle();
				bundle.putString("title", appSettingTitles[0]);
				notificationDialog = new NotificationDialogFragment();
				notificationDialog.setArguments(bundle);
				notificationDialog.show(getSupportFragmentManager(), "ntc_setting");
				break;
			case 1:
				Toast.makeText(getApplicationContext(), getString(R.string.hint_under_development), Toast.LENGTH_SHORT).show();
				break;
			case 2:	//	app version
				Toast.makeText(getApplicationContext(), getString(R.string.hint_current_version) + "0.1.0", Toast.LENGTH_SHORT).show();
				break;
			case 3:	//	logout
				SPUtil.clear(AppSettingPage.this, MyApp.PREF_TYPE_LOGIN);
				intent.setClass(AppSettingPage.this, StartPage.class);
				//clear activity stack
				intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
				intent.putExtra("logout", true);
				startActivity(intent);
				finish();
				break;

			default:
				break;
			}
		}
		
	}

	@Override
	public void onNtcTypeSelected() {
		// TODO Auto-generated method stub
		
	}
}
