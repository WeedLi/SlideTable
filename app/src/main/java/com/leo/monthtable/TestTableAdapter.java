package com.leo.monthtable;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.leo.monthtable.bean.TestTableContentBean;
import com.leo.monthtable.bean.TestTableFirstColumn;
import com.leo.monthtable.bean.TestTableFirstRow;
import com.leo.monthtable.utils.DateUtil;
import com.leo.tablelibrary.adapter.BaseViewHolder;
import com.leo.tablelibrary.adapter.ITableAdapter;
import com.leo.tablelibrary.adapter.LinearLayoutViewHolder;
import com.leo.tablelibrary.bean.TableContentBean;
import com.leo.tablelibrary.bean.TableFirstColumnBean;
import com.leo.tablelibrary.bean.TableFirstRowBean;

import java.util.List;
import java.util.Random;

/**
 * 作者：Leo on 2017/11/2 17:36
 * <p>
 * 描述：
 */

public class TestTableAdapter implements ITableAdapter {

    private Context mContext;
    List<TableFirstRowBean> fRowBeans;
    List<TableFirstColumnBean> fColumnBeans;
    List<List<TableContentBean>> contentBeans;

    public TestTableAdapter(Context mContext, List<TableFirstRowBean> fRowBeans,
                            List<TableFirstColumnBean> fColumnBeans, List<List<TableContentBean>> contentBeans) {
        this.mContext = mContext;
        this.fRowBeans = fRowBeans;
        this.fColumnBeans = fColumnBeans;
        this.contentBeans = contentBeans;
    }

    @Override
    public int getColumnCount() {
        return fRowBeans.size();
    }

    @Override
    public int getFirstRowHeight() {
        return (int) getDimen(R.dimen.height1);
    }

    @Override
    public int getFirstColumnWidth() {
        return (int) getDimen(R.dimen.x240);
    }

    @Override
    public int getOtherRowHeight() {
        return (int) getDimen(R.dimen.x300);
    }

    @Override
    public int getOtherColumnWidth() {
        return (int) getDimen(R.dimen.x270);
    }

    @Override
    public int getColumnPadding() {
        return (int) getDimen(R.dimen.padding0) * 2;
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    //
    //  第一行
    //
    ////////////////////////////////////////////////////////////////////////////////////////////////

    @Override
    public int getFirstRowLayoutId() {
        return R.layout.item_date_view;
    }

    @Override
    public List<TableFirstRowBean> getFirstRowData() {
        return fRowBeans;
    }

    @Override
    public void bindFirstRowItemHolder(BaseViewHolder holder, int position, TableFirstRowBean tableRowBean) {
        TextView tvWeek = holder.getTextView(R.id.tv_week);
        TextView tvDay = holder.getTextView(R.id.tv_day);
        tvWeek.setText(DateUtil.getWeekTest(((TestTableFirstRow) tableRowBean).date));
        if (position + 1 < 10)
            tvDay.setText("0" + (position + 1));
        else
            tvDay.setText("" + (position + 1));
        //判断是不是今天
        if (DateUtil.isToday(((TestTableFirstRow) tableRowBean).date)) {
            tvWeek.setTextColor(mContext.getResources().getColor(R.color.white));
            tvDay.setTextColor(mContext.getResources().getColor(R.color.white));
            holder.getView(R.id.llyt_row_bg).setBackgroundResource(R.drawable.shape_table_today_bg);
        } else {
            tvWeek.setTextColor(mContext.getResources().getColor(R.color.secondTextColor));
            tvDay.setTextColor(mContext.getResources().getColor(R.color.firstTextColor));
            holder.getView(R.id.llyt_row_bg).setBackgroundResource(0);
        }
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    //
    //   第一列
    //
    ////////////////////////////////////////////////////////////////////////////////////////////////

    @Override
    public int getFirstColumnLayoutId() {
        return R.layout.item_table_col_view;
    }

    @Override
    public List<TableFirstColumnBean> getFirstColumnData() {
        return fColumnBeans;
    }

    @Override
    public void bindFirstColumnItemHolder(BaseViewHolder holder, int position, TableFirstColumnBean tableColumnBean) {
        holder.setText(R.id.tv_name, ((TestTableFirstColumn) tableColumnBean).name);
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    //
    //  表格的每一格
    //
    ////////////////////////////////////////////////////////////////////////////////////////////////

    @Override
    public int getContentLayoutId() {
        return R.layout.item_content_item_view;
    }

    @Override
    public List<List<TableContentBean>> getContentData() {
        return contentBeans;
    }

    @Override
    public void bindContentItemHolder(LinearLayoutViewHolder holder, int position, TableContentBean tableContentBean, int parentPosition) {
        LinearLayout rootView = holder.getView(R.id.llyt_bg);
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
        holder.setText(R.id.tv_content, parentPosition + ":" + ((TestTableContentBean) tableContentBean).content + ":" + position);

        rootView.setTag(parentPosition + ":" + ((TestTableContentBean) tableContentBean).content + ":" + position);
        rootView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String tag = (String) v.getTag();
                String[] split = tag.split(":");
                TestTableFirstRow tableFirstRowBean = (TestTableFirstRow) fRowBeans.get(Integer.parseInt(split[2]));
                TestTableFirstColumn tableFirstColumnBean = (TestTableFirstColumn) fColumnBeans.get(Integer.parseInt(split[0]));
                new InformationPopupWindow((Activity) mContext, tableFirstColumnBean.name
                        , tableFirstRowBean.date, (String) v.getTag()).showThePopupWindow(v);
            }
        });
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    //
    //
    //
    ////////////////////////////////////////////////////////////////////////////////////////////////

    private float getDimen(int resId) {
        return mContext.getResources().getDimension(resId);
    }
}
