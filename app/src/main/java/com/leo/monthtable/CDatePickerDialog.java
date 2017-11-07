package com.leo.monthtable;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.os.Build;
import android.view.View;
import android.widget.DatePicker;


import java.lang.reflect.Field;

/**
 * 作者：Leo on 2017/8/8 15:40
 * <p>
 * 描述：
 */

public class CDatePickerDialog {

    private boolean isShowDay;

    int year;
    int month;
    int day;

    public CDatePickerDialog(String dateStr, boolean isShowDay) {
        this.isShowDay = isShowDay;
        String[] split = dateStr.split("-");
        year = Integer.parseInt(split[0]);
        month = Integer.parseInt(split[1]) - 1;
        if (isShowDay)
            day = Integer.parseInt(split[2]);
        else
            day = 1;
    }

    public void showDatePickDlg(Context mActivity, final OnCDateSelectListener dateSetListener,
                                DialogInterface.OnDismissListener dismissListener) {
        DatePickerDialog datePickerDialog = new DatePickerDialog(mActivity, AlertDialog.THEME_HOLO_LIGHT, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                if (dateSetListener != null) {
                    dateSetListener.onDateSel(year, ++month, dayOfMonth);
                }
            }
        }, year, month, day);
        datePickerDialog.show();
        datePickerDialog.getButton(DialogInterface.BUTTON_POSITIVE).setText("确定");
        datePickerDialog.getButton(DialogInterface.BUTTON_NEGATIVE).setText("取消");
        DatePicker datePicker = datePickerDialog.getDatePicker();
        datePicker.setSpinnersShown(true);
        datePicker.setCalendarViewShown(false);
        if (!isShowDay)
            hideDay(datePicker);
        datePickerDialog.setOnDismissListener(dismissListener);
    }

    //隐藏天
    private void hideDay(DatePicker mDatePicker) {
        try {
            /* 处理android5.0以上的特殊情况 */
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                int daySpinnerId = Resources.getSystem().getIdentifier("day", "id", "android");
                if (daySpinnerId != 0) {
                    View daySpinner = mDatePicker.findViewById(daySpinnerId);
                    if (daySpinner != null) {
                        daySpinner.setVisibility(View.GONE);
                    }
                }
            } else {
                Field[] datePickerfFields = mDatePicker.getClass().getDeclaredFields();
                for (Field datePickerField : datePickerfFields) {
                    if ("mDaySpinner".equals(datePickerField.getName()) || ("mDayPicker").equals(datePickerField.getName())) {
                        datePickerField.setAccessible(true);
                        Object dayPicker = new Object();
                        try {
                            dayPicker = datePickerField.get(mDatePicker);
                        } catch (IllegalAccessException e) {
                            e.printStackTrace();
                        } catch (IllegalArgumentException e) {
                            e.printStackTrace();
                        }
                        ((View) dayPicker).setVisibility(View.GONE);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public interface OnCDateSelectListener {
        void onDateSel(int year, int month, int dayOfMonth);
    }

}
