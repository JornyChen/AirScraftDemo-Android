package com.jorny.aircraftdemo;/*
 * @Description:
 * 作者  陈兆龙
 * 时间 2017年06月02
 * 
 */

import android.app.Application;
import android.content.Context;

public class ApplycationContext extends Application {

    public static Context sContext;
    @Override
    public void onCreate() {
        super.onCreate();
        sContext = this;
    }
}
