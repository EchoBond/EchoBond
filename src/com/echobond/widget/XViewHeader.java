package com.echobond.widget;

import com.echobond.R;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

@SuppressLint("InflateParams") public class XViewHeader extends LinearLayout {

	public static final int STATE_NORMAL = 0;
	public static final int STATE_READY = 1;
	public static final int STATE_REFRESHING = 2;
	private int mState = STATE_NORMAL;
	
	public final int ROTATE_ANIM_DURATION = 180;
	private Animation mRotateUpAnim, mRotateDownAnim;
	
	private LinearLayout mContainer;
	private ProgressBar mProgressBar;
	private TextView mHintTextView;
	
	public XViewHeader(Context context) {
		super(context);
		initView(context);
	}
	
	/**
	 * 
	 * @param context
	 * @param attrs
	 */
	public XViewHeader(Context context, AttributeSet attrs) {
		super(context, attrs);
		initView(context);
	}

	private void initView(Context context) { 
		//	set the initial height as 0
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, 0);
		mContainer = (LinearLayout)LayoutInflater.from(context).inflate(R.layout.refresh_pull_down, null);
		addView(mContainer, params);
		setGravity(Gravity.BOTTOM);
		
		mHintTextView = (TextView)findViewById(R.id.pulldown_header_textview);
		mProgressBar = (ProgressBar)findViewById(R.id.pulldown_header_loading);
		
//		mRotateUpAnim = new RotateAnimation(0.0f, -180.0f, 
//				Animation.RELATIVE_TO_SELF, 0.5f, 
//				Animation.RELATIVE_TO_SELF, 0.5f);
//		mRotateDownAnim = new RotateAnimation(-180.0f, 0.0f, 
//				Animation.RELATIVE_TO_SELF, 0.5f, 
//				Animation.RELATIVE_TO_SELF, 0.5f);
//		mRotateUpAnim.setDuration(ROTATE_ANIM_DURATION);
//		mRotateDownAnim.setDuration(ROTATE_ANIM_DURATION);
//		mRotateUpAnim.setFillAfter(true);
//		mRotateDownAnim.setFillAfter(true);
	}

	public void setState(int state) {
		if (state == mState) {
			return;
		}
		
		if (state == STATE_REFRESHING) {	// get the loading status
			mProgressBar.setVisibility(View.VISIBLE);
		} else {
			mProgressBar.setVisibility(View.INVISIBLE);
		}
		
		switch (state) {
		case STATE_NORMAL:
			mHintTextView.setText(R.string.hint_xview_header_normal);
			break;
		case STATE_READY:
			if (mState != STATE_READY) {
				mHintTextView.setText(R.string.hint_xview_header_ready);
			}
			break;
		case STATE_REFRESHING:
			mHintTextView.setText(R.string.hint_xview_header_loading);
			break;
		default:
			break;
		}
		mState = state;
	}
	
	public void setVisibleHeight(int height) {
		if (height < 0) {
			height = 0;
		}
		LinearLayout.LayoutParams mParams = (LinearLayout.LayoutParams)mContainer.getLayoutParams();
		mParams.height = height;
		mContainer.setLayoutParams(mParams);
	}
	
	public int getVisibleHeight() {
		return mContainer.getLayoutParams().height;
	}
	
}
