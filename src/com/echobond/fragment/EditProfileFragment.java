package com.echobond.fragment;

import com.echobond.R;
import com.echobond.activity.NewPostPage;
import com.echobond.activity.ViewMorePage;
import com.echobond.intf.EditProfileSwitchCallback;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.TextView;
/**
 * 
 * @author aohuijun
 *
 */
public class EditProfileFragment extends Fragment {

	private EditProfileSwitchCallback switchCallback;
	private TextView setIconView, moreTagsView, moreGroupsView;
	private EditText userName, userGender, userBio, userAge, userOrigin, 
					userDNA, userTrophy, userTodo, userPhilo, userDesc, userInterest, userSec, userLang, 
					userTags, userGroups;
	
	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		View editProfileView = inflater.inflate(R.layout.fragment_edit_profile, container, false);
		
		userName = (EditText)editProfileView.findViewById(R.id.edit_profile_title_text);
		userGender = (EditText)editProfileView.findViewById(R.id.edit_profile_gender_text);
		userBio = (EditText)editProfileView.findViewById(R.id.edit_profile_bio_text);
		userAge = (EditText)editProfileView.findViewById(R.id.edit_profile_age_text);
		userOrigin = (EditText)editProfileView.findViewById(R.id.edit_profile_earth_text);
		
		userDNA = (EditText)editProfileView.findViewById(R.id.edit_profile_dna_text);
		userTrophy = (EditText)editProfileView.findViewById(R.id.edit_profile_trophy_text);
		userTodo = (EditText)editProfileView.findViewById(R.id.edit_profile_todo_text);
		userPhilo = (EditText)editProfileView.findViewById(R.id.edit_profile_philo_text);
		userDesc = (EditText)editProfileView.findViewById(R.id.edit_profile_desc_text);
		userInterest = (EditText)editProfileView.findViewById(R.id.edit_profile_heart_text);
		userSec = (EditText)editProfileView.findViewById(R.id.edit_profile_sec_text);
		userLang = (EditText)editProfileView.findViewById(R.id.edit_profile_lang_text);
		
		userTags = (EditText)editProfileView.findViewById(R.id.edit_profile_tag_text);
		userGroups = (EditText)editProfileView.findViewById(R.id.edit_profile_group_text);
		
		setIconView = (TextView)editProfileView.findViewById(R.id.edit_profile_icon);
		moreTagsView = (TextView)editProfileView.findViewById(R.id.edit_profile_tag_more);
		moreGroupsView = (TextView)editProfileView.findViewById(R.id.edit_profile_group_more);
		setIconView.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				switchCallback.setPoster(true);
			}
		});
		moreTagsView.setOnClickListener(new ViewMoreClickListener(NewPostPage.TAG));
		moreGroupsView.setOnClickListener(new ViewMoreClickListener(NewPostPage.GROUP));
		
		return editProfileView;
	}
	
	public class ViewMoreClickListener implements OnClickListener {

		private String typeString;
		
		public ViewMoreClickListener(String tag) {
			this.typeString = tag;
		}

		@Override
		public void onClick(View v) {
			Intent intent = new Intent();
			intent.putExtra("title", typeString);
			intent.setClass(getActivity(), ViewMorePage.class);
			startActivity(intent);
		}
		
	}
	
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		try {
			switchCallback = (EditProfileSwitchCallback) activity;
		} catch (Exception e) {
			throw new ClassCastException(activity.toString() + "must implement switchCallback in EditProfileFragment. ");
		}
	}

}
