package com.binioter.util;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * 创建时间: 2016/11/18 13:43 <br>
 * 作者: zhangbin <br>
 * 描述: UI工具类
 */
public class UIUtils {

  public static View inflate(Context context, int resId) {
    return LayoutInflater.from(context).inflate(resId, null);
  }

  public static Resources getResources(Context context) {
    return context.getResources();
  }

  public static String getString(Context context, int resId) {
    return getResources(context).getString(resId);
  }

  public static String getString(Context context, int id, Object... formatArgs)
      throws Resources.NotFoundException {
    return getResources(context).getString(id, formatArgs);
  }

  /** 获取文字数组 */
  public static String[] getStringArray(Context context, int resId) {
    return getResources(context).getStringArray(resId);
  }

  /** 获取dimen */
  public static int getDimens(Context context, int resId) {
    return getResources(context).getDimensionPixelSize(resId);
  }

  /** 获取drawable */
  public static Drawable getDrawable(Context context, int resId) {
    return getResources(context).getDrawable(resId);
  }

  /** 获取颜色 */
  public static int getColor(Context context, int resId) {
    return getResources(context).getColor(resId);
  }

  /** 获取颜色选择器 */
  public static ColorStateList getColorStateList(Context context, int resId) {
    return getResources(context).getColorStateList(resId);
  }

  public static AssetFileDescriptor openRawResourceFd(Context context, int beep) {
    return getResources(context).openRawResourceFd(beep);
  }

  public static AssetManager getAssets(Context context) {
    return getResources(context).getAssets();
  }

  public static DisplayMetrics getDisplayMetrics(Context context) {
    return getResources(context).getDisplayMetrics();
  }

  public static Bitmap toRoundCorner(Bitmap bitmap, float pixels) {
    Bitmap output = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Config.ARGB_8888);
    Canvas canvas = new Canvas(output);

    final int color = 0xff424242;
    final Paint paint = new Paint();
    final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
    final RectF rectF = new RectF(rect);
    final float roundPx = pixels;

    paint.setAntiAlias(true);
    canvas.drawARGB(0, 0, 0, 0);

    paint.setColor(color);
    canvas.drawRoundRect(rectF, roundPx, roundPx, paint);
    paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
    canvas.drawBitmap(bitmap, rect, rect, paint);

    return output;
  }

  public static void saveBitmap2File(Bitmap bm, String fileDir) {
    File round_file = new File(fileDir);
    if (round_file.exists()) {
      round_file.delete();
    }

    try {
      FileOutputStream out = new FileOutputStream(round_file);
      bm.compress(Bitmap.CompressFormat.PNG, 90, out);
      out.flush();
      out.close();
    } catch (FileNotFoundException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    } catch (IOException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
  }

  /**
   * 直接使用，不用转换View
   */
  @SuppressWarnings("unchecked") public static <T extends View> T findViewById(View view, int id) {
    return (T) view.findViewById(id);
  }

  /** 动态设置view的margin */
  public static void setMargins(View v, int l, int t, int r, int b) {
    if (v.getLayoutParams() instanceof ViewGroup.MarginLayoutParams) {
      ViewGroup.MarginLayoutParams p = (ViewGroup.MarginLayoutParams) v.getLayoutParams();
      p.setMargins(l, t, r, b);
      v.requestLayout();
    }
  }
}
