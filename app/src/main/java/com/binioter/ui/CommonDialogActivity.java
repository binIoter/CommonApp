package com.binioter.ui;

import android.content.DialogInterface;
import android.os.Bundle;
import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.binioter.R;
import com.binioter.base.BaseActivity;
import com.binioter.dialog.DialogUtil;
import com.binioter.widget.TopTitleBar;

/**
 * 创建时间: 2016/11/24 16:56 <br>
 * 作者: zhangbin <br>
 * 描述: 通用dialog activity
 */

public class CommonDialogActivity extends BaseActivity {

  @Bind(R.id.title_bar) TopTitleBar mTitleBar;

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_common_dialog_layout);
    ButterKnife.bind(this);
    initBar(mTitleBar);
    //低级api
    //new CustomDialog.Builder(this).create().show();
  }

  @OnClick(R.id.btn_no_title_one_btn) void onBtnNoTitleOneBtn() {
    DialogUtil.createDialogButton(this, "我是没有标题有一个按钮的弹窗", "我知道了",
        new DialogInterface.OnClickListener() {
          @Override public void onClick(DialogInterface dialogInterface, int i) {
            dialogInterface.dismiss();
          }
        }).show();
  }

  @OnClick(R.id.btn_no_title_two_btn) void onBtnNoTitleTwoBtn() {
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
  }

  @OnClick(R.id.btn_with_title_one_btn) void onBtnWithTitleOneBtn() {
    DialogUtil.createDialogMsg(this, "弹窗标题", "我是有标题有一个按钮的弹窗", "我知道了",
        new DialogInterface.OnClickListener() {
          @Override public void onClick(DialogInterface dialogInterface, int i) {
            dialogInterface.dismiss();
          }
        }).show();
  }

  @OnClick(R.id.btn_with_title_two_btn) void onBtnWithTitleTwoBtn() {
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
  }
}


