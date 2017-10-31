package com.leo.monthtable.monthtablelibrary;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
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

    public BaseViewHolder(View itemView) {
        super(itemView);
        this.view = itemView;
    }

    private <T extends View> T findViewById(int id) {
        View viewById = view.findViewById(id);
        return (T) viewById;
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
