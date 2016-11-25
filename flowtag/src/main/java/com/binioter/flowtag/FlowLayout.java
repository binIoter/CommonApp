package com.binioter.flowtag;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import binitoer.com.flowtag.R;

/**
 * 创建时间: 2016/11/02  <br>
 * 作者: zhangbin <br>
 * 描述: 标签流式布局,支持设置最大行数
 */
public class FlowLayout extends ViewGroup {

  private static final int DEFAULT_HORIZONTAL_SPACING = 5;
  private static final int DEFAULT_VERTICAL_SPACING = 5;

  private int mVerticalSpacing;
  private int mHorizontalSpacing;
  private int mLineNum;

  public FlowLayout(Context context) {
    super(context);
  }

  public FlowLayout(Context context, AttributeSet attrs) {
    super(context, attrs);

    TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.FlowLayout);
    try {
      mHorizontalSpacing = a.getDimensionPixelSize(R.styleable.FlowLayout_horizontal_spacing,
          DEFAULT_HORIZONTAL_SPACING);
      mVerticalSpacing = a.getDimensionPixelSize(R.styleable.FlowLayout_vertical_spacing,
          DEFAULT_VERTICAL_SPACING);
      mLineNum = a.getInteger(R.styleable.FlowLayout_max_line_count, Integer.MAX_VALUE);
    } finally {
      a.recycle();
    }
  }

  /**
   * 设置最大行数
   */
  public void setLineNum(int lineNum) {
    mLineNum = lineNum;
  }

  /**
   * 设置水平间距
   */
  public void setHorizontalSpacing(int pixelSize) {
    mHorizontalSpacing = pixelSize;
  }

  /**
   * 设置垂直间距
   */
  public void setVerticalSpacing(int pixelSize) {
    mVerticalSpacing = pixelSize;
  }

  @Override protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
    int myWidth = resolveSize(0, widthMeasureSpec);

    int paddingLeft = getPaddingLeft();
    int paddingTop = getPaddingTop();
    int paddingRight = getPaddingRight();
    int paddingBottom = getPaddingBottom();

    int childLeft = paddingLeft;
    int childTop = paddingTop;
    int lineNum = 0;

    int lineHeight = 0;

    // Measure each child and put the child to the right of previous child
    // if there's enough room for it, otherwise, wrap the line and put the child to next line.
    for (int i = 0, childCount = getChildCount(); i < childCount; ++i) {
      View child = getChildAt(i);
      if (child.getVisibility() != View.GONE) {
        measureChild(child, widthMeasureSpec, heightMeasureSpec);
      } else {
        continue;
      }

      int childWidth = child.getMeasuredWidth();
      int childHeight = child.getMeasuredHeight();

      lineHeight = Math.max(childHeight, lineHeight);

      if (childLeft + childWidth + paddingRight > myWidth) {
        lineNum++;
        if (mLineNum > lineNum) {
          childLeft = paddingLeft;
          childTop += mVerticalSpacing + lineHeight;
          lineHeight = childHeight;
        }
      }
      childLeft += childWidth + mHorizontalSpacing;
    }

    int wantedHeight = childTop + lineHeight + paddingBottom;

    setMeasuredDimension(myWidth, resolveSize(wantedHeight, heightMeasureSpec));
  }

  @Override protected void onLayout(boolean changed, int l, int t, int r, int b) {
    int myWidth = r - l;

    int paddingLeft = getPaddingLeft();
    int paddingTop = getPaddingTop();
    int paddingRight = getPaddingRight();

    int childLeft = paddingLeft;
    int childTop = paddingTop;
    int lineNum = 0;
    int lineHeight = 0;
    for (int i = 0, childCount = getChildCount(); i < childCount; ++i) {
      View childView = getChildAt(i);

      if (childView.getVisibility() == View.GONE) {
        continue;
      }

      int childWidth = childView.getMeasuredWidth();
      int childHeight = childView.getMeasuredHeight();

      lineHeight = Math.max(childHeight, lineHeight);

      if (childLeft + childWidth + paddingRight > myWidth) {
        lineNum++;
        if (mLineNum <= lineNum) {
          return;
        }
        childLeft = paddingLeft;
        childTop += mVerticalSpacing + lineHeight;
        lineHeight = childHeight;
      }
      childView.layout(childLeft, childTop, childLeft + childWidth, childTop + childHeight);
      childLeft += childWidth + mHorizontalSpacing;
    }
  }
}