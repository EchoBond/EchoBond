package com.echobond.activity;

import java.util.ArrayList;

import org.json.JSONObject;

import com.echobond.R;
import com.echobond.connector.PostThoughtAsyncTask;
import com.echobond.entity.Tag;
import com.echobond.entity.Thought;
import com.echobond.fragment.NewCategoryFragment;
import com.echobond.fragment.NewContentsFragment;
import com.echobond.fragment.NewGroupsFragment;
import com.echobond.intf.NewPostFragmentsSwitchAsyncTaskCallback;
import com.echobond.intf.PostThoughtCallback;
import com.echobond.util.HTTPUtil;
import com.echobond.util.SPUtil;

import android.app.ActionBar;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
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
public class NewPostPage extends ActionBarActivity implements NewPostFragmentsSwitchAsyncTaskCallback, PostThoughtCallback {
	
	public static final int NEW_POST_CATEGORY = 0;
	public static final int NEW_POST_DRAW = 1;
	public static final int NEW_POST_WRITE = 2;
	public static final int NEW_POST_GROUP = 3;
	
	private NewCategoryFragment categoryFragment;
	private NewContentsFragment contentsFragment;
	private NewGroupsFragment groupsFragment;
	private String contentsString = "", tagsString = "";
	private int categoryId = -1, groupId = -1;
	private ImageView backButton, forwardButton;
	private TextView barTitle;
	private int fgIndex;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_new_post_page);
		initActionBar();
		initView();
	}

	@Override
	public void getCategory(int categoryId) {
		this.categoryId = categoryId;
	}

	@Override
	public void getContent(String content, String tags) {
		this.contentsString = content;
		this.tagsString = tags;
	}
	
	@Override
	public void getGroup(int groupId) {
		this.groupId = groupId;
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
		
		barTitle.setText(R.string.title_new_post_share);
	}

	public class backOnClickListener implements OnClickListener {

		@Override
		public void onClick(View v) {
			switch (fgIndex) {
			case NEW_POST_CATEGORY:
				activityBackStack();
				break;
			case NEW_POST_DRAW:
				
				break;
			case NEW_POST_WRITE:
				barTitle.setText(R.string.title_new_post_share);
				getSupportFragmentManager().beginTransaction().hide(contentsFragment).show(categoryFragment).commit();
				fgIndex -= 1;
				fgIndex -= 1;
				break;
			case NEW_POST_GROUP:
				barTitle.setText(R.string.title_new_post_write);
				forwardButton.setImageDrawable(getResources().getDrawable(R.drawable.forward_button));
				getSupportFragmentManager().beginTransaction().hide(groupsFragment).show(contentsFragment).commit();
				fgIndex -= 1;
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
				postThought();
				break;
			default:
				break;
			}
		}

		private void isCategorySelected() {
			if (categoryId == -1) {
				
			} else {
				barTitle.setText(R.string.title_new_post_write);
				getSupportFragmentManager().beginTransaction().hide(categoryFragment).show(contentsFragment).commit();
				fgIndex += 1;
				fgIndex += 1;
			}
		}

		private void isContentsEmpty() { 
			if (contentsString == null || contentsString.equals("")) {
				Toast.makeText(getApplicationContext(), getResources().getString(R.string.hint_new_post_empty_content), Toast.LENGTH_LONG).show();
			}else {
				barTitle.setText(R.string.title_new_post_group);
				forwardButton.setImageDrawable(getResources().getDrawable(R.drawable.done_button));
				getSupportFragmentManager().beginTransaction().hide(contentsFragment).show(groupsFragment).commit();
				fgIndex += 1;
			}
		}
		
		private void postThought() {
			Thought t = new Thought();
			ArrayList<Tag> tags = Tag.str2TagList(tagsString);
			t.setTags(tags);
			t.setCategoryId(categoryId);
			t.setContent(contentsString);
			t.setUserId((String) SPUtil.get(NewPostPage.this, "login", "loginUser_id", null, String.class));
			t.setGroupId(groupId);
			new PostThoughtAsyncTask().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, 
					HTTPUtil.getInstance().composePreURL(NewPostPage.this) + getResources().getString(R.string.url_post_thought), 
					t, NewPostPage.this);
		}
		
	}
	
	private void initView() {
		FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
		if (null == categoryFragment || null == contentsFragment || null == groupsFragment) {
			categoryFragment = new NewCategoryFragment();
			contentsFragment = new NewContentsFragment();
			groupsFragment = new NewGroupsFragment();
			transaction.add(R.id.new_post_content, categoryFragment);
			transaction.add(R.id.new_post_content, contentsFragment);
			transaction.add(R.id.new_post_content, groupsFragment);
			transaction.hide(contentsFragment);
			transaction.hide(groupsFragment);
			transaction.show(categoryFragment).commit();
			fgIndex = 0;
		}
	}

	private void activityBackStack() {
		Intent upIntent = NavUtils.getParentActivityIntent(NewPostPage.this);
		if (NavUtils.shouldUpRecreateTask(NewPostPage.this, upIntent)) {
			TaskStackBuilder.create(NewPostPage.this).addNextIntentWithParentStack(upIntent).startActivities();
		}else {
			upIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			NavUtils.navigateUpTo(NewPostPage.this, upIntent);
		}
	}
	
	
	@Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
    	//pressed back key
    	if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
    		//in the start fragment
    		if(fgIndex == 0){
    			// Click it to return to MainPage. 
    			activityBackStack();
				return true;
			}
        	//in other fragments
        	else if (fgIndex == 2){ 
        		getSupportFragmentManager().beginTransaction().hide(contentsFragment).show(categoryFragment).commit();
    			fgIndex -= 1;
    			fgIndex -= 1;
        	}else if (fgIndex == 3) {
        		getSupportFragmentManager().beginTransaction().hide(groupsFragment).show(contentsFragment).commit();
        		fgIndex -= 1;
			}
    	}
    	return true;
	}

	@Override
	public void onPostThoughtResult(JSONObject result) {
		if(null == result){
			Toast.makeText(getApplicationContext(), getResources().getString(R.string.hint_new_post_failed_post), Toast.LENGTH_LONG).show();
		} else {
			Toast.makeText(getApplicationContext(), getResources().getString(R.string.hint_new_post_successful_post), Toast.LENGTH_LONG).show();
			activityBackStack();
		}
		
	}
    

}
