package com.leo.monthtable.monthtablelibrary.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.SparseArray;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * 作者：Leo on 2016/12/5 11:07
 * <p/>
 * 描述：适配器中holder的基类
 */
public class BaseViewHolder extends RecyclerView.ViewHolder {

    private View view;
    private SparseArray<View> views;

    public BaseViewHolder(View itemView) {
        super(itemView);
        this.view = itemView;
        views = new SparseArray<>();
    }

    private <T extends View> T findViewById(int id) {
        View viewById = views.get(id);
        if (viewById == null) {
            viewById = view.findViewById(id);
            views.put(id, viewById);
        }
        return (T) viewById;
    }

    public boolean isViewIdFound(int viewId) {
        return views.get(viewId) != null;
    }

    public View getRootView() {
        return view;
    }

    public Context getContext() {
        return view.getContext();
    }

    public View getView(int viewId) {
        return findViewById(viewId);
    }

    public TextView getTextView(int viewId) {
        return findViewById(viewId);
    }

    public ImageView getImageView(int viewId) {
        return findViewById(viewId);
    }


    public void setText(int viewId, String text) {
        if (!TextUtils.isEmpty(text))
            getTextView(viewId).setText(text);
    }

}
