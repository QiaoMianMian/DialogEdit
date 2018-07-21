package com.dialog.edit.units;

import android.annotation.TargetApi;
import android.graphics.Outline;
import android.os.Build;
import android.view.View;
import android.view.ViewOutlineProvider;

public class LayoutUtils {

    //视图切圆角
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public static void setClipViewCornerRadius(View view, final int radius) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            //不支持5.0版本以下的系统
            return;
        }
        if (view == null) return;
        if (radius <= 0) {
            return;
        }
        view.setOutlineProvider(new ViewOutlineProvider() {

            @Override
            public void getOutline(View view, Outline outline) {
                outline.setRoundRect(0, 0, view.getWidth(), view.getHeight(), radius);
            }
        });
        view.setClipToOutline(true);
    }

}
