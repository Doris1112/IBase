package com.doris.ibase.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.doris.ibase.config.INumberConfig;

/**
 * @author Doris
 * @date 2018/8/20.
 */
public abstract class IBaseAppCompatActivity extends AppCompatActivity {

    /**
     * 最后一次点击时间
     */
    private long lastClickTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 在界面未初始化之前调用的初始化窗口
        initWidows();
        // 初始化参数
        if (initArgs(getIntent())) {
            // 添加Activity
            IActivityContainer.getInstance().addActivity(this);
            // 得到界面Id并设置到Activity界面中
            setContentView(getContentLayoutId());
            initBefore();
            initWidget();
            initData();
        } else {
            finish();
        }
    }

    /**
     * 初始化控件调用之前
     */
    protected void initBefore() {

    }

    /**
     * 初始化窗口
     */
    protected void initWidows() {

    }

    /**
     * 初始化相关参数
     *
     * @param intent
     * @return 如果参数正确返回True，错误返回False
     */
    protected boolean initArgs(Intent intent) {
        return true;
    }

    /**
     * 得到当前界面的资源文件Id
     *
     * @return 资源文件Id
     */
    protected abstract int getContentLayoutId();

    /**
     * 初始化控件
     */
    protected void initWidget() {

    }

    /**
     * 初始化数据
     */
    protected void initData() {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        IActivityContainer.getInstance().removeActivity(this);
    }

    /**
     * 进入到下个activity
     */
    protected void startActivity(Class clz) {
        if (isFastDoubleClick()) {
            return;
        }
        Intent intent = new Intent(this, clz);
        startActivity(intent);
    }

    /**
     * 进入到下个activity
     */
    protected void startActivityForResult(Class clz, int requestCode) {
        if (isFastDoubleClick()) {
            return;
        }
        Intent intent = new Intent(this, clz);
        startActivityForResult(intent, requestCode);
    }

    /**
     * 进入到下个activity
     */
    protected void startActivity(Class clz, Bundle bundle) {
        if (isFastDoubleClick()) {
            return;
        }
        Intent intent = new Intent(this, clz);
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
        Intent intent = new Intent(this, clz);
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
