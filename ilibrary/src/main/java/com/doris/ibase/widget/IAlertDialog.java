package com.doris.ibase.widget;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.view.animation.Transformation;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.StringRes;

import com.doris.ibase.ilibrary.R;

/**
 * @author Doris
 * @date 2018/9/20
 */
@SuppressWarnings({"WeakerAccess", "UnusedReturnValue"})
public class IAlertDialog extends Dialog implements View.OnClickListener {

    public static final int CANCEL_TYPE = 0;
    public static final int CONFIRM_TYPE = 1;

    private AnimationSet mDialogInAnim;
    private AnimationSet mDialogOutAnim;
    private Animation mOverlayOutAnim;

    private View mDialogView;
    private TextView mTitle, mContent;
    private Button mCancel, mConfirm;
    private View mGap;

    private String mTitleText, mContentText, mCancelText, mConfirmText;
    private int mTitleTextColor = Color.BLACK, mContentTextColor = Color.DKGRAY,
            mCancelTextColor = Color.GRAY, mConfirmTextColor = Color.BLACK;

    private OnDialogClickListener mCancelClickListener, mConfirmClickListener;
    private boolean mCloseFromCancel, mCancelable = true;
    private int mAlertType;

    public IAlertDialog(@NonNull Context context) {
        this(context, CANCEL_TYPE);
    }

    public IAlertDialog(@NonNull Context context, int alertType) {
        super(context, R.style.i_alert_dialog);
        setCanceledOnTouchOutside(false);

        mAlertType = alertType;

        mDialogInAnim = (AnimationSet) AnimationUtils.loadAnimation(context, R.anim.i_dialog_in);
        mDialogOutAnim = (AnimationSet) AnimationUtils.loadAnimation(context, R.anim.i_dialog_out);
        mDialogOutAnim.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                mDialogView.setVisibility(View.GONE);
                mDialogView.post(new Runnable() {
                    @Override
                    public void run() {
                        if (mCloseFromCancel) {
                            IAlertDialog.super.cancel();
                        } else {
                            IAlertDialog.super.dismiss();
                        }
                    }
                });
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        mOverlayOutAnim = new Animation() {
            @Override
            protected void applyTransformation(float interpolatedTime, Transformation t) {
                if (getWindow() == null) {
                    return;
                }
                WindowManager.LayoutParams wlp = getWindow().getAttributes();
                wlp.alpha = 1 - interpolatedTime;
                getWindow().setAttributes(wlp);
            }
        };
        mOverlayOutAnim.setDuration(120);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.i_alert_dialog);

        if (getWindow() != null){
            mDialogView = getWindow().getDecorView().findViewById(android.R.id.content);
        }
        mTitle = findViewById(R.id.i_tv_dialog_title);
        mContent = findViewById(R.id.i_tv_dialog_content);
        mCancel = findViewById(R.id.i_b_dialog_cancel);
        mConfirm = findViewById(R.id.i_b_dialog_confirm);
        mGap = findViewById(R.id.i_v_dialog_gap);

        switch (mAlertType) {
            case CANCEL_TYPE:
                mCancel.setVisibility(View.VISIBLE);
                mGap.setVisibility(View.VISIBLE);
                mConfirm.setBackgroundResource(R.drawable.i_btn_alert_dialog_right);
                break;
            case CONFIRM_TYPE:
                mCancel.setVisibility(View.GONE);
                mGap.setVisibility(View.GONE);
                mConfirm.setBackgroundResource(R.drawable.i_btn_alert_dialog);
                break;
            default:
                break;
        }

        setCancelable(mCancelable);

        mCancel.setOnClickListener(this);
        mConfirm.setOnClickListener(this);

        setTitleText(mTitleText);
        setContentText(mContentText);
        setCancelText(mCancelText);
        setConfirmText(mConfirmText);

