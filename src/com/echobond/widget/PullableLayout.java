package com.echobond.widget;

import java.util.Timer;
import java.util.TimerTask;

import com.echobond.R;
import com.echobond.intf.Pullable;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

@SuppressLint("HandlerLeak") 
public class PullableLayout extends RelativeLayout {

	private OnPullableListener onPullableListener;

	public static final int INIT = 0;
	public static final int RELEASE_TO_REFRESH = 1;
	public static final int REFRESHING = 2;
	public static final int RELEASE_TO_LOAD = 3;
	public static final int LOADING = 4;
	public static final int DONE = 5;
	private int state = INIT;
	
	public static final int SUCCEED = 0;
	public static final int FAIL = 1;
	
	private RotateAnimation rotateAnimation, refreshingAnimation;
	
	//	header
	private View refreshView;
	private ImageView refreshArrowView;			//	arrow
	private ImageView refreshingView;
	private ImageView refreshStateImageView;	//	result image
	private TextView refreshStateTextView;		//	result text
	
	//	footer
	private View loadMoreView;
	private ImageView loadArrowView;			//	arrow
	private ImageView loadingView;
	private ImageView loadStateImageView;		//	result image
	private TextView loadStateTextView;			//	result text
	
	private View pullableView;
	
	private int multiEvents;
	private boolean canPullDown = true;
	private boolean canPullUp = true;
	private boolean isLayout = false;
	private boolean isTouch = false;
	
	private float downY, lastY;
	private float pullDownY = 0;
	private float pullUpY = 0;
	private float refreshDist = 200;
	private float loadMoreDist = 200;
	public float MOVE_SPEED = 8;
	private float ratio = 2;
	
	private LayoutTimer timer;
	
	Handler updateHandler = new Handler() {
		
		@Override
		public void handleMessage(Message msg) {
			MOVE_SPEED = (float)(8 + 5 * Math.tan((pullDownY + Math.abs(pullUpY)) * Math.PI / 2 / getMeasuredHeight()));
			if (!isTouch) {
				if (state == REFRESHING && pullDownY <= refreshDist) {
					pullDownY = refreshDist;
					timer.cancel();
				} else if (state == LOADING && -pullUpY <= loadMoreDist) {
					pullUpY = -loadMoreDist;
					timer.cancel();
				}
			}
			
			if (pullDownY > 0) {
				pullDownY -= MOVE_SPEED;
			} else if (pullDownY < 0) {
				pullDownY = 0;
				refreshArrowView.clearAnimation();
				if (state != REFRESHING && state != LOADING) {
					changeState(INIT);
				}
				timer.cancel();
			}
			if (pullUpY < 0) {
				pullUpY += MOVE_SPEED;
			} else if (pullUpY > 0) {
				pullUpY = 0;
				loadArrowView.clearAnimation();
				if (state != REFRESHING && state != LOADING) {
					changeState(INIT);
				}
				timer.cancel();
			}
			requestLayout();
			
			if (pullDownY + Math.abs(pullUpY) == 0) {
				timer.cancel();
			}
		}
	};
	
	public PullableLayout(Context context, AttributeSet attrs,
			int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		initView(context);
	}

