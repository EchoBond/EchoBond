package com.echobond.activity;

import com.echobond.R;
import com.echobond.entity.User;

import android.app.ActionBar;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
/**
 * 
 * @author aohuijun
 *
 */
public class PeoplePage extends ActionBarActivity {

	private ImageView peopleFigureView;
	private TextView titleView;
	private TextView peopleTitle, peopleBio, peopleGender;
	private TextView peopleDNA, peopleTrophy, peopleTodo, peoplePhilo, peopleEarth, 
					peopleDesc, peopleHeart, peopleSec, peopleLang, peopleTag, peopleGroup; 
	private TextView viewThoughtsButton;
	
	private String userName;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_people_page);
		initActionBar();
		initContent();
	}

	private void initActionBar() {
		Toolbar peopleToolbar = (Toolbar)findViewById(R.id.toolbar_people);
		setSupportActionBar(peopleToolbar);
		getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
		getSupportActionBar().setCustomView(R.layout.title_bar_setting);
		
		titleView = (TextView)findViewById(R.id.title_name);
		userName = getIntent().getStringExtra("userName");
		titleView.setText(userName + "'s Profile");
	}

	private void initContent() {
		peopleFigureView = (ImageView)findViewById(R.id.people_pic);
		peopleTitle = (TextView)findViewById(R.id.people_title);
		peopleBio = (TextView)findViewById(R.id.people_content);
		peopleGender = (TextView)findViewById(R.id.people_gender);
		
		peopleDNA = (TextView)findViewById(R.id.people_about_dna_text);
		peopleTrophy = (TextView)findViewById(R.id.people_about_trophy_text);
		peopleTodo = (TextView)findViewById(R.id.people_about_todo_text);
		peoplePhilo = (TextView)findViewById(R.id.people_about_philo_text);
		peopleEarth = (TextView)findViewById(R.id.people_about_earth_text); 
		peopleDesc = (TextView)findViewById(R.id.people_about_desc_text);
		peopleHeart = (TextView)findViewById(R.id.people_about_heart_text);
		peopleSec = (TextView)findViewById(R.id.people_about_sec_text);
		peopleLang = (TextView)findViewById(R.id.people_about_lang_text);
		peopleTag = (TextView)findViewById(R.id.people_about_tag_text);
		peopleGroup = (TextView)findViewById(R.id.people_about_group_text);
		
		viewThoughtsButton = (TextView)findViewById(R.id.people_view_thoughts);
		viewThoughtsButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent();
				intent.putExtra("userName", userName);
				intent.setClass(PeoplePage.this, ThoughtsListPage.class);
				startActivity(intent);
				Toast.makeText(getApplicationContext(), "Thought List", Toast.LENGTH_SHORT).show();
			}
		});
		
		User user = new User();
	}
	
}
