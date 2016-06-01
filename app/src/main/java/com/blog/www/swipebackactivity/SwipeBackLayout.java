package com.blog.www.swipebackactivity;

import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.support.v4.view.MotionEventCompat;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.Interpolator;
import android.widget.FrameLayout;
import android.widget.Scroller;

/**
 * <h1>滑动返回</h1> <br>
 * 滑动返回:支持将Activity设置为向右滑返回; <h3>原理</h3> 继承自FrameLayout <br>
 * 将Activity中显示内容的View添加到到SwipeBackLayout的实例中,再将SwipeBackLayout的实例添加到Activity中. <br>
 * 对SwipeBackLayout实例借助Scroller类调用ScrollTo方法来实现滑动显示内容的View. <br>
 * Activity的背景会被设置为透明的,这样在显示内容的View滑动的过程中才可以显示出底层View. 另外还需要调用了
 * {@link #setBgTransparent()}将SwipeBackLayout的实例的背景设置为透明.
 */
public class SwipeBackLayout extends FrameLayout {

    private Activity mActivity;
    private View mContentView;
    private ViewGroup mRealContentView;
    private Scroller mScroller;
    private int mViewWidth;

    private int mTouchSlop;
    private static final int MARGIN_THRESHOLD = 24; // dips

    private float mLastMotionX;
    private float mLastMotionY;
    private float mDownX;
    private int mCurMotionX;

    private int mActivePointerId = INVALID_POINTER;
    private static final int INVALID_POINTER = -1;

    private boolean mIsSilding = false;
    private boolean mIsFinish = false;
    private boolean mIsSwipeBackEnabled = true;
    private boolean mIsScrolling = false;

    private int mMarginThreshold;

    private int mAlphaBgColor = 0;
    private Rect mColorRect = new Rect();

    private VelocityTracker mVelocityTracker;
    private int mMinimumVelocity;
    private int mMaximumVelocity;
    private int mFlingDistance;
    private int mMoveDistance;
    private static final int MIN_DISTANCE_FOR_MOVE = 24; // dips
    private float mXVelocity;

    /**
     * SwipeLayout的构造函数
     *
     * @param context
     */
    public SwipeBackLayout(Context context) {
        super(context);
        init(context);
    }

