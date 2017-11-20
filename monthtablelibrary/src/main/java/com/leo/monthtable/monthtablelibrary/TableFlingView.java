package com.leo.monthtable.monthtablelibrary;

import android.content.Context;
import android.graphics.PointF;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.v4.view.GestureDetectorCompat;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.animation.FastOutLinearInInterpolator;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.view.ViewConfiguration;
import android.widget.FrameLayout;
import android.widget.OverScroller;
import android.widget.Toast;


/**
 * 作者：Leo on 2017/10/3 15:27
 * <p>
 * 描述：
 */

public class TableFlingView extends FrameLayout {

    public TableFlingView(Context context) {
        this(context, null);
    }

    public TableFlingView(final Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.view_c_table_fling, this, true);

        findViewById(R.id.tv1).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(context, "unicorn1", Toast.LENGTH_SHORT).show();
            }
        });

        findViewById(R.id.tv2).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(context, "unicorn1", Toast.LENGTH_SHORT).show();
            }
        });

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

    private OnTableFlingViewScrollListener listener;

    public void setOnTableFlingViewScrollListener(OnTableFlingViewScrollListener listener) {
        this.listener = listener;
    }

    public interface OnTableFlingViewScrollListener {

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

    private SlideTableView slideTableView;
    private GestureDetectorCompat mGestureDetector;
    private OverScroller mScroller;
    private ScaleGestureDetector mScaleDetector;
    public PointF mCurrentOrigin = new PointF(0f, 0f);
    private float mXScrollingSpeed = 1f;
    private float mYScrollingSpeed = 1f;
    private boolean mIsZooming;
    private int mScaledTouchSlop = 0;
    private int mMinimumFlingVelocity = 0;
    private int locationDuration = 500;//重新定位的时长

    public void setLocationDuration(int locationDuration) {
        this.locationDuration = locationDuration;
    }

    public void setSlideTableView(SlideTableView slideTableView) {
        this.slideTableView = slideTableView;
    }

    private GestureDetector.SimpleOnGestureListener mGestureListener = new GestureDetector.SimpleOnGestureListener() {

        @Override
        public boolean onSingleTapConfirmed(MotionEvent e) {
//            return super.onSingleTapConfirmed(e);
            return true;
        }

        @Override
        public boolean onSingleTapUp(MotionEvent e) {
//            return super.onSingleTapUp(e);
            return false;
        }

        @Override
        public boolean onDown(MotionEvent e) {
            mScroller.abortAnimation();
            return true;
        }

        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
            if (mIsZooming)
                return true;

            switch (mCurrentScrollDirection) {
                case NONE: {
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
                    if (Math.abs(distanceX) > Math.abs(distanceY) && (distanceX < -mScaledTouchSlop)) {
                        mCurrentScrollDirection = Direction.RIGHT;
                    }
                    break;
                }
                case RIGHT: {
                    if (Math.abs(distanceX) > Math.abs(distanceY) && (distanceX > mScaledTouchSlop)) {
                        mCurrentScrollDirection = Direction.LEFT;
                    }
                    break;
                }
            }

            switch (mCurrentScrollDirection) {
                case LEFT:
                case RIGHT:
                    //TODO SCROLL
                    if (listener != null) {
                        listener.onScroll((int) distanceX, 0);
                    }
//                    scrollTo((int) mCurrentOrigin.x, (int) mCurrentOrigin.y);
//                    ViewCompat.postInvalidateOnAnimation(TableFlingView.this);
                    break;
                case VERTICAL:
                    //TODO SCROLL
                    if (listener != null) {
                        listener.onScroll(0, (int) distanceY);
                    }
//                    scrollTo((int) mCurrentOrigin.x, (int) mCurrentOrigin.y);
//                    ViewCompat.postInvalidateOnAnimation(TableFlingView.this);
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
                            0, -(int) (velocityY * mYScrollingSpeed),
                            Integer.MIN_VALUE, Integer.MAX_VALUE,
                            Integer.MIN_VALUE, Integer.MAX_VALUE);
                    break;
            }
            ViewCompat.postInvalidateOnAnimation(TableFlingView.this);
            return true;
        }
    };

    private void goToNearestOrigin() {
        int scollXDistance = slideTableView.getScollXDistance();
        int scollYDistance = slideTableView.getScollYDistance();
        mScroller.forceFinished(true);
        mScroller.startScroll((int) mCurrentOrigin.x, (int) mCurrentOrigin.y
                , scollXDistance, scollYDistance,
                locationDuration);
        ViewCompat.postInvalidateOnAnimation(TableFlingView.this);
        mCurrentScrollDirection = mCurrentFlingDirection = Direction.NONE;
        if (listener != null)
            listener.onStop();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        mScaleDetector.onTouchEvent(event);
        boolean val = mGestureDetector.onTouchEvent(event);
        if (event.getAction() == MotionEvent.ACTION_UP && !mIsZooming && mCurrentFlingDirection == Direction.NONE) {
            goToNearestOrigin();
            mCurrentScrollDirection = Direction.NONE;
        }
        return val;
    }

    @Override
    public void computeScroll() {
        super.computeScroll();
        if (mScroller.isFinished()) {
            if (mCurrentFlingDirection != Direction.NONE) {
                goToNearestOrigin();
            }
        } else {
            if (mCurrentFlingDirection != Direction.NONE && forceFinishScroll()) {
                goToNearestOrigin();
            } else if (mScroller.computeScrollOffset()) {
                if (listener != null) {
                    listener.onFling((int) (mScroller.getCurrX() - mCurrentOrigin.x), (int) (mScroller.getCurrY() - mCurrentOrigin.y));
                }
//                scrollTo(mScroller.getCurrX(), mScroller.getCurrY());
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

    public void flingToSomeColAndRow(int disX, int disY) {
        mScroller.startScroll((int) mCurrentOrigin.x, (int) mCurrentOrigin.y
                , disX, disY,
                Math.max(1000, Math.max(disX / 2, disY / 2)));
        ViewCompat.postInvalidateOnAnimation(TableFlingView.this);
    }

}
