package com.leo.monthtable.monthtablelibrary;

import android.widget.TextView;

import com.leo.monthtable.monthtablelibrary.utils.DateUtil;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;


/**
 * 作者：Leo on 2017/9/29 12:23
 * <p>
 * 描述：日期的适配器
 */

public class DateAdapter extends BaseAdapter<String> {

    private String todayStr;

    public DateAdapter(List<String> mDatas) {
        super(mDatas);
        setTodayDate(DateUtil.getYear(), DateUtil.getMonth(), DateUtil.getDay());
    }

    private void setTodayDate(int year, int month, int day) {
        String m = "";
        if (month < 10)
            m = "0" + month;
        else
            m = "" + month;
        String d = "";
        if (day < 10)
            d = "0" + day;
        else
            d = "" + day;
        todayStr = year + "-" + m + "-" + d;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.item_date_view;
    }

    @Override
    protected void bindViewHolder(BaseViewHolder holder, int position, String str) {
        TextView tvWeek = holder.getTextView(R.id.tvWeek);
        TextView tvDay = holder.getTextView(R.id.tvDay);
        tvWeek.setText(getWeek(str));
        if (position + 1 < 10)
            tvDay.setText("0" + (position + 1));
        else
            tvDay.setText("" + (position + 1));
        //判断是不是今天
        String[] t = todayStr.split("-");
        String[] s = str.split("-");
        if (Integer.parseInt(t[0]) == Integer.parseInt(s[0])
                && Integer.parseInt(t[1]) == Integer.parseInt(s[1]) && Integer.parseInt(t[2]) == Integer.parseInt(s[2])) {
            tvWeek.setTextColor(getColor(R.color.white));
            tvDay.setTextColor(getColor(R.color.white));
            holder.getView(R.id.bgLlyt).setBackgroundResource(R.drawable.shape_table_today_bg);
        } else {
            tvWeek.setTextColor(getColor(R.color.secondTextColor));
            tvDay.setTextColor(getColor(R.color.firstTextColor));
            holder.getView(R.id.bgLlyt).setBackgroundResource(0);
        }
    }

    private String getWeek(String pTime) {
        String week = "";
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Calendar c = Calendar.getInstance();
        try {
            c.setTime(format.parse(pTime));
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        if (c.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY) {
            week = "Sun";
        }
        if (c.get(Calendar.DAY_OF_WEEK) == Calendar.MONDAY) {
            week = "Mon";
        }
        if (c.get(Calendar.DAY_OF_WEEK) == Calendar.TUESDAY) {
            week = "Tues";
        }
        if (c.get(Calendar.DAY_OF_WEEK) == Calendar.WEDNESDAY) {
            week = "Wed";
        }
        if (c.get(Calendar.DAY_OF_WEEK) == Calendar.THURSDAY) {
            week = "Thur";
        }
        if (c.get(Calendar.DAY_OF_WEEK) == Calendar.FRIDAY) {
            week = "Fri";
        }
        if (c.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY) {
            week = "Sat";
        }
        return week;
    }

}
