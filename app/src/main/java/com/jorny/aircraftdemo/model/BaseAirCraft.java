package com.jorny.aircraftdemo.model;/*
 * @Description:
 * 作者  陈兆龙
 * 时间 2017年06月02
 * 
 */

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.Log;

import com.jorny.aircraftdemo.view.AirCraftDisPlayView;


public abstract class BaseAirCraft { //飞行器类
    protected float x;
    protected float y;
    private float mWidth;
    private float mHeight;
    private Bitmap mBitmap;
    private boolean isDestroyed;
    protected boolean isVisable=true;
    public int getFrameIndex() {
        return mFrameIndex;
    }

    public void setFrameIndex(int frameIndex) {
        this.mFrameIndex = frameIndex;
    }

    private int mFrameIndex = 0;
    public BaseAirCraft(Bitmap bitmap) {
        Log.v("createAirCraft", "create" + this.getClass().getName());
        setBitmap(bitmap);
    }

    public float getX() {
        return x;
    }

    public void setX(float x) {
        this.x = x;
    }

    public float getY() {
        return y;
    }

    public void setY(float y) {
        this.y = y;
    }

    public float getWidth() {
        return mWidth;
    }


    public float getHeight() {
        return mHeight;
    }


    public Bitmap getBitmap() {
        return mBitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        this.mBitmap = bitmap;
        if (bitmap != null) {
            mWidth = bitmap.getWidth();
            mHeight = bitmap.getHeight();
        } else {
            mWidth = 0;
            mHeight = 0;
        }
    }

    public boolean isDestroyed() {
        return isDestroyed;
    }

    public void setDestroyed(boolean destroyed) {
        isDestroyed = destroyed;
    }

    protected void onDraw(Canvas canvas, Paint paint) {
        if (!isDestroyed && mBitmap != null) {
            //将Sprite绘制到Canvas上
            Rect srcRef = getBitmapSrcRec();
            RectF dstRecF = getRectF();
            if(isVisable) {
                canvas.drawBitmap(mBitmap, srcRef, dstRecF, paint);
            }
            mFrameIndex++;
        }
    }




    public RectF getRectF() {
        float left = x;
        float top = y;
        float right = left + getWidth();
        float bottom = top + getHeight();
        RectF rectF = new RectF(left, top, right, bottom);
        return rectF;
    }


    public void draw(Canvas canvas, Paint paint, AirCraftDisPlayView view) {
        if (canvas != null) {
            onBeforeDraw(canvas, paint, view);
            onDraw(canvas, paint);
            onAfterDraw(canvas, paint, view);
        }
    }

    public abstract void onBeforeDraw(Canvas canvas, Paint paint, AirCraftDisPlayView view);

    public abstract void onAfterDraw(Canvas canvas, Paint paint, AirCraftDisPlayView view);

    protected Rect getBitmapSrcRec() {
        Rect rect = new Rect();
        rect.left = 0;
        rect.top = 0;
        rect.right = (int) getWidth(); //一点要使用getWidth 不能使用getHeight
        rect.bottom = (int) getHeight();
        return rect;
    }

    /***
     * 移动偏移量
     * @param offsetX
     * @param offsetY
     */
    public void move(float offsetX, float offsetY) {
        this.x += offsetX;
        this.y += offsetY;
    }


    /***
     * 移动到
     * @param x
     * @param y
     */
    public void moveTo(float x, float y) {
        this.x = x;
        this.y = y;
    }

    public void centerTo(float cenX, float cenY) {
        float w = getWidth();
        float h = getHeight();
        x = cenX - w / 2;
        y = cenY - h / 2;
    }

    private void init() {

    }


    public void destroy() {
        isDestroyed = true;
        isVisable = true; //还原
    }

    public void finalDestory() {
        mBitmap = null;
        isDestroyed = false;
    }


    public boolean hasCollideWithOther(BaseAirCraft s) {
        RectF rectF1 = getRectF();
        RectF rectF2 = s.getRectF();
        RectF rectF = new RectF();
        return rectF.setIntersect(rectF1, rectF2);
    }
    /***
     * 恢复初始值  缓存使用
     */
    public void reset() {
        isDestroyed = false;
    }

    //飞行器爆炸爆炸包括子弹爆炸
    protected void explode(AirCraftDisPlayView disPlayView){

    }
}
