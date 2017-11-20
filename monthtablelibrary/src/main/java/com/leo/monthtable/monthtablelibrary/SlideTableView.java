package com.leo.monthtable.monthtablelibrary;

import android.content.Context;
import android.graphics.PointF;
import android.os.Build;
import android.support.v4.view.GestureDetectorCompat;
import android.support.v4.view.MotionEventCompat;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.animation.FastOutLinearInInterpolator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.OverScroller;

import com.leo.monthtable.monthtablelibrary.adapter.BaseViewHolder;
import com.leo.monthtable.monthtablelibrary.adapter.ContentAdapter;
import com.leo.monthtable.monthtablelibrary.adapter.ITableAdapter;
import com.leo.monthtable.monthtablelibrary.adapter.LinearLayoutViewHolder;
import com.leo.monthtable.monthtablelibrary.adapter.TableFirstColAdapter;
import com.leo.monthtable.monthtablelibrary.adapter.TableFirstRowAdapter;
import com.leo.monthtable.monthtablelibrary.bean.TableContentBean;
import com.leo.monthtable.monthtablelibrary.bean.TableFirstColumnBean;
import com.leo.monthtable.monthtablelibrary.bean.TableFirstRowBean;


/**
 * 作者：Leo on 2017/9/29 11:39
 * <p>
 * 描述：排班的展示控件
 */

public class SlideTableView extends FrameLayout {

    private ITableAdapter tableAdapter;
    private View firstView;
    private OnlyClickRecyclerView rowRV, columnRV, contentRV;
    private View divide_line;

    //自定义属性
    private int firstColumnWidth;//第一列列宽
    private int otherColumnWidth;//其他列列宽

    private int firstRowHeight;//第一行行高
    private int otherRowHeight;//其他行行高
    private int locationDuration = 500;//重新定位的时长
    private int columnPadding;//行与行之间的padding

    private int contentPaddingLeft;//内容区域左边的padding
    private int contentPaddingRight;//内容区域右边的padding

    public SlideTableView(Context context) {
        this(context, null);
    }

    public SlideTableView(Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.view_c_table, this, true);
        firstView = findViewById(R.id.first_view);
        rowRV = findViewById(R.id.recycler_row);
        columnRV = findViewById(R.id.recycler_column);
        contentRV = findViewById(R.id.recycler_content);
        divide_line = findViewById(R.id.first_row_divide_line);

        //////////////////////////////////////////////////////////////////////////////////////
        mTouchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
        mGestureDetector = new GestureDetectorCompat(getContext(), mGestureListener);

        mGestureDetector.setIsLongpressEnabled(false);

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

    ///////////////////////////////////////////////////////////////////////////////////////////////
    //
    //     处理触摸事件
    //
    ///////////////////////////////////////////////////////////////////////////////////////////////

    private Direction mCurrentFlingDirection = Direction.NONE;
    private Direction mCurrentScrollDirection = Direction.NONE;

    private enum Direction {
        NONE, LEFT, RIGHT, VERTICAL
    }

    private GestureDetectorCompat mGestureDetector;
    private OverScroller mScroller;
    private ScaleGestureDetector mScaleDetector;
    private int mTouchSlop;
    public PointF mCurrentOrigin = new PointF(0f, 0f);
    private float mXScrollingSpeed = 1f;
    private float mYScrollingSpeed = 1f;
    private boolean mIsZooming;
    private int mScaledTouchSlop = 0;
    private int mMinimumFlingVelocity = 0;

