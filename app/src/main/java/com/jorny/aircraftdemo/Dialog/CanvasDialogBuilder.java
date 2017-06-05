package com.jorny.aircraftdemo.Dialog;/*
 * @Description:
 * 作者  陈兆龙
 * 时间 2017年06月03
 * 
 */

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.text.TextPaint;

import com.jorny.aircraftdemo.utils.DisPlayUtils;

public class CanvasDialogBuilder implements AirCraftDialogBuilder {

    private Paint mTextPaint;
    private Paint mPaint;
    private float borderSize;
    private float fontSize2;
    private Rect mPostiveButtonRect;
    private AirCraftDialogBuilderInfo info;
    public CanvasDialogBuilder(Context context) {
        init(context);
    }

    private void init(Context context) {
        mPaint = new Paint();
        mPaint.setStyle(Paint.Style.FILL);
        mTextPaint = new TextPaint(Paint.ANTI_ALIAS_FLAG | Paint.FAKE_BOLD_TEXT_FLAG);
        mTextPaint.setColor(0x88000000);
        mTextPaint.setTextSize(DisPlayUtils.dip2px(context, 12f));
        borderSize = DisPlayUtils.dip2px(context, 2);
        fontSize2 = DisPlayUtils.dip2px(context, 20);
    }

    @Override
    public void ShowAlertDialog(Context context, Canvas canvas, AirCraftDialogBuilderInfo dialogBuilderInfo) {
        this.info=dialogBuilderInfo;
        int canvasWidth = canvas.getWidth();
        int canvasHeight = canvas.getHeight();
        //存储原始值
        float originalFontSize = mTextPaint.getTextSize();
        Paint.Align originalFontAlign = mTextPaint.getTextAlign();
        int originalColor = mPaint.getColor();
        Paint.Style originalStyle = mPaint.getStyle();
        int w1 = (int) (20.0 / 360.0 * canvasWidth);
        int w2 = canvasWidth - 2 * w1;
        int buttonWidth = (int) (140.0 / 360.0 * canvasWidth);

        int h1 = (int) (150.0 / 558.0 * canvasHeight);
        int h2 = (int) (60.0 / 558.0 * canvasHeight);
        int h3 = (int) (124.0 / 558.0 * canvasHeight);
        int h4 = (int) (76.0 / 558.0 * canvasHeight);
        int buttonHeight = (int) (42.0 / 558.0 * canvasHeight);

        canvas.translate(w1, h1);
        //绘制背景色
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setColor(0xFFD7DDDE);
        Rect rect1 = new Rect(0, 0, w2, canvasHeight - 2 * h1);
        canvas.drawRect(rect1, mPaint);
        //绘制边框
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setColor(0xFF515151);
        mPaint.setStrokeWidth(borderSize);
        //mPaint.setStrokeCap(Paint.Cap.ROUND);
        mPaint.setStrokeJoin(Paint.Join.ROUND);
        canvas.drawRect(rect1, mPaint);
        mTextPaint.setTextSize(fontSize2);
        mTextPaint.setTextAlign(Paint.Align.CENTER);
        canvas.drawText(dialogBuilderInfo.getTitle(), w2 / 2, (h2 - fontSize2) / 2 + fontSize2, mTextPaint);
        canvas.translate(0, h2);
        canvas.drawLine(0, 0, w2, 0, mPaint);

        canvas.drawText(dialogBuilderInfo.getMessage(), w2 / 2, (h3 - fontSize2) / 2 + fontSize2, mTextPaint);
        canvas.translate(0, h3);
        canvas.drawLine(0, 0, w2, 0, mPaint);
        Rect rect2 = new Rect();
        rect2.left = (w2 - buttonWidth) / 2;
        rect2.right = w2 - rect2.left;
        rect2.top = (h4 - buttonHeight) / 2;
        rect2.bottom = h4 - rect2.top;
        canvas.drawRect(rect2, mPaint);
        canvas.translate(0, rect2.top);
        canvas.drawText(dialogBuilderInfo.getPostiveText(), w2 / 2, (buttonHeight - fontSize2) / 2 + fontSize2, mTextPaint);
        mPostiveButtonRect = new Rect(rect2);
        mPostiveButtonRect.left = w1 + rect2.left;
        mPostiveButtonRect.right = mPostiveButtonRect.left + buttonWidth;
        mPostiveButtonRect.top = h1 + h2 + h3 + rect2.top;
        mPostiveButtonRect.bottom = mPostiveButtonRect.top + buttonHeight;
        //重置
        mTextPaint.setTextSize(originalFontSize);
        mTextPaint.setTextAlign(originalFontAlign);
        mPaint.setColor(originalColor);
        mPaint.setStyle(originalStyle);
    }

    /**
     * 是否点击了按钮
     * @param x
     * @param y
     * @return
     */
    public boolean isClickPostiveButton(float x, float y) {
        return mPostiveButtonRect.contains((int) x, (int) y);
    }

    @Override
    public void clickPostiveButton() {
        if (info != null && info.getListener() != null) {
            info.getListener().onClick();
        }
    }
}
