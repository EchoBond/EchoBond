package com.echobond.widget;

import com.echobond.R;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.animation.DecelerateInterpolator;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;  
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Scroller;
/**
 * 
 * @author aohuijun
 *
 */
public class XListView extends ListView implements OnScrollListener {

	private static final int SCROLLBACK_HEADER = 0;
	private static final int SCROLLBACK_FOOTER = 1;
	
	private static final int SCROLL_DURATION = 400;
	private static final int PULL_LOAD_MORE_DELTA = 10;	// the height to trigger load
	private static final float OFFSET_RADIO = 1.8f;
	
	private float mLastY = -1;	// save event y
	// the interface to trigger refresh and load more
	private IXListViewListener mListViewListener;
	
	private Scroller scroller;
	private OnScrollListener mScrollListener;
	private int mTotalItemCount;
	private int mScrollBack;
	
	// -- header view
	private XViewHeader mHeaderView;
	private RelativeLayout mHeaderViewContent;	// the whole header
	private int mHeaderViewHeight;	// header view's height
	private boolean mEnablePullRefresh = true;	// can be pull down or not (initial:it can)
	private boolean mPullRefreshing = false;	// is refreshing or not (initial: no)
	
	// -- footer view
	private XViewFooter mFooterView;
	private boolean mEnablePullLoad;	// can be pull up or not
	private boolean mPullLoading;	// is loading or not
	private boolean mIsFooterReady = false;	// load the footer or not
	
	/**
	 * 
	 * @param context
	 */
	public XListView(Context context) {
		super(context);
		initWithContext(context);
	}
	
	public XListView(Context context, AttributeSet attrs) {
		super(context, attrs);
		initWithContext(context);
	}

