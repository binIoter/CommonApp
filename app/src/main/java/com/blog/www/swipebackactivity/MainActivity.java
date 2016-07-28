package com.blog.www.swipebackactivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import com.blog.www.swipebackactivity.tintbar.TopTitleBar;

public class MainActivity extends BaseActivity {

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    setSwipeBackEnabled(false);
    initTitleBar();
    Button btn = (Button) findViewById(R.id.btn_jump);
    btn.setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View view) {
        startActivity(new Intent(MainActivity.this, SecondActivity.class));
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
