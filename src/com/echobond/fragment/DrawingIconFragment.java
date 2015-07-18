package com.echobond.fragment;

import com.echobond.R;
import com.echobond.activity.EditProfilePage;
import com.echobond.application.MyApp;
import com.echobond.intf.EditProfileSwitchCallback;

import android.app.Activity;
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
public class DrawingIconFragment extends Fragment {

	private EditProfileSwitchCallback callCanvas;
	private RelativeLayout avatarLayout;
	private String[] colors;
	private ImageView postChange, textChange, avatarType, switchCanvas;
	private EditText avatarText;
	private TextView switchIcon;
	private int modeSelected;
	private int count = 0;
	private int[] icons = {R.drawable.poster_type_dream, R.drawable.poster_type_feelings, R.drawable.poster_type_goodideas, 
						R.drawable.poster_type_interests, R.drawable.poster_type_others, R.drawable.poster_type_plans, 
						R.drawable.profile_about_gender, R.drawable.profile_about_earth, R.drawable.profile_about_trophy};
	private ImageView[] colorViews = new ImageView[12];
	private int[] colorButtons = {R.id.edit_profile_color0, R.id.edit_profile_color1, R.id.edit_profile_color2, 
								R.id.edit_profile_color3, R.id.edit_profile_color4, R.id.edit_profile_color5, 
								R.id.edit_profile_color6, R.id.edit_profile_color7, R.id.edit_profile_color8, 
								R.id.edit_profile_color9, R.id.edit_profile_colora, R.id.edit_profile_colorb};
	private int textPrevColor = -1;
	private int backPrevColor = -1;
	
	private final static int CHANGE_BACKGROUND = 0;
	private final static int CHANGE_TEXT = 1;
	private final static int ICON_TYPE_COUNT = 9;
	
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
		switchCanvas = (ImageView)drawingIconView.findViewById(R.id.edit_profile_switch_canvas);
		switchIcon = (TextView)drawingIconView.findViewById(R.id.edit_profile_switch_icon);
		
		for (int i = 0; i < colors.length; i++) {
			colorViews[i] = (ImageView)drawingIconView.findViewById(colorButtons[i]);
			colorViews[i].setOnClickListener(new SelectedColor(i));
		}
		colorViews[MyApp.COLOR_BLACK].setBackgroundResource(R.drawable.color_selection_background);
		
		modeSelected = CHANGE_TEXT;
		avatarType.setColorFilter(Color.parseColor(colors[MyApp.COLOR_BLACK]), PorterDuff.Mode.SRC_IN);
		textChange.setImageDrawable(getResources().getDrawable(R.drawable.thoughts_text_colorchange_selected));
		postChange.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				setColor(backPrevColor);
				if (backPrevColor == -1) {
					colorViews[MyApp.COLOR_WHITE].setBackgroundResource(R.drawable.color_selection_background);
				}
				DrawingIconFragment.this.modeSelected = CHANGE_BACKGROUND;
				postChange.setImageDrawable(getResources().getDrawable(R.drawable.thoughts_background_colorchange_selected));
				textChange.setImageDrawable(getResources().getDrawable(R.drawable.thoughts_text_colorchange_normal));
				Toast.makeText(getActivity().getApplicationContext(), "Please select avatar's background color. ", Toast.LENGTH_SHORT).show();
			}
		});
		textChange.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				setColor(textPrevColor);
				if (textPrevColor == -1) {
					colorViews[MyApp.COLOR_BLACK].setBackgroundResource(R.drawable.color_selection_background);
				}
				DrawingIconFragment.this.modeSelected = CHANGE_TEXT;
				textChange.setImageDrawable(getResources().getDrawable(R.drawable.thoughts_text_colorchange_selected));
				postChange.setImageDrawable(getResources().getDrawable(R.drawable.thoughts_background_colorchange_nomal));
				Toast.makeText(getActivity().getApplicationContext(), "Please select avatar's text color. ", Toast.LENGTH_SHORT).show();
			}
		});
		switchCanvas.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				callCanvas.setAvatarType(EditProfilePage.PAGE_CANVAS);
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
		
		return drawingIconView;
	}
	
	public class SelectedColor implements OnClickListener {

		private int colorIndex;
		public SelectedColor(int type) {
			colorIndex = type;
		}

		@Override
		public void onClick(View v) {
			setColor(colorIndex);
			if (modeSelected == CHANGE_BACKGROUND) {
				avatarLayout.setBackgroundColor(Color.parseColor(colors[colorIndex]));
				backPrevColor = colorIndex;
			}
			if (modeSelected == CHANGE_TEXT) {
				avatarText.setTextColor(Color.parseColor(colors[colorIndex]));
				avatarType.setColorFilter(Color.parseColor(colors[colorIndex]), PorterDuff.Mode.SRC_IN);
				textPrevColor = colorIndex;
			}
		}
		
	}


	private void setColor(int color) {
		for (int i = 0; i < colors.length; i++) {
			colorViews[i].setBackgroundResource(0);
		}
		if (color != -1) {
			colorViews[color].setBackgroundResource(R.drawable.color_selection_background);
		}
	}
	
	public RelativeLayout getAvatarLayout() {
		return avatarLayout;
	}

	public EditText getAvatarText() {
		return avatarText;
	}
	
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		try {
			callCanvas = (EditProfileSwitchCallback) activity;
		} catch (ClassCastException e) {
			throw new ClassCastException(activity.toString() + "must implement EditProfileSwitchCallback in DrawingIconFragment. ");
		}
	}
	
}
