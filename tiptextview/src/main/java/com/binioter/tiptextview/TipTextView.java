package com.binioter.tiptextview;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.os.Handler;
import android.util.AttributeSet;
import android.widget.TextView;
import com.binioter.util.DensityUtil;

/**
 * 创建时间: 2016/11/28 13:35 <br>
 * 作者: zhangbin <br>
 * 描述: 提示TextView
 */
public class TipTextView extends TextView {
  /**
   * 动画执行时间/毫秒
   */
  private static final int DURATION_TIME = 400;
  /**
   * autoTips显示时间/毫秒
   */
  private static final int SHOW_TIME = 5000;
  private static final String TRANSLATIONY = "translationY";
  private static final String ALPHA = "alpha";
  /**
   * 高度
   */
  private int mHeight = 47;
  /**
   * tips是否正在显示
   */
  private boolean mIsShowing;
  private Context mContext;

  public TipTextView(Context context) {
    super(context);
    this.mContext = context;
  }

  public TipTextView(Context context, AttributeSet paramAttributeSet) {
    super(context, paramAttributeSet);
    this.mContext = context;
  }

  public TipTextView(Context context, AttributeSet paramAttributeSet, int paramInt) {
    super(context, paramAttributeSet, paramInt);
    this.mContext = context;
  }

  /**
   * 显示tips
   *
   * @param tips 显示文本
   */
  public void showTips(String tips) {
    if (mIsShowing) {
      return;
    }
    setText(tips);
    //向下移动动画
    ObjectAnimator animTransIn =
        ObjectAnimator.ofFloat(this, TRANSLATIONY, 0f, DensityUtil.dip2px(mContext, mHeight));
    ObjectAnimator animFadeIn = ObjectAnimator.ofFloat(this, ALPHA, 0f, 1f);
    AnimatorSet animSet = new AnimatorSet();
    animSet.play(animTransIn).with(animFadeIn);
    animSet.setDuration(DURATION_TIME);
    animSet.start();
    mIsShowing = true;
  }

  /**
   * 隐藏tips
   */
  public void hideTips() {
    if (!mIsShowing) {
      return;
    }
    //向上移动动画
    ObjectAnimator animTransOut =
        ObjectAnimator.ofFloat(this, TRANSLATIONY, DensityUtil.dip2px(mContext, mHeight), 0f);
    ObjectAnimator animFadeOut = ObjectAnimator.ofFloat(this, ALPHA, 1f, 0f);
    AnimatorSet animSet = new AnimatorSet();
    animSet.play(animTransOut).with(animFadeOut);
    animSet.setDuration(DURATION_TIME);
    animSet.start();
    mIsShowing = false;
  }

  /**
   * 显示tips并自动消失
   *
   * @param tips 显示文本
   */
  public void showAutoHideTips(String tips) {
    showTips(tips);
    new Handler().postDelayed(new Runnable() {
      @Override public void run() {
        hideTips();
      }
    }, SHOW_TIME);
  }

  /**
   * 设置标题栏高度
   */
  public void setHeight(int height) {
    this.mHeight = height;
  }
}
