package com.leo.monthtable.monthtablelibrary;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.util.List;
import java.util.Random;


/**
 * 作者：Leo on 2017/9/29 13:40
 * <p>
 * 描述：
 */

public class ContentAdapter extends BaseAdapter<String> {

    private int days;//天数

    public ContentAdapter(List<String> mDatas) {
        super(mDatas);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.item_content_view;
    }

    @Override
    protected void bindViewHolder(BaseViewHolder holder, int position, String s) {
        LinearLayout container = (LinearLayout) holder.getView(R.id.container);
        LinearLayout root = (LinearLayout) holder.getView(R.id.root);
        int dimen = (int) getDimen(R.dimen.x270);
        int padding0 = (int) getDimen(R.dimen.padding0);
        ViewGroup.LayoutParams rootParams = root.getLayoutParams();
        rootParams.width = dimen * days + padding0 * 2;
        root.setLayoutParams(rootParams);
        ViewGroup.LayoutParams layoutParams = container.getLayoutParams();
        layoutParams.width = dimen * days;
        container.setLayoutParams(layoutParams);
        updateUI(container, s);
    }

    private void updateUI(LinearLayout container, String s) {
        int childCount = container.getChildCount();
        if (childCount == 0) {
            for (int i = 0; i < 31; i++) {
                container.addView(createItemView(null, s + (i + 1)));
            }
        } else {
            for (int i = 0; i < childCount; i++) {
                createItemView(container.getChildAt(i), s + (i + 1));
            }
        }
    }

    private View createItemView(View convertView, final String s) {
        NestFullViewHolder nestFullViewHolder = null;
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_content_item_view, null, false);
            nestFullViewHolder = new NestFullViewHolder(mContext, convertView);
            convertView.setTag(nestFullViewHolder);
        } else {
            nestFullViewHolder = (NestFullViewHolder) convertView.getTag();
        }
        LinearLayout rootView = nestFullViewHolder.getView(R.id.rootView);
        ViewGroup.LayoutParams layoutParams = rootView.getLayoutParams();
        int dimen = (int) getDimen(R.dimen.x270);
        int padding0 = (int) getDimen(R.dimen.padding0);
        layoutParams.width = dimen * 1 - padding0 * 2;
        rootView.setLayoutParams(layoutParams);
        switch (new Random().nextInt(3)) {
            case 0:
                rootView.setBackgroundResource(R.drawable.shape_shift_table_freedom);
                break;
            case 1:
                rootView.setBackgroundResource(R.drawable.shape_shift_table_fixed);
                break;
            case 2:
                rootView.setBackgroundResource(R.drawable.shape_shift_table_multi_class);
                break;
            case 3:
                rootView.setBackgroundResource(R.drawable.shape_shift_table_rest);
                break;
        }
        rootView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(mContext, s, Toast.LENGTH_SHORT).show();
            }
        });
        nestFullViewHolder.setText(R.id.tvName, s);
        return convertView;
    }

    public void setDays(int days) {
        this.days = days;
    }

}
