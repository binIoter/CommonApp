package com.binioter.ui;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.binioter.R;
import com.binioter.base.BaseActivity;
import com.binioter.widget.TopTitleBar;

public class MainActivity extends BaseActivity {

  @Bind(R.id.btn_jump_dialog_aty) Button mBtnJumpDialogAty;
  @Bind(R.id.btn_jump_flow_aty) Button mBtnJumpFlowAty;

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    ButterKnife.bind(this);
    setSwipeBackEnabled(false);
    initTitleBar();
  }

  private void initTitleBar() {
    TopTitleBar topTitleBar = (TopTitleBar) findViewById(R.id.title_bar);
    topTitleBar.setTitle("首页");
    topTitleBar.setLeftVisible(false);
    initBar(topTitleBar);
  }

  @OnClick(R.id.btn_jump_dialog_aty) void onBtnJumpDialogAty() {
    startActivity(new Intent(MainActivity.this, CommonDialogActivity.class));
  }

  @OnClick(R.id.btn_jump_flow_aty) void onBtnJumpFlowAty() {
    startActivity(new Intent(MainActivity.this, FlowLayoutActivity.class));
  }
}
