package com.doris.ibase.helper;

import android.app.Activity;
import android.os.Bundle;

import com.doris.ibase.application.IBaseActivityLifecycle;

import java.util.Stack;

/**
 * Created by Doris on 2018/9/22.
 */
public class IActivityLifeHelper extends IBaseActivityLifecycle {

    private static IActivityLifeHelper sLifecycleHelper;
    private Stack<Activity> mActivityStack;

    private IActivityLifeHelper() {
        mActivityStack = new Stack<>();
    }

    public static IActivityLifeHelper instance() {
        if (sLifecycleHelper == null) {
            synchronized (IActivityLifeHelper.class) {
                if (sLifecycleHelper == null) {
                    sLifecycleHelper = new IActivityLifeHelper();
                }
            }
        }
        return sLifecycleHelper;
    }

    @Override
    public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
        addActivity(activity);
    }

    @Override
    public void onActivityDestroyed(Activity activity) {
        removeActivity(activity);
    }

    /**
     * 添加Activity到堆栈
     */
    public void addActivity(Activity activity) {
        if (activity != null) {
            mActivityStack.add(activity);
        }
    }

    /**
     * 将Activity从堆栈移除
     * @param activity
     */
    public void removeActivity(Activity activity) {
        if (activity != null) {
            mActivityStack.remove(activity);
        }
    }

    /**
     * 获取当前Activity
     */
    public Activity getCurrentActivity() {
        Activity activity = mActivityStack.lastElement();
        return activity;
    }

    /**
     * 获取上一个Activity
     */
    public Activity getPreviewActivity() {
        int size = mActivityStack.size();
        if (size < 2) return null;
        return mActivityStack.elementAt(size - 2);
    }
}
