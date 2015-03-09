package com.echobond.activity;

import com.echobond.R;
import com.echobond.fragment.NewCategoryFragment;
import com.echobond.fragment.NewCategoryFragment.CategoryInterface;
import com.echobond.fragment.NewContentsFragment;
import com.echobond.fragment.NewContentsFragment.ContentsInterface;

import android.app.ActionBar;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.NavUtils;
import android.support.v4.app.TaskStackBuilder;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


public class NewPostPage extends ActionBarActivity implements CategoryInterface, ContentsInterface {
	
	public static final int NEW_POST_CATEGORY = 0;
	public static final int NEW_POST_DRAW = 1;
	public static final int NEW_POST_WRITE = 2;
	public static final int NEW_POST_GROUP = 3;
	
	private FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
	private NewCategoryFragment categoryFragment;
	private NewContentsFragment contentsFragment;
	private ImageView backButton, forwardButton;
	private TextView barTitle;
	private int fgIndex;
	private String categoryString, contentsString, tagsString;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_new_post_page);
		initActionBar();
		initView();
		
	}

	@Override
	public void getIndex(int index) {
		fgIndex = index;
	}

	@Override
	public void getCategory(String category) {
		categoryString = category;
	}

	@Override
	public void getContent(String content, String tags) {
		contentsString = content;
		tagsString = tags;
	}
	
	private void initActionBar() {
		Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar_new_post);
		setSupportActionBar(toolbar);
		getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
		getSupportActionBar().setCustomView(R.layout.title_bar_setting);
		
		barTitle = (TextView)findViewById(R.id.title_name);
		backButton = (ImageView)findViewById(R.id.button_left_side);
		forwardButton = (ImageView)findViewById(R.id.button_right_side);
		
		backButton.setImageDrawable(getResources().getDrawable(R.drawable.back_button));
		forwardButton.setImageDrawable(getResources().getDrawable(R.drawable.forward_button));
		backButton.setOnClickListener(new backOnClickListener());
		forwardButton.setOnClickListener(new forwardOnClickListener());
	}

	public class backOnClickListener implements OnClickListener {

		@Override
		public void onClick(View v) {
			switch (fgIndex) {
			case NEW_POST_CATEGORY:
				Toast.makeText(getApplicationContext(), "get", Toast.LENGTH_SHORT).show();
				Intent upIntent = NavUtils.getParentActivityIntent(NewPostPage.this);
				if (NavUtils.shouldUpRecreateTask(NewPostPage.this, upIntent)) {
					TaskStackBuilder.create(NewPostPage.this).addNextIntentWithParentStack(upIntent).startActivities();
				}else {
					upIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
					NavUtils.navigateUpTo(NewPostPage.this, upIntent);
				}
				break;
			case NEW_POST_DRAW:
				
				break;
			case NEW_POST_WRITE:
//				transaction.hide(contentsFragment).show(categoryFragment).commit();
				break;
			case NEW_POST_GROUP:
				
				break;
			default:
				break;
			}

		}
		
	}
	
	public class forwardOnClickListener implements OnClickListener {

		@Override
		public void onClick(View v) {
			switch (fgIndex) {
			case NEW_POST_CATEGORY:
				transaction.hide(categoryFragment).show(contentsFragment).commit();
				break;
			case NEW_POST_DRAW:
				
				break;
			case NEW_POST_WRITE:
//				Intent upIntent = NavUtils.getParentActivityIntent(NewPostPage.this);
//				if (NavUtils.shouldUpRecreateTask(NewPostPage.this, upIntent)) {
//					TaskStackBuilder.create(NewPostPage.this).addNextIntentWithParentStack(upIntent).startActivities();
//				}else {
//					upIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//					NavUtils.navigateUpTo(NewPostPage.this, upIntent);
//				}
				break;
			case NEW_POST_GROUP:
				
				break;
			default:
				break;
			}
		}
		
	}
	
	private void initView() {
		if (null == categoryFragment || null == contentsFragment) {
			categoryFragment = new NewCategoryFragment();
			contentsFragment = new NewContentsFragment();
			transaction.add(R.id.new_post_content, categoryFragment);
			transaction.add(R.id.new_post_content, contentsFragment);
			transaction.hide(contentsFragment);
			transaction.show(categoryFragment).commit();
			fgIndex = 0;
		}
		
	}

}
