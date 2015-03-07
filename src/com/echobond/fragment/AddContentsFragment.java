package com.echobond.fragment;

import com.echobond.R;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

public class AddContentsFragment extends Fragment {
	
	private ImageView backButton, proceedButton;
	private TextView titleView;
	
	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		
		View addContentsView = inflater.inflate(R.layout.fragment_main_add_contents, container, false);
		backButton = (ImageView)addContentsView.findViewById(R.id.button_left_side);
		proceedButton = (ImageView)addContentsView.findViewById(R.id.button_right_side);
		titleView = (TextView)addContentsView.findViewById(R.id.title_name);
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
