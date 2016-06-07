package com.blog.www.swipebackactivity;

import android.os.Bundle;
import com.blog.www.swipebackactivity.tintbar.TopTitleBar;

public class SecondActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);
        initTitle();
    }
    private void initTitle() {
        TopTitleBar topTitleBar = (TopTitleBar) findViewById(R.id.title_bar);
        topTitleBar.setTitle("第二页");
        initBar(topTitleBar);
    }

}
