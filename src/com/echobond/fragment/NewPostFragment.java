package com.echobond.fragment;

import com.echobond.R;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

public class NewPostFragment extends Fragment {
	
	private RelativeLayout relativeLayout;
	private String[] colors;
	private ImageView color0, color1, color2, color3, color4, color5, color6, color7, color8, color9, colorA, colorB;
	private ImageView postChange, textChange;
	private EditText postText;
	
	private final static int WHITE = 0;
	private final static int DARK_RED = 1;
	private final static int BRIGHT_RED = 2;
	private final static int PINK = 3;
	private final static int YELLOW = 4;
	private final static int ORANGE = 5;
	private final static int BLACK = 6;
	private final static int CYAN = 7;
	private final static int MINT = 8;
	private final static int PURPLE = 9;
	private final static int HORIZON = 10;
	private final static int GREEN = 11;
	
	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		View postView = inflater.inflate(R.layout.fragment_new_post_pic, container, false);
		colors = getResources().getStringArray(R.array.color_array);
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
		
		postChange.setOnClickListener(new selectedMode());
		textChange.setOnClickListener(new selectedMode());
		
		color0.setOnClickListener(new selectedColor(WHITE));
		color1.setOnClickListener(new selectedColor(DARK_RED));
		color2.setOnClickListener(new selectedColor(BRIGHT_RED));
		color3.setOnClickListener(new selectedColor(PINK));
		color4.setOnClickListener(new selectedColor(YELLOW));
		color5.setOnClickListener(new selectedColor(ORANGE));
		color6.setOnClickListener(new selectedColor(BLACK));
		color7.setOnClickListener(new selectedColor(CYAN));
		color8.setOnClickListener(new selectedColor(MINT));
		color9.setOnClickListener(new selectedColor(PURPLE));
		colorA.setOnClickListener(new selectedColor(HORIZON));
		colorB.setOnClickListener(new selectedColor(GREEN));
		return postView;
	}
	
	public class selectedMode implements OnClickListener {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			Toast.makeText(getActivity().getApplicationContext(), "clicked", Toast.LENGTH_SHORT).show();
		}
		
	}
	
	public class selectedColor implements OnClickListener {

		private int colorIndex = WHITE;
		public selectedColor(int i) {	colorIndex = i;}

		@Override
		public void onClick(View v) {
			postText.setTextColor(Color.parseColor(colors[colorIndex]));
		}
		
	}
}
