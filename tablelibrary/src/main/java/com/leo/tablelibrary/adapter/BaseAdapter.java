package com.leo.tablelibrary.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;
import java.util.NoSuchElementException;

/**
 * 作者：Leo on 2016/12/5 11:05
 * <p>
 * 描述：
 */
public abstract class BaseAdapter<T> extends RecyclerView.Adapter<BaseViewHolder> {

    protected OnMyItemClickListener itemClickListener;
    protected OnMyItemLongClickListener itemLongClickListener;
    protected List<T> mDatas;
    protected Context mContext;

    public BaseAdapter(List<T> mDatas) {
        this.mDatas = mDatas;
    }

    @Override
    public BaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (getLayoutId() == 0) {
            throw new NoSuchElementException("适配器布局不能是0!");
        }
        this.mContext = parent.getContext();
        return new BaseViewHolder(LayoutInflater.from(mContext).inflate(getLayoutId(), parent, false));
    }

    @Override
    public int getItemViewType(int position) {
        if (mDatas == null || mDatas.isEmpty()) {
            //显示空的View
            return 1;
        } else {
            return 0;
        }
    }

    @Override
    public void onBindViewHolder(BaseViewHolder holder, int position) {
        if (position >= mDatas.size())
            bindViewHolder(holder, position, null);
        else
            bindViewHolder(holder, position, mDatas.get(position));
    }

    @Override
    public int getItemCount() {
        if (mDatas == null || mDatas.isEmpty()) {
            return 0;
        }
        return mDatas.size();
    }


    /**
     * 布局
     *
     * @return
     */
    protected abstract int getLayoutId();

    /**
     * 做View的绑定
     *
     * @param holder
     * @param position
     * @param t
     */
    protected abstract void bindViewHolder(BaseViewHolder holder, int position, T t);

    /**
     * 设置点击的监听
     *
     * @param view
     * @param position
     */
    protected void setViewClickListener(View view, int position) {
        view.setTag(position);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int pos = (int) v.getTag();
                clickView(pos);
                if (itemClickListener != null) {
                    itemClickListener.onItemClick(v, mDatas.get(pos), pos);
                }
            }
        });
    }


    /**
     * 设置长按点击的监听
     *
     * @param view
     * @param position
     */
    protected void setViewLongClickListener(View view, int position) {
        view.setTag(position);
        view.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                int pos = (int) v.getTag();
                clickView(pos);
                if (itemLongClickListener != null)
                    itemLongClickListener.onItemLongClick(v, mDatas.get(pos), pos);
                return false;
            }
        });
    }

    /**
     * 点击的时候做的事情
     *
     * @param pos
     */
    protected void clickView(int pos) {

    }

    public void setOnMyItemClickListener(OnMyItemClickListener<T> listener) {
        this.itemClickListener = listener;
    }

    public void setOnMyItemLongClickListener(OnMyItemLongClickListener<T> listener) {
        this.itemLongClickListener = listener;
    }

    /**
     * 每一个item的点击监听
     */
    public interface OnMyItemClickListener<T> {
        void onItemClick(View view, T t, int position);
    }

    /**
     * 每一个item的长按点击监听
     */
    public interface OnMyItemLongClickListener<T> {
        void onItemLongClick(View view, T t, int position);
    }

}
