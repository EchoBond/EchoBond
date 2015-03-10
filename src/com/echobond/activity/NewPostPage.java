package com.echobond.activity;

import com.echobond.R;
import com.echobond.fragment.NewCategoryFragment;
import com.echobond.fragment.NewContentsFragment;
import com.echobond.intf.NewPostFragmentsSwitchAsyncTaskCallback;

import android.app.ActionBar;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.NavUtils;
import android.support.v4.app.TaskStackBuilder;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
/**
 * 
 * @author aohuijun
 *
 */
public class NewPostPage extends ActionBarActivity implements NewPostFragmentsSwitchAsyncTaskCallback {
	
	public static final int NEW_POST_CATEGORY = 0;
	public static final int NEW_POST_DRAW = 1;
	public static final int NEW_POST_WRITE = 2;
	public static final int NEW_POST_GROUP = 3;
	
	private NewCategoryFragment categoryFragment;
	private NewContentsFragment contentsFragment;
	private String categoryString, contentsString = "", tagsString = "";

	private ImageView backButton, forwardButton;
	private TextView barTitle;
	private int fgIndex;
    private long exitTime = 0;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_new_post_page);
		initActionBar();
		initView();
		
	}

	@Override
	public void getCategory(String category) {
		this.categoryString = category;
	}

	@Override
	public void getContent(String content, String tags) {
		this.contentsString = content;
		this.tagsString = tags;
	}
	
	@Override
	public void getGroup() {
		// TODO Auto-generated method stub
		
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
				barTitle.setText(R.string.title_new_post_share);
				FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
				transaction.hide(contentsFragment).show(categoryFragment).commit();
				fgIndex -= 1;
				fgIndex -= 1;
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
				isCategorySelected();
				break;
			case NEW_POST_DRAW:
				
				break;
			case NEW_POST_WRITE:
				isContentsEmpty();
				break;
			case NEW_POST_GROUP:
				
				break;
			default:
				break;
			}
		}

		private void isCategorySelected() {
			barTitle.setText(R.string.title_new_post_write);
			FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
			transaction.hide(categoryFragment).show(contentsFragment).commit();
			fgIndex += 1;
			fgIndex += 1;
		}

		private void isContentsEmpty() { 
			if (contentsString == null || contentsString.equals("")) {
				Toast.makeText(getApplicationContext(), contentsString + " 同埋 " + tagsString, Toast.LENGTH_LONG).show();
			}else {
				Toast.makeText(getApplicationContext(), contentsString + " 同埋 " + tagsString, Toast.LENGTH_LONG).show();
				Intent upIntent = NavUtils.getParentActivityIntent(NewPostPage.this);
				if (NavUtils.shouldUpRecreateTask(NewPostPage.this, upIntent)) {
					TaskStackBuilder.create(NewPostPage.this).addNextIntentWithParentStack(upIntent).startActivities();
				}else {
					upIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
					NavUtils.navigateUpTo(NewPostPage.this, upIntent);
				}
			}
		}
		
	}
	
	private void initView() {
		FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
		if (null == categoryFragment || null == contentsFragment) {
			categoryFragment = new NewCategoryFragment();
			contentsFragment = new NewContentsFragment();
			transaction.add(R.id.new_post_content, categoryFragment);
			transaction.add(R.id.new_post_content, contentsFragment);
			transaction.hide(contentsFragment);
			transaction.show(categoryFragment).commit();
			fgIndex = 0;
			barTitle.setText(R.string.title_new_post_share);
		}
	}

	@Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
    	//pressed back key
    	if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
    		//in the start fragment
    		if(fgIndex == 0){
    			// Click it to return to MainPage. 
    			Intent upIntent = NavUtils.getParentActivityIntent(NewPostPage.this);
				if (NavUtils.shouldUpRecreateTask(NewPostPage.this, upIntent)) {
					TaskStackBuilder.create(NewPostPage.this).addNextIntentWithParentStack(upIntent).startActivities();
				}else {
					upIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
					NavUtils.navigateUpTo(NewPostPage.this, upIntent);
				}
				return true;
			}
        	//in other fragments
        	else { 
        		getSupportFragmentManager().beginTransaction().hide(contentsFragment).show(categoryFragment).commit();
    			fgIndex -= 1;
    			fgIndex -= 1;
        	}
    	}
    	return true;
	}
    
}
