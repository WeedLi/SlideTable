package com.leo.monthtable.monthtablelibrary;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.LinearLayout;

import com.leo.monthtable.monthtablelibrary.utils.DateUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * 作者：Leo on 2017/9/29 11:39
 * <p>
 * 描述：排班的展示控件
 */

public class CTableView extends LinearLayout {

    private RecyclerView dateList, staffList, contentList;
    private CFlingView cFlingView;
    private View surfaceView;

    private DateAdapter dateAdapter;
    private TableColAdapter tableColAdapter;
    private ContentAdapter contentAdapter;

    private String currDate;
    private List<String> dateData;
    private List<String> staffData;
    private List<String> contentData;

    private int itemWidth;
    private int itemHeight;
    private int padding0;
    private int currX;
    private int currY;

    private float surfaceWidth;
    private float surfaceHeight;

    /**
     * 获得Dimens类型的数据
     *
     * @param resId
     * @return
     */
    public float getDimen(int resId) {
        return getContext().getResources().getDimension(resId);
    }


    public CTableView(Context context) {
        this(context, null);
    }

    public CTableView(Context context, AttributeSet attrs) {
        super(context, attrs);
        //默认加载今天的日期
        currDate = DateUtil.getYearAndMonth();

        itemWidth = (int) getDimen(R.dimen.x270);
        itemHeight = (int) getDimen(R.dimen.x300);
        padding0 = (int) getDimen(R.dimen.padding0);

        LayoutInflater.from(context).inflate(R.layout.view_c_table, this, true);
        dateList = findViewById(R.id.dateList);
        staffList = findViewById(R.id.staffList);
        contentList = findViewById(R.id.contentList);

        dateData = new ArrayList<>();
        staffData = new ArrayList<>();
        contentData = new ArrayList<>();
        configureDateList();
        configureStaffList();
        configureContentList();

//        configureCFling();
        configureLeoGeaFling();
    }

    public int deatalX;
    public int deatalY;

    private void configureLeoGeaFling() {
        final LeoGeaFlingView lgf = findViewById(R.id.leo_gea_fling);
        lgf.setcTableView(this);
        lgf.setOnCFlingViewScrollListener(new LeoGeaFlingView.OnCFlingViewScrollListener() {
            @Override
            public void onScroll(int disX, int disY) {
                Log.e("CTABLE", "onScroll---disX：" + disX);
                Log.e("CTABLE", "onScroll---disY：" + disY);

                deatalX += disX;
                deatalY += disY;

                dateList.scrollBy(disX, 0);
                staffList.scrollBy(0, disY);
                contentList.scrollBy(disX, disY);

                lgf.mCurrentOrigin.x = deatalX;
                lgf.mCurrentOrigin.y = deatalY;

            }

            @Override
            public void onFling(int disX, int disY) {

                Log.e("CTABLE", "onFling---disX：" + disX);
                Log.e("CTABLE", "onFling---disY：" + disY);

                deatalX += disX;
                deatalY += disY;

                dateList.scrollBy(disX, 0);
                staffList.scrollBy(0, disY);
                contentList.scrollBy(disX, disY);

                lgf.mCurrentOrigin.x = deatalX;
                lgf.mCurrentOrigin.y = deatalY;
            }

            @Override
            public void onStop() {

            }
        });
    }

    private void configureCFling() {
//        cFlingView = findViewById(R.id.cFlingView);
//        surfaceView = findViewById(R.id.surfaceView);
//        cFlingView.setCTableView(this);
//        ViewGroup.LayoutParams surfaceParams = surfaceView.getLayoutParams();
//        surfaceParams.width = (int) surfaceWidth;
//        surfaceParams.height = (int) surfaceHeight;
//        surfaceView.setLayoutParams(surfaceParams);
//        cFlingView.setOnScrollDistanceListener(new CFlingView.OnScrollDistanceListener() {
//            @Override
//            public void onScrollDistance(int deltaX, int deltaY) {
//                dateList.scrollBy(deltaX, 0);
//                staffList.scrollBy(0, deltaY);
//                contentList.scrollBy(deltaX, deltaY);
//            }
//        });
    }

