package com.doris.ibase.ilibrary.application;

import android.content.Context;
import android.os.Build;
import android.os.StrictMode;
import android.support.multidex.MultiDex;
import android.support.multidex.MultiDexApplication;

import com.doris.ibase.ilibrary.utils.IAppCrashHandler;
import com.doris.ibase.ilibrary.utils.ILogUtils;

/**
 * Created by Doris on 2018/1/25.
 */
public abstract class IBaseApplication extends MultiDexApplication {

    @Override
    public void onCreate() {
        super.onCreate();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
            StrictMode.setVmPolicy(builder.build());
        }
        IAppCrashHandler.getInstance(getLogUtils()).init(getApplicationContext());
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    protected abstract ILogUtils getLogUtils();

}
