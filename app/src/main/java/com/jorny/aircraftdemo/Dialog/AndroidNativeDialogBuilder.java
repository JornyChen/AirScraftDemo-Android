package com.jorny.aircraftdemo.Dialog;
/*
 * @Description:
 * 作者  陈兆龙
 * 时间 2017年06月03
 * 
 */

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Canvas;
import android.support.v7.app.AlertDialog;

public class AndroidNativeDialogBuilder implements AirCraftDialogBuilder {

    @Override
    public void ShowAlertDialog(Context context, Canvas canvas, final AirCraftDialogBuilderInfo dialogDisPlayVo) {
        AlertDialog.Builder buider = new AlertDialog.Builder(context);
        buider.setTitle(dialogDisPlayVo.getTitle());
        buider.setMessage(dialogDisPlayVo.getMessage());
        buider.setCancelable(false);
        buider.setPositiveButton(dialogDisPlayVo.getPostiveText(), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogDisPlayVo.getListener().onClick();
            }
        });
        buider.show();
    }

    @Override
    public boolean isClickPostiveButton(float x, float y) {
        return false;
    }

    @Override
    public void clickPostiveButton() {
    }


}
