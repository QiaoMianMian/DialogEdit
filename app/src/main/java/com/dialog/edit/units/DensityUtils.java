package com.dialog.edit.units;

import android.content.Context;
import android.util.DisplayMetrics;

public class DensityUtils {

    //dip 转 px
    public static int dip2px(Context context, int dip) {
        DisplayMetrics metrics = context.getResources().getDisplayMetrics();
        float density = metrics.density;
        return (int) (dip * density + 0.5f);
    }

    public static int px2dip(Context context, int px) {
        DisplayMetrics metrics = context.getResources().getDisplayMetrics();
        float density = metrics.density;
        return (int) (px / density + 0.5f);
    }
}
