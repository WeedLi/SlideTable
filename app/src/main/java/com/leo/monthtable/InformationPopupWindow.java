package com.leo.monthtable;

import android.app.Activity;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;
import android.widget.TextView;


/**
 * 作者：Leo on 2017/8/16 10:13
 * <p>
 * 描述：密码输入框的PopupWindow
 */

public class InformationPopupWindow extends PopupWindow {

    public InformationPopupWindow(final Activity context, String name, String date, String content) {
        View conentView = LayoutInflater.from(context).inflate(R.layout.popup_information, null, false);
        this.setContentView(conentView);
        this.setWidth(ViewGroup.LayoutParams.WRAP_CONTENT);
        this.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        this.setFocusable(true);
        this.setOutsideTouchable(true);
        this.update();
        ColorDrawable dw = new ColorDrawable(0000000000);
        this.setBackgroundDrawable(dw);
        setAnimationStyle(android.R.style.Animation_Dialog);
        TextView tvName = conentView.findViewById(R.id.tv_name);
        TextView tvDate = conentView.findViewById(R.id.tv_date);
        TextView tvContent = conentView.findViewById(R.id.tv_content);
        tvName.setText(name);
        tvDate.setText(date);
        tvContent.setText(content);
    }

    public void showThePopupWindow(View view) {
        showAtLocation(view, Gravity.CENTER, 0, 0);
    }

}