	public XListView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		initWithContext(context);
	}
	
	private void initWithContext(Context context) {
		scroller = new Scroller(context, new DecelerateInterpolator());
		//	XListView needs the scroll event, and it will dispatch
		//	the event to user's listener (as a proxy). 
		super.setOnScrollListener(this);
		
		// init header view
		mHeaderView = new XViewHeader(context);
		mHeaderViewContent = (RelativeLayout)mHeaderView.findViewById(R.id.pulldown_header_content);
		addHeaderView(mHeaderView);
		// init header height
		mHeaderView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
			
			@SuppressLint("NewApi") @Override
			public void onGlobalLayout() {
				mHeaderViewHeight = mHeaderViewContent.getHeight();	// get the header view's height
				getViewTreeObserver().removeOnGlobalLayoutListener(this);
			}
		});
		
		// init footer view
		mFooterView = new XViewFooter(context);
		
	}

	@Override
	public void setAdapter(ListAdapter adapter) {
		// make sure that XViewFooter is the last footer view, and only add once
		if (mIsFooterReady == false) {
			mIsFooterReady = true;
			addFooterView(mFooterView);
		}
		super.setAdapter(adapter);
	}
	
	@SuppressLint("ClickableViewAccessibility") 
	@Override
	public boolean onTouchEvent(MotionEvent ev) {
		if (mLastY == -1) {
			mLastY = ev.getRawY();
		}
		
		switch (ev.getAction()) {
		case MotionEvent.ACTION_DOWN:
			mLastY = ev.getRawY();
			break;
		case MotionEvent.ACTION_MOVE:
			final float deltaY = ev.getRawY() - mLastY;
			mLastY = ev.getRawY();
			System.out.println("testing: " + getFirstVisiblePosition() + "---->"
					 + getLastVisiblePosition());
			if (getFirstVisiblePosition() == 0 && (mHeaderView.getVisibleHeight() > 0 || deltaY > 0)) {
				// the first item is showing, header has shown or pull down
				updateHeaderHeight(deltaY / OFFSET_RADIO);
				invokeOnScrolling();
			} else if (getLastVisiblePosition() == mTotalItemCount - 1 && (mFooterView.getBottomMargin() > 0 || deltaY < 0)) {
				// last item, already pull up or want to pull up
				updateFooterHeight(-deltaY / OFFSET_RADIO);
			}
			break;
		default:
			mLastY = -1;	// reset
			if (getFirstVisiblePosition() == 0) {
				//	invoke refresh
				if (mEnablePullRefresh && mHeaderView.getVisibleHeight() > mHeaderViewHeight) {
					mPullRefreshing = true;
					mHeaderView.setState(XViewHeader.STATE_REFRESHING);
					if (mListViewListener != null) {
						mListViewListener.onRefresh();	// load more new contents, to be implemented in the activity
					}
				}
				resetHeaderHeight();
			} else if (getLastVisiblePosition() == mTotalItemCount - 1) {
				// invoke load more
				if (mEnablePullLoad && mFooterView.getBottomMargin() > PULL_LOAD_MORE_DELTA && !mPullLoading) {
					startLoadMore();
				}
				resetFooterHeight();
			}
			break;
		}
		return super.onTouchEvent(ev);
	}
	
	/**
	 * enable or disable pull down refresh feature. 
	 * 
	 * @param enable
	 * 
	 */
	public void setPullRefreshEnable(boolean enable) {
		mEnablePullRefresh = enable;
		if (!mEnablePullRefresh) {	// disable, hide the content
			mHeaderViewContent.setVisibility(View.INVISIBLE);
		}else {
			mHeaderViewContent.setVisibility(View.VISIBLE);
		}
	}
	
	/**
	 * enable or disable pull up load more content
	 * 
	 * @param enable
	 * 
	 */
	public void setPullLoadEnable(boolean enable) {
		mEnablePullLoad = enable;
		if (!mEnablePullLoad) {
			mFooterView.hide();
			mFooterView.setOnClickListener(null);
			//	make sure "pull up" do not show a line in bottom when listview with one page
			setFooterDividersEnabled(false);
		} else {
			mPullLoading = false;
			mFooterView.show();
			mFooterView.setState(XViewFooter.STATE_NORMAL);
			//	make sure "pull up" do not show a line in bottom when listview with one page
			setFooterDividersEnabled(true);
			//	both "pull up" and "click" will invoke load more
			mFooterView.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					startLoadMore();
				}
			});
		}
	}
	
	private void startLoadMore() {
		mPullLoading = true;
		mFooterView.setState(XViewFooter.STATE_LOADING);
		if (mListViewListener != null) {
			mListViewListener.onLoadMore();	// load more previous contents, to be implemented in the activity
		}
	}
	
	/**
	 * stop load more, reset footer view
	 */
	public void stopLoadMore() {
		if (mPullLoading == true) {
			mPullLoading = false;
			mFooterView.setState(XViewFooter.STATE_NORMAL);
		}
	}
	
	/**
	 * stop refresh, reset header view
	 */
	public void stopRefresh() {
		if (mPullRefreshing == true) {
			mPullRefreshing = false;
			resetHeaderHeight();
		}
	}
	
	/**
	 * reset header view's height
	 */
	private void resetHeaderHeight() {
		int height = mHeaderView.getVisibleHeight();
		if (height == 0) {	// not visible
			return;
		}
		
		int finalHeight = 0; //	default: scroll back to dismiss header
		//	refreshing and header isn't shown fully, do nothing
		if (mPullRefreshing && height <= mHeaderViewHeight) {
			return;
		}
		// is refreshing, just scroll back to show all the header
		if (mPullRefreshing && height > mHeaderViewHeight) {
			finalHeight = mHeaderViewHeight;
		}
		mScrollBack = SCROLLBACK_HEADER;
		scroller.startScroll(0, height, 0, finalHeight - height, SCROLL_DURATION);
		//  trigger computeScroll
		invalidate();
	}

	//	like a animation to show the effect of dragging the header
	public void updateHeaderHeight(float delta) {
		mHeaderView.setVisibleHeight((int) delta + mHeaderView.getVisibleHeight());
		if (mEnablePullRefresh && !mPullRefreshing) {
			if (mHeaderView.getVisibleHeight() > mHeaderViewHeight) {
				mHeaderView.setState(XViewHeader.STATE_READY);
			} else {
				mHeaderView.setState(XViewHeader.STATE_NORMAL);
			}
		}
		setSelection(0);	//scroll to top each time
	}
	
	private void resetFooterHeight() {
		int bottomMargin = mFooterView.getBottomMargin();
		if (bottomMargin > 0) {
			mScrollBack = SCROLLBACK_FOOTER;
			scroller.startScroll(0, bottomMargin, 0, -bottomMargin, SCROLL_DURATION);
			invalidate();
		}
	}
	
	//	like a animation to show the effect of dragging the footer
	private void updateFooterHeight(float delta) {
		int height = mFooterView.getBottomMargin() + (int) delta;
		if (mEnablePullLoad && !mPullLoading) {
			if (height > PULL_LOAD_MORE_DELTA) { // height enough to invoke load more
				mFooterView.setState(XViewFooter.STATE_READY);
			} else {
				mFooterView.setState(XViewFooter.STATE_NORMAL);
			}
		}
		mFooterView.setBottomMargin(height);
//		setSelection(mTotalItemCount - 1);	// scroll to bottom
	}
	
	public void invokeOnScrolling() {
		if (mScrollListener instanceof OnXScrollListener) {
			OnXScrollListener listener = (OnXScrollListener)mScrollListener;
			listener.onXScrolling(this);
		}
	}
	
	@Override
	public void computeScroll() {
		if (scroller.computeScrollOffset()) {
			if (mScrollBack == SCROLLBACK_HEADER) {
				mHeaderView.setVisibleHeight(scroller.getCurrY());
			} else {
				mFooterView.setBottomMargin(scroller.getCurrY());
			}
			postInvalidate();
			invokeOnScrolling();
		}
		super.computeScroll();
	}
	
	@Override
	public void setOnScrollListener(OnScrollListener l) {
		mScrollListener = l;
//		super.setOnScrollListener(l);
	}
	
	public void setXListViewListener(IXListViewListener l) {
		mListViewListener = l;
	}
	
	@Override
	public void onScroll(AbsListView view, int firstVisibleItem,
			int visibleItemCount, int totalItemCount) {
		//	sent to user's listener
		mTotalItemCount = totalItemCount;
		if (mScrollListener != null) {
			mScrollListener.onScroll(view, firstVisibleItem, visibleItemCount, totalItemCount);
		}
	}

	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {
		if (mScrollListener != null) {
			mScrollListener.onScrollStateChanged(view, scrollState);
		}
	}
	
	/** 
     * you can listen ListView.OnScrollListener or this one. it will invoke 
     * onXScrolling when header/footer scroll back. 
     */
	public interface OnXScrollListener extends OnScrollListener {
		public void onXScrolling(View view);
	}
	
	/** 
	 * implements this interface to get refresh/load more event. 
     */
	public interface IXListViewListener {
		public void onRefresh();
		public void onLoadMore();
	}
}
