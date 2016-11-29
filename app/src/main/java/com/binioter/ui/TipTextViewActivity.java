package com.binioter.ui;

import android.os.Bundle;
import android.widget.Button;
import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.binioter.R;
import com.binioter.base.BaseActivity;
import com.binioter.tiptextview.TipTextView;
import com.binioter.util.DensityUtil;
import com.binioter.widget.TopTitleBar;

/**
 * 创建时间: 2016/11/29 11:36 <br>
 * 作者: zhangbin <br>
 * 描述: 弹出tips的Activity
 */

public class TipTextViewActivity extends BaseActivity {
  @Bind(R.id.btn_show) Button mBtnShow;
  @Bind(R.id.btn_hide) Button mBtnHide;
  @Bind(R.id.tip_tv) TipTextView mTipTv;
  @Bind(R.id.title_bar) TopTitleBar mTitleBar;

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.tip_textview_activity_layout);
    ButterKnife.bind(this);
    initBar(mTitleBar);
    //因为是带有沉浸式状态栏所以要加上状态栏的高度,没有沉浸式状态栏,这行代码可以去掉,默认为titlebar的高度
    mTipTv.setHeight(
        DensityUtil.px2dip(this, DensityUtil.getStatusBarHeight(this)) + mTitleBarHeight);
  }

  @OnClick(R.id.btn_show) void showTips() {
    mTipTv.showTips(getResources().getString(R.string.txt_tips));
  }

  @OnClick(R.id.btn_hide) void hideTips() {
    mTipTv.hideTips();
  }
}
