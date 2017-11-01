package com.leo.monthtable.monthtablelibrary;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.widget.FrameLayout;
import android.widget.OverScroller;

/**
 * 作者：Leo on 2017/10/3 15:27
 * <p>
 * 描述：
 */

public class CFlingView extends FrameLayout {

    //Fling components
    private OverScroller mScroller;
    private VelocityTracker mVelocityTracker;

    /* Positions of the last motion event */
    private float mLastTouchX, mLastTouchY;
    /* Drag threshold */
    private int mTouchSlop;
    /* Fling Velocity */
    private int mMaximumVelocity, mMinimumVelocity;
    /* Drag Lock */
    private boolean mDragging = false;
    private int mMinimumFlingVelocity = 0;
    private CTableView ctableView;

    public void setCTableView(CTableView ctableView) {
        this.ctableView = ctableView;
    }

    public CFlingView(@NonNull Context context) {
        this(context, null);
    }

    public CFlingView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    private void init(Context context) {
        mScroller = new OverScroller(context);
        mVelocityTracker = VelocityTracker.obtain();
        //Get system constants for touch thresholds
        mTouchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
        mMinimumFlingVelocity = ViewConfiguration.get(context).getScaledMinimumFlingVelocity();
        mMaximumVelocity = ViewConfiguration.get(context).getScaledMaximumFlingVelocity();
        mMinimumVelocity = ViewConfiguration.get(context).getScaledMinimumFlingVelocity();
    }

    /*
     * Override the measureChild... implementations to guarantee that the child view
     * gets measured to be as large as it wants to be.  The default implementation will
     * force some children to be only as large as this view.
     */
    @Override
    protected void measureChild(View child, int parentWidthMeasureSpec, int parentHeightMeasureSpec) {
        int childWidthMeasureSpec;
        int childHeightMeasureSpec;

        childWidthMeasureSpec = MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED);
        childHeightMeasureSpec = MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED);

        child.measure(childWidthMeasureSpec, childHeightMeasureSpec);
    }

    @Override
    protected void measureChildWithMargins(View child, int parentWidthMeasureSpec, int widthUsed,
                                           int parentHeightMeasureSpec, int heightUsed) {
        final MarginLayoutParams lp = (MarginLayoutParams) child.getLayoutParams();

        final int childWidthMeasureSpec = MeasureSpec.makeMeasureSpec(
                lp.leftMargin + lp.rightMargin, MeasureSpec.UNSPECIFIED);
        final int childHeightMeasureSpec = MeasureSpec.makeMeasureSpec(
                lp.topMargin + lp.bottomMargin, MeasureSpec.UNSPECIFIED);

        child.measure(childWidthMeasureSpec, childHeightMeasureSpec);
    }

    @Override
    public void computeScroll() {
//        Log.e("LEO666666", "1111111:"+mScroller.computeScrollOffset());
        if (mScroller.computeScrollOffset()) {
            int oldX = getScrollX();
            int oldY = getScrollY();
            int x = mScroller.getCurrX();
            int y = mScroller.getCurrY();

            if (getChildCount() > 0) {
                View child = getChildAt(0);
                x = clamp(x, getWidth() - getPaddingRight() - getPaddingLeft(), child.getWidth());
                y = clamp(y, getHeight() - getPaddingBottom() - getPaddingTop(), child.getHeight());
                if (x != oldX || y != oldY) {
//                    scrollTo(x, y);
                    scrollBy(x - oldX, y - oldY);
                    if (listener != null)
                        listener.onScrollDistance(x - oldX, y - oldY);

                    Log.e("LEO666666", "1111111");

                    // Keep on drawing until the animation has finished.
                    postInvalidate();
                }
            }
        }

//        if (mScroller.isFinished()) {
//            // Snap to day after fling is finished.
//            Log.e("LEO666666", "自动归位...");
//            goToNearestOrigin();
//        }

    }

    private void goToNearestOrigin() {
        int scollXDistance = ctableView.getScollXDistance();
        int scollYDistance = ctableView.getScollYDistance();
        Log.e("LEO666666", "自动归位...scollXDistance：" + scollXDistance);
        Log.e("LEO666666", "自动归位...scollYDistance：" + scollYDistance);
        mScroller.startScroll(mScroller.getCurrX(), mScroller.getCurrY(), scollXDistance, scollYDistance, 500);
        invalidate();
    }

    //Override scrollTo to do bounds checks on any scrolling request
    @Override
    public void scrollTo(int x, int y) {
        // we rely on the fact the View.scrollBy calls scrollTo.
        if (getChildCount() > 0) {
            View child = getChildAt(0);
            x = clamp(x, getWidth() - getPaddingRight() - getPaddingLeft(), child.getWidth());
            y = clamp(y, getHeight() - getPaddingBottom() - getPaddingTop(), child.getHeight());
            if (x != getScrollX() || y != getScrollY()) {
                super.scrollTo(x, y);
            }
        }
    }

    /*
     * Utility method to initialize the Scroller and start redrawing
     */
    public void fling(int velocityX, int velocityY) {
        if (getChildCount() > 0) {
            int height = getHeight() - getPaddingBottom() - getPaddingTop();
            int width = getWidth() - getPaddingLeft() - getPaddingRight();
            int bottom = getChildAt(0).getHeight();
            int right = getChildAt(0).getWidth();

            mScroller.fling(getScrollX(), getScrollY(), (int) (velocityX / 2f), (int) (velocityY / 2f),
                    0, Math.max(0, right - width),
                    0, Math.max(0, bottom - height));

            Log.e("LEO666666", "222222222");

            invalidate();
        }
    }

    /*
     * Utility method to assist in doing bounds checking
     */
    private int clamp(int n, int my, int child) {
        if (my >= child || n < 0) {
            /* my >= child is this case:
             *                    |--------------- me ---------------|
             *     |------ child ------|
             * or
             *     |--------------- me ---------------|
             *            |------ child ------|
             * or
             *     |--------------- me ---------------|
             *                                  |------ child ------|
             *
             * n < 0 is this case:
             *     |------ me ------|
             *                    |-------- child --------|
             *     |-- mScrollX --|
             */
            return 0;
        }
        if ((my + n) > child) {
            /* this case:
             *                    |------ me ------|
             *     |------ child ------|
             *     |-- mScrollX --|
             */
            return child - my;
        }
        return n;
    }

    /*
     * Monitor touch events passed down to the children and
     * intercept as soon as it is determined we are dragging.  This
     * allows child views to still receive touch events if they are
     * interactive (i.e. Buttons)
     */
    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                //Stop any flinging in progress
                if (!mScroller.isFinished()) {
                    mScroller.abortAnimation();
                }
                //Reset the velocity tracker
                mVelocityTracker.clear();
                mVelocityTracker.addMovement(event);
                //Save the initial touch point
                mLastTouchX = event.getX();
                mLastTouchY = event.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                final float x = event.getX();
                final float y = event.getY();
                final int yDiff = (int) Math.abs(y - mLastTouchY);
                final int xDiff = (int) Math.abs(x - mLastTouchX);
                //Verify that either difference is enough to be a drag
                if (yDiff > mTouchSlop || xDiff > mTouchSlop) {
                    mDragging = true;
                    mVelocityTracker.addMovement(event);
                    //Start capturing events ourselves
                    return true;
                }
                break;
            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_UP:
                mDragging = false;
                mVelocityTracker.clear();
                break;
        }

        return super.onInterceptTouchEvent(event);
    }

    /*
     * Feed all touch events we receive to the detector for
     * processing.
     */
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        mVelocityTracker.addMovement(event);

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                // We've already stored the initial point,
                // but if we got here a child view didn't capture
                // the event, so we need to.

