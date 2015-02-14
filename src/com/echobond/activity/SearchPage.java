package com.echobond.activity;

import com.echobond.R;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v4.app.TaskStackBuilder;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
/**
 * 
 * @author aohuijun
 *
 */
public class SearchPage extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
//		
//		ColorDrawable colorDrawable = new ColorDrawable();
//		colorDrawable.setColor(Color.WHITE);
//		getActionBar().setBackgroundDrawable(colorDrawable);
//		getActionBar().setHomeButtonEnabled(true);
//		getActionBar().setDisplayHomeAsUpEnabled(true);
		
		setContentView(R.layout.activity_search_page);
		initActionBar();
	}

	private void initActionBar() {
		// TODO Auto-generated method stub
		
	}
	
//	@Override
//	public boolean onCreateOptionsMenu(Menu menu) {
//		MenuInflater menuInflater = getMenuInflater();
//		menuInflater.inflate(R.menu.searchpage_actionbar_menu, menu);
//		return true;
//	}
//	
//	@Override
//	public boolean onOptionsItemSelected(MenuItem item) {
//		switch (item.getItemId()) {
//		case android.R.id.home:
//			Intent upIntent = NavUtils.getParentActivityIntent(this);  
//	        if (NavUtils.shouldUpRecreateTask(this, upIntent)) {  
//	            TaskStackBuilder.create(this)
//	                    .addNextIntentWithParentStack(upIntent)  
//	                    .startActivities();  
//	        } else {  
//	            upIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);  
//	            NavUtils.navigateUpTo(this, upIntent);  
//	        }
//			return true;
//		default:
//			return super.onOptionsItemSelected(item);
//		}
//		
//	}
}
