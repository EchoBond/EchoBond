package com.echobond.fragment;

import com.echobond.R;
import com.echobond.activity.NewPostPage;
import com.echobond.application.MyApp;
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
	private ImageView[] colorViews = new ImageView[12];
	private int[] colorButtons = {R.id.thought_poster_color0, R.id.thought_poster_color1, R.id.thought_poster_color2, 
								R.id.thought_poster_color3, R.id.thought_poster_color4, R.id.thought_poster_color5, 
								R.id.thought_poster_color6, R.id.thought_poster_color7, R.id.thought_poster_color8, 
								R.id.thought_poster_color9, R.id.thought_poster_colora, R.id.thought_poster_colorb};

	private ImageView postChange, textChange, categoryType, switchCanvas;
	private EditText postText;
	private int modeSelected;
	
	private final static int CHANGE_BACKGROUND = 0;
	private final static int CHANGE_TEXT = 1;
	
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
		
		for (int i = 0; i < colors.length; i++) {
			colorViews[i] = (ImageView)postView.findViewById(colorButtons[i]);
			colorViews[i].setOnClickListener(new SelectedColor(i));
		}
		colorViews[6].setBackgroundResource(R.drawable.color_selection_background);

		categoryType.setColorFilter(Color.parseColor(colors[MyApp.COLOR_BLACK]), PorterDuff.Mode.SRC_IN);
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
			for (int i = 0; i < colors.length; i++) {
				colorViews[i].setBackgroundResource(0);
			}
			colorViews[colorIndex].setBackgroundResource(R.drawable.color_selection_background);
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