    /**
     * SwipeLayout的构造函数
     *
     * @param context
     * @param attrs
     */
    public SwipeBackLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    /**
     * SwipeLayout的构造函数
     *
     * @param context
     * @param attrs
     * @param defStyle
     */
    public SwipeBackLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context);
    }

    /**
     * 实例化一些需要的对象实例和参数
     *
     * @param context
     */
    private void init(Context context) {
        mMarginThreshold = (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP, MARGIN_THRESHOLD, getResources()
                        .getDisplayMetrics());
        mScroller = new Scroller(context, new MyAccelerateInterpolator(1.5f));
        mTouchSlop = ViewConfiguration.get(context).getScaledTouchSlop() * 2;
        mMaximumVelocity = ViewConfiguration.getMaximumFlingVelocity();
        mMinimumVelocity = ViewConfiguration.getMinimumFlingVelocity();
        final float density = context.getResources().getDisplayMetrics().density;
        mMoveDistance = (int) (MIN_DISTANCE_FOR_MOVE * density);
        int deviceWidth = DevUtils.getEquipmentWidth(context);
        mFlingDistance = deviceWidth / 4;
    }

    /**
     * @param activity
     */
    public void attachToActivity(Activity activity) {
        try {
            mActivity = activity;

            Window window = activity.getWindow();
            window.setBackgroundDrawable(new ColorDrawable(0));

            ViewGroup decor = (ViewGroup) window.getDecorView();
            mRealContentView = (ViewGroup) decor.getChildAt(0);
            decor.removeView(mRealContentView);
            mRealContentView.setClickable(true);
            addView(mRealContentView);
            mContentView = (View) mRealContentView.getParent();
            decor.addView(this);
        } catch (Exception e) {
            mIsSwipeBackEnabled = false;
        }
    }

    @Override
    protected void dispatchDraw(Canvas canvas) {
        super.dispatchDraw(canvas);
        if (mContentView != null) {

            int left = 0;
            int right = mCurMotionX;
            int top = 0;
            int bottom = mContentView.getBottom();

            mColorRect.top = top;
            mColorRect.bottom = bottom;
            mColorRect.left = left;
            mColorRect.right = right;
            canvas.clipRect(mColorRect);

            if (mViewWidth != 0) {
                mAlphaBgColor = 100 - (int) (((float) (-mCurMotionX) / (float) mViewWidth) * 120);
            }

            if (mAlphaBgColor > 100) {
                mAlphaBgColor = 100;
            }

            if (mIsFinish) {
                mAlphaBgColor = 0;
            }

            if (mAlphaBgColor < 0) {
                mAlphaBgColor = 0;
            }

            canvas.drawARGB(mAlphaBgColor, 0, 0, 0);
        }
    }

    /**
     * 事件拦截操作
     */
    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {

        if (!mIsSwipeBackEnabled) {
            return super.onInterceptTouchEvent(event);
        }

        if (mIsFinish || mIsScrolling) {
            return super.onInterceptTouchEvent(event);
        }

        final int action = event.getAction() & MotionEventCompat.ACTION_MASK;

        if (action == MotionEvent.ACTION_CANCEL
                || action == MotionEvent.ACTION_UP) {
            endDrag();
            return super.onInterceptTouchEvent(event);
        }

        switch (action) {
            case MotionEvent.ACTION_DOWN:

                int index = MotionEventCompat.getActionIndex(event);
                mActivePointerId = MotionEventCompat.getPointerId(event, index);

                if (isInvalidEvent(event, index, mActivePointerId)) {
                    break;
                }

                mLastMotionX = MotionEventCompat.getX(event, index);
                mLastMotionY = MotionEventCompat.getY(event, index);
                mDownX = MotionEventCompat.getX(event, index);

                break;
            case MotionEvent.ACTION_MOVE:

                determineDrag(event);

                break;
        }

        return mIsSilding;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        if (mIsFinish || mIsScrolling) {
            return super.onTouchEvent(event);
        }

        if (mVelocityTracker == null) {
            mVelocityTracker = VelocityTracker.obtain();
        }
        mVelocityTracker.addMovement(event);

        switch (event.getAction() & MotionEventCompat.ACTION_MASK) {
            case MotionEvent.ACTION_DOWN:

                completeScroll();

                int index = MotionEventCompat.getActionIndex(event);
                mActivePointerId = MotionEventCompat.getPointerId(event, index);
                mLastMotionX = event.getX();
                mDownX = MotionEventCompat.getX(event, index);

                break;
            case MotionEvent.ACTION_MOVE:

                if (!mIsSilding) {
                    determineDrag(event);
                }

                if (mIsSilding) {
                    final int activePointerIndex = getPointerIndex(event,
                            mActivePointerId);

                    if (isInvalidEvent(event, activePointerIndex, mActivePointerId)) {
                        break;
                    }

                    final float x = MotionEventCompat.getX(event,
                            activePointerIndex);
                    final float deltaX = mLastMotionX - x;
                    mLastMotionX = x;
                    float oldScrollX = getScrollX();
                    float scrollX = oldScrollX + deltaX;
                    final float leftBound = -mViewWidth;
                    final float rightBound = 0;
                    if (scrollX < leftBound) {
                        scrollX = leftBound;
                    } else if (scrollX > rightBound) {
                        scrollX = rightBound;
                    }
                    mLastMotionX += scrollX - (int) scrollX;
                    mCurMotionX = (int) scrollX;
                    mContentView.scrollTo((int) scrollX, getScrollY());
                }

                break;
            case MotionEvent.ACTION_UP:

                final VelocityTracker velocityTracker = mVelocityTracker;
                velocityTracker.computeCurrentVelocity(1000, mMaximumVelocity);
                mXVelocity = velocityTracker.getXVelocity();

                int diffX = getDiffX(event);

                endDrag();

                if (Math.abs(mXVelocity) > mMinimumVelocity && diffX > mFlingDistance) {
                    if (mXVelocity > 0) {
                        mIsFinish = true;
                        scrollRight();
                    } else {
                        scrollOrigin();
                        mIsFinish = false;
                    }
                    return true;
                }

                if (mContentView.getScrollX() <= -mViewWidth / 2) {
                    mIsFinish = true;
                    scrollRight();
                } else {
                    scrollOrigin();
                    mIsFinish = false;
                }
                break;

            case MotionEvent.ACTION_CANCEL:
                releaseVelocityTracker();
                break;
        }

        return super.onTouchEvent(event);
    }

    private void determineDrag(MotionEvent ev) {
        final int activePointerId = mActivePointerId;
        final int pointerIndex = getPointerIndex(ev, activePointerId);

        if (isInvalidEvent(ev, pointerIndex, activePointerId)) {
            return;
        }

        final float x = MotionEventCompat.getX(ev, pointerIndex);
        final float dx = x - mLastMotionX;
        final float xDiff = Math.abs(dx);
        final float y = MotionEventCompat.getY(ev, pointerIndex);
        final float dy = y - mLastMotionY;
        final float yDiff = Math.abs(dy);
        if (dx > 0 && xDiff > mMoveDistance && xDiff > yDiff) {
            mIsSilding = true;
            mLastMotionX = x;
            mLastMotionY = y;
        }
    }

    private int getDiffX(MotionEvent ev) {
        final int activePointerId = mActivePointerId;
        final int pointerIndex = getPointerIndex(ev, activePointerId);

        if (isInvalidEvent(ev, pointerIndex, activePointerId)) {
            return 0;
        }

        final float x = MotionEventCompat.getX(ev, pointerIndex);
        final float dx = x - mDownX;
        final float xDiff = Math.abs(dx);
        return (int) xDiff;
    }

    private void endDrag() {
        mIsSilding = false;
        mActivePointerId = INVALID_POINTER;
        releaseVelocityTracker();
    }

    private boolean isInvalidEvent(MotionEvent event, int pointerIndex, int pointerId) {

        if (event == null) {
            return true;
        }

        if (pointerId == INVALID_POINTER || pointerIndex == INVALID_POINTER) {
            return true;
        }

        if (pointerIndex >= event.getPointerCount()) {
            return true;
        }

        return false;
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        if (changed) {
            mViewWidth = this.getWidth();
        }
    }

    private int getPointerIndex(MotionEvent ev, int id) {
        int activePointerIndex = MotionEventCompat.findPointerIndex(ev, id);
        if (activePointerIndex == -1)
            mActivePointerId = INVALID_POINTER;
        return activePointerIndex;
    }

    /**
     * 滚动出界面
     */
    private void scrollRight() {
        mIsScrolling = true;
        final int delta = (mViewWidth + mContentView.getScrollX());
        mScroller.startScroll(mContentView.getScrollX(), 0, -delta + 1, 0);
        postInvalidate();
    }

    /**
     * 滚动到起始位置
     */
    private void scrollOrigin() {
        mIsScrolling = true;
        int delta = mContentView.getScrollX();
        mScroller.startScroll(mContentView.getScrollX(), 0, -delta, 0);
        postInvalidate();
    }

    private void completeScroll() {
        boolean needPopulate = mIsScrolling;
        if (needPopulate) {
            mScroller.abortAnimation();
            int oldX = getScrollX();
            int oldY = getScrollY();
            int x = mScroller.getCurrX();
            int y = mScroller.getCurrY();
            if (oldX != x || oldY != y) {
                mContentView.scrollTo(x, y);
            }
        }
        mIsScrolling = false;
    }

    @Override
    public void computeScroll() {
        if (!mScroller.isFinished() && mScroller.computeScrollOffset()) {

            int oldX = getScrollX();
            int oldY = getScrollY();
            int x = mScroller.getCurrX();
            int y = mScroller.getCurrY();

            if (oldX != x || oldY != y) {
                mContentView.scrollTo(x, y);
            }

            invalidate();
        }

        if (mScroller.isFinished() && mIsFinish) {
            mActivity.finish();
            mActivity.overridePendingTransition(0, 0);
        }

        if (mScroller.isFinished()) {
            completeScroll();
        }
    }

    private void releaseVelocityTracker() {
        if (mVelocityTracker != null) {
            mVelocityTracker.clear();
            mVelocityTracker.recycle();
            mVelocityTracker = null;
        }
    }

    /**
     * 是否滑动返回打开
     *
     * @return
     */
    public boolean isSwipeBackEnabled() {
        return mIsSwipeBackEnabled;
    }

    /**
     * 设置滑动返回的开关
     *
     * @param isSwipeBackEnabled
     */
    public void setSwipeBackEnabled(boolean isSwipeBackEnabled) {
        this.mIsSwipeBackEnabled = isSwipeBackEnabled;
    }

    /**
     * 设置背景为透明
     */
    public void setBgTransparent() {
        if (mRealContentView != null) {
            mRealContentView.setBackgroundResource(R.color.transparent);
        }
    }

    private static class MyAccelerateInterpolator implements Interpolator {

        private final float mFactor;

        public MyAccelerateInterpolator(float factor) {
            mFactor = factor;
        }

        public float getInterpolation(float input) {
            float result = input * (float) mFactor;

            if (result > 0.9) {
                result = 1.0f;
            }

            return result;
        }
    }

    /**
     * 滑动返回的接口
     */
    public interface SwipeControlInterface {
        /**
         * 打开滑动返回
         */
        public void enableSwipeBack();

        /**
         * 关闭滑动返回
         */
        public void disableSwipeBack();
    }
}