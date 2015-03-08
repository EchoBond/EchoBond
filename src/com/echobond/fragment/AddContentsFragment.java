package com.echobond.fragment;

import com.echobond.R;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
<<<<<<< HEAD
import android.widget.EditText;
=======
>>>>>>> 97066ed46b4452f9e85ca9b52eec5ecaf7ef334f
import android.widget.ImageView;
import android.widget.TextView;

public class AddContentsFragment extends Fragment {
	
	private ImageView backButton, proceedButton;
	private TextView titleView;
<<<<<<< HEAD
	private EditText thoughtsContent, tagsContent;
=======
>>>>>>> 97066ed46b4452f9e85ca9b52eec5ecaf7ef334f
	
	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		
		View addContentsView = inflater.inflate(R.layout.fragment_main_add_contents, container, false);
<<<<<<< HEAD
		
		backButton = (ImageView)addContentsView.findViewById(R.id.button_left_side);
		proceedButton = (ImageView)addContentsView.findViewById(R.id.button_right_side);
		titleView = (TextView)addContentsView.findViewById(R.id.title_name);
		thoughtsContent = (EditText)addContentsView.findViewById(R.id.thoughts_content);
		tagsContent = (EditText)addContentsView.findViewById(R.id.tags_content);
=======
		backButton = (ImageView)addContentsView.findViewById(R.id.button_left_side);
		proceedButton = (ImageView)addContentsView.findViewById(R.id.button_right_side);
		titleView = (TextView)addContentsView.findViewById(R.id.title_name);
>>>>>>> 97066ed46b4452f9e85ca9b52eec5ecaf7ef334f
		backButton.setImageResource(R.drawable.back_button);
		proceedButton.setImageResource(R.drawable.proceed_button);
		titleView.setText("Write your Thought");
		
		return addContentsView;
	}
	
	@Override
	public void onActivityCreated(@Nullable Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
	}
}
