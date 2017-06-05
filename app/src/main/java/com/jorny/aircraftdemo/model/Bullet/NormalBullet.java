package com.jorny.aircraftdemo.model.Bullet;
/*
 * @Description:
 * 作者  陈兆龙
 * 时间 2017年06月02
 * 
 */

import android.graphics.Bitmap;

public class NormalBullet extends Bullet {
    public static final String TAG = NormalBullet.class.getSimpleName() ;

    public NormalBullet(Bitmap bitmap) {
        super(bitmap);
        setSpeed(-15);
        mPower = 1;
    }

}
