package com.leo.monthtable.monthtablelibrary;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;

/**
 * 作者：Leo on 2017/9/29 21:02
 * <p>
 * 描述：
 */

public class CTableRecyclerView extends RecyclerView {


    public CTableRecyclerView(Context context) {
        super(context);
    }

    public CTableRecyclerView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CTableRecyclerView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }


    @Override
    public boolean fling(int velocityX, int velocityY) {
        velocityX *= 0.88;
        return super.fling(velocityX, velocityY);
    }

}
