## 原理<br>
1. 继承自FrameLayout,将Activity中显示内容的View添加到到SwipeBackLayout的实例中,再将SwipeBackLayout的实例添加到Activity中.<br>
2. 对SwipeBackLayout实例借助Scroller类调用ScrollTo方法来实现滑动显示内容的View.<br>
3. Activity的背景会被设置为透明的,这样在显示内容的View滑动的过程中才可以显示出底层View.<br><br>
![image](https://github.com/binIoter/SwipeBackActivity/blob/master/app/src/main/res/assets/sweepbackactivity.gif )
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
    }
    /**
     * 是否禁用滑动返回
     */
    public void setSwipeBackEnabled(boolean isSwipeBackEnabled) {
            mSwipeBackLayout.setSwipeBackEnabled(isSwipeBackEnabled);
    }
}