	public PullableLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
		initView(context);
	}

	public PullableLayout(Context context) {
		super(context);
		initView(context);
	}

	private void initView(Context context) {
		timer = new LayoutTimer(updateHandler);
		rotateAnimation = (RotateAnimation) AnimationUtils.loadAnimation(context, R.anim.reverse_anim);
		refreshingAnimation = (RotateAnimation) AnimationUtils.loadAnimation(context, R.anim.rotating);
		LinearInterpolator lir = new LinearInterpolator();
		rotateAnimation.setInterpolator(lir);
		refreshingAnimation.setInterpolator(lir);
	}
	
	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		if (!isLayout) {
			refreshView = getChildAt(0);
			pullableView = getChildAt(1);
			loadMoreView = getChildAt(2);
			isLayout = true;
			initView();
			refreshDist = ((ViewGroup) refreshView).getChildAt(0).getMeasuredHeight();
			loadMoreDist = ((ViewGroup) loadMoreView).getChildAt(0).getMeasuredHeight();
		}
		refreshView.layout(0, (int)(pullDownY + pullUpY) - refreshView.getMeasuredHeight(), refreshView.getMeasuredWidth(), (int)(pullDownY + pullUpY));
		pullableView.layout(0, (int)(pullDownY + pullUpY), pullableView.getMeasuredWidth(), (int)(pullDownY + pullUpY) + pullableView.getMeasuredHeight());
		loadMoreView.layout(0, (int)(pullDownY + pullUpY) + pullableView.getMeasuredHeight(), loadMoreView.getMeasuredWidth(), (int)(pullDownY + pullUpY) + pullableView.getMeasuredHeight() + loadMoreView.getMeasuredHeight());
	}
	
	private void initView() {
		//	init refresh view
		refreshArrowView = (ImageView)refreshView.findViewById(R.id.pull_down_arrow);
		refreshingView = (ImageView)refreshView.findViewById(R.id.pull_down_refreshing_view);
		refreshStateImageView = (ImageView)refreshView.findViewById(R.id.pull_down_state_image);
		refreshStateTextView = (TextView)refreshView.findViewById(R.id.pull_down_state_text);
		//	init load view
		loadArrowView = (ImageView)loadMoreView.findViewById(R.id.pull_up_arrow);
		loadingView = (ImageView)loadMoreView.findViewById(R.id.pull_up_loading_view);
		loadStateImageView = (ImageView)loadMoreView.findViewById(R.id.pull_up_state_image);
		loadStateTextView = (TextView)loadMoreView.findViewById(R.id.pull_up_state_text);
	}
	
	@Override
	public boolean dispatchTouchEvent(MotionEvent ev) {
		switch (ev.getActionMasked()) {
		case MotionEvent.ACTION_DOWN:
			downY = ev.getY();
			lastY = downY;
			timer.cancel();
			multiEvents = 0;
			releasePull();
			break;
		case MotionEvent.ACTION_POINTER_DOWN:
		case MotionEvent.ACTION_POINTER_UP:
			multiEvents = -1;
			break;
		case MotionEvent.ACTION_MOVE:
			if (multiEvents == 0) {
				if (pullDownY > 0 || (((Pullable)pullableView).canPullDown() && canPullDown && state != LOADING)) {
					pullDownY = pullDownY + (ev.getY() - lastY) / ratio;
					if (pullDownY <= 0) {
						pullDownY = 0;
						canPullDown = false;
						canPullUp = true;
					}
					if (pullDownY > getMeasuredHeight()) {
						pullDownY = getMeasuredHeight();
					}
					if (state == REFRESHING) {
						isTouch = true;
					}
				} else if (pullUpY < 0 || (((Pullable)pullableView).canPullUp() && canPullUp && state != REFRESHING)) {
					pullUpY = pullUpY + (ev.getY() - lastY) / ratio;
					if (pullUpY >= 0) {
						pullUpY = 0;
						canPullDown = true;
						canPullUp = false;
					}
					if (pullUpY < -getMeasuredHeight()) {
						pullUpY = -getMeasuredHeight();
					}
					if (state == LOADING) {
						isTouch = true;
					}
				} else {
					releasePull();
				}
			} else {
				multiEvents = 0;
			}
			lastY = ev.getY();
			ratio = (float)(2 + 2 * Math.tan(Math.PI / 2 / getMeasuredHeight() * (pullDownY + Math.abs(pullUpY))));
			
			if (pullDownY > 0 || pullUpY < 0) {
				requestLayout();
			}
			if (pullDownY > 0) {
				if (pullDownY <= refreshDist && (state == RELEASE_TO_REFRESH || state == DONE)) {
					changeState(INIT);
				}
				if (pullDownY > refreshDist && state == INIT) {
					changeState(RELEASE_TO_REFRESH);
				}
			} else if (pullUpY < 0) {
				if (-pullUpY <= loadMoreDist && (state == RELEASE_TO_LOAD || state == DONE)) {
					changeState(INIT);
				}
				if (-pullUpY > loadMoreDist && state == INIT) {
					changeState(RELEASE_TO_LOAD);
				}
			}
			
			if ((pullDownY + Math.abs(pullUpY)) > 8) {
				ev.setAction(MotionEvent.ACTION_CANCEL);
			}
			break;
		case MotionEvent.ACTION_UP:
			if (pullDownY > refreshDist || -pullUpY > loadMoreDist) {
				isTouch = false;
			}
			if (state == RELEASE_TO_REFRESH) {
				changeState(REFRESHING);
				if (onPullableListener != null) {
					onPullableListener.onRefresh(this);
				}
			} else if (state == RELEASE_TO_LOAD) {
				changeState(LOADING);
				if (onPullableListener != null) {
					onPullableListener.onLoadMore(this);
				}
			}
			hide();
			break;
		default:
			break;
		}
		super.dispatchTouchEvent(ev);
		return true;
	}
	
	private void releasePull() {
		canPullDown = true;
		canPullUp = true;
	}
	
	private void hide() {
		timer.schedule(5);
	}
	
	private void changeState(int to) {
		state = to;
		switch (state) {
		case INIT:
			//	init pull down
			refreshStateImageView.setVisibility(View.GONE);
			refreshStateTextView.setText(R.string.hint_pull_to_refresh);
			refreshArrowView.setVisibility(View.VISIBLE);
			refreshArrowView.clearAnimation();
			// init pull up
			loadStateImageView.setVisibility(View.GONE);
			loadStateTextView.setText(R.string.hint_pullup_to_load);
			loadArrowView.setVisibility(View.VISIBLE);
			loadArrowView.clearAnimation();
			break;
		case RELEASE_TO_REFRESH:
			refreshStateTextView.setText(R.string.hint_release_to_refresh);
			refreshArrowView.startAnimation(rotateAnimation);
			break;
		case REFRESHING:
			refreshArrowView.clearAnimation();
			refreshArrowView.setVisibility(View.INVISIBLE);
			refreshingView.setVisibility(View.VISIBLE);
			refreshingView.startAnimation(refreshingAnimation);
			refreshStateTextView.setText(R.string.hint_refreshing);
			break;
		case RELEASE_TO_LOAD:
			loadStateTextView.setText(R.string.hint_release_to_load);
			loadArrowView.startAnimation(rotateAnimation);
			break;
		case LOADING:
			loadArrowView.clearAnimation();
			loadArrowView.setVisibility(View.INVISIBLE);
			loadingView.setVisibility(View.VISIBLE);
			loadingView.startAnimation(refreshingAnimation);
			loadStateTextView.setText(R.string.hint_loading);
			break;
		case DONE:
			break;
		default:
			break;
		}
	}
	
	public class LayoutTimer {
		private Handler handler;
		private Timer timer;
		private ScrollBackTask task;

		public LayoutTimer(Handler handler) {
			this.handler = handler;
			timer = new Timer();
		}

		public void schedule(long period) {
			if (task != null) {
				task.cancel();
				task = null;
			}
			task = new ScrollBackTask(handler);
			timer.schedule(task, 0, period);
		}

		public void cancel() {
			if (task != null) {
				task.cancel();
				task = null;
			}
		}
		
		class ScrollBackTask extends TimerTask {
			private Handler handler;
			
			public ScrollBackTask(Handler handler) {
				this.handler = handler;
			}

			@Override
			public void run() {
				handler.obtainMessage().sendToTarget();
			}
			
		}
	}
	
	public void refreshFinished(int refreshResult) {
		refreshingView.clearAnimation();
		refreshingView.setVisibility(View.GONE);
		refreshStateImageView.setVisibility(View.VISIBLE);
		switch (refreshResult) {
		case SUCCEED:
			refreshStateImageView.setImageDrawable(getResources().getDrawable(R.drawable.pullable_succeed));
			refreshStateTextView.setText(R.string.hint_refresh_succeed);
			break;
		case FAIL:
		default:
			refreshStateImageView.setImageDrawable(getResources().getDrawable(R.drawable.pullable_failed));
			refreshStateTextView.setText(R.string.hint_refresh_fail);
			break;
		}
		if (pullDownY > 0) {
			new Handler() {
				@Override
				public void handleMessage(Message msg) {
					changeState(DONE);
					hide();
				};
			}.sendEmptyMessageDelayed(0, 1000);
		} else {
			changeState(DONE);
			hide();
		}
	}
	
	public void loadMoreFinished(int loadResult) {
		loadingView.clearAnimation();
		loadingView.setVisibility(View.GONE);
		loadStateImageView.setVisibility(View.VISIBLE);
		switch (loadResult) {
		case SUCCEED:
			loadStateImageView.setImageDrawable(getResources().getDrawable(R.drawable.pullable_succeed));
			loadStateTextView.setText(R.string.hint_load_succeed);
			break;
		case FAIL:
		default:
			loadStateImageView.setImageDrawable(getResources().getDrawable(R.drawable.pullable_failed));
			loadStateTextView.setText(R.string.hint_load_fail);
			break;
		}
		if (pullUpY < 0) {
			new Handler() {
				@Override
				public void handleMessage(Message msg) {
					changeState(DONE);
					hide();
				};
			}.sendEmptyMessageDelayed(0, 1000);
		} else {
			changeState(DONE);
			hide();
		}
	}
	
