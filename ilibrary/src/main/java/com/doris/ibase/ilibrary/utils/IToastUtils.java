package com.doris.ibase.ilibrary.utils;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.doris.ibase.ilibrary.R;

/**
 * Created by Doris on 2018/9/2.
 */
public class IToastUtils {

    private static Handler uiHandler = new Handler(Looper.getMainLooper());
    private static final int TOAST_DEFAULT_TIME = 2000;

    /**
     * 创建自定义toast并显示下部
     *
     * @param context
     * @param text
     */
    public static void showToastBottom(final Context context, final String text) {
        uiHandler.post(new Runnable() {
            @Override
            public void run() {
                showToast(context, text, TOAST_DEFAULT_TIME);
            }
        });
    }

    public static void showToastBottom(final Context context, final int resId) {
        uiHandler.post(new Runnable() {
            @Override
            public void run() {
                showToast(context, context.getResources().getString(resId), TOAST_DEFAULT_TIME);
            }
        });
    }

    public static void showToastCenter(final Context context, final String text) {
        uiHandler.post(new Runnable() {
            @Override
            public void run() {
                showToast(context, Gravity.CENTER, text, TOAST_DEFAULT_TIME);
            }
        });
    }

    public static void showToastCenter(final Context context, final String text, final int show_time) {
        uiHandler.post(new Runnable() {
            @Override
            public void run() {
                showToast(context, Gravity.CENTER, text, show_time);
            }
        });
    }

    /**
     * 自定义Toast 位置自定义
     *
     * @param context
     * @param gravity 放的位置 eg: Gravity.BOTTOM
     * @param resId   内容资源id
     */
    public static void showToast(final Context context, final int gravity, final int resId) {
        uiHandler.post(new Runnable() {
            @Override
            public void run() {
                showToast(context, gravity, context.getResources().getString(resId), TOAST_DEFAULT_TIME);
            }
        });
    }

    /**
     * 自定义Toast 位置自定义
     *
     * @param context
     * @param gravity 放的位置 eg: Gravity.BOTTOM
     * @param text    内容
     */
    public static void showToast(final Context context, final int gravity, final String text) {
        uiHandler.post(new Runnable() {
            @Override
            public void run() {
                showToast(context, gravity, text, TOAST_DEFAULT_TIME);
            }
        });
    }

    /**
     * 居中大Toast
     *
     * @param context
     * @param text    内容
     * @param flagRes
     */
    public static void showBigToastCenter(final Context context, final String text, final int flagRes) {
        uiHandler.post(new Runnable() {
            @Override
            public void run() {
                showBigToast(context, text, flagRes, TOAST_DEFAULT_TIME);
            }
        });
    }


    /**
     * 创建自定义toast并显示
     *
     * @param context
     * @param text
     * @param time    单位毫秒
     */
    private static void showToast(final Context context, final String text, final int time) {
        if (context == null) {
            return;
        }
        Toast toast = new Toast(context);
        View view = LayoutInflater.from(context).inflate(R.layout.toast, null);
        TextView textView = view.findViewById(R.id.tv_toast);
        textView.setText(text);
        toast.setView(view);
        toast.setGravity(Gravity.BOTTOM, 0, 200);
        toast.setDuration(time);
        toast.show();
    }

    public static void showToast(final Context context, final int gravity, final String text, final int time) {
        Toast toast = new Toast(context);
        View view = LayoutInflater.from(context).inflate(R.layout.toast, null);
        TextView textView = view.findViewById(R.id.tv_toast);
        textView.setText(text);
        toast.setView(view);
        toast.setGravity(gravity, 0, 0);
        toast.setDuration(time);
        toast.show();
    }

    /**
     * 中间大提示
     *
     * @param context
     * @param text
     * @param flagRes
     * @param time
     */
    private static void showBigToast(final Context context, final String text, int flagRes, final int time) {
        Toast toast = new Toast(context);
        View view = LayoutInflater.from(context).inflate(R.layout.toast_big, null);
        TextView textView = view.findViewById(R.id.tv_toast);
        ImageView imageView = view.findViewById(R.id.tv_toast_img);
        imageView.setImageResource(flagRes);
        textView.setText(text);
        toast.setView(view);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.setDuration(time);
        toast.show();
    }

}
