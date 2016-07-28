package com.blog.www.swipebackactivity.tintbar;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.blog.www.swipebackactivity.R;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by zb on 2016/7/28.
 */
public class TopTitleBar extends LinearLayout implements View.OnClickListener {

  private static final String TAG = TopTitleBar.class.getSimpleName();
  //private static final int DEFAULT_TITLE_BAR_HEIGHT = ;
  private static final int DEFAULT_LEFT_TEXT_SIZE = 16;
  private static final int DEFAULT_MAIN_TEXT_SIZE = 18;
  private static final int DEFAULT_SUB_TEXT_SIZE = 12;
  private static final int DEFAULT_ACTION_TEXT_SIZE = 16;

  private static final String STATUS_BAR_HEIGHT_RES_NAME = "status_bar_height";

  private Context mContext;

  private TextView mLeftText;
  private LinearLayout mRightLayout;
  private LinearLayout mCenterLayout;
  private TextView mCenterText;
  private TextView mSubTitleText;
  private View mDividerView;

  private boolean mImmersive;

  private int mScreenWidth;
  private int mStatusBarHeight;
  private int mActionPadding;
  private int mOutPadding;
  private int mMinSidePadding;
  private int mMainTextColor;
  private int mActionTextColor;
  private int mHeight;

  //中间布局是否一直居中，默认是
  private boolean isCenterAlways = true;

  /** titlebar中间的标题 */
  private String mTitle = "";

  /** titlebar背景色 */
  private int mBackground = 0xf9f9f9;

  /** titlebar返回按钮图片 */
  private int mBackIconRes = R.mipmap.btn_back_normal;

  /** titlebar返回按钮图片 */
  private boolean mDividerVisible = true;

  /** action缓存列表,这里不用弱引用是因为action可能在函数体内部加入，退出函数作用域action就释放了 */
  private final List<Action> mActionList = new ArrayList<>();

  public TopTitleBar(Context context) {
    this(context, null);
  }

  public TopTitleBar(Context context, AttributeSet attrs) {
    this(context, attrs, 0);
  }

