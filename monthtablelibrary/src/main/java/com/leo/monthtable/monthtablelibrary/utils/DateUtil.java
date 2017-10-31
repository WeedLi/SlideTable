package com.leo.monthtable.monthtablelibrary.utils;

import android.annotation.SuppressLint;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class DateUtil {

    /**
     * 通过年份和月份 得到当月的日子
     *
     * @param year
     * @param month
     * @return
     */
    public static int getMonthDaysNormal(int year, int month) {
        switch (month) {
            case 1:
            case 3:
            case 5:
            case 7:
            case 8:
            case 10:
            case 12:
                return 31;
            case 4:
            case 6:
            case 9:
            case 11:
                return 30;
            case 2:
                if (((year % 4 == 0) && (year % 100 != 0)) || (year % 400 == 0)) {
                    return 29;
                } else {
                    return 28;
                }
            default:
                return -1;
        }
    }

    public static String getYearAndMonth() {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH) + 1;
        String date = year + "-" + getAddZeroToNum(month);
        return date;
    }

    /**
     * 小于10的数字前面加0
     *
     * @param num
     * @return
     */
    public static String getAddZeroToNum(int num) {
        String str = "";
        if (num < 10)
            str = "0" + num;
        else
            str = "" + num;
        return str;
    }


    public static int getYear() {
        return Calendar.getInstance().get(Calendar.YEAR);
    }

    public static int getMonth() {
        return Calendar.getInstance().get(Calendar.MONTH) + 1;
    }

    public static int getDay() {
        return Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
    }

    /**
     * 获取今天的日期
     *
     * @return
     */
    public static int getCurrentMonthDay() {
        return Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
    }

    public static int getWeekDay() {
        return Calendar.getInstance().get(Calendar.DAY_OF_WEEK);
    }

    public static int getHour() {
        return Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
    }

    public static int getMinute() {
        return Calendar.getInstance().get(Calendar.MINUTE);
    }

    public static int getSecond() {
        return Calendar.getInstance().get(Calendar.SECOND);
    }

    public static int[] getWeekSunday(int year, int month, int day, int pervious) {
        int[] time = new int[3];
        Calendar c = Calendar.getInstance();
        c.set(Calendar.YEAR, year);
        c.set(Calendar.MONTH, month);
        c.set(Calendar.DAY_OF_MONTH, day);
        c.add(Calendar.DAY_OF_MONTH, pervious);
        time[0] = c.get(Calendar.YEAR);
        time[1] = c.get(Calendar.MONTH) + 1;
        time[2] = c.get(Calendar.DAY_OF_MONTH);
        return time;

    }

    /**
     * 拿到第一天是星期几, 在这里控制星期的显示时间
     *
     * @param year
     * @param month
     * @return
     */
    public static int getWeekDayFromDate(int year, int month) {
        // TODO
        Calendar cal = Calendar.getInstance();
        cal.setTime(getDateFromString(year, month));
        int week_index = cal.get(Calendar.DAY_OF_WEEK);// 返回的是,1:星期日,2:星期一.....7:星期六
        week_index -= 1;
        return week_index;
    }

    @SuppressLint("SimpleDateFormat")
    public static Date getDateFromString(int year, int month) {
        String dateString = year + "-" + (month > 9 ? month : ("0" + month)) + "-01";
        Date date = null;
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-M-d");
            date = sdf.parse(dateString);
        } catch (ParseException e) {
            System.out.println(e.getMessage());
        }
        return date;
    }

    /**
     * 获得一个日期所在的周的星期几的日期，如要找出2002年2月3日所在周的星期一是几号
     *
     * @param sdate
     * @param num
     * @return
     */
    @SuppressLint("SimpleDateFormat")
    public static String getWeek(String sdate, String num) {
        // 再转换为时间
//        Date dd = reduceOne(sdate);
        Date dd = strToDate(sdate);
        Calendar c = Calendar.getInstance();
        c.setTime(dd);
        if (num.equals("1")) // 返回星期一所在的日期
            c.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
        else if (num.equals("2")) // 返回星期二所在的日期
            c.set(Calendar.DAY_OF_WEEK, Calendar.TUESDAY);
        else if (num.equals("3")) // 返回星期三所在的日期
            c.set(Calendar.DAY_OF_WEEK, Calendar.WEDNESDAY);
        else if (num.equals("4")) // 返回星期四所在的日期
            c.set(Calendar.DAY_OF_WEEK, Calendar.THURSDAY);
        else if (num.equals("5")) // 返回星期五所在的日期
            c.set(Calendar.DAY_OF_WEEK, Calendar.FRIDAY);
        else if (num.equals("6")) // 返回星期六所在的日期
            c.set(Calendar.DAY_OF_WEEK, Calendar.SATURDAY);
        else if (num.equals("0")) // 返回星期日所在的日期
            c.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
        return new SimpleDateFormat("yyyy-M-d").format(c.getTime());
    }

    // 把字符串转为日期
    @SuppressLint("SimpleDateFormat")
    public static Date ConverToDate(String strDate) throws Exception {
        DateFormat df = new SimpleDateFormat("yyyy-M-d");
        return df.parse(strDate);
    }

    /**
     * 将短时间格式字符串转换为时间 yyyy-MM-dd
     *
     * @param strDate
     * @return
     */
    public static Date strToDate(String strDate) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-M-d");
        ParsePosition pos = new ParsePosition(0);
        Date strtodate = formatter.parse(strDate, pos);
        return strtodate;
    }

    public static boolean isSunday(Date dd) {
        Calendar c = Calendar.getInstance();
        c.setTime(dd);
        String mWay = String.valueOf(c.get(Calendar.DAY_OF_WEEK));
        if ("0".equals(mWay)) {
            return true;
        }
        return false;
    }

    /**
     * 获取到这个日期往后退一天的日期
     *
     * @param str
     * @return
     */
    public static Date reduceOne(String str) {
        Calendar calendarBefore = new GregorianCalendar();
        Date date = strToDate(str);
        calendarBefore.setTime(date);
        calendarBefore.add(calendarBefore.DATE, -1);// 把日期往前推一天.整数往后推,负数往前移动
        date = calendarBefore.getTime(); // 这个时间就是日期往前推一天的结果
        return date;
    }

    /**
     * 获取当前月份,格式是2015-05
     *
     * @return
     */
    public static String getCurrentMonth(int year, int month) {
        if (month < 10) {
            return year + "-" + "0" + month;
        }
        return year + "-" + month;
    }


    /**
     * 获取前一个月格式是2015-05
     *
     * @return
     */
    public static String getPreMonth(int year1, int month1) {
        // 前一个月
        int month = month1 - 1;
        int year = year1;
        if (month < 1) {
            year -= 1;
            month = 12;
        }
        if (month < 10) {
            return year + "-" + "0" + month;
        }
        return year + "-" + month;
    }

    /**
     * 获取后一个月格式是2015-05
     *
     * @return
     */
    public static String getNextMonth(int year1, int month1) {
        // 后一个月
        int month = month1 + 1;
        int year = year1;
        if (month > 12) {
            year += 1;
            month = 1;
        }
        if (month < 10) {
            return year + "-" + "0" + month;
        }
        return year + "-" + month;
    }

    /**
     * 获取当前月份,格式是2015-05-01
     *
     * @return
     */
    public static String getCurrentMonthDays(int year1, int month1, int day1) {
        int day = day1;
        String d = day + "";
        if (day < 10) {
            d = "0" + day;
        }
        if (month1 < 10) {
            return year1 + "-" + "0" + month1 + "-" + d;
        }
        return year1 + "-" + month1 + "-" + d;
    }

    public static String getCurTime() {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        Date curDate = new Date(System.currentTimeMillis());// 获取当前时间
        return formatter.format(curDate);
    }

    public static String getBeginTime() {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date curDate = new Date(System.currentTimeMillis());// 获取当前时间
        return formatter.format(curDate);
    }


}