    private GestureDetector.SimpleOnGestureListener mGestureListener = new GestureDetector.SimpleOnGestureListener() {

        public boolean onDown(MotionEvent e) {

            log1(e.getAction() + "......................onDown....................." + mIsZooming);

            //Cancel any current fling
            if (!mScroller.isFinished()) {
                mScroller.abortAnimation();
                goToNearestOrigin();
            }
            return true;
        }

        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {

            log("......................onScroll....................." + mIsZooming);

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
                    scrollItemRV((int) distanceX, 0);
                    break;
                case VERTICAL:
                    scrollItemRV(0, (int) distanceY);
                    break;
            }
            return true;
        }

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {

            log("......................onFling....................." + mIsZooming);

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
            ViewCompat.postInvalidateOnAnimation(SlideTableView.this);
            return true;
        }
    };

    private void goToNearestOrigin() {
        int scollXDistance = getScollXDistance();
        int scollYDistance = getScollYDistance();
        mScroller.forceFinished(true);
        mScroller.startScroll((int) mCurrentOrigin.x, (int) mCurrentOrigin.y
                , scollXDistance, scollYDistance,
                locationDuration);
        ViewCompat.postInvalidateOnAnimation(SlideTableView.this);
        mCurrentScrollDirection = mCurrentFlingDirection = Direction.NONE;
    }

    private float mInitialX, mInitialY;


    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                log1("ACTION_DOWN...........................................");
                int activePointerIndex = MotionEventCompat.getActionIndex(event);
                mInitialX = MotionEventCompat.getX(event, activePointerIndex);
                mInitialY = MotionEventCompat.getY(event, activePointerIndex);
                //Feed the down event to the detector so it has
                // context when/if dragging begins
                mGestureDetector.onTouchEvent(event);
                break;

            case MotionEvent.ACTION_MOVE:
                final float x = event.getX();
                final float y = event.getY();
                final int yDiff = (int) Math.abs(y - mInitialY);
                final int xDiff = (int) Math.abs(x - mInitialX);
                //Verify that either difference is enough to be a drag
                if (yDiff > mTouchSlop || xDiff > mTouchSlop) {
                    //Start capturing events
                    log("ACTION_MOVE...........................................true");
                    return true;
                } else {
                    log("ACTION_MOVE...........................................false");
                }
                break;
        }
        return super.onInterceptTouchEvent(event);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        mScaleDetector.onTouchEvent(event);
        boolean val = mGestureDetector.onTouchEvent(event);
        log("...........................................onTouchEvent......" + val);
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
                flingItemRV((int) (mScroller.getCurrX() - mCurrentOrigin.x), (int) (mScroller.getCurrY() - mCurrentOrigin.y));
                ViewCompat.postInvalidateOnAnimation(SlideTableView.this);
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
        ViewCompat.postInvalidateOnAnimation(this);
    }
    ///////////////////////////////////////////////////////////////////////////////////////////////
    //
    //
    //
    ///////////////////////////////////////////////////////////////////////////////////////////////

    public void setTableAdapter(ITableAdapter iTableAdapter) {
        tableAdapter = iTableAdapter;
        firstRowHeight = tableAdapter.getFirstRowHeight();
        firstColumnWidth = tableAdapter.getFirstColumnWidth();
        otherRowHeight = tableAdapter.getOtherRowHeight();
        otherColumnWidth = tableAdapter.getOtherColumnWidth();
        columnPadding = tableAdapter.getColumnPadding();
        contentPaddingLeft = (int) (columnPadding / 2f);
        contentPaddingRight = (int) (columnPadding / 2f);
        resetLayoutParams(firstView, firstColumnWidth, firstRowHeight);
        resetLayoutParams(rowRV, ViewGroup.LayoutParams.MATCH_PARENT, firstRowHeight);
        resetLayoutParams(columnRV, firstColumnWidth, ViewGroup.LayoutParams.MATCH_PARENT);
        rowRV.setPadding(contentPaddingLeft, 0, contentPaddingRight, 0);
        configureFirstRowList();
        configureFirstColumnList();
        configureContentList();
    }

    //重设参数
    private void resetLayoutParams(View view, int width, int height) {
        ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
        layoutParams.width = width;
        layoutParams.height = height;
        view.setLayoutParams(layoutParams);
    }

    private void scrollItemRV(int disX, int disY) {
        rowRV.scrollBy(disX, 0);
        columnRV.scrollBy(0, disY);
        contentRV.scrollBy(disX, disY);
        mCurrentOrigin.x += disX;
        mCurrentOrigin.y += disY;
    }

    private void flingItemRV(int disX, int disY) {
        rowRV.scrollBy(disX, 0);
        columnRV.scrollBy(0, disY);
        contentRV.scrollBy(disX, disY);
        mCurrentOrigin.x += disX;
        mCurrentOrigin.y += disY;
    }

    private void configureFirstRowList() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        rowRV.setLayoutManager(linearLayoutManager);
        TableFirstRowAdapter tableFirstRowAdapter = new TableFirstRowAdapter(tableAdapter.getFirstRowData()) {

            @Override
            protected int getLayoutId() {
                return tableAdapter.getFirstRowLayoutId();
            }

            @Override
            protected void bindRowItemHolder(BaseViewHolder holder, int position, TableFirstRowBean tableRowBean) {
                tableAdapter.bindFirstRowItemHolder(holder, position, tableRowBean);
            }
        };
        tableFirstRowAdapter.setItemWidth(otherColumnWidth);
        tableFirstRowAdapter.setItemHeight(firstRowHeight);
        rowRV.setAdapter(tableFirstRowAdapter);
    }

    private void configureFirstColumnList() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        columnRV.setLayoutManager(linearLayoutManager);
        TableFirstColAdapter tableFirstColAdapter = new TableFirstColAdapter(tableAdapter.getFirstColumnData()) {
            @Override
            protected int getLayoutId() {
                return tableAdapter.getFirstColumnLayoutId();
            }

            @Override
            protected void bindFirstColumnItemHolder(BaseViewHolder holder, int position, TableFirstColumnBean tableColumnBean) {
                tableAdapter.bindFirstColumnItemHolder(holder, position, tableColumnBean);
            }
        };
        tableFirstColAdapter.setItemWidth(firstColumnWidth);
        tableFirstColAdapter.setItemHeight(otherRowHeight);
        columnRV.setAdapter(tableFirstColAdapter);
    }

    private void configureContentList() {
        FixedGridLayoutManager manager = new FixedGridLayoutManager();
        manager.setTotalColumnCount(1);
        contentRV.setLayoutManager(manager);
        ContentAdapter contentAdapter = new ContentAdapter(tableAdapter.getContentData()) {
            @Override
            protected int getItemLayoutId() {
                return tableAdapter.getContentLayoutId();
            }

            @Override
            protected void bindContentHolder(LinearLayoutViewHolder holder, int position, TableContentBean tableContentBean, int parentPosition) {
                tableAdapter.bindContentItemHolder(holder, position, tableContentBean, parentPosition);
            }
        };
        contentAdapter.setDays(tableAdapter.getColumnCount());
        contentAdapter.setItemWidth(otherColumnWidth);
        contentAdapter.setItemHeight(otherRowHeight);
        contentAdapter.setItemPaddingLeft(contentPaddingLeft);
        contentAdapter.setItemPaddingRight(contentPaddingRight);
        contentRV.setAdapter(contentAdapter);
    }

    //获取水平方向的偏移量
    public int getScollXDistance() {
        LinearLayoutManager layoutManager = (LinearLayoutManager) rowRV.getLayoutManager();
        int position = layoutManager.findFirstVisibleItemPosition();
        View firstVisiableChildView = layoutManager.findViewByPosition(position);

        int itemWidthPadding = otherColumnWidth;

        Log.e("LEO666666", "getLeft：" + Math.abs(firstVisiableChildView.getLeft()));

        int leftDix = firstVisiableChildView.getLeft() - contentPaddingLeft;
        if (Math.abs(leftDix) < itemWidthPadding / 2) {
            return leftDix;
        } else {
            int distance = itemWidthPadding - Math.abs(firstVisiableChildView.getLeft()) - contentPaddingLeft;
            return Math.abs(distance);
        }

    }

    //获取竖直方向的偏移量
    public int getScollYDistance() {
        LinearLayoutManager layoutManager = (LinearLayoutManager) columnRV.getLayoutManager();
        int position = layoutManager.findFirstVisibleItemPosition();
        View firstVisiableChildView = layoutManager.findViewByPosition(position);
        int distance = otherRowHeight - Math.abs(firstVisiableChildView.getTop());
        if (Math.abs(firstVisiableChildView.getTop()) < otherRowHeight / 2) {
            return firstVisiableChildView.getTop();
        } else {
            return distance;
        }
    }


    ///////////////////////////////////////////////////////////////////////////////////////////////
    //
    //     公共方法
    //
    ///////////////////////////////////////////////////////////////////////////////////////////////

    private void log(String text) {
        Log.e("LEOTABLE", text);
    }

    private void log1(String text) {
        Log.e("LEOTABLE1", text);
    }

    /**
     * 滑到某一行
     *
     * @param row
     */
    public void slideToSomeRow(int row) {
        if (row <= 0)
            return;
        flingToSomeColAndRow(0, row * tableAdapter.getOtherRowHeight());
    }

    /**
     * 滑到某一列
     *
     * @param column
     */
    public void slideToSomeColumn(int column) {
        if (column <= 0)
            return;
        flingToSomeColAndRow(column * tableAdapter.getOtherColumnWidth(), 0);
    }

    /**
     * 滑动到某行某列
     *
     * @param col
     * @param row
     */
    public void slideToSomeColAndRow(int col, int row) {
        if (row < 0)
            row = 0;
        if (col < 0)
            col = 0;
        flingToSomeColAndRow(col * tableAdapter.getOtherColumnWidth(), row * tableAdapter.getOtherRowHeight());
    }

    /**
     * 获取第一行的RecyclerView
     *
     * @return
     */
    public RecyclerView getFirstRowRecyclerView() {
        return rowRV;
    }


    /**
     * 获取第一列的RecyclerView
     *
     * @return
     */
    public RecyclerView getFirstColumnRecyclerView() {
        return columnRV;
    }


    /**
     * 获取表格内容的RecyclerView
     *
     * @return
     */
    public RecyclerView getContentRecyclerView() {
        return contentRV;
    }


    /**
     * 获取第一行跟表格内容的分界线
     *
     * @return
     */
    public View getHorDivide_line() {
        return divide_line;
    }

    public void addFirstView(View replaceView) {
        if (firstView == null) {
            return;
        }
        if (!(firstView.getParent() instanceof ViewGroup)) {
            return;
        }

        ViewGroup firstViewParent = (ViewGroup) firstView.getParent();
        int index = firstViewParent.indexOfChild(firstView);
        replaceView.setLayoutParams(firstView.getLayoutParams());
        firstViewParent.addView(replaceView, index);
        firstViewParent.removeView(firstView);
        firstView = replaceView;
    }

}
