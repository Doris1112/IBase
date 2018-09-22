package com.doris.ibase.helper;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by Doris on 2018/9/22.
 */
public class IWindowHelper {

    private static IWindowHelper sWindowHelper;
    private int mScreenWidth;

    public static IWindowHelper instance() {
        if (sWindowHelper == null) {
            synchronized (IWindowHelper.class) {
                if (sWindowHelper == null) {
                    sWindowHelper = new IWindowHelper();
                }
            }
        }
        return sWindowHelper;
    }

    public ViewGroup getDecorView(Activity activity) {
        if (activity == null) {
            return null;
        }
        return (ViewGroup) activity.getWindow().getDecorView();
    }

    public View getContentView(Activity activity) {
        ViewGroup decorView = getDecorView(activity);
        if (decorView == null) {
            return null;
        }
        return decorView.getChildAt(0);
    }

    //获取屏幕宽度
    public int getScreenWidth(Context context) {
        if (mScreenWidth == 0) {
            mScreenWidth = context.getResources().getDisplayMetrics().widthPixels;
        }
        return mScreenWidth;
    }

}
