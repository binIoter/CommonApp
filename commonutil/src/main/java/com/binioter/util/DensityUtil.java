package com.binioter.util;

import android.content.Context;
import android.util.DisplayMetrics;
import android.view.Surface;
import android.view.WindowManager;
import java.lang.reflect.Field;

/**
 * 创建时间: 2016/11/25 11:22 <br>
 * 作者: zhangbin <br>
 * 描述: 单位转换工具类
 */

public class DensityUtil {
  static int displayMetricsWidthPixels;

  /**
   * dip转换px
   *
   * @param context 上下文
   * @param dpValue dip值
   * @return px值
   */

  public static int dip2px(Context context, float dpValue) {
    final float scale = UIUtils.getDisplayMetrics(context).density;
    return (int) (dpValue * scale + 0.5f);
  }

  /**
   * px转换dip
   *
   * @param context 上下文
   * @param pxValue px值
   * @return dip值
   */
  public static int px2dip(Context context, float pxValue) {
    final float scale = UIUtils.getDisplayMetrics(context).density;
    return (int) (pxValue / scale + 0.5f);
  }

  public static int getEquipmentWidth(Context context) {
    DisplayMetrics metric = new DisplayMetrics();
    WindowManager manager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
    manager.getDefaultDisplay().getMetrics(metric);
    int orientation = manager.getDefaultDisplay().getOrientation();
    if (orientation == Surface.ROTATION_90 || orientation == Surface.ROTATION_270) {
      displayMetricsWidthPixels = metric.heightPixels; // 屏幕宽度（像素）
    } else {
      displayMetricsWidthPixels = metric.widthPixels; // 屏幕宽度（像素）
    }
    return displayMetricsWidthPixels;
  }

  /**
   * 获取状态栏/通知栏的高度
   */
  public static int getStatusBarHeight(Context context) {
    Class<?> c = null;
    Object obj = null;
    Field field = null;
    int x = 0, sbar = 0;
    try {
      c = Class.forName("com.android.internal.R$dimen");
      obj = c.newInstance();
      field = c.getField("status_bar_height");
      x = Integer.parseInt(field.get(obj).toString());
      sbar = context.getResources().getDimensionPixelSize(x);
    } catch (Exception e1) {
      e1.printStackTrace();
    }
    return sbar;
  }
}
