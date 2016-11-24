package com.binioter.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import com.binioter.R;
import com.binioter.base.BaseActivity;
import com.binioter.widget.TopTitleBar;

public class MainActivity extends BaseActivity {

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    setSwipeBackEnabled(false);
    initTitleBar();
    Button btn = (Button) findViewById(R.id.btn_jump);
    btn.setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View view) {
        startActivity(new Intent(MainActivity.this, CommonDialogActivity.class));
      }
    });
  }

  private void initTitleBar() {
    TopTitleBar topTitleBar = (TopTitleBar) findViewById(R.id.title_bar);
    topTitleBar.setTitle("首页");
    topTitleBar.setLeftVisible(false);
    initBar(topTitleBar);
  }
}
