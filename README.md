## 简介<br>
CommonApp,旨在帮助开发者进行组件化开发，包含了App通用的模块，各个模块采用单独module进行维护，目前包含以下模块：<br>
* 沉浸式状态栏模块 
* 通用的titlebar模块
* Activity堆栈管理器模块
* 全局右滑关闭页面模块
* 通用dialog,流式标签模块
* 带动画的提示TextView
* 持续更新中...<br>
![image](https://github.com/binIoter/CommonApp/blob/master/app/src/main/file/review.gif )


## usage<br>
 在BaseActivity中进行初始化,并且通过调用setSwipeBackEnabled()方法控制当前Activity是否可以右滑关闭 <br>
 
 public class BaseActivity extends Activity {
    protected SwipeBackLayout mSwipeBackLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);
        mSwipeBackLayout = new SwipeBackLayout(this);
        mSwipeBackLayout.attachToActivity(this);
        mSwipeBackLayout.setBgTransparent();
        SystemBarTintManager tintManager = new SystemBarTintManager(this);
        // enable status bar tint
        tintManager.setStatusBarTintEnabled(true);
        // enable navigation bar tint
        tintManager.setNavigationBarTintEnabled(true);
        tintManager.setStatusBarTintResource(getResources().getColor(R.color.title_bar_color));
        if (hasKitKat() && !hasLollipop()) {
            // 透明状态栏
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            // 透明导航栏
            // getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        } else if (hasLollipop()) {
            Window window = getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS |
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    // | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(getColor(R.color.title_bar_color));
        }
    }
    /**
     * 是否禁用滑动返回
     */
    public void setSwipeBackEnabled(boolean isSwipeBackEnabled) {
            mSwipeBackLayout.setSwipeBackEnabled(isSwipeBackEnabled);
    }
}
<br>styles文件设置<br>

    <style name="window_translucent" parent="android:Theme">
        <item name="android:windowBackground">@android:color/transparent</item>
        <item name="android:colorBackgroundCacheHint">@null</item>
        <item name="android:windowIsTranslucent">true</item>
    </style>
    <style name="swipeback_activity_style" parent="window_translucent"></style>
   
    
<br>AndroidManifest.xml文件添加主题<br>
android:theme="@style/swipeback_activity_style"
License
-------

    Copyright 2016 binIoter

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
