package com.blog.www.swipebackactivity.app;

import android.text.TextUtils;
import java.lang.ref.SoftReference;
import java.util.ArrayList;

import android.app.Activity;

/**
 * 维护应用Activity栈，用来控制栈的大小，超出则finish首页之后的若干个页面，至小于限制为止 上限值通过setActivityStackMaxSize设置，为0则不限制
 * 注意，这个行为在SDK中应该有所不同。
 */
final public class AppManager {
    private static ArrayList<SoftReference<Activity>> sActivityStack;
    private static AppManager sInstance;

    private OnAllActivityClosed mActivityClosed;

    // Activity栈的MaxSize，为0表示不限制
    private int mActivityStackMaxSize = 0;

    /**
     * 设置所有的Activity都关闭了的监听回调
     *
     * @param activityClosed
     *            回调
     */
    public void setOnActivityAllClosed(OnAllActivityClosed activityClosed) {
        mActivityClosed = activityClosed;
    }

    private AppManager() {
        if (sActivityStack == null) {
            sActivityStack = new ArrayList<SoftReference<Activity>>(20);
        }
        // BdLog.addLogPackage("com.baidu.adp.base.BdActivityStack");
    }

    /**
     * 得到单例
     *
     * @return
     */
    public static AppManager getInst() {
        if (sInstance == null) {
            sInstance = new AppManager();
        }
        return sInstance;
    }

    /**
     * 得到当前Activity栈里面的管理的Activity的数量
     *
     * @return
     */
    public int getSize() {
        return sActivityStack.size();
    }

    /**
     * 向Activity栈中压入一个Activity
     *
     * @param activity
     */
    public void pushActivity(Activity activity) {
        // BdLog.d("Activity pushed: " + activity);
        if (activity != null) {
            sActivityStack.add(new SoftReference<Activity>(activity));
            checkAndMaintainActivityStack(mActivityStackMaxSize);
        }
    }

    /**
     * 从Activity栈中移除最上面的Activity
     *
     * @return 返回移除的Activity
     */
    public Activity popActivity() {
        int size = sActivityStack.size();
        if (size == 0) {
            return null;
        }
        SoftReference<Activity> sr = sActivityStack.remove(size - 1);
        if (sr == null) {
            return null;
        }
        Activity activity = sr.get();
        // BdLog.d("Activity popped: " + activity);
        return activity;
    }

    /**
     * 从Activity栈中移除某个index对应的Activity
     *
     * @param index
     *            索引
     * @return 返回移除的Activity
     */
    public Activity popActivity(int index) {
        int size = sActivityStack.size();
        if (size == 0) {
            return null;
        }
        if (index < 0 || index >= size) {
            return null;
        }
        SoftReference<Activity> sr = sActivityStack.remove(index);
        if (sr == null) {
            return null;
        }
        Activity activity = sr.get();
        return activity;
    }

    /**
     * 从Activity栈中移除某个指定的Activity
     *
     * @param activity
     *            要移除的Activity
     */
    public void popActivity(Activity activity) {
        if (activity != null) {
            int size = sActivityStack.size();
            if (size == 0) {
                if (mActivityClosed != null) {
                    mActivityClosed.onActivityClosed();
                }
                return;
            }

            for (int i = size - 1; i >= 0; i--) {
                SoftReference<Activity> sr = sActivityStack.get(i);
                if (sr == null) {
                    sActivityStack.remove(i);
                    continue;
                }

                Activity item = sr.get();
                if (activity.equals(item)) {
                    sActivityStack.remove(i);
                    if (sActivityStack.size() == 0) {
                        if (mActivityClosed != null) {
                            mActivityClosed.onActivityClosed();
                        }
                    }
                    return;
                }

                if (sActivityStack.size() == 0) {
                    if (mActivityClosed != null) {
                        mActivityClosed.onActivityClosed();
                    }
                }
            }
        }
    }

    /**
     * 得到当前处于栈顶的Activity
     *
     * @return
     */
    public Activity currentActivity() {
        int size = sActivityStack.size();
        if (size == 0) {
            return null;
        }
        SoftReference<Activity> sr = sActivityStack.get(size - 1);
        if (sr == null) {
            return null;
        }
        Activity activity = sr.get();
        return activity;
    }

    /**
     * 设置Activity栈的最大数量
     *
     * @param size
     */
    public void setActivityStackMaxSize(int size) {
        // 不允许小于10
        if (size < 10 && size != 0) {
            return;
        }
        mActivityStackMaxSize = size;
    }

    /**
     * 保留3个Activity，其他的Activity都释放移除并且finish
     */
    public void releaseAllPossibleAcitivities() {
        this.checkAndMaintainActivityStack(3);
    }

    /**
     * 清空Activity栈，所有的Activity都释放移除并且finish
     */
    public void releaseAllAcitivities() {
        if (sActivityStack != null) {
            while (!sActivityStack.isEmpty()) {
                SoftReference<Activity> act = sActivityStack.remove(0);

                if (act != null && act.get() != null) {
                    Activity activity = act.get();
                    if (activity != null) {
                        activity.finish();
                    }
                }
            }
        }

        if (mActivityClosed != null) {
            mActivityClosed.onActivityClosed();
        }
    }

    /**
     * 获得Activity栈的最大数量
     *
     * @see AppManager
     * @return
     */
    public int getActivityStackMaxSize() {
        return mActivityStackMaxSize;
    }

    private void checkAndMaintainActivityStack(int activityStackMaxSize) {
        if (activityStackMaxSize == 0) {
            return;
        }

        int currentSize = AppManager.getInst().getSize();
        while (currentSize > activityStackMaxSize) {
            currentSize--;
            // remove the second bottom.
            Activity act = AppManager.getInst().popActivity(1);
            if (act != null) {
                act.finish();
            }
        }
    }

    /**
     * Activity全部关闭时调用的接口
     *
     * @author libin18
     *
     */
    public interface OnAllActivityClosed {
        public void onActivityClosed();
    }

    /**
     * 获取当前栈中的所有Activity名称
     */
    public String getActivityStack() {
        if (sActivityStack == null || sActivityStack.size() == 0) {
            return "";
        }

        StringBuilder builder = new StringBuilder();

        for (SoftReference<Activity> softActivity : sActivityStack) {
            if (softActivity == null) {
                continue;
            }

            Activity activity = softActivity.get();
            if (activity == null) {
                continue;
            }
            String className = "";
            if ((activity != null) && (activity.getClass() != null)) {
                className = activity.getClass().getSimpleName();
            }
            // }

            if (TextUtils.isEmpty(className)) {
                continue;
            }

            builder.append(className + ";");
        }
        return builder.toString();
    }

}
