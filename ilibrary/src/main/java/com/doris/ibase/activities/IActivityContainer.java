package com.doris.ibase.activities;

import android.app.Activity;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Doris
 * @date 2018/8/20
 */
@SuppressWarnings("WeakerAccess")
public class IActivityContainer {

    private IActivityContainer() { }

    private static IActivityContainer instance;
    private final List<Activity> activityStack = new ArrayList<>();

    public static IActivityContainer getInstance() {
        if (instance == null){
            instance  = new IActivityContainer();
        }
        return instance;
    }

    /**
     * 添加 activity
     */
    public void addActivity(Activity activity) {
        activityStack.add(activity);
    }

    /**
     * 移除 activity
     */
    public void removeActivity(Activity activity) {
        activityStack.remove(activity);
    }

    /**
     * 移除最后Activity
     */
    public void removeLastActivity(){
        if (activityStack.size() > 0){
            Activity activity = activityStack.get(activityStack.size() - 1);
            if (activity != null){
                activity.finish();
            }
            activityStack.remove(activity);
        }
    }

    /**
     * 结束所有Activity
     */
    public void finishAllActivity() {
        for (int i = activityStack.size() - 1; i >= 0; i--) {
            if (null != activityStack.get(i)) {
                activityStack.get(i).finish();
            }
        }
        activityStack.clear();
    }
}
