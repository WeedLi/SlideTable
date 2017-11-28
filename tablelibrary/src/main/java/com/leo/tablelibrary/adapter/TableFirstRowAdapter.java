package com.leo.tablelibrary.adapter;

import android.view.View;
import android.view.ViewGroup;

import com.leo.tablelibrary.bean.TableFirstRowBean;

import java.util.List;

/**
 * 作者：Leo on 2017/9/29 12:23
 * <p>
 * 描述：第一行的适配器
 */

public abstract class TableFirstRowAdapter extends BaseAdapter<TableFirstRowBean> {


    private int itemWidth;
    private int itemHeight;

    public void setItemWidth(int itemWidth) {
        this.itemWidth = itemWidth;
    }

    public void setItemHeight(int itemHeight) {
        this.itemHeight = itemHeight;
    }

    public TableFirstRowAdapter(List<TableFirstRowBean> mDatas) {
        super(mDatas);

    }

    protected abstract void bindRowItemHolder(BaseViewHolder holder, int position, TableFirstRowBean tableRowBean);

    @Override
    protected void bindViewHolder(BaseViewHolder holder, int position, TableFirstRowBean tableRowBean) {
        View rootView = holder.getRootView();
        ViewGroup.LayoutParams layoutParams = rootView.getLayoutParams();
        layoutParams.width = itemWidth;
        layoutParams.height = itemHeight;
        rootView.setLayoutParams(layoutParams);
        bindRowItemHolder(holder, position, tableRowBean);
    }

}
