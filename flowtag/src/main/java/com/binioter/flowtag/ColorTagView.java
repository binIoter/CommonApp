package com.binioter.flowtag;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.GradientDrawable;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.binioter.util.DensityUtil;

/**
 * 创建时间：2016/09/07 18:43 <br>
 * 作者：zhangbin <br>
 * 描述：彩色标签view
 */
public class ColorTagView extends TextView {
  private static final String DEFAULT_BACKGROUND_TRANSPARENCY = "26";
  private static final String DEFAULT_TEXT_COLOR = "849AAE";
  private static final String DEFAULT_BACKGROUND_COLOR = "f0f3f5";

  private float mTextCenterX;   //文本中轴线X坐标
  private float mTextBaselineY; //文本baseline线Y坐标
  private Paint mPaint;         //控件画笔
  private Paint.FontMetrics mFontMetrics;

  public ColorTagView(Context context, ColorTag colorTag) {
    super(context);
    //setTypeface(MyApplication.getInstance().typeface);
    int textColor = TagUtil.parseColor(colorTag.color, DEFAULT_TEXT_COLOR);//文字颜色
    int fillColor = TagUtil.parseColor(DEFAULT_BACKGROUND_TRANSPARENCY + colorTag.color,
        DEFAULT_BACKGROUND_COLOR);//背景颜色
    GradientDrawable gd = new GradientDrawable();//创建drawable
    gd.setColor(fillColor);
    gd.setCornerRadius(DensityUtil.dip2px(context, 2));
    setTextColor(textColor);
    setBackgroundDrawable(gd);
    setTextSize(13);
    setText(colorTag.text);
    setPadding(DensityUtil.dip2px(context, 4), DensityUtil.dip2px(context, 1),
        DensityUtil.dip2px(context, 4), DensityUtil.dip2px(context, 1));
    LinearLayout.LayoutParams params =
        new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
            LinearLayout.LayoutParams.WRAP_CONTENT);
    setLayoutParams(params);
  }

  @Override protected void onDraw(Canvas canvas) {
    mPaint = getPaint();
    mPaint.setColor(getCurrentTextColor());
    mPaint.setTextAlign(Paint.Align.CENTER);
    setTextLocation();
    canvas.drawText(getText().toString(), mTextCenterX, mTextBaselineY, mPaint);
  }

  /**
   * 定位文本绘制的位置
   */
  private void setTextLocation() {
    int viewWidth = getWidth();
    int viewHeight = getHeight();
    mFontMetrics = mPaint.getFontMetrics();
    float textCenterVerticalBaselineY =
        viewHeight / 2 - mFontMetrics.descent + (mFontMetrics.descent - mFontMetrics.ascent) / 2;
    mTextCenterX = (float) viewWidth / 2;
    mTextBaselineY = textCenterVerticalBaselineY;
  }
}
