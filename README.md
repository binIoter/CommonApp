## 原理<br>
1. 继承自FrameLayout,将Activity中显示内容的View添加到到SwipeBackLayout的实例中,再将SwipeBackLayout的实例添加到Activity中.<br>
2. 对SwipeBackLayout实例借助Scroller类调用ScrollTo方法来实现滑动显示内容的View.<br>
3. Activity的背景会被设置为透明的,这样在显示内容的View滑动的过程中才可以显示出底层View.<br><br>
![image](https://github.com/binIoter/SwipeBackActivity/blob/master/app/src/main/res/assets/review.gif )


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
