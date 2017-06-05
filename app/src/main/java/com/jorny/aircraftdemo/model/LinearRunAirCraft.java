package com.jorny.aircraftdemo.model;
/*
 * @Description:
 * 作者  陈兆龙
 * 时间 2017年06月02
 * 
 */

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;

import com.jorny.aircraftdemo.Factory.AirCraftFactory;
import com.jorny.aircraftdemo.view.AirCraftDisPlayView;


public class LinearRunAirCraft extends BaseAirCraft {

    protected int mOriginalSpeed = 2;//默认是2
    protected int mSpeed = mOriginalSpeed;

    public int getSpeed() {
        return mSpeed;
    }

    public void setSpeed(int mSpeed) {
        this.mSpeed = mSpeed;
    }

    public LinearRunAirCraft(Bitmap bitmap) {
        super(bitmap);
    }

    @Override
    public void onBeforeDraw(Canvas canvas, Paint paint, AirCraftDisPlayView view) {
        if (!isDestroyed()) {
            //移动speed像素
            move(0, (mSpeed * view.getDensity()));
        }
    }

    @Override
    public void onAfterDraw(Canvas canvas, Paint paint, AirCraftDisPlayView view) {
        if (!isDestroyed()) {
            //检查Sprite是否超出了Canvas的范围，如果超出，则销毁Sprite
            RectF canvasRecF = new RectF(0, 0, canvas.getWidth(), canvas.getHeight());
            RectF spriteRecF = getRectF();
            if (!RectF.intersects(canvasRecF, spriteRecF)) {
                destroy();
            }
        }
    }

    @Override
    public void destroy() {
        super.destroy();
        AirCraftFactory.getInstance().addAirCraftToPool(this);
    }


}
