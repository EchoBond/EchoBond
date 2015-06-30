package com.echobond.activity;

import org.json.JSONObject;

import com.echobond.R;
import com.echobond.fragment.MoreGroupsFragment;
import com.echobond.fragment.MoreTagsFragment;
import com.echobond.intf.ViewMoreSwitchCallback;

import android.app.ActionBar;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
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
public class ViewMorePage extends ActionBarActivity implements ViewMoreSwitchCallback {

	private TextView titleView;
	private ImageView backButton;
	private MoreGroupsFragment moreGroupsFragment;
	private MoreTagsFragment moreTagsFragment;
	
	private String title;
	private boolean mode;
	private Integer profile;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_view_more_page);
		getData();
		initToolBar();
		initContent();
	}

	private void getData() {
		title = getIntent().getStringExtra("title");
		mode = getIntent().getBooleanExtra("mode", false);
		profile = getIntent().getIntExtra("profile", 0);
	}

	private void initToolBar() {
		Toolbar viewMoreToolbar = (Toolbar)findViewById(R.id.toolbar_view_more);
		setSupportActionBar(viewMoreToolbar);
		getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
		getSupportActionBar().setCustomView(R.layout.title_bar_setting);
		
		backButton = (ImageView)findViewById(R.id.button_left_side);
		backButton.setImageDrawable(getResources().getDrawable(R.drawable.button_back));
		backButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				finish();
			}
		});
		
		titleView = (TextView)findViewById(R.id.title_name);
		titleView.setText("More " + title);
	}

	private void initContent() {
		FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
		if (moreGroupsFragment == null || moreTagsFragment == null) {
			moreGroupsFragment = new MoreGroupsFragment();
			moreTagsFragment = new MoreTagsFragment();
		}
		Bundle bundle = new Bundle();
		bundle.putBoolean("mode", mode);
		bundle.putInt("profile", profile);
		if (title.equals("Groups")) {
			moreGroupsFragment.setArguments(bundle);
			transaction.add(R.id.view_more_container, moreGroupsFragment).show(moreGroupsFragment).commit();
		}
		if (title.equals("Hashtags")) {
			moreTagsFragment.setArguments(bundle);
			transaction.add(R.id.view_more_container, moreTagsFragment).show(moreTagsFragment).commit();
		}
	}

	@Override
	public void onTypeSelected(int type) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onSearchSelected(JSONObject jso) {
		// TODO Auto-generated method stub
		
	}
}