  public TopTitleBar(Context context, AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
    TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.TopTitleBar);
    mTitle = typedArray.getString(R.styleable.TopTitleBar_tb_title);
    mBackground = typedArray.getColor(R.styleable.TopTitleBar_tb_background,
        context.getResources().getColor(R.color.color_f9f9f9));
    mBackIconRes =
        typedArray.getResourceId(R.styleable.TopTitleBar_tb_back_icon, R.mipmap.btn_back_normal);
    mDividerVisible = typedArray.getBoolean(R.styleable.TopTitleBar_tb_divider_visible, true);
    typedArray.recycle();
    mContext = context;
    init(context);
  }

  private void init(Context context) {
    mScreenWidth = getResources().getDisplayMetrics().widthPixels;
    //若是浸没模式，则获取状态栏高度
    if (mImmersive) {
      mStatusBarHeight = getStatusBarHeight();
    } else {
      mStatusBarHeight = 0;
    }
    mOutPadding = dip2px(15); //layout间margin
    mActionPadding = dip2px(5); //layout中控件间的padding
    mMinSidePadding = dip2px(15); //非恒定居中模式时，左右两边的最小间距
    mHeight = context.getResources().getDimensionPixelSize(R.dimen.title_height);
    initView(context);
  }

  private void initView(Context context) {
    setOrientation(HORIZONTAL);
    mLeftText = new TextView(context);
    mCenterLayout = new LinearLayout(context);
    mRightLayout = new LinearLayout(context);
    mDividerView = new View(context);

    LayoutParams layoutParams =
        new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT);

    //左布局初始化默认值
    setDefaultTextStyle(mLeftText);
    mLeftText.setTextSize(DEFAULT_LEFT_TEXT_SIZE);
    mLeftText.setPadding(mOutPadding, 0, 0, 0);//有文字或图片加入再重设padding
    //mLeftText.setCompoundDrawablePadding(mActionPadding);// add draw padding
    //左布局默认实现
    mLeftText.setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View view) {
        if (null != mContext && mContext instanceof Activity) {
          ((Activity) mContext).finish();
        }
      }
    });
    //中央布局初始化默认值,默认是主标题+子标题形式
    mCenterText = new TextView(context);
    mSubTitleText = new TextView(context);
    mCenterLayout.addView(mCenterText);
    mCenterLayout.addView(mSubTitleText);
    mCenterLayout.setGravity(Gravity.CENTER);
    setDefaultTextStyle(mCenterText);
    setDefaultTextStyle(mSubTitleText);
    mCenterText.setTextSize(DEFAULT_MAIN_TEXT_SIZE);
    mCenterText.setTextColor(context.getResources().getColor(R.color.color_394043));
    mCenterText.setGravity(Gravity.CENTER);
    mSubTitleText.setTextSize(DEFAULT_SUB_TEXT_SIZE);
    mSubTitleText.setTextColor(context.getResources().getColor(R.color.color_394043));
    mSubTitleText.setGravity(Gravity.CENTER);

    //右布局通过addAction动态添加
    mRightLayout.setPadding(mOutPadding, 0, 0, 0);

    //底部分割线
    mDividerView.setBackgroundColor(context.getResources().getColor(R.color.color_cccccc));

    addView(mLeftText, layoutParams);
    addView(mCenterLayout);
    addView(mRightLayout, layoutParams);
    addView(mDividerView, new LayoutParams(LayoutParams.MATCH_PARENT, 1));
    setBackgroundColor(context.getResources().getColor(R.color.color_6b7072));
    setTitle(mTitle);
    setBackgroundColor(mBackground);
    setLeftImageResource(mBackIconRes);
    setDividerVisible(mDividerVisible);
  }

  /**
   * 开启沉浸式，状态栏的高度会增加到title bar的高度中
   * java代码中也需要进行设置
   */
  public void setImmersive(boolean immersive) {
    mImmersive = immersive;
    if (mImmersive) {
      mStatusBarHeight = getStatusBarHeight();
    } else {
      mStatusBarHeight = 0;
    }
  }

  public void setHeight(int height) {
    mHeight = height;
    setMeasuredDimension(getMeasuredWidth(), mHeight);
  }

  /**
   * 默认文字样式设置
   */
  private void setDefaultTextStyle(TextView mTextView) {
    if (null != mContext) {
      mTextView.setTextColor(mContext.getResources().getColor(R.color.color_6b7072));
      mTextView.setSingleLine();
      mTextView.setGravity(Gravity.CENTER_VERTICAL);
      mTextView.setEllipsize(TextUtils.TruncateAt.END);
    }
  }

  /**
   * 左布局设置方法
   */
  public void setLeftText(CharSequence title) {
    if (null == title) {
      return;
    }
    //设置文字和返回按钮间距
    mLeftText.setCompoundDrawablePadding(mActionPadding);
    mLeftText.setText(title);
    adjustLeftTextPadding();
  }

  public void setLeftText(int resid) {
    //设置文字和返回按钮间距
    mLeftText.setCompoundDrawablePadding(mActionPadding);
    mLeftText.setText(resid);
  }

  public void setLeftImageResource(int resId) {
    mLeftText.setCompoundDrawablesWithIntrinsicBounds(resId, 0, 0, 0);
    adjustLeftTextPadding();
  }

  private void adjustLeftTextPadding() {
    mLeftText.setPadding(mOutPadding, 0, mOutPadding, 0);
  }

  public void setLeftClickListener(OnClickListener l) {
    mLeftText.setOnClickListener(l);
  }

  /**
   * @param left 单位dp
   * @param top 单位dp
   * @param right 单位dp
   * @param bottom 单位dp
   */
  public void setLeftTextPadding(int left, int top, int right, int bottom) {
    mLeftText.setPadding(dip2px(left), dip2px(top), dip2px(right), dip2px(bottom));
  }

  public void setLeftTextBackground(int resId) {
    mLeftText.setBackgroundResource(resId);
  }

  public void setLeftTextSize(float size) {
    mLeftText.setTextSize(size);
  }

  public void setLeftTextColor(int color) {
    mLeftText.setTextColor(color);
  }

  public void setLeftVisible(boolean visible) {
    mLeftText.setVisibility(visible ? View.VISIBLE : View.GONE);
  }

  /**
   * 设置文字标题，是否需要从新添加title，一般来说不需要，除非之前添加过centerview
   *
   * @param title 标题
   */
  public void setTitle(CharSequence title) {
    setTitle(title, false);
  }

  /**
   * 中央布局设置方法
   *
   * @param title 标题
   * @param needReAdd 是否需要从新添加,一般来说不需要，除非之前添加过centerview
   */
  public void setTitle(CharSequence title, boolean needReAdd) {
    if (null == title) {
      return;
    }
    if (needReAdd) {
      mCenterLayout.removeAllViews();
      mCenterLayout.addView(mCenterText);
      mCenterLayout.addView(mSubTitleText);
    }
    //若用换行符分隔文字，则主标题和子标题竖直排列
    int index = title.toString().indexOf("\n");
    if (index > 0) {
      setTitle(title.subSequence(0, index), title.subSequence(index + 1, title.length()),
          LinearLayout.VERTICAL);
    } else {
      //若用制表符分隔文字，则主标题和子标题水平排列
      index = title.toString().indexOf("\t");
      if (index > 0) {
        setTitle(title.subSequence(0, index), " " + title.subSequence(index + 1, title.length()),
            LinearLayout.HORIZONTAL);
      } else {
        mCenterText.setText(title);
        mSubTitleText.setVisibility(View.GONE);
      }
    }
  }

  private void setTitle(CharSequence title, CharSequence subTitle, int orientation) {
    mCenterLayout.setOrientation(orientation);
    mCenterText.setText(title);
    mSubTitleText.setText(subTitle);
    mSubTitleText.setVisibility(View.VISIBLE);
  }

  public void setTitle(int resid) {
    setTitle(getResources().getString(resid));
  }

  public void setCenterClickListener(OnClickListener l) {
    mCenterLayout.setOnClickListener(l);
  }

  public void setMainTitleSize(float size) {
    mCenterText.setTextSize(size);
  }

  public void setMainTitleColor(int color) {
    mCenterText.setTextColor(color);
  }

  public void setMainTitleBackground(int resid) {
    mCenterText.setBackgroundResource(resid);
  }

  public void setMainTitleVisible(boolean visible) {
    mCenterText.setVisibility(visible ? View.VISIBLE : View.GONE);
  }

  public void setSubTitleSize(float size) {
    mSubTitleText.setTextSize(size);
  }

  public void setSubTitleColor(int color) {
    mSubTitleText.setTextColor(color);
  }

  public void setSubTitleBackground(int resid) {
    mSubTitleText.setBackgroundResource(resid);
  }

  public void setSubTitleVisible(boolean visible) {
    mSubTitleText.setVisibility(visible ? View.VISIBLE : View.GONE);
  }

  /**
   * 中央布局设置自定义view，如搜索框等
   */
  public void setCustomTitleView(View titleView) {
    LinearLayout.LayoutParams lp =
        new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT);
    titleView.setLayoutParams(lp);
    mCenterLayout.removeAllViews();
    mCenterLayout.addView(titleView);
  }

  /**
   * 设置中央布局控件可见性
   */
  public void setCenterViewVisible(int visible) {
    mCenterLayout.setVisibility(visible);
  }

  /**
   * 设置中央布局不居中，紧凑排布
   */
  public void setIsCenterAlways(boolean isCenterAlways) {
    this.isCenterAlways = isCenterAlways;
    invalidate();
  }

  /**
   * 获得布局的真实宽度
   */
  private int getRealWidth(View view) {
    int result = mMinSidePadding;
    if (null != view) {
      result = (view.getVisibility() == View.GONE) ? mMinSidePadding : view.getMeasuredWidth();
    }
    return result;
  }

  /**
   * 底部分割线设置方法
   */

  public void setDivider(int resId) {
    mDividerView.setBackgroundResource(resId);
  }

  public void setDividerColor(int color) {
    mDividerView.setBackgroundColor(color);
  }

  public void setDividerHeight(int dividerHeight) {
    mDividerView.getLayoutParams().height = dividerHeight;
  }

  public void setDividerVisible(boolean visible) {
    mDividerView.setVisibility(visible ? View.VISIBLE : View.GONE);
  }

  /**
   *
   */
    /*public void setActionTextColor(int colorResId) {
        mActionTextColor = colorResId;
    }*/
  @Override public void onClick(View view) {
    final Object tag = view.getTag();
    if (tag instanceof Action) {
      final Action action = (Action) tag;
      action.performAction(view);
    }
  }

  /**
   * Adds a list of {@link Action}s.
   *
   * @param actionList the actions to add
   */
  public void addActions(ActionList actionList) {
    int actions = actionList.size();
    for (int i = 0; i < actions; i++) {
      addAction(actionList.get(i));
    }
  }

  /**
   * Adds a new {@link Action}.
   *
   * @param action the action to add
   */
  public View addAction(Action action) {
    final int index = mRightLayout.getChildCount();
    return addAction(action, index);
  }

  /**
   * Adds a new {@link Action} at the specified index.
   *
   * @param action the action to add
   * @param index the position at which to add the action
   */
  public View addAction(Action action, int index) {
    if (null == action) {
      return null;
    }
    mActionList.add(action);
    LinearLayout.LayoutParams params =
        new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT);
    View view = inflateAction(action);
    mRightLayout.addView(view, index, params);
    return view;
  }

  /**
   * Removes all action views from this action bar
   */
  public void removeAllActions() {
    mRightLayout.removeAllViews();
  }

  /**
   * Remove a action from the action bar.
   *
   * @param index position of action to remove
   */
  public void removeActionAt(int index) {
    mRightLayout.removeViewAt(index);
  }

  /**
   * Remove a action from the action bar.
   *
   * @param action The action to remove
   */
  public void removeAction(Action action) {
    int childCount = mRightLayout.getChildCount();
    for (int i = 0; i < childCount; i++) {
      View view = mRightLayout.getChildAt(i);
      if (view != null) {
        final Object tag = view.getTag();
        if (tag instanceof Action && tag.equals(action)) {
          mRightLayout.removeView(view);
        }
      }
    }
  }

  /**
   * Returns the number of actions currently registered with the action bar.
   *
   * @return action count
   */
  public int getActionCount() {
    return mRightLayout.getChildCount();
  }

  private View inflateAction(Action action) {
    View view = null;
    if (action instanceof ImageAction) {
      ImageView img = new ImageView(getContext());
      img.setImageResource(action.getDrawable());
      view = img;
    } else if (action instanceof TextAction) {
      TextView text = new TextView(getContext());
      setDefaultTextStyle(text);
      text.setText(action.getText());
      text.setTextSize(DEFAULT_ACTION_TEXT_SIZE);
      if (((TextAction) action).getColor() != -1) {
        text.setTextColor(((TextAction) action).getColor());
      }
      view = text;
    } else if (action instanceof ViewAction) {
      view = ((ViewAction) action).getView();
    }
    if (action.getBackground() != 0) {
      view.setBackgroundResource(action.getBackground());
    }
    view.setPadding(0, 0, mOutPadding, 0);
    view.setTag(action);
    view.setOnClickListener(this);
    return view;
  }

  public View getViewByAction(Action action) {
    View view = findViewWithTag(action);
    return view;
  }

  /**
   * 设置右边第一个按钮图标
   *
   * @param drawable 图片资源
   */
  public void setRightImage(Drawable drawable) {
    setRightImage(0, drawable);
  }

  /**
   * 设置右边按钮图标
   *
   * @param index 图标位置
   * @param drawable 图片资源
   */
  public void setRightImage(int index, Drawable drawable) {
    Action action = mActionList.get(index);
    if (null != action) {
      View view = getViewByAction(action);
      if (null != view && view instanceof ImageView) {
        ImageView imageView = (ImageView) view;
        imageView.setImageDrawable(drawable);
      }
    }
  }

  @Override protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
    int heightMode = MeasureSpec.getMode(heightMeasureSpec);
    int height;
    if (heightMode != MeasureSpec.EXACTLY) {// 没有指定高度
      height = mHeight + mStatusBarHeight;
      heightMeasureSpec = MeasureSpec.makeMeasureSpec(mHeight, MeasureSpec.EXACTLY);
    } else {// 指定后高度
      height = MeasureSpec.getSize(heightMeasureSpec) + mStatusBarHeight;
    }

    measureChild(mLeftText, widthMeasureSpec, heightMeasureSpec);
    measureChild(mRightLayout, widthMeasureSpec, heightMeasureSpec);

    if (isCenterAlways) {
      if (mLeftText.getMeasuredWidth() > mRightLayout.getMeasuredWidth()) {
        mCenterLayout.measure(
            MeasureSpec.makeMeasureSpec(mScreenWidth - 2 * mLeftText.getMeasuredWidth(),
                MeasureSpec.EXACTLY), heightMeasureSpec);
      } else {
        mCenterLayout.measure(
            MeasureSpec.makeMeasureSpec(mScreenWidth - 2 * mRightLayout.getMeasuredWidth(),
                MeasureSpec.EXACTLY), heightMeasureSpec);
      }
    } else {
      mCenterLayout.measure(MeasureSpec.makeMeasureSpec(mScreenWidth -
          getRealWidth(mLeftText) -
          getRealWidth(mRightLayout), MeasureSpec.EXACTLY), heightMeasureSpec);
    }

    measureChild(mDividerView, widthMeasureSpec, heightMeasureSpec);
    setMeasuredDimension(MeasureSpec.getSize(widthMeasureSpec), height);
  }

  @Override protected void onLayout(boolean changed, int l, int t, int r, int b) {
    mLeftText.layout(0, mStatusBarHeight, mLeftText.getMeasuredWidth(),
        mLeftText.getMeasuredHeight() + mStatusBarHeight);
    mRightLayout.layout(mScreenWidth - mRightLayout.getMeasuredWidth(), mStatusBarHeight,
        mScreenWidth, mRightLayout.getMeasuredHeight() + mStatusBarHeight);

    if (isCenterAlways) {
      if (mLeftText.getMeasuredWidth() > mRightLayout.getMeasuredWidth()) {
        mCenterLayout.layout(mLeftText.getMeasuredWidth(), mStatusBarHeight,
            mScreenWidth - mLeftText.getMeasuredWidth(), getMeasuredHeight());
      } else {
        mCenterLayout.layout(mRightLayout.getMeasuredWidth(), mStatusBarHeight,
            mScreenWidth - mRightLayout.getMeasuredWidth(), getMeasuredHeight());
      }
    } else {
      mCenterLayout.layout(getRealWidth(mLeftText), mStatusBarHeight,
          mScreenWidth - getRealWidth(mRightLayout), getMeasuredHeight());
    }

    mDividerView.layout(0, getMeasuredHeight() - mDividerView.getMeasuredHeight(),
        getMeasuredWidth(), getMeasuredHeight());
  }

  private int dip2px(int dpValue) {
    final float scale = Resources.getSystem().getDisplayMetrics().density;
    return (int) (dpValue * scale + 0.5f);
  }

  /**
   * 计算状态栏高度高度 getStatusBarHeight
   */
  public static int getStatusBarHeight() {
    return getInternalDimensionSize(Resources.getSystem(), STATUS_BAR_HEIGHT_RES_NAME);
  }

  private static int getInternalDimensionSize(Resources res, String key) {
    int result = 0;
    int resourceId = res.getIdentifier(key, "dimen", "android");
    if (resourceId > 0) {
      result = res.getDimensionPixelSize(resourceId);
    }
    return result;
  }

  /**
   * A {@link LinkedList} that holds a list of {@link Action}s.
   */
  @SuppressWarnings("serial") public static class ActionList extends LinkedList<Action> {
  }

  /**
   * Definition of an action that could be performed, along with a icon to
   * show.
   */
  public interface Action {

    String getText();

    int getDrawable();

    void performAction(View view);

    int getBackground();
  }

  public static class BaseAction implements Action {
    @Override public int getDrawable() {
      return 0;
    }

    @Override public void performAction(View view) {

    }

    @Override public String getText() {
      return null;
    }

    @Override public int getBackground() {
      return 0;
    }
  }

  public static class ImageAction extends BaseAction {

    private int mDrawable;

    public ImageAction(int drawable) {
      mDrawable = drawable;
    }

    @Override public int getDrawable() {
      return mDrawable;
    }
  }

  public static class TextAction extends BaseAction {

    final private String mText;
    private int mColor = -1;

    public TextAction(String text) {
      mText = text;
    }

    public TextAction(String text, int color) {
      mText = text;
      mColor = color;
    }

    @Override public String getText() {
      return mText;
    }

    public int getColor() {
      return mColor;
    }
  }

  /**
   * 低级接口，直接返回view
   */
  public static class ViewAction extends BaseAction {

    private View mView;

    public ViewAction(View view) {
      mView = view;
    }

    public View getView() {
      return mView;
    }
  }
}

