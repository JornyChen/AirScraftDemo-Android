package com.jorny.aircraftdemo.model.Bullet;
/*
 * @Description:
 * 作者  陈兆龙
 * 时间 2017年06月02
 * 
 */

import android.graphics.Bitmap;

@Deprecated
public class SuperBullet extends Bullet {
    public static final String TAG = SuperBullet.class.getSimpleName();

    public SuperBullet(Bitmap bitmap) {
        super(bitmap);
        setSpeed(-10);
        mPower = 2;
    }

}
