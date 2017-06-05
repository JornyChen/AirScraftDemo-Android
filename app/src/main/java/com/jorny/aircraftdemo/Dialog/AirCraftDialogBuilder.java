package com.jorny.aircraftdemo.Dialog;/*
 * @Description:
 * 作者  陈兆龙
 * 时间 2017年06月03
 * 
 */

import android.content.Context;
import android.graphics.Canvas;

public interface AirCraftDialogBuilder {
    void ShowAlertDialog(Context context, Canvas canvas, AirCraftDialogBuilderInfo dialogDisPlayVo);

    boolean isClickPostiveButton(float x, float y);

    void clickPostiveButton();

    interface PostiveClickListener {
        void onClick();
    }
}



