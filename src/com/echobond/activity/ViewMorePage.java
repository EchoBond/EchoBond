package com.echobond.activity;

import org.json.JSONObject;

import com.echobond.R;
import com.echobond.fragment.MoreGroupsFragment;
import com.echobond.fragment.MoreTagsFragment;
import com.echobond.fragment.ProfileGroupsFragment;
import com.echobond.fragment.ProfileTagsFragment;
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
	private ImageView backButton, doneButton;
	private MoreGroupsFragment moreGroupsFragment;
	private MoreTagsFragment moreTagsFragment;
	private ProfileGroupsFragment profileGroupsFragment;
	private ProfileTagsFragment profileTagsFragment;
	
	private String title;
	private int mode;
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
		mode = getIntent().getIntExtra("mode", -1);
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
		doneButton = (ImageView)findViewById(R.id.button_right_side);
		doneButton.setImageDrawable(getResources().getDrawable(R.drawable.button_done));
		doneButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
			}
		});
		titleView = (TextView)findViewById(R.id.title_name);
		titleView.setText("More " + title);
	}

	private void initContent() {
		FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
		if (moreGroupsFragment == null || moreTagsFragment == null || profileGroupsFragment == null || profileTagsFragment == null) {
			moreGroupsFragment = new MoreGroupsFragment();
			moreTagsFragment = new MoreTagsFragment();
			profileGroupsFragment = new ProfileGroupsFragment();
			profileTagsFragment = new ProfileTagsFragment();
		}
		Bundle bundle = new Bundle();
		bundle.putInt("mode", mode);
		bundle.putInt("profile", profile);
		if (title.equals(NewPostPage.GROUP)) {
			moreGroupsFragment.setArguments(bundle);
			transaction.add(R.id.view_more_container, moreGroupsFragment).show(moreGroupsFragment).commit();
		} else if (title.equals(NewPostPage.TAG)) {
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
