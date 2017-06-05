package com.jorny.aircraftdemo.model;
/*
 * @Description:
 * 作者  陈兆龙
 * 时间 2017年06月04
 * 
 */

import android.os.Parcel;
import android.os.Parcelable;

/***
 * 设置信息
 */
public class SettingInfo implements Parcelable {
    private boolean isVoiceOpen;
    private boolean isSysDialog;
    private boolean isBulleInfinite;//是否子弹无限

    public SettingInfo(boolean isVoiceOpen, boolean isSysDialog, boolean isBulleInfinite) {
        this.isVoiceOpen = isVoiceOpen;
        this.isSysDialog = isSysDialog;
        this.isBulleInfinite = isBulleInfinite;
    }

    public boolean isVoiceOpen() {
        return isVoiceOpen;
    }

    public void setVoiceOpen(boolean voiceOpen) {
        isVoiceOpen = voiceOpen;
    }

    public boolean isSysDialog() {
        return isSysDialog;
    }

    public void setSysDialog(boolean sysDialog) {
        isSysDialog = sysDialog;
    }

    public boolean isBulleInfinite() {
        return isBulleInfinite;
    }

    public void setBulleInfinite(boolean bulleInfinite) {
        isBulleInfinite = bulleInfinite;
    }


    protected SettingInfo(Parcel in) {
        isVoiceOpen = in.readByte() != 0;
        isSysDialog = in.readByte() != 0;
        isBulleInfinite = in.readByte() != 0;
    }

    public static final Creator<SettingInfo> CREATOR = new Creator<SettingInfo>() {
        @Override
        public SettingInfo createFromParcel(Parcel in) {
            return new SettingInfo(in);
        }

        @Override
        public SettingInfo[] newArray(int size) {
            return new SettingInfo[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeByte((byte) (isVoiceOpen ? 1 : 0));
        parcel.writeByte((byte) (isSysDialog ? 1 : 0));
        parcel.writeByte((byte) (isBulleInfinite ? 1 : 0));
    }
}
