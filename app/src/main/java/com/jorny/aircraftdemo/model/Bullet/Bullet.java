package com.jorny.aircraftdemo.model.Bullet;
/*
 * @Description:
 * 作者  陈兆龙
 * 时间 2017年06月02
 * 
 */

import android.graphics.Bitmap;

import com.jorny.aircraftdemo.model.LinearRunAirCraft;


public class Bullet extends LinearRunAirCraft {
    public int getPower() {
        return mPower;
    }

    public void setPower(int power) {
        this.mPower = power;
    }

    protected int mPower = 1;

    public Bullet(Bitmap bitmap) {
        super(bitmap);
    }

}
