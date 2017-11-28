package com.leo.tablelibrary.adapter;

import android.util.SparseArray;
import android.view.View;
import android.widget.TextView;

/**
 * 作者：Leo on 2017/9/30 10:29
 * <p>
 * 描述：
 */

public class LinearLayoutViewHolder {

    private SparseArray<View> mViews;
    private View mConvertView;

    public LinearLayoutViewHolder(View view) {
        this.mViews = new SparseArray<>();
        mConvertView = view;
    }

    /**
     * 通过viewId获取控件
     *
     * @param viewId
     * @return
     */
    public <T extends View> T getView(int viewId) {
        View view = mViews.get(viewId);
        if (view == null) {
            view = mConvertView.findViewById(viewId);
            mViews.put(viewId, view);
        }
        return (T) view;
    }

    public View getConvertView() {
        return mConvertView;
    }

    /**
     * 设置TextView的值
     *
     * @param viewId
     * @param text
     * @return
     */
    public void setText(int viewId, String text) {
        TextView tv = getView(viewId);
        tv.setText(text);
    }

}

