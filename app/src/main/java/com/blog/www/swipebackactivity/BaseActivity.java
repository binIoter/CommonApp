package com.blog.www.swipebackactivity;

import android.app.Activity;
import android.os.Bundle;

public class BaseActivity extends Activity {
    protected SwipeBackLayout mSwipeBackLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);
        mSwipeBackLayout = new SwipeBackLayout(this);
        mSwipeBackLayout.attachToActivity(this);
        mSwipeBackLayout.setBgTransparent();
    }
    /**
     * 是否禁用滑动返回
     */
    public void setSwipeBackEnabled(boolean isSwipeBackEnabled) {
            mSwipeBackLayout.setSwipeBackEnabled(isSwipeBackEnabled);
    }
}
