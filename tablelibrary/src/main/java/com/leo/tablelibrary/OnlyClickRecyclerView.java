package com.leo.tablelibrary;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * 作者：Leo on 2017/11/16 12:29
 * <p>
 * 描述：
 */

public class OnlyClickRecyclerView extends RecyclerView {

    public OnlyClickRecyclerView(Context context) {
        this(context, null);
    }

    public OnlyClickRecyclerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_MOVE:
                return false;
        }
        return super.onTouchEvent(ev);
    }
}
