package com.doris.ibase.ilibrary.application;

import android.content.Context;
import android.os.Build;
import android.os.StrictMode;
import android.support.multidex.MultiDex;
import android.support.multidex.MultiDexApplication;

import com.doris.ibase.ilibrary.utils.AppCrashHandler;
import com.doris.ibase.ilibrary.utils.ILogUtils;

import java.io.File;

/**
 * Created by Doris on 2018/1/25.
 */
public abstract class IBaseApplication extends MultiDexApplication {

    private static IBaseApplication instance;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
            StrictMode.setVmPolicy(builder.build());
        }
        AppCrashHandler.getInstance(getLogUtils()).init(getApplicationContext());
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    protected abstract ILogUtils getLogUtils();

    /**
     * 外部获取单例
     *
     * @return Application
     */
    public static IBaseApplication getInstance() {
        return instance;
    }

    /**
     * 获取缓存文件夹地址
     *
     * @return 当前APP的缓存文件夹地址
     */
    public static File getCacheDirFile() {
        return instance.getCacheDir();
    }

}
