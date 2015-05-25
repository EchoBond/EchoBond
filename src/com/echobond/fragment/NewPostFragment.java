package com.echobond.fragment;

import com.echobond.R;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;

public class NewPostFragment extends Fragment {
	
	private ImageView[] colors = {};
	private ImageView color0, color1, color2, color3, color4, color5, color6, color7, color8, color9, colorA, colorB;
	private ImageView postChange, textChange;
	private EditText postText;
	
	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		View postView = inflater.inflate(R.layout.fragment_new_post_pic, container, false);
		postText = (EditText)postView.findViewById(R.id.thought_poster_words);
		
		postChange = (ImageView)postView.findViewById(R.id.thought_changepostcolor);
		textChange = (ImageView)postView.findViewById(R.id.thought_changetextcolor);
		
		color0 = (ImageView)postView.findViewById(R.id.thought_poster_color0);
		color1 = (ImageView)postView.findViewById(R.id.thought_poster_color1);
		color2 = (ImageView)postView.findViewById(R.id.thought_poster_color2);
		color3 = (ImageView)postView.findViewById(R.id.thought_poster_color3);
		color4 = (ImageView)postView.findViewById(R.id.thought_poster_color4);
		color5 = (ImageView)postView.findViewById(R.id.thought_poster_color5);
		color6 = (ImageView)postView.findViewById(R.id.thought_poster_color6);
		color7 = (ImageView)postView.findViewById(R.id.thought_poster_color7);
		color8 = (ImageView)postView.findViewById(R.id.thought_poster_color8);
		color9 = (ImageView)postView.findViewById(R.id.thought_poster_color9);
		colorA = (ImageView)postView.findViewById(R.id.thought_poster_colora);
		colorB = (ImageView)postView.findViewById(R.id.thought_poster_colorb);
		
		return postView;
	}
}
