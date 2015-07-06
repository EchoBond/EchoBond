package com.echobond.widget;

import com.echobond.R;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

@SuppressLint("InflateParams") 
public class XViewHeader extends LinearLayout {

	public static final int STATE_NORMAL = 0;
	public static final int STATE_READY = 1;
	public static final int STATE_REFRESHING = 2;
	private int mState = STATE_NORMAL;
	
	private RotateAnimation rotateAnimation, refreshingAnimation;
	
	private LinearLayout mContainer;
	private ImageView refreshArrowView;
	private ImageView refreshingView;
	private TextView refreshStateTextView;
	
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
		mContainer = (LinearLayout)LayoutInflater.from(context).inflate(R.layout.pullable_refresh, null);
		addView(mContainer, params);
		setGravity(Gravity.BOTTOM);
		
		refreshArrowView = (ImageView)findViewById(R.id.pull_down_arrow);
		refreshingView = (ImageView)findViewById(R.id.pull_down_refreshing_view);
		refreshStateTextView = (TextView)findViewById(R.id.pull_down_state_text);
		
		rotateAnimation = (RotateAnimation)AnimationUtils.loadAnimation(context, R.anim.reverse_anim);
		refreshingAnimation = (RotateAnimation)AnimationUtils.loadAnimation(context, R.anim.rotating);
		LinearInterpolator lir = new LinearInterpolator();
		rotateAnimation.setInterpolator(lir);
		refreshingAnimation.setInterpolator(lir);
	}

	public void setState(int state) {
		if (state == mState) {
			return;
		}
		
		switch (state) {
		case STATE_NORMAL:
			refreshArrowView.setVisibility(View.VISIBLE);
			refreshArrowView.clearAnimation();
			refreshingView.clearAnimation();
			refreshingView.setVisibility(View.GONE);
			refreshStateTextView.setText(R.string.hint_pull_to_refresh);
			break;
		case STATE_READY:
			refreshArrowView.startAnimation(rotateAnimation);
			refreshStateTextView.setText(R.string.hint_release_to_refresh);
			break;
		case STATE_REFRESHING:
			refreshArrowView.clearAnimation();
			refreshArrowView.setVisibility(View.INVISIBLE);
			refreshingView.setVisibility(View.VISIBLE);
			refreshingView.startAnimation(refreshingAnimation);
			refreshStateTextView.setText(R.string.hint_refreshing);
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