    private void configureDateList() {
        int currentMonthDays = getMonthDays();
        surfaceWidth = getDimen(R.dimen.x240) + padding0 * 2 + itemWidth * currentMonthDays;
        dateData.clear();
        for (int i = 0; i < currentMonthDays; i++) {
            int day = i + 1;
            dateData.add(currDate + "-" + day);
        }

        if (dateAdapter == null) {
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
            linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
            dateList.setLayoutManager(linearLayoutManager);
            dateAdapter = new DateAdapter(dateData);
            dateList.setAdapter(dateAdapter);
        } else {
            dateAdapter.notifyDataSetChanged();
        }
    }

    private void configureStaffList() {
        staffData.clear();

        surfaceHeight = getDimen(R.dimen.height1) + itemHeight * 60;

        for (int i = 0; i < 60; i++) {
            staffData.add("Leo:" + (i + 1));
        }
        if (tableColAdapter == null) {
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
            linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
            staffList.setLayoutManager(linearLayoutManager);
            tableColAdapter = new TableColAdapter(staffData);
            staffList.setAdapter(tableColAdapter);
        } else {
            tableColAdapter.notifyDataSetChanged();
        }
    }

    private void configureContentList() {
        int currentMonthDays = getMonthDays();
        contentData.clear();
        for (int i = 0; i < 60; i++) {
            contentData.add((i + 1) + "-");
        }

        if (contentAdapter == null) {
            FixedGridLayoutManager manager = new FixedGridLayoutManager();
            manager.setTotalColumnCount(1);
            contentList.setLayoutManager(manager);
            contentAdapter = new ContentAdapter(contentData);
            contentAdapter.setDays(currentMonthDays);
            contentList.setAdapter(contentAdapter);
        } else {
            contentAdapter.notifyDataSetChanged();
        }
    }

    private int getMonthDays() {
        return DateUtil.getMonthDaysNormal(Integer.parseInt(currDate.split("-")[0])
                , Integer.parseInt(currDate.split("-")[1]));
    }

    private void reSetContentView() {
        final int distanceX = getScollXDistance();
        final int distanceY = getScollYDistance();
        currX = 0;
        currY = 0;
        ValueAnimator animX = ObjectAnimator.ofInt(0, Math.abs(distanceX)).setDuration((long) (Math.abs(distanceX) * 5));
        animX.setInterpolator(new LinearInterpolator());
        animX.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                Integer aValue = (Integer) animation.getAnimatedValue();
                int dX = aValue - currX;
                currX = aValue;
                if (distanceX < 0) {
                    dX = -dX;
                }
                dateList.scrollBy(dX, 0);
                contentList.scrollBy(dX, 0);
            }
        });
        animX.start();

        ValueAnimator animY = ObjectAnimator.ofInt(0, Math.abs(distanceY)).setDuration((long) (Math.abs(distanceY) * 5));
        animY.setInterpolator(new LinearInterpolator());
        animY.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                Integer aValue = (Integer) animation.getAnimatedValue();
                int dY = aValue - currY;
                currY = aValue;
                if (distanceY < 0) {
                    dY = -dY;
                }
                staffList.scrollBy(0, dY);
                contentList.scrollBy(0, dY);
            }

        });
        animY.start();

    }


    public int getScollXDistance() {
        LinearLayoutManager layoutManager = (LinearLayoutManager) dateList.getLayoutManager();
        int position = layoutManager.findFirstVisibleItemPosition();
        View firstVisiableChildView = layoutManager.findViewByPosition(position);

        int itemWidthPadding = itemWidth;

        Log.e("LEO666666", "getLeft：" + Math.abs(firstVisiableChildView.getLeft()));

        int leftDix = firstVisiableChildView.getLeft() - padding0;
        if (Math.abs(leftDix) < itemWidthPadding / 2) {
            return leftDix;
        } else {
            int distance = itemWidthPadding - Math.abs(firstVisiableChildView.getLeft()) - padding0;
            return Math.abs(distance);
        }

    }

    public int getScollYDistance() {
        LinearLayoutManager layoutManager = (LinearLayoutManager) staffList.getLayoutManager();
        int position = layoutManager.findFirstVisibleItemPosition();
        View firstVisiableChildView = layoutManager.findViewByPosition(position);
        int distance = itemHeight - Math.abs(firstVisiableChildView.getTop());

        Log.e("LEO666666", "getTop：" + Math.abs(firstVisiableChildView.getTop()));

        if (Math.abs(firstVisiableChildView.getTop()) < itemHeight / 2) {
            return firstVisiableChildView.getTop();
        } else {
            return distance;
        }
    }

}
