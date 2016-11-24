package com.binioter.ui;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import com.binioter.R;
import com.binioter.base.BaseActivity;
import com.binioter.utils.DialogUtil;
import com.binioter.widget.TopTitleBar;

/**
 * 创建时间: 2016/11/24 16:56 <br>
 * 作者: zhangbin <br>
 * 描述: 通用dialog activity
 */

public class CommonDialogActivity extends BaseActivity implements View.OnClickListener {

  private Button mBtnNoTitleOneBtn, mBtnNoTitleTwoBtn, mBtnWithTitleOneBtn, mBtnWithTitleTwoBtn;

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_common_dialog_layout);
    initTitle();
    mBtnNoTitleOneBtn = (Button) findViewById(R.id.btn_no_title_one_btn);
    mBtnNoTitleTwoBtn = (Button) findViewById(R.id.btn_no_title_two_btn);
    mBtnWithTitleOneBtn = (Button) findViewById(R.id.btn_with_title_one_btn);
    mBtnWithTitleTwoBtn = (Button) findViewById(R.id.btn_with_title_two_btn);
    mBtnNoTitleOneBtn.setOnClickListener(this);
    mBtnNoTitleTwoBtn.setOnClickListener(this);
    mBtnWithTitleOneBtn.setOnClickListener(this);
    mBtnWithTitleTwoBtn.setOnClickListener(this);
    //低级api
    //new CustomDialog.Builder(this).create().show();
  }

  private void initTitle() {
    TopTitleBar topTitleBar = (TopTitleBar) findViewById(R.id.title_bar);
    topTitleBar.setTitle("DialogActivity");
    initBar(topTitleBar);
  }

  @Override public void onClick(View view) {
    switch (view.getId()) {
      case R.id.btn_no_title_one_btn:
        DialogUtil.createDialogButton(this, "我是没有标题有一个按钮的弹窗", "我知道了",
            new DialogInterface.OnClickListener() {
              @Override public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
              }
            }).show();
        break;
      case R.id.btn_no_title_two_btn:
        DialogUtil.createDialog2Button(this, "我是没有标题和两个按钮的弹窗", "取消",
            new DialogInterface.OnClickListener() {
              @Override public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
              }
            }, "确定", new DialogInterface.OnClickListener() {
              @Override public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
              }
            }).show();
        break;
      case R.id.btn_with_title_one_btn:
        DialogUtil.createDialogMsg(this, "弹窗标题", "我是有标题有一个按钮的弹窗", "我知道了",
            new DialogInterface.OnClickListener() {
              @Override public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
              }
            }).show();
        break;
      case R.id.btn_with_title_two_btn:
        DialogUtil.createDialog2ButtonMsg(this, "弹窗标题", "我是有标题和两个按钮的弹窗", "取消",
            new DialogInterface.OnClickListener() {
              @Override public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
              }
            }, "确定", new DialogInterface.OnClickListener() {
              @Override public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
              }
            }).show();
        break;
      default:
        break;
    }
  }
}


