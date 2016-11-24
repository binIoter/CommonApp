package com.binioter.utils;

import android.content.Context;
import android.content.DialogInterface;
import android.text.TextUtils;
import android.widget.LinearLayout;
import com.binioter.widget.CustomDialog;

/**
 * 创建时间: 2016/11/18 14:15 <br>
 * 作者: zhangbin <br>
 * 描述: dialog工具类
 */

public class DialogUtil {
  /**
   * 只有msg和一个按钮
   *
   * @param context 上下文
   * @param msg 提示信息
   * @param btn1title 左边按钮名称
   * @param mlistener1 左边按钮事件
   */
  public static CustomDialog createDialogButton(Context context, String msg, String btn1title,
      DialogInterface.OnClickListener mlistener1) {
    return createDialog(context, null, msg, btn1title, mlistener1, null, null, null);
  }

  /**
   * 只有msg和两个按钮
   *
   * @param msg 提示信息
   * @param btn1title 左边按钮名称
   * @param mlistener1 左边按钮事件
   * @param btn2title 右边按钮名称
   * @param mlistener2 右边按钮事件
   */
  public static CustomDialog createDialog2Button(Context context, String msg, String btn1title,
      DialogInterface.OnClickListener mlistener1, String btn2title,
      DialogInterface.OnClickListener mlistener2) {
    return createDialog(context, null, msg, btn1title, mlistener1, btn2title, mlistener2, null);
  }

  /**
   * 只有title和一个按钮和提示信息
   *
   * @param title 对话框标题
   * @param msg 提示信息
   * @param btn1title 左边按钮名称
   * @param mlistener1 左边按钮事件
   */
  public static CustomDialog createDialogMsg(Context context, String title, String msg,
      String btn1title, DialogInterface.OnClickListener mlistener1) {
    return createDialog(context, title, msg, btn1title, mlistener1, null, null, null);
  }

  /**
   * 自定义提示信息部分dialog
   *
   * @param contentView 自定义view
   * @param title 对话框标题
   * @param btn1title 左边按钮名称
   * @param mlistener1 左边按钮事件
   * @param btn2title 右边按钮名称
   * @param mlistener2 右边按钮事件
   */
  public static CustomDialog createCustomDialog(Context context, String title,
      LinearLayout contentView, String btn1title, DialogInterface.OnClickListener mlistener1,
      String btn2title, DialogInterface.OnClickListener mlistener2) {
    return createDialog(context, title, null, btn1title, mlistener1, btn2title, mlistener2,
        contentView);
  }

  /**
   * 有title和两个按钮和提示信息
   *
   * @param title 对话框标题
   * @param msg 提示信息
   * @param btn1title 左边按钮名称
   * @param mlistener1 左边按钮事件
   * @param btn2title 右边按钮名称
   * @param mlistener2 右边按钮事件
   */
  public static CustomDialog createDialog2ButtonMsg(Context context, String title, String msg,
      String btn1title, DialogInterface.OnClickListener mlistener1, String btn2title,
      DialogInterface.OnClickListener mlistener2) {
    return createDialog(context, title, msg, btn1title, mlistener1, btn2title, mlistener2, null);
  }

  /**
   * 有title和两个按钮和自定义view
   *
   * @param title 对话框标题
   * @param msg 提示信息
   * @param btn1title 左边按钮名称
   * @param mlistener1 左边按钮事件
   * @param btn2title 右边按钮名称
   * @param mlistener2 右边按钮事件
   * @param contentView 自定义view
   */
  private static CustomDialog createDialog(Context context, String title, String msg,
      String btn1title, DialogInterface.OnClickListener mlistener1, String btn2title,
      DialogInterface.OnClickListener mlistener2, LinearLayout contentView) {
    CustomDialog.Builder builder = new CustomDialog.Builder(context).setTitle(title);
    if (!TextUtils.isEmpty(msg)) {
      builder.setMessage(msg);
    }
    if (!TextUtils.isEmpty(btn1title) && !TextUtils.isEmpty(btn2title)) {
      builder.setRightBtnText(btn2title, mlistener2);
      builder.setLeftBtnText(btn1title, mlistener1);
    } else {
      builder.setRightBtnText(btn1title, mlistener1);
    }
    if (null != contentView) {
      builder.setContentView(contentView);
    }
    return builder.create();
  }
}

