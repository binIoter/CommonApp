package com.binioter.flowtag;

import android.graphics.Color;
import android.support.annotation.NonNull;
import android.text.TextUtils;

/**
 * 创建时间: 2016/11/25 11:20 <br>
 * 作者: zhangbin <br>
 * 描述: 标签工具类
 */

public class TagUtil {
  /**
   * 解析颜色，可以以#开头，也可以不用
   *
   * @param colorStr 39ac6a
   * @param defaultColor 39ac6a
   * @return Color.parseColor("39ac6a")
   */
  public static int parseColor(String colorStr, @NonNull String defaultColor) {
    int color;
    if (TextUtils.isEmpty(colorStr)) {
      colorStr = defaultColor;
    }
    String prefix = colorStr.startsWith("#") ? "" : "#";
    String prefixDef = defaultColor.startsWith("#") ? "" : "#";
    try {
      color = Color.parseColor(prefix + colorStr);
    } catch (Exception ex) {
      // 务必正确书写 defaultColor
      color = Color.parseColor(prefixDef + defaultColor);
      ex.printStackTrace();
    }
    return color;
  }
}
