package com.jorny.aircraftdemo.model.EnemyAirCraft;
/*
 * @Description:
 * 作者  陈兆龙
 * 时间 2017年06月02
 *
 */

import android.graphics.Bitmap;

/***
 * 侦察机
 */
public class SpyPlane extends EnemyAirCraft {
    public final static String TAG = SpyPlane.class.getSimpleName();

    public SpyPlane(Bitmap bitmap) {
        super(bitmap);
        mOriginalSpeed = 4;
        mMaxLife = 1;
        mScore=200;
        initEnemyAirCraftInfo();
    }

}