//	private class AutoRefreshAndLoadTask extends AsyncTask<Integer, Float, String> {
//
//		@Override
//		protected String doInBackground(Integer... params) {
//			while (pullDownY < 4 / 3 * refreshDist) {
//				pullDownY += MOVE_SPEED;
//				publishProgress(pullDownY);
//				try {
//					Thread.sleep(params[0]);
//				} catch (InterruptedException e) {
//					e.printStackTrace();
//				}
//			}
//			return null;
//		}
//
//		@Override
//		protected void onPostExecute(String result) {
//			changeState(REFRESHING);
//			if (onPullableListener != null) {
//				onPullableListener.onRefresh(PullToRefreshLayout.this);
//			}
//			hide();
//		}
//
//		@Override
//		protected void onProgressUpdate(Float... values) {
//			if (pullDownY > refreshDist) {
//				changeState(RELEASE_TO_REFRESH);
//			}
//			requestLayout();
//		}
//		
//	}
//	
//	public void autoRefresh() {
//		AutoRefreshAndLoadTask task = new AutoRefreshAndLoadTask();
//		task.execute(20);
//	}
//	
//	public void autoLoad() {
//		pullUpY = -loadMoreDist;
//		requestLayout();
//		changeState(LOADING);
//		if (onPullableListener != null) {
//			onPullableListener.onLoadMore(this);
//		}
//	}
	
	public void setOnPullableListener(OnPullableListener listener) {
		onPullableListener = listener;
	}
	
	public interface OnPullableListener {
		void onRefresh(PullableLayout pullableLayout);
		void onLoadMore(PullableLayout pullableLayout);
	}
	
}
