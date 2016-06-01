# SwipeBackActivity
# 滑动返回:支持将Activity设置为向右滑返回
## <h3>原理</h3>
<br>1. 继承自FrameLayout,将Activity中显示内容的View添加到到SwipeBackLayout的实例中,再将SwipeBackLayout的实例添加到Activity中.
<br>2. 对SwipeBackLayout实例借助Scroller类调用ScrollTo方法来实现滑动显示内容的View.
<br>3. Activity的背景会被设置为透明的,这样在显示内容的View滑动的过程中才可以显示出底层View.
<br>![image](https://github.com/binIoter/SwipeBackActivity/blob/master/app/src/main/res/assets/sweepbackactivity.gif )  
