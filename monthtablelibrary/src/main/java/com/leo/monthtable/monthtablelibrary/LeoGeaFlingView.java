package com.leo.monthtable.monthtablelibrary;

import android.content.Context;
import android.graphics.PointF;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.v4.view.GestureDetectorCompat;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.animation.FastOutLinearInInterpolator;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.ViewConfiguration;
import android.widget.FrameLayout;
import android.widget.OverScroller;


/**
 * 作者：Leo on 2017/10/3 15:27
 * <p>
 * 描述：
 */

public class LeoGeaFlingView extends FrameLayout {


    public LeoGeaFlingView(Context context) {
        this(context, null);
    }

    public LeoGeaFlingView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        mGestureDetector = new GestureDetectorCompat(getContext(), mGestureListener);
        mScroller = new OverScroller(getContext(), new FastOutLinearInInterpolator());
        mMinimumFlingVelocity = ViewConfiguration.get(getContext()).getScaledMinimumFlingVelocity();
        mScaledTouchSlop = ViewConfiguration.get(getContext()).getScaledTouchSlop();
        mScaleDetector = new ScaleGestureDetector(getContext(), new ScaleGestureDetector.OnScaleGestureListener() {
            @Override
            public void onScaleEnd(ScaleGestureDetector detector) {
                mIsZooming = false;
            }

            @Override
            public boolean onScaleBegin(ScaleGestureDetector detector) {
                mIsZooming = true;
                goToNearestOrigin();
                return true;
            }

            @Override
            public boolean onScale(ScaleGestureDetector detector) {
//                mNewHourHeight = Math.round(mHourHeight * detector.getScaleFactor());
//                invalidate();
                return true;
            }
        });
    }

    private OnCFlingViewScrollListener listener;

    public void setOnCFlingViewScrollListener(OnCFlingViewScrollListener listener) {
        this.listener = listener;
    }

    public interface OnCFlingViewScrollListener {

        void onScroll(int disX, int disY);

        void onFling(int disX, int disY);

        void onStop();
    }

    //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    //
    //
    //
    //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    private Direction mCurrentFlingDirection = Direction.NONE;
    private Direction mCurrentScrollDirection = Direction.NONE;

    private enum Direction {
        NONE, LEFT, RIGHT, VERTICAL
    }

    private CTableView cTableView;

    private GestureDetectorCompat mGestureDetector;
    private OverScroller mScroller;
    private ScaleGestureDetector mScaleDetector;
    public PointF mCurrentOrigin = new PointF(0f, 0f);
    private float mXScrollingSpeed = 1f;
    private boolean mIsZooming;
    private int mScaledTouchSlop = 0;
    private int mMinimumFlingVelocity = 0;

    public void setcTableView(CTableView cTableView) {
        this.cTableView = cTableView;
    }

    private final GestureDetector.SimpleOnGestureListener mGestureListener = new GestureDetector.SimpleOnGestureListener() {

        @Override
        public boolean onDown(MotionEvent e) {
//            goToNearestOrigin();
            return true;
        }

        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
            // Check if view is zoomed.
            if (mIsZooming)
                return true;

            switch (mCurrentScrollDirection) {
                case NONE: {
                    // Allow scrolling only in one direction.
                    if (Math.abs(distanceX) > Math.abs(distanceY)) {
                        if (distanceX > 0) {
                            mCurrentScrollDirection = Direction.LEFT;
                        } else {
                            mCurrentScrollDirection = Direction.RIGHT;
                        }
                    } else {
                        mCurrentScrollDirection = Direction.VERTICAL;
                    }
                    break;
                }
                case LEFT: {
                    // Change direction if there was enough change.
                    if (Math.abs(distanceX) > Math.abs(distanceY) && (distanceX < -mScaledTouchSlop)) {
                        mCurrentScrollDirection = Direction.RIGHT;
                    }
                    break;
                }
                case RIGHT: {
                    // Change direction if there was enough change.
                    if (Math.abs(distanceX) > Math.abs(distanceY) && (distanceX > mScaledTouchSlop)) {
                        mCurrentScrollDirection = Direction.LEFT;
                    }
                    break;
                }
            }
            // Calculate the new origin after scroll.
            switch (mCurrentScrollDirection) {
                case LEFT:
                case RIGHT:
                    //TODO SCROLL
                    if (listener != null) {
                        listener.onScroll((int) distanceX, 0);
                    }
                    Log.e("LEO6666666", "滑动...distanceX：" + distanceX);

                    scrollTo((int) mCurrentOrigin.x, (int) mCurrentOrigin.y);

//                    mCurrentOrigin.x -= distanceX * mXScrollingSpeed;
//                    ViewCompat.postInvalidateOnAnimation(LeoGeaFlingView.this);
                    break;
                case VERTICAL:
                    //TODO SCROLL
                    if (listener != null) {
                        listener.onScroll(0, (int) distanceY);
                    }

                    scrollTo((int) mCurrentOrigin.x, (int) mCurrentOrigin.y);
//                    mCurrentOrigin.y -= distanceY;
//                    ViewCompat.postInvalidateOnAnimation(LeoGeaFlingView.this);
                    break;
            }
            return true;
        }

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            if (mIsZooming)
                return true;

            mScroller.forceFinished(true);
            mCurrentFlingDirection = mCurrentScrollDirection;
            switch (mCurrentFlingDirection) {
                case LEFT:
                case RIGHT:

//                    fling(int startX, int startY, int velocityX, int velocityY, int minX, int maxX, int minY, int maxY)

                    mScroller.fling((int) mCurrentOrigin.x, (int) mCurrentOrigin.y,
                            -(int) (velocityX * mXScrollingSpeed), 0,
                            Integer.MIN_VALUE, Integer.MAX_VALUE,
                            Integer.MIN_VALUE, Integer.MAX_VALUE);
                    break;
                case VERTICAL:
                    mScroller.fling((int) mCurrentOrigin.x, (int) mCurrentOrigin.y,
                            0, -(int) velocityY,
                            Integer.MIN_VALUE, Integer.MAX_VALUE,
                            Integer.MIN_VALUE, Integer.MAX_VALUE);
                    break;
            }
            ViewCompat.postInvalidateOnAnimation(LeoGeaFlingView.this);
            return true;
        }
    };

    private void goToNearestOrigin() {
        int scollXDistance = cTableView.getScollXDistance();
        int scollYDistance = cTableView.getScollYDistance();

        Log.e("CTABLE", "自动归位...scollXDistance：" + scollXDistance);
        Log.e("CTABLE", "自动归位...scollYDistance：" + scollYDistance);

        mScroller.forceFinished(true);
        mScroller.startScroll((int) mCurrentOrigin.x, (int) mCurrentOrigin.y
                , scollXDistance, scollYDistance,
                500);

        ViewCompat.postInvalidateOnAnimation(LeoGeaFlingView.this);

        mCurrentScrollDirection = mCurrentFlingDirection = Direction.NONE;
        if (listener != null)
            listener.onStop();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        mScaleDetector.onTouchEvent(event);
        boolean val = mGestureDetector.onTouchEvent(event);
        // Check after call of mGestureDetector, so mCurrentFlingDirection and mCurrentScrollDirection are set.
        if (event.getAction() == MotionEvent.ACTION_UP && !mIsZooming && mCurrentFlingDirection == Direction.NONE) {
            if (mCurrentScrollDirection == Direction.RIGHT || mCurrentScrollDirection == Direction.LEFT) {
                goToNearestOrigin();
            }
            mCurrentScrollDirection = Direction.NONE;
        }
        return val;
    }

    @Override
    public void computeScroll() {
        super.computeScroll();

        Log.e("LEO888", "正在做动画");

        if (mScroller.isFinished()) {
            if (mCurrentFlingDirection != Direction.NONE) {
                // Snap to day after fling is finished.
                goToNearestOrigin();
            }
        } else {
            if (mCurrentFlingDirection != Direction.NONE && forceFinishScroll()) {
                goToNearestOrigin();
            } else if (mScroller.computeScrollOffset()) {

                Log.e("CTABLE1", "自动归位...mScroller.getCurrX()：" + mScroller.getCurrX());
                Log.e("CTABLE1", "自动归位...mCurrentOrigin.x：" + mCurrentOrigin.x);
                Log.e("CTABLE1", "-----------------------------------------------------------------------");
//                Log.e("CTABLE1", "自动归位...mScroller.getCurrY()：" + mScroller.getCurrY());
//                Log.e("CTABLE1", "自动归位...mCurrentOrigin.y：" + mCurrentOrigin.y);
//                Log.e("CTABLE1", "-----------------------------------------------------------------------");

                if (listener != null) {
                    listener.onFling((int) (mScroller.getCurrX() - mCurrentOrigin.x), (int) (mScroller.getCurrY() - mCurrentOrigin.y));
//                    listener.onFling(mScroller.getCurrX(), mScroller.getCurrY());
                }
                scrollTo(mScroller.getCurrX(), mScroller.getCurrY());
                ViewCompat.postInvalidateOnAnimation(this);
            }
        }
    }

    /**
     * Check if scrolling should be stopped.
     *
     * @return true if scrolling should be stopped before reaching the end of animation.
     */
    private boolean forceFinishScroll() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
            // current velocity only available since api 14
            return mScroller.getCurrVelocity() <= mMinimumFlingVelocity;
        } else {
            return false;
        }
    }

}
