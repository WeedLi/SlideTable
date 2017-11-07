package com.leo.monthtable.monthtablelibrary.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.leo.monthtable.monthtablelibrary.R;
import com.leo.monthtable.monthtablelibrary.bean.TableContentBean;

import java.util.List;


/**
 * 作者：Leo on 2017/9/29 13:40
 * <p>
 * 描述：
 */

public abstract class ContentAdapter extends RecyclerView.Adapter<BaseViewHolder> {

    private Context mContext;

    private List<List<TableContentBean>> mDatas;

    public ContentAdapter(List<List<TableContentBean>> mDatas) {
        this.mDatas = mDatas;
    }

    @Override
    public int getItemCount() {
        return mDatas == null ? 0 : mDatas.size();
    }

    @Override
    public BaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        mContext = parent.getContext();
        return new BaseViewHolder(LayoutInflater.from(mContext).inflate(R.layout.item_content_view, parent, false));
    }

    @Override
    public void onBindViewHolder(BaseViewHolder holder, int position) {
        if (!holder.isViewIdFound(R.id.root_view)) {
            LinearLayout root = (LinearLayout) holder.getView(R.id.root_view);
            ViewGroup.LayoutParams rootParams = root.getLayoutParams();
            rootParams.width = itemWidth * count + itemPaddingLeft + itemPaddingRight;
            rootParams.height = itemHeight;
            root.setLayoutParams(rootParams);
        }
        if (!holder.isViewIdFound(R.id.container_item)) {
            LinearLayout container = (LinearLayout) holder.getView(R.id.container_item);
            ViewGroup.LayoutParams layoutParams = container.getLayoutParams();
            layoutParams.width = itemWidth * count;
            layoutParams.height = itemHeight;
            container.setLayoutParams(layoutParams);
        }
        LinearLayout container = (LinearLayout) holder.getView(R.id.container_item);
        updateUI(container, mDatas.get(position), position);
    }

    private void updateUI(LinearLayout container, List<TableContentBean> beanList, int parentPosition) {
        int childCount = container.getChildCount();
        if (childCount == 0) {
            for (int i = 0; i < beanList.size(); i++) {
                container.addView(createItemView(null, i, beanList.get(i), parentPosition));
            }
        } else {
            for (int i = 0; i < childCount; i++) {
                createItemView(container.getChildAt(i), i, beanList.get(i), parentPosition);
            }
        }
    }

    private View createItemView(View convertView, int position, TableContentBean tableContentBean, int parentPosition) {
        //TODO 待优化回收机制
        LinearLayoutViewHolder layoutViewHolder = null;
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(getItemLayoutId(), null, false);
            layoutViewHolder = new LinearLayoutViewHolder(convertView);
            convertView.setTag(layoutViewHolder);
            convertView.setPadding(itemPaddingLeft, 0, itemPaddingRight, 0);
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(itemWidth, ViewGroup.LayoutParams.MATCH_PARENT);
            convertView.setLayoutParams(layoutParams);
        } else {
            layoutViewHolder = (LinearLayoutViewHolder) convertView.getTag();
        }
        bindContentHolder(layoutViewHolder, position, tableContentBean, parentPosition);
        return convertView;
    }

    protected abstract int getItemLayoutId();

    protected abstract void bindContentHolder(LinearLayoutViewHolder layoutViewHolder, int position, TableContentBean tableContentBean, int parentPosition);

    private int count;//item的数目
    private int itemWidth;
    private int itemHeight;
    private int itemPaddingLeft;
    private int itemPaddingRight;

    public void setDays(int count) {
        this.count = count;
    }

    public void setItemWidth(int itemWidth) {
        this.itemWidth = itemWidth;
    }

    public void setItemHeight(int itemHeight) {
        this.itemHeight = itemHeight;
    }

    public void setItemPaddingLeft(int itemPaddingLeft) {
        this.itemPaddingLeft = itemPaddingLeft;
    }

    public void setItemPaddingRight(int itemPaddingRight) {
        this.itemPaddingRight = itemPaddingRight;
    }

}
