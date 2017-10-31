package com.leo.monthtable.monthtablelibrary;

import java.util.List;


/**
 * 作者：Leo on 2017/9/29 13:35
 * <p>
 * 描述：
 */

public class TableColAdapter extends BaseAdapter<String> {

    public TableColAdapter(List<String> mDatas) {
        super(mDatas);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.item_table_col_view;
    }

    @Override
    protected void bindViewHolder(BaseViewHolder holder, int position, String s) {

        holder.setText(R.id.tvName, s);
    }
}
