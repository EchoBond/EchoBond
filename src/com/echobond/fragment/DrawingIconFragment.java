package com.echobond.fragment;

import com.echobond.R;

import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
/**
 * 
 * @author aohuijun
 *
 */
public class DrawingIconFragment extends Fragment{

	private RelativeLayout avatarLayout;
	private String[] colors;
	private ImageView color0, color1, color2, color3, color4, color5, color6, color7, color8, color9, colorA, colorB;
	private ImageView postChange, textChange, avatarType;
	private EditText avatarText;
	private TextView switchIcon;
	private int modeSelected;
	private int count = 0;
	private int[] icons = {R.drawable.poster_type_dream, R.drawable.poster_type_feelings, R.drawable.poster_type_goodideas, 
						R.drawable.poster_type_interests, R.drawable.poster_type_others, R.drawable.poster_type_plans, 
						R.drawable.profile_about_gender, R.drawable.profile_about_earth, R.drawable.profile_about_trophy};
	
	private final static int CHANGE_BACKGROUND = 0;
	private final static int CHANGE_TEXT = 1;
	private final static int ICON_TYPE_COUNT = 9;
	
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
		View drawingIconView = inflater.inflate(R.layout.fragment_edit_profile_icon, container, false);
		
		avatarLayout = (RelativeLayout)drawingIconView.findViewById(R.id.edit_profile_picview);
		colors = getResources().getStringArray(R.array.color_array);
		avatarText = (EditText)drawingIconView.findViewById(R.id.edit_profile_words);
		avatarType = (ImageView)drawingIconView.findViewById(R.id.edit_profile_typeview);
		postChange = (ImageView)drawingIconView.findViewById(R.id.edit_profile_changepostcolor);
		textChange = (ImageView)drawingIconView.findViewById(R.id.edit_profile_changetextcolor);
		switchIcon = (TextView)drawingIconView.findViewById(R.id.edit_profile_switch_icon);
		
		color0 = (ImageView)drawingIconView.findViewById(R.id.edit_profile_color0);
		color1 = (ImageView)drawingIconView.findViewById(R.id.edit_profile_color1);
		color2 = (ImageView)drawingIconView.findViewById(R.id.edit_profile_color2);
		color3 = (ImageView)drawingIconView.findViewById(R.id.edit_profile_color3);
		color4 = (ImageView)drawingIconView.findViewById(R.id.edit_profile_color4);
		color5 = (ImageView)drawingIconView.findViewById(R.id.edit_profile_color5);
		color6 = (ImageView)drawingIconView.findViewById(R.id.edit_profile_color6);
		color7 = (ImageView)drawingIconView.findViewById(R.id.edit_profile_color7);
		color8 = (ImageView)drawingIconView.findViewById(R.id.edit_profile_color8);
		color9 = (ImageView)drawingIconView.findViewById(R.id.edit_profile_color9);
		colorA = (ImageView)drawingIconView.findViewById(R.id.edit_profile_colora);
		colorB = (ImageView)drawingIconView.findViewById(R.id.edit_profile_colorb);
		
		modeSelected = CHANGE_BACKGROUND;
		avatarType.setColorFilter(Color.parseColor(colors[BLACK]), PorterDuff.Mode.SRC_IN);
		postChange.setImageDrawable(getResources().getDrawable(R.drawable.thoughts_background_colorchange_selected));
		postChange.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				DrawingIconFragment.this.modeSelected = CHANGE_BACKGROUND;
				postChange.setImageDrawable(getResources().getDrawable(R.drawable.thoughts_background_colorchange_selected));
				textChange.setImageDrawable(getResources().getDrawable(R.drawable.thoughts_text_colorchange_normal));
				Toast.makeText(getActivity().getApplicationContext(), "Please select avatar's background color. ", Toast.LENGTH_SHORT).show();
			}
		});
		textChange.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				DrawingIconFragment.this.modeSelected = CHANGE_TEXT;
				textChange.setImageDrawable(getResources().getDrawable(R.drawable.thoughts_text_colorchange_selected));
				postChange.setImageDrawable(getResources().getDrawable(R.drawable.thoughts_background_colorchange_nomal));
				Toast.makeText(getActivity().getApplicationContext(), "Please select avatar's text color. ", Toast.LENGTH_SHORT).show();
			}
		});
		switchIcon.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				avatarType.setImageResource(icons[count]);
				count++;
				if (count >= ICON_TYPE_COUNT) {
					count = 0;
				}
			}
		});
		
		color0.setOnClickListener(new SelectedColor(WHITE));
		color1.setOnClickListener(new SelectedColor(DARK_RED));
		color2.setOnClickListener(new SelectedColor(BRIGHT_RED));
		color3.setOnClickListener(new SelectedColor(PINK));
		color4.setOnClickListener(new SelectedColor(YELLOW));
		color5.setOnClickListener(new SelectedColor(ORANGE));
		color6.setOnClickListener(new SelectedColor(BLACK));
		color7.setOnClickListener(new SelectedColor(CYAN));
		color8.setOnClickListener(new SelectedColor(MINT));
		color9.setOnClickListener(new SelectedColor(PURPLE));
		colorA.setOnClickListener(new SelectedColor(HORIZON));
		colorB.setOnClickListener(new SelectedColor(GREEN));
		
		return drawingIconView;
	}
	
	public class SelectedColor implements OnClickListener {

		private int colorIndex;
		public SelectedColor(int type) {
			colorIndex = type;
		}

		@Override
		public void onClick(View v) {
			if (modeSelected == CHANGE_BACKGROUND) {
				avatarLayout.setBackgroundColor(Color.parseColor(colors[colorIndex]));
			}
			if (modeSelected == CHANGE_TEXT) {
				avatarText.setTextColor(Color.parseColor(colors[colorIndex]));
				avatarType.setColorFilter(Color.parseColor(colors[colorIndex]), PorterDuff.Mode.SRC_IN);
			}
		}
		
	}

	public RelativeLayout getAvatarLayout() {
		return avatarLayout;
	}

	public EditText getAvatarText() {
		return avatarText;
	}
	
}
