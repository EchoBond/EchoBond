package com.echobond.fragment;

import com.echobond.R;
import com.echobond.activity.NewPostPage;
import com.echobond.intf.NewPostFragmentsSwitchAsyncTaskCallback;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.PorterDuff;
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
/**
 * 
 * @author aohuijun
 *
 */
public class NewPostFragment extends Fragment {
	
	private NewPostFragmentsSwitchAsyncTaskCallback callCanvas;
	private RelativeLayout postLayout;
	private String[] colors;
	private ImageView color0, color1, color2, color3, color4, color5, color6, color7, color8, color9, colorA, colorB;
	private ImageView postChange, textChange, categoryType, switchCanvas;
	private EditText postText;
	private int modeSelected;
	
	private final static int CHANGE_BACKGROUND = 0;
	private final static int CHANGE_TEXT = 1;
	
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
		
		postLayout = (RelativeLayout)postView.findViewById(R.id.thought_poster_picview);
		colors = getResources().getStringArray(R.array.color_array);
		postText = (EditText)postView.findViewById(R.id.thought_poster_words);
		categoryType = (ImageView)postView.findViewById(R.id.thought_poster_typeview);
		postChange = (ImageView)postView.findViewById(R.id.thought_changepostcolor);
		textChange = (ImageView)postView.findViewById(R.id.thought_changetextcolor);
		switchCanvas = (ImageView)postView.findViewById(R.id.thought_switch_canvas);
		
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
		
		categoryType.setColorFilter(Color.parseColor(colors[BLACK]), PorterDuff.Mode.SRC_IN);
		setCategoryType();
		modeSelected = CHANGE_TEXT;
		textChange.setImageResource(R.drawable.thoughts_text_colorchange_selected);
		postChange.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				NewPostFragment.this.modeSelected = CHANGE_BACKGROUND;
				postChange.setImageResource(R.drawable.thoughts_background_colorchange_selected);
				textChange.setImageResource(R.drawable.thoughts_text_colorchange_normal);
				Toast.makeText(getActivity().getApplicationContext(), "Please select poster's background color. ", Toast.LENGTH_SHORT).show();
			}
		});
		textChange.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				NewPostFragment.this.modeSelected = CHANGE_TEXT;
				textChange.setImageResource(R.drawable.thoughts_text_colorchange_selected);
				postChange.setImageResource(R.drawable.thoughts_background_colorchange_nomal);
				Toast.makeText(getActivity().getApplicationContext(), "Please select poster's text color. ", Toast.LENGTH_SHORT).show();
			}
		});
		switchCanvas.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				callCanvas.switchCanvas();
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
		
		return postView;
	}
	
	public void setCategoryType() {
		int categoryId = ((NewPostPage)getActivity()).getCategoryId();
		switch (categoryId) {
		case 1:
			categoryType.setImageResource(R.drawable.poster_type_feelings);
			break;
		case 2:
			categoryType.setImageResource(R.drawable.poster_type_goodideas);
			break;
		case 3:
			categoryType.setImageResource(R.drawable.poster_type_dream);
			break;
		case 4:
			categoryType.setImageResource(R.drawable.poster_type_interests);
			break;
		case 5:
			categoryType.setImageResource(R.drawable.poster_type_plans);
			break;
		case 6:
			categoryType.setImageResource(R.drawable.poster_type_others);
			break;

		default:
			break;
		}
	}

	public class SelectedColor implements OnClickListener {

		private int colorIndex;
		public SelectedColor(int i) {
			colorIndex = i;
		}

		@Override
		public void onClick(View v) {
			if (modeSelected == CHANGE_BACKGROUND) {
				postLayout.setBackgroundColor(Color.parseColor(colors[colorIndex]));
			}
			if (modeSelected == CHANGE_TEXT){
				postText.setTextColor(Color.parseColor(colors[colorIndex]));
				categoryType.setColorFilter(Color.parseColor(colors[colorIndex]), PorterDuff.Mode.SRC_IN);
			}
		}
		
	}

	public RelativeLayout getPostLayout() {
		return postLayout;
	}

	public EditText getPostText() {
		return postText;
	}
	
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		try {
			callCanvas = (NewPostFragmentsSwitchAsyncTaskCallback) activity;
		} catch (ClassCastException e) {
			throw new ClassCastException(activity.toString() + "must implement callCanvas in NewPostFragment. ");
		}
	}
}
