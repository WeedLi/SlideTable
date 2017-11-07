package com.leo.monthtable.monthtablelibrary;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

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

public class SlideTableView extends LinearLayout {

    private ITableAdapter tableAdapter;

    private TableFlingView tableFlingView;
    private View firstView;
    private RecyclerView rowRV, columnRV, contentRV;
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
    }

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
        configureLeoGeaFling();
    }

    //重设参数
    private void resetLayoutParams(View view, int width, int height) {
        ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
        layoutParams.width = width;
        layoutParams.height = height;
        view.setLayoutParams(layoutParams);
    }

    private void configureLeoGeaFling() {
        tableFlingView = findViewById(R.id.table_fling_view);
        tableFlingView.setLocationDuration(locationDuration);
        tableFlingView.setSlideTableView(this);
        tableFlingView.setOnTableFlingViewScrollListener(new TableFlingView.OnTableFlingViewScrollListener() {
            @Override
            public void onScroll(int disX, int disY) {
                rowRV.scrollBy(disX, 0);
                columnRV.scrollBy(0, disY);
                contentRV.scrollBy(disX, disY);
                tableFlingView.mCurrentOrigin.x += disX;
                tableFlingView.mCurrentOrigin.y += disY;
            }

            @Override
            public void onFling(int disX, int disY) {
                rowRV.scrollBy(disX, 0);
                columnRV.scrollBy(0, disY);
                contentRV.scrollBy(disX, disY);
                tableFlingView.mCurrentOrigin.x += disX;
                tableFlingView.mCurrentOrigin.y += disY;
            }

            @Override
            public void onStop() {
                Log.e("onstop", "滑动停止了");
            }
        });
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

    /**
     * 滑到某一行
     *
     * @param row
     */
    public void slideToSomeRow(int row) {
        if (row <= 0)
            return;
        if (tableFlingView != null) {
            tableFlingView.flingToSomeColAndRow(0, row * tableAdapter.getOtherRowHeight());
        }
    }

    /**
     * 滑到某一列
     *
     * @param column
     */
    public void slideToSomeColumn(int column) {
        if (column <= 0)
            return;
        if (tableFlingView != null) {
            tableFlingView.flingToSomeColAndRow(column * tableAdapter.getOtherColumnWidth(), 0);
        }
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
        if (tableFlingView != null) {
            tableFlingView.flingToSomeColAndRow(col * tableAdapter.getOtherColumnWidth(), row * tableAdapter.getOtherRowHeight());
        }
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
