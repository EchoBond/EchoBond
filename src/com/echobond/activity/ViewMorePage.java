package com.echobond.activity;

import org.json.JSONObject;

import com.echobond.R;
import com.echobond.application.MyApp;
import com.echobond.fragment.MoreGroupsFragment;
import com.echobond.fragment.MoreTagsFragment;
import com.echobond.intf.ViewMoreSwitchCallback;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
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
	
	private String title;
	private int mode;
	
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
				Intent intent = new Intent();
				if(title.equals(MyApp.VIEW_MORE_TAG)){
					intent.putIntegerArrayListExtra("idList", moreTagsFragment.getTagIds());
					intent.putStringArrayListExtra("nameList", moreTagsFragment.getTagNames());
				} else if(title.equals(MyApp.VIEW_MORE_GROUP)){
					intent.putIntegerArrayListExtra("idList", moreGroupsFragment.getGroupIds());
					intent.putStringArrayListExtra("nameList", moreGroupsFragment.getGroupNames());					
				}
				setResult(Activity.RESULT_OK, intent);
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
		bundle.putInt("mode", mode);
		if (title.equals(MyApp.VIEW_MORE_GROUP)) {
			moreGroupsFragment.setArguments(bundle);
			transaction.add(R.id.view_more_container, moreGroupsFragment).show(moreGroupsFragment).commit();
		} else if (title.equals(MyApp.VIEW_MORE_TAG)) {
			moreTagsFragment.setArguments(bundle);
			transaction.add(R.id.view_more_container, moreTagsFragment).show(moreTagsFragment).commit();
		}
	}

	@Override
	public void onTypeSelected(int type) {
		
	}

	@Override
	public void onSearchSelected(JSONObject jso) {
		
	}
}
