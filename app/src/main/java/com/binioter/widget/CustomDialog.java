package com.binioter.widget;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.AnimatorRes;
import android.text.TextUtils;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.binioter.R;

/**
 * 创建时间: 2016/11/18 13:43 <br>
 * 作者: zhangbin <br>
 * 描述: 自定义dialog
 */

public class CustomDialog extends Dialog {

  public CustomDialog(Context context) {
    super(context);
  }

  public CustomDialog(Context context, int themeResId) {
    super(context, themeResId);
  }

  protected CustomDialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
    super(context, cancelable, cancelListener);
  }

  public static class Builder {
    private Context mContext;
    private String mTitle;
    private String mMessage;
    private String mLeftBtnText;
    private String mRightBtnText;
    private View mContentView;
    private int mGravity;
    private int mAnimation;
    private boolean isCancelable;
    private OnClickListener mLeftBtnClickListener;
    private OnClickListener mRightBtnClickListener;

    public Builder(Context context) {
      this.mContext = context;
    }

    /**
     * 设置提示信息
     *
     * @param message 提示内容
     */
    public Builder setMessage(String message) {
      this.mMessage = message;
      return this;
    }

    /**
     * 根据资源id设置提示信息
     */
    public Builder setMessage(int resId) {
      this.mMessage = (String) mContext.getText(resId);
      return this;
    }

    /**
     * 根据资源id设置标题
     */
    public Builder setTitle(int resId) {
      this.mTitle = (String) mContext.getText(resId);
      return this;
    }

    /**
     * 设置标题
     */

    public Builder setTitle(String title) {
      this.mTitle = title;
      return this;
    }

    /**
     * 设置自定义布局
     *
     * @param v 布局view
     */
    public Builder setContentView(View v) {
      this.mContentView = v;
      return this;
    }

    /**
     * 根据资源id设置自定义布局
     *
     * @param layoutId 布局id
     */
    public Builder setContentView(int layoutId) {
      this.mContentView = LayoutInflater.from(mContext).inflate(layoutId, null);
      return this;
    }

    /**
     * 点击屏幕dialog是否消失
     */
    public Builder setCancelable(boolean cancelable) {
      this.isCancelable = cancelable;
      return this;
    }

    /**
     * 根据资源id设置左边按钮
     *
     * @param resId 资源id
     * @param listener 点击事件监听
     */
    public Builder setLeftBtnText(int resId, OnClickListener listener) {
      this.mLeftBtnText = (String) mContext.getText(resId);
      this.mLeftBtnClickListener = listener;
      return this;
    }

    /**
     * 设置左边按钮
     *
     * @param leftBtnText 按钮名称
     * @param listener 点击事件监听
     */
    public Builder setLeftBtnText(String leftBtnText, OnClickListener listener) {
      this.mLeftBtnText = leftBtnText;
      this.mLeftBtnClickListener = listener;
      return this;
    }

    /**
     * 根据资源id设置右边按钮
     *
     * @param resId 资源id
     * @param listener 点击事件监听
     */
    public Builder setRightBtnText(int resId, OnClickListener listener) {
      this.mRightBtnText = (String) mContext.getText(resId);
      this.mRightBtnClickListener = listener;
      return this;
    }

    /**
     * 设置右边按钮
     *
     * @param rightBtnText 按钮名称
     * @param listener 点击事件监听
     */
    public Builder setRightBtnText(String rightBtnText, OnClickListener listener) {
      this.mRightBtnText = rightBtnText;
      this.mRightBtnClickListener = listener;
      return this;
    }

    /**
     * dialog的位置
     *
     * @param gravity {@link Gravity }
     */
    public Builder setGravity(int gravity) {
      this.mGravity = gravity;
      return this;
    }

    /**
     * 设置dialog动画
     *
     * @param animation 动画资源id
     */
    public Builder setAnimation(@AnimatorRes int animation) {
      this.mAnimation = animation;
      return this;
    }

    /**
     * 创建dialog
     */
    public CustomDialog create() {
      LayoutInflater inflater =
          (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
      //初始化dialog
      final CustomDialog dialog = new CustomDialog(mContext, R.style.Dialog);
      View layout = inflater.inflate(R.layout.custom_dialog_layout, null);
      dialog.addContentView(layout,
          new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
      // 设置title
      if (!TextUtils.isEmpty(mTitle)) {
        ((TextView) layout.findViewById(R.id.tv_title)).setText(mTitle);
      } else {
        layout.findViewById(R.id.tv_title).setVisibility(View.GONE);
      }

      // 设置左边按钮
      if (TextUtils.isEmpty(mLeftBtnText) || TextUtils.isEmpty(mRightBtnText)) {
        //没有左侧按钮
        layout.findViewById(R.id.tv_left_btn).setVisibility(View.GONE);
      } else {
        ((TextView) layout.findViewById(R.id.tv_left_btn)).setText(mLeftBtnText);
        if (mLeftBtnClickListener != null) {
          (layout.findViewById(R.id.tv_left_btn)).setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
              mLeftBtnClickListener.onClick(dialog, DialogInterface.BUTTON_NEGATIVE);
            }
          });
        }
      }
      //设置右侧按钮一直存在
      ((TextView) layout.findViewById(R.id.tv_right_btn)).setText(mRightBtnText);
      if (mRightBtnClickListener != null) {
        (layout.findViewById(R.id.tv_right_btn)).setOnClickListener(new View.OnClickListener() {
          public void onClick(View v) {
            mRightBtnClickListener.onClick(dialog, DialogInterface.BUTTON_POSITIVE);
          }
        });
      }

      //设置提示内容
      if (mMessage != null) {
        ((TextView) layout.findViewById(R.id.tv_message)).setText(mMessage);
      } else if (mContentView != null) {
        //没有提示内容,设置自定义布局
        ((LinearLayout) layout.findViewById(R.id.ll_content)).removeAllViews();
        ((LinearLayout) layout.findViewById(R.id.ll_content)).addView(mContentView,
            new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
      }
      Window window = dialog.getWindow();
      WindowManager windowManager = window.getWindowManager();
      Display display = windowManager.getDefaultDisplay();
      WindowManager.LayoutParams lp = dialog.getWindow().getAttributes();
      lp.width = (int) (display.getWidth() * 0.9); //设置宽度
      dialog.getWindow().setAttributes(lp);
      window.setGravity(mGravity);
      window.setWindowAnimations(mAnimation);
      dialog.setCancelable(isCancelable);
      dialog.setContentView(layout);
      return dialog;
    }
  }
}
