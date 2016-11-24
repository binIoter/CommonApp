package com.binioter.utils;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import com.binioter.R;
import com.binioter.widget.TopTitleBar;

public class TitleBarHelper {

  private Context mContext;
  private TopTitleBar mTopTitleBar;

  public TitleBarHelper(Context context, TopTitleBar titleBar) {
    this.mContext = context;

    this.mTopTitleBar = titleBar;
  }

  /**
   * decorate the titleBar,white
   * 初始化并开启沉浸模式
   */

  public void init() {
    if (mTopTitleBar != null) {
      mTopTitleBar.setBackgroundResource(R.color.title_bar_color);
      mTopTitleBar.setLeftClickListener(mDefaultListener);// set default
      // listener
      mTopTitleBar.setMainTitleColor(mContext.getResources().getColor(R.color.title_color));
      mTopTitleBar.setLeftImageResource(R.mipmap.btn_back_normal);
      mTopTitleBar.setDividerHeight(1);
      mTopTitleBar.setDividerColor(mContext.getResources().getColor(R.color.title_color));
      mTopTitleBar.setLeftTextColor(mContext.getResources().getColor(R.color.title_color));
      mTopTitleBar.setImmersive(true);
    }
  }

  private View.OnClickListener mDefaultListener = new View.OnClickListener() {
    @Override public void onClick(View v) {
      if (mContext != null) {
        Activity activity = (Activity) mContext;
        activity.finish();
      }
    }
  };
}
