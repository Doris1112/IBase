package com.doris.ibase.helper;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

import com.doris.ibase.utils.IBaseLogUtils;

import java.lang.ref.WeakReference;

/**
 * @author Doris
 * @date 2018/8/29
 */
public class IAppCrashHandler implements Thread.UncaughtExceptionHandler {

    private Thread.UncaughtExceptionHandler mDefaultHandler;
    private static IAppCrashHandler instance;
    private WeakReference<Context> mContext;
    private final IBaseLogUtils logUtils;

    private IAppCrashHandler(IBaseLogUtils logUtils) {
        this.logUtils = logUtils;
    }

    public static IAppCrashHandler getInstance(IBaseLogUtils logUtils) {
        if (instance == null) {
            instance = new IAppCrashHandler(logUtils);
        }
        return instance;
    }

    public void init(Context context) {
        mContext = new WeakReference<>(context);
        //获取系统默认的UncaughtException处理器
        mDefaultHandler = Thread.getDefaultUncaughtExceptionHandler();
        //设置该CrashHandler为程序的默认处理器
        Thread.setDefaultUncaughtExceptionHandler(this);
    }

    @Override
    public void uncaughtException(Thread arg0, Throwable arg1) {
        if (!handleException(arg1) && mDefaultHandler != null) {
            mDefaultHandler.uncaughtException(arg0, arg1);
        } else {
            try {
                Thread.sleep(2000);
            } catch (Exception e) {
                e.printStackTrace();
            }
            android.os.Process.killProcess(android.os.Process.myPid());
            System.exit(1);
        }
    }

    private boolean handleException(Throwable ex) {
        if (ex == null) {
            return false;
        }
        saveCrashInfoToFile(ex);
        return true;
    }

    private void saveCrashInfoToFile(Throwable ex) {
        if (mContext.get() == null) {
            return;
        }
        try {
            if (logUtils != null) {
                PackageManager pm = mContext.get().getPackageManager();
                PackageInfo pi = pm.getPackageInfo(mContext.get().getPackageName(),
                        PackageManager.GET_ACTIVITIES);
                if (pi != null) {
                    logUtils.writeLog("程序崩溃！版本号: " + pi.versionName);
                }
                logUtils.writeLog(ex);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
