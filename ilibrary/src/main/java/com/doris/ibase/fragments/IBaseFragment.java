package com.doris.ibase.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.IdRes;
import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.doris.ibase.config.INumberConfig;

/**
 * @author Doris
 * @date 2018/8/20
 */
@SuppressWarnings("WeakerAccess")
public abstract class IBaseFragment extends Fragment {

    protected View mRoot;
    /**
     * 标示是否第一次初始化数据
     */
    private boolean mIsFirstInitData = true;
    /**
     * 最后一次点击时间
     */
    private long lastClickTime;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        // 初始化参数
        initArgs(getArguments());
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (mRoot == null) {
            // 初始化当前的根布局，但是不在创建时就添加到container里边
            View root = inflater.inflate(getContentLayoutId(), container, false);
            initBefore(savedInstanceState);
            initWidget(root);
            mRoot = root;
        } else if (mRoot.getParent() != null) {
            // 把当前Root从其父控件中移除
            ((ViewGroup) mRoot.getParent()).removeView(mRoot);
        }
        return mRoot;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (mIsFirstInitData) {
            // 触发一次以后就不会触发
            mIsFirstInitData = false;
            // Kotlin 初始化控件
            initKotlinWidget();
            // 触发
            onFirstInit();
        }
        // 当View创建完成后初始化数据
        initData();
    }

    /**
     * 初始化相关参数
     */
    protected void initArgs(Bundle bundle) {

    }

    /**
     * 得到当前界面的资源文件Id
     *
     * @return 资源文件Id
     */
    @LayoutRes
    protected abstract int getContentLayoutId();

    /**
     * 初始化控件调用之前
     */
    protected void initBefore(Bundle savedInstanceState) {

    }

    /**
     * 初始化控件
     */
    protected void initWidget(View root) {

    }

    protected final <T extends View> T $(@IdRes int id) {
        if (mRoot != null) {
            return mRoot.findViewById(id);
        }
        return null;
    }

    /**
     * Kotlin 初始化控件在return view之后
     */
    protected void initKotlinWidget() {

    }

    /**
     * 当首次初始化数据的时候会调用的方法
     */
    protected void onFirstInit() {

    }

    /**
     * 初始化数据
     */
    protected void initData() {

    }

    /**
     * 返回按键触发时调用
     *
     * @return 返回True代表我已处理返回逻辑，Activity不用自己finish。
     * 返回False代表我没有处理逻辑，Activity自己走自己的逻辑
     */
    public boolean onBackPressed() {
        return false;
    }

    /**
     * 进入到下个activity
     */
    protected void startActivity(Class clz) {
        if (isFastDoubleClick()) {
            return;
        }
        Intent intent = new Intent(getActivity(), clz);
        startActivity(intent);
    }

    /**
     * 进入到下个activity
     */
    protected void startActivityForResult(Class clz, int requestCode) {
        if (isFastDoubleClick()) {
            return;
        }
        Intent intent = new Intent(getActivity(), clz);
        startActivityForResult(intent, requestCode);
    }

    /**
     * 进入到下个activity
     */
    protected void startActivity(Class clz, Bundle bundle) {
        if (isFastDoubleClick()) {
            return;
        }
        Intent intent = new Intent(getActivity(), clz);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    /**
     * 进入到下个activity
     */
    protected void startActivityForResult(Class clz, Bundle bundle, int requestCode) {
        if (isFastDoubleClick()) {
            return;
        }
        Intent intent = new Intent(getActivity(), clz);
        intent.putExtras(bundle);
        startActivityForResult(intent, requestCode);
    }

    /**
     * 防止连续点击跳转两个页面
     */
    private boolean isFastDoubleClick() {
        long time = System.currentTimeMillis();
        if (time - lastClickTime < INumberConfig.CLICK_INTERVAL) {
            return true;
        }
        lastClickTime = time;
        return false;
    }
}