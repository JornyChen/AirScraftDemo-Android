package com.jorny.aircraftdemo.Dialog;
/*
 * @Description:
 * 作者  陈兆龙
 * 时间 2017年06月03
 * 
 */

public class AirCraftDialogBuilderInfo {
    public int getScore() {
        return mScore;
    }

    public void setScore(int mScore) {
        this.mScore = mScore;
    }

    public String getMessage() {
        return mMessage;
    }

    public void setMessage(String mMessage) {
        this.mMessage = mMessage;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String mTitle) {
        this.mTitle = mTitle;
    }

    public AirCraftDialogBuilder.PostiveClickListener getListener() {
        return listener;
    }

    public void setListener(AirCraftDialogBuilder.PostiveClickListener listener) {
        this.listener = listener;
    }

    private int mScore;
    private String mMessage;
    private String mTitle;
    private AirCraftDialogBuilder.PostiveClickListener listener;

    public String getPostiveText() {
        return mPostiveText;
    }

    public void setPostiveText(String mPostiveText) {
        this.mPostiveText = mPostiveText;
    }

    private String mPostiveText;
}
