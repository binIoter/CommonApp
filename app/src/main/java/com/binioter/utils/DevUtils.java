package com.binioter.utils;

import android.content.Context;
import android.util.DisplayMetrics;
import android.view.Surface;
import android.view.WindowManager;

/**
 * Created by zhangbin on 16/5/31.
 */
public class DevUtils {

    static int displayMetricsWidthPixels;

    public static int getEquipmentWidth(Context context) {
        DisplayMetrics metric = new DisplayMetrics();
        WindowManager manager = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);
        manager.getDefaultDisplay().getMetrics(metric);
        int orientation = manager.getDefaultDisplay().getOrientation();
        if (orientation == Surface.ROTATION_90 || orientation == Surface.ROTATION_270) {
            displayMetricsWidthPixels = metric.heightPixels; // 屏幕宽度（像素）
        } else {
            displayMetricsWidthPixels = metric.widthPixels; // 屏幕宽度（像素）
        }
        return displayMetricsWidthPixels;
    }
}