//                goToNearestOrigin();

                return true;
            case MotionEvent.ACTION_MOVE:
                final float x = event.getX();
                final float y = event.getY();
                float deltaY = mLastTouchY - y;
                float deltaX = mLastTouchX - x;
                //Check for slop on direct events
                if (!mDragging && (Math.abs(deltaY) > mTouchSlop || Math.abs(deltaX) > mTouchSlop)) {
                    mDragging = true;
                }

                if (mDragging) {
                    //Scroll the view
                    scrollBy((int) deltaX, (int) deltaY);
                    if (listener != null)
                        listener.onScrollDistance((int) deltaX, (int) deltaY);
                    //Update the last touch event
                    mLastTouchX = x;
                    mLastTouchY = y;
                }
                break;
            case MotionEvent.ACTION_CANCEL:
                mDragging = false;
                //Stop any flinging in progress
                if (!mScroller.isFinished()) {
                    mScroller.abortAnimation();
                }
                break;
            case MotionEvent.ACTION_UP:
                if (mDragging) {
                    mDragging = false;
                    // Compute the current velocity and start a fling if it is above
                    // the minimum threshold.
                    mVelocityTracker.computeCurrentVelocity(1000, mMaximumVelocity);
                    int velocityX = (int) mVelocityTracker.getXVelocity();
                    int velocityY = (int) mVelocityTracker.getYVelocity();
                    if (Math.abs(velocityX) > mMinimumVelocity || Math.abs(velocityY) > mMinimumVelocity) {
                        fling(-velocityX, -velocityY);
                    }
                }
                break;
        }
        return super.onTouchEvent(event);
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    //
    //    滑动距离监听
    //
    ////////////////////////////////////////////////////////////////////////////////////////////////

    private OnScrollDistanceListener listener;

    public void setOnScrollDistanceListener(OnScrollDistanceListener listener) {
        this.listener = listener;
    }

    public interface OnScrollDistanceListener {

        void onScrollDistance(int dix, int diy);

    }

}
