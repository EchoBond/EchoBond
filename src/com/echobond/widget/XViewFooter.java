package com.echobond.widget;

import com.echobond.R;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

@SuppressLint("InflateParams") 
public class XViewFooter extends LinearLayout {

	public static final int STATE_NORMAL = 0;
	public static final int STATE_READY = 1;
	public static final int STATE_LOADING = 2;
	
	private int footerState;
	private RotateAnimation rotateAnimation, loadingAnimation;

	private Context mContext;
	private View mContentView;
	private ImageView loadArrowView;
	private ImageView loadingView;
	private TextView loadStateTextView;
	
	public XViewFooter(Context context) {
		super(context);
		initView(context);
	}

	public XViewFooter(Context context, AttributeSet attrs) {
		super(context, attrs);
		initView(context);
	}
	
	private void initView(Context context) { 
		mContext = context;
		LinearLayout moreView = (LinearLayout)LayoutInflater.from(mContext).inflate(R.layout.pullable_load, null);
		addView(moreView);
		moreView.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
		
		mContentView = moreView.findViewById(R.id.pullup_content);
		loadArrowView = (ImageView)moreView.findViewById(R.id.pull_up_arrow);
		loadingView = (ImageView)moreView.findViewById(R.id.pull_up_loading_view);
		loadStateTextView = (TextView)moreView.findViewById(R.id.pull_up_state_text);
		
		rotateAnimation = (RotateAnimation)AnimationUtils.loadAnimation(context, R.anim.reverse_anim);
		loadingAnimation = (RotateAnimation)AnimationUtils.loadAnimation(context, R.anim.rotating);
		LinearInterpolator lir = new LinearInterpolator();
		rotateAnimation.setInterpolator(lir);
		loadingAnimation.setInterpolator(lir);
	}
	
	public void setState(int state) {
		switch (state) {
		case STATE_NORMAL:
			loadArrowView.setVisibility(View.VISIBLE);
			loadArrowView.clearAnimation();
			loadingView.clearAnimation();
			loadingView.setVisibility(View.GONE);
			loadStateTextView.setText(R.string.hint_pullup_to_load);
			break;
		case STATE_READY:
			loadArrowView.startAnimation(rotateAnimation);
			loadStateTextView.setText(R.string.hint_release_to_load);
			break;
		case STATE_LOADING:
			loadArrowView.clearAnimation();
			loadArrowView.setVisibility(View.INVISIBLE);
			loadingView.setVisibility(View.VISIBLE);
			loadingView.startAnimation(loadingAnimation);
			loadStateTextView.setText(R.string.hint_loading);
			break;
		default:
			break;
		}
		footerState = state;
	}
	
	public int getFooterState() {
		return footerState;
	}

	public void setBottomMargin(int height) {
		if (height < 0) {
			return;
		}
		LinearLayout.LayoutParams mParams = (LinearLayout.LayoutParams)mContentView.getLayoutParams();
		mParams.bottomMargin = height;
		mContentView.setLayoutParams(mParams);
	}
	
	public int getBottomMargin() {
		LinearLayout.LayoutParams mParams = (LinearLayout.LayoutParams)mContentView.getLayoutParams();
		return mParams.bottomMargin;
	}
	
	/**
	 * normal status
	 */
	public void normal() {
		loadStateTextView.setVisibility(View.VISIBLE);
	}
	
	/**
	 * loading status
	 */
//	public void loading() {
//		loadStateTextView.setVisibility(View.GONE);
//	}
	
	/**
	 * hide footer when disable the function of load MORE
	 */
	public void hide() {
		LinearLayout.LayoutParams mParams = (LinearLayout.LayoutParams)mContentView.getLayoutParams();
		mParams.height = 0;
		mContentView.setLayoutParams(mParams);
	}
	
	/**
	 * show footer
	 */
	public void show() {
		LinearLayout.LayoutParams mParams = (LinearLayout.LayoutParams)mContentView.getLayoutParams();
		mParams.height = LayoutParams.WRAP_CONTENT;
		mContentView.setLayoutParams(mParams);
	}
	
}
