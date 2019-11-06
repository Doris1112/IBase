package com.doris.ibase.utils;

import android.annotation.SuppressLint;
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
 * @author Doris
 * @date 2018/9/2
 */
@SuppressWarnings("WeakerAccess")
public class IToastUtils {

    private static Handler uiHandler = new Handler(Looper.getMainLooper());
    private static final int TOAST_DEFAULT_TIME = 2000;

    // 显示在底部
    public static void showToastBottom(Context context, int resId) {
        if (context == null) {
            return;
        }
        showToastBottom(context, context.getString(resId), TOAST_DEFAULT_TIME);
    }

    public static void showToastBottom(Context context, String text) {
        showToastBottom(context, text, TOAST_DEFAULT_TIME);
    }

    public static void showToastBottom(Context context, int resId, int showTime) {
        if (context == null) {
            return;
        }
        showToastBottom(context, context.getString(resId), showTime);
    }

    public static void showToastBottom(final Context context, final String text, final int showTime) {
        uiHandler.post(new Runnable() {
            @Override
            public void run() {
                showBottomToast(context, text, showTime);
            }
        });
    }

    // 显示在中间
    public static void showToastCenter(Context context, int resId) {
        if (context == null) {
            return;
        }
        showToastCenter(context, context.getString(resId), TOAST_DEFAULT_TIME);
    }

    public static void showToastCenter(Context context, String text) {
        showToastCenter(context, text, TOAST_DEFAULT_TIME);
    }

    public static void showToastCenter(Context context, int resId, int showTime) {
        if (context == null) {
            return;
        }
        showToastCenter(context, context.getString(resId), showTime);
    }

    public static void showToastCenter(final Context context, final String text, final int showTime) {
        uiHandler.post(new Runnable() {
            @Override
            public void run() {
                showDefaultToast(context, Gravity.CENTER, text, showTime);
            }
        });
    }

    /**
     * 自定义显示位置
     * @param context Context
     * @param gravity 显示位置
     * @param resId 显示消息
     */
    public static void showToast(Context context, int gravity, int resId) {
        if (context == null) {
            return;
        }
        showToast(context, gravity, context.getString(resId), TOAST_DEFAULT_TIME);
    }

    public static void showToast(Context context, int gravity, String text) {
        showToast(context, gravity, text, TOAST_DEFAULT_TIME);
    }

    public static void showToast(Context context, int gravity, int resId, int showTime) {
        if (context == null) {
            return;
        }
        showToast(context, gravity, context.getString(resId), showTime);
    }

    public static void showToast(final Context context, final int gravity, final String text, final int showTime) {
        uiHandler.post(new Runnable() {
            @Override
            public void run() {
                showDefaultToast(context, gravity, text, showTime);
            }
        });
    }

    /**
     * 显示在中间大Toast
     * @param context Context
     * @param resId 显示文字
     * @param flagRes 显示图片
     */
    public static void showBigToastCenter(Context context, int resId, int flagRes) {
        showBigToastCenter(context, context.getString(resId), flagRes, TOAST_DEFAULT_TIME);
    }

    public static void showBigToastCenter(Context context, String text, int flagRes) {
        showBigToastCenter(context, text, flagRes, TOAST_DEFAULT_TIME);

    }

    public static void showBigToastCenter(Context context, int resId, int flagRes, int showTime) {
        if (context == null){
            return;
        }
        showBigToastCenter(context, context.getString(resId), flagRes, showTime);
    }

    public static void showBigToastCenter(final Context context, final String text, final int flagRes, final int showTime) {
        uiHandler.post(new Runnable() {
            @Override
            public void run() {
                showBigToast(context, text, flagRes, showTime);
            }
        });
    }

    // 显示Toast
    private static void showBottomToast(final Context context, final String text, final int time) {
        if (context == null) {
            return;
        }
        Toast toast = new Toast(context);
        @SuppressLint("InflateParams")
        View view = LayoutInflater.from(context).inflate(R.layout.i_toast, null);
        TextView textView = view.findViewById(R.id.i_tv_toast);
        textView.setText(text);
        toast.setView(view);
        toast.setGravity(Gravity.BOTTOM, 0, 200);
        toast.setDuration(time);
        toast.show();
    }

    private static void showDefaultToast(final Context context, final int gravity, final String text, final int time) {
        if (context == null) {
            return;
        }
        Toast toast = new Toast(context);
        @SuppressLint("InflateParams")
        View view = LayoutInflater.from(context).inflate(R.layout.i_toast, null);
        TextView textView = view.findViewById(R.id.i_tv_toast);
        textView.setText(text);
        toast.setView(view);
        toast.setGravity(gravity, 0, 0);
        toast.setDuration(time);
        toast.show();
    }

    private static void showBigToast(final Context context, final String text, int flagRes, final int time) {
        Toast toast = new Toast(context);
        @SuppressLint("InflateParams")
        View view = LayoutInflater.from(context).inflate(R.layout.i_toast_big, null);
        TextView textView = view.findViewById(R.id.i_tv_toast);
        ImageView imageView = view.findViewById(R.id.i_tv_toast_img);
        imageView.setImageResource(flagRes);
        textView.setText(text);
        toast.setView(view);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.setDuration(time);
        toast.show();
    }

}