        setTitleTextColor(mTitleTextColor);
        setContentTextColor(mContentTextColor);
        setCancelTextColor(mCancelTextColor);
        setConfirmTextColor(mConfirmTextColor);
    }

    @Override
    protected void onStart() {
        mDialogView.startAnimation(mDialogInAnim);
    }

    @Override
    public void cancel() {
        dismissWithAnimation(true);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.i_b_dialog_cancel) {
            // 取消
            if (mCancelClickListener != null) {
                mCancelClickListener.onClick(IAlertDialog.this);
            }
        } else if (v.getId() == R.id.i_b_dialog_confirm) {
            // 确认
            if (mConfirmClickListener != null) {
                mConfirmClickListener.onClick(IAlertDialog.this);
            }
        }
        dismissWithAnimation(false);
    }

    private void dismissWithAnimation(boolean fromCancel) {
        mCloseFromCancel = fromCancel;
        mConfirm.startAnimation(mOverlayOutAnim);
        mDialogView.startAnimation(mDialogOutAnim);
    }

    /**
     * 点击返回键是否取消对话框
     *
     * @param cancelable 是否取消对话框
     * @return IAlertDialog
     */
    public IAlertDialog setOnBackClickCancelable(boolean cancelable) {
        mCancelable = cancelable;
        setCancelable(mCancelable);
        return this;
    }

    /**
     * 设置对话框标题
     *
     * @param title 标题
     * @return IAlertDialog
     */
    public IAlertDialog setTitleText(String title) {
        mTitleText = title;
        if (mTitleText != null && mTitle != null) {
            mTitle.setVisibility(View.VISIBLE);
            mTitle.setText(mTitleText);
        }
        return this;
    }

    /**
     * 设置对话框标题
     *
     * @param titleResId 标题对应资源ID
     * @return IAlertDialog
     */
    public IAlertDialog setTitleText(@StringRes int titleResId) {
        mTitleText = getContext().getString(titleResId);
        if (mTitle != null) {
            mTitle.setVisibility(View.VISIBLE);
            mTitle.setText(mTitleText);
        }
        return this;
    }

    /**
     * 设置标题文字颜色
     *
     * @param textColor 标题文字颜色
     * @return IAlertDialog
     */
    public IAlertDialog setTitleTextColor(int textColor) {
        mTitleTextColor = textColor;
        if (mTitle != null) {
            mTitle.setTextColor(textColor);
        }
        return this;
    }

    /**
     * 设置对话框提示内容
     *
     * @param content 内容
     * @return IAlertDialog
     */
    public IAlertDialog setContentText(String content) {
        mContentText = content;
        if (mContentText != null && mContent != null) {
            mContent.setText(content);
        }
        return this;
    }

    /**
     * 设置对话框提示内容
     *
     * @param contentResId 内容对应资源ID
     * @return IAlertDialog
     */
    public IAlertDialog setContentText(@StringRes int contentResId) {
        mContentText = getContext().getString(contentResId);
        if (mContent != null) {
            mContent.setText(mContentText);
        }
        return this;
    }

    /**
     * 设置对话框提示内容文字颜色
     *
     * @param textColor 内容文字颜色
     * @return IAlertDialog
     */
    public IAlertDialog setContentTextColor(int textColor) {
        mContentTextColor = textColor;
        if (mContent != null) {
            mContent.setTextColor(textColor);
        }
        return this;
    }

    /**
     * 设置取消按钮文字
     *
     * @param cancel 取消按钮文字
     * @return IAlertDialog
     */
    public IAlertDialog setCancelText(String cancel) {
        mCancelText = cancel;
        if (mCancelText != null && mCancel != null) {
            mCancel.setVisibility(View.VISIBLE);
            mGap.setVisibility(View.VISIBLE);
            mCancel.setText(cancel);
        }
        return this;
    }

    /**
     * 设置取消按钮文字
     *
     * @param cancelResId 取消按钮文字对应资源ID
     * @return IAlertDialog
     */
    public IAlertDialog setCancelText(@StringRes int cancelResId) {
        mCancelText = getContext().getString(cancelResId);
        if (mCancel != null) {
            mCancel.setVisibility(View.VISIBLE);
            mGap.setVisibility(View.VISIBLE);
            mCancel.setText(mCancelText);
        }
        return this;
    }

    /**
     * 设置取消按钮文字颜色
     *
     * @param textColor 取消按钮文字颜色
     * @return IAlertDialog
     */
    public IAlertDialog setCancelTextColor(int textColor) {
        mCancelTextColor = textColor;
        if (mCancel != null) {
            mCancel.setTextColor(textColor);
        }
        return this;
    }

    /**
     * 设置确定按钮文字
     *
     * @param confirm 确定按钮文字
     * @return IAlertDialog
     */
    public IAlertDialog setConfirmText(String confirm) {
        mConfirmText = confirm;
        if (mConfirmText != null && mConfirm != null) {
            mConfirm.setText(confirm);
        }
        return this;
    }

    /**
     * 设置确定按钮文字
     *
     * @param confirmResId 确定按钮文字对应资源ID
     * @return IAlertDialog
     */
    public IAlertDialog setConfirmText(@StringRes int confirmResId) {
        mConfirmText = getContext().getString(confirmResId);
        if (mConfirm != null) {
            mConfirm.setText(mConfirmText);
        }
        return this;
    }

    /**
     * 设置确定按钮文字颜色
     *
     * @param textColor 确定按钮文字颜色
     * @return IAlertDialog
     */
    public IAlertDialog setConfirmTextColor(int textColor) {
        mConfirmTextColor = textColor;
        if (mConfirm != null) {
            mConfirm.setTextColor(textColor);
        }
        return this;
    }

    /**
     * 设置点击取消按钮事件
     *
     * @param listener 事件
     * @return IAlertDialog
     */
    public IAlertDialog setCancelClickListener(OnDialogClickListener listener) {
        mCancelClickListener = listener;
        return this;
    }

    /**
     * 设置点击确定按钮事件
     *
     * @param listener 事件
     * @return IAlertDialog
     */
    public IAlertDialog setConfirmClickListener(OnDialogClickListener listener) {
        mConfirmClickListener = listener;
        return this;
    }

    public interface OnDialogClickListener {
        void onClick(IAlertDialog sweetAlertDialog);
    }
}