package com.jorny.aircraftdemo.model;
/*
 * @Description:
 * 作者  陈兆龙
 * 时间 2017年06月03
 */

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;

import com.jorny.aircraftdemo.Factory.AirCraftFactory;
import com.jorny.aircraftdemo.view.AirCraftDisPlayView;


/**
 * 爆炸效果
 */
public class ExplosionPieces extends BaseAirCraft {
    public final static String TAG = ExplosionPieces.class.getSimpleName();
    private int segment = 14;
    private int level = 0;
    private int explodeFrequency = 2;//每个爆炸绘制2帧

    public ExplosionPieces(Bitmap bitmap) {
        super(bitmap);
    }

    @Override
    public float getWidth() {
        Bitmap bitmap = getBitmap();
        if (bitmap != null) {
            return bitmap.getWidth() / segment;
        }
        return 0;
    }


    @Override
    public void onBeforeDraw(Canvas canvas, Paint paint, AirCraftDisPlayView view) {

    }

    @Override
    public void onAfterDraw(Canvas canvas, Paint paint, AirCraftDisPlayView view) {
        if (!isDestroyed()) {
            if (getFrameIndex() % explodeFrequency == 0) {
                //level自加1，用于绘制下个爆炸片段
                level++;
                if (level >= segment) {
                    destroy();
                }
            }
        }
    }

    @Override
    public Rect getBitmapSrcRec() {
        Rect rect = super.getBitmapSrcRec();
        int left = (int) (level * getWidth());
        rect.offsetTo(left, 0);
        return rect;
    }


    public int getExplodeDurationFrame() {
        return segment * explodeFrequency;
    }

    @Override
    public void reset() {
        super.reset();
        level = 0;
    }

    @Override
    public void destroy() {
        super.destroy();
        AirCraftFactory.getInstance().addAirCraftToPool(this);
    }
}
