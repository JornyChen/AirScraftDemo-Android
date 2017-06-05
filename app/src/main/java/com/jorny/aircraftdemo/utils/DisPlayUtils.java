package com.jorny.aircraftdemo.utils;/*
 * @Description:
 * 作者  陈兆龙
 * 时间 2017年06月03
 * 
 */

import android.content.Context;

public class DisPlayUtils {
    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    public static int px2dip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }
}
