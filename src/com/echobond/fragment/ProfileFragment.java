package com.echobond.fragment;

import com.echobond.R;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
/**
 * 
 * @author aohuijun
 *
 */
public class ProfileFragment extends Fragment {
	
	private ImageView profileFigureView;
	private TextView profileTitle, profileBio, profileGentle;
	private TextView profileDNA, profileTrophy, profileTodo, profilePhilo, profileEarth, 
					profileDesc, profileHeart, profileSec, profileLang, profileTag; 
	private TextView viewThoughtsButton;
	private String[] testString = {
			"I speak Chinese! Have spent 5 years in Beijing.", 
			"Have performed sexophone across India. Went to Grand Canyon!", 
			"Set up my startup office in Silicon Valley. Visit Israel.", 
			"You Only Live Once. Make it count. Be yourself and be nice.", 
			"People stop talking about war but ART! No racism, everyone is a global citizen.", 
			"Funny, care-free, music talent, nice, king of sexophone lol", 
			"hiking, meet new fds, chinese food, cooking, sexophone", 
			"didn’t know Santa Claus was a myth by 13 years old", 
			"English, Hindi, Mandarin.", 
			"#hkust #sexophone #india #beijing #mandarin #engineering #tech start up #hong kong #cooking #hiking #travelling #music #exchange #language exchange #party #chinese culture"
	};
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		
		View profileView = inflater.inflate(R.layout.fragment_main_profile, container, false);
		profileFigureView = (ImageView)profileView.findViewById(R.id.profile_pic);
		profileTitle = (TextView)profileView.findViewById(R.id.profile_title);
		profileBio = (TextView)profileView.findViewById(R.id.profile_content);
		profileGentle = (TextView)profileView.findViewById(R.id.profile_gender);
		
		profileDNA = (TextView)profileView.findViewById(R.id.profile_about_dna_text);
		profileTrophy = (TextView)profileView.findViewById(R.id.profile_about_trophy_text);
		profileTodo = (TextView)profileView.findViewById(R.id.profile_about_todo_text);
		profilePhilo = (TextView)profileView.findViewById(R.id.profile_about_philo_text);
		profileEarth = (TextView)profileView.findViewById(R.id.profile_about_earth_text);
		profileDesc = (TextView)profileView.findViewById(R.id.profile_about_desc_text);
		profileHeart = (TextView)profileView.findViewById(R.id.profile_about_heart_text);
		profileSec = (TextView)profileView.findViewById(R.id.profile_about_sec_text);
		profileLang = (TextView)profileView.findViewById(R.id.profile_about_lang_text);
		profileTag = (TextView)profileView.findViewById(R.id.profile_about_tag_text);
		
		viewThoughtsButton = (TextView)profileView.findViewById(R.id.profile_view_thoughts);
		
		profileFigureView.setImageResource(R.drawable.default_avatar);
		profileTitle.setText(getResources().getString(R.string.app_name));
		profileBio.setText(getResources().getString(R.string.hello_world));
		
		profileDNA.setText(testString[0]);
		profileTrophy.setText(testString[1]);
		profileTodo.setText(testString[2]);
		profilePhilo.setText(testString[3]);
		profileEarth.setText(testString[4]);
		profileDesc.setText(testString[5]);
		profileHeart.setText(testString[6]);
		profileSec.setText(testString[7]);
		profileLang.setText(testString[8]);
		profileTag.setText(testString[9]);
		
		viewThoughtsButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				Toast.makeText(getActivity().getApplicationContext(), "Navigate to ThoughtsFragment.class: TO BE DONE", Toast.LENGTH_SHORT).show();
				// TODO create a new fragment/activity
			}
		});
		
		return profileView;
		
	}
}
