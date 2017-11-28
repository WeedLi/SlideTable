package com.leo.tablelibrary.adapter;

import android.view.View;
import android.view.ViewGroup;

import com.leo.tablelibrary.bean.TableFirstColumnBean;

import java.util.List;


/**
 * 作者：Leo on 2017/9/29 13:35
 * <p>
 * 描述：
 */

public abstract class TableFirstColAdapter extends BaseAdapter<TableFirstColumnBean> {

    private int itemWidth;
    private int itemHeight;

    public void setItemWidth(int itemWidth) {
        this.itemWidth = itemWidth;
    }

    public void setItemHeight(int itemHeight) {
        this.itemHeight = itemHeight;
    }

    public TableFirstColAdapter(List<TableFirstColumnBean> mDatas) {
        super(mDatas);
    }

    protected abstract void bindFirstColumnItemHolder(BaseViewHolder holder, int position, TableFirstColumnBean tableColumnBean);

    @Override
    protected void bindViewHolder(BaseViewHolder holder, int position, TableFirstColumnBean tableColumnBean) {
        View rootView = holder.getRootView();
        ViewGroup.LayoutParams layoutParams = rootView.getLayoutParams();
        layoutParams.width = itemWidth;
        layoutParams.height = itemHeight;
        rootView.setLayoutParams(layoutParams);
        bindFirstColumnItemHolder(holder,position,tableColumnBean);
    }
}
