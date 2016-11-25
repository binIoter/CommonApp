package com.binioter.ui;

import android.os.Bundle;
import butterknife.Bind;
import butterknife.ButterKnife;
import com.binioter.R;
import com.binioter.base.BaseActivity;
import com.binioter.flowtag.ColorTag;
import com.binioter.flowtag.ColorTagView;
import com.binioter.flowtag.FlowLayout;
import com.binioter.widget.TopTitleBar;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * 创建时间: 2016/11/25 10:44 <br>
 * 作者: zhangbin <br>
 * 描述: 流式标签activity
 */

public class FlowLayoutActivity extends BaseActivity {
  @Bind(R.id.flow_layout) FlowLayout mFlowLayout;
  @Bind(R.id.title_bar) TopTitleBar mTitleBar;
  private List<ColorTag> mTagData = new ArrayList<>();
  private static String mTagType[] = { "有电梯", "绿化率高", "小区环境好", "安静", "南北通透税费低" };
  private static String mTagColor[] = { "394043", "be5453", "00ae66", "b27f39", "ae005d" };

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_flowtag_layout);
    ButterKnife.bind(this);
    initBar(mTitleBar);
    initData();
    initView();
  }

  private void initView() {
    mFlowLayout.removeAllViews();
    for (int i = 0; i < mTagData.size(); i++) {
      mFlowLayout.addView(new ColorTagView(this, mTagData.get(i)));
    }
  }

  private void initData() {
    Random random = new Random();
    for (int i = 0; i < 30; i++) {
      ColorTag colorTag = new ColorTag();
      colorTag.text = mTagType[random.nextInt(5)];
      colorTag.color = mTagColor[random.nextInt(5)];
      mTagData.add(colorTag);
    }
  }
}
