package com.doris.ibase.ilibrary.utils;

import android.content.Context;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Doris on 2018/9/1.
 */
public class IPermissionUtils {

    /**
     * 监测App是否已授予某些权限
     * @param context
     * @param permissions
     * @return
     */
    public static String[] checkPermissions(Context context, String...permissions){
        List<String> mPermissions = new ArrayList<>();
        for (String permission : permissions){
            if (ActivityCompat.checkSelfPermission(context, permission) ==
                    PackageManager.PERMISSION_DENIED){
                // 为授予该权限
                mPermissions.add(permission);
            }
        }
        permissions = new String[mPermissions.size()];
        return mPermissions.toArray(permissions);
    }

    /**
     * 监测App是否已授予指定权限
     * @param context
     * @param permission
     * @return
     */
    public static boolean checkPermissions(Context context, String permission){
        return ActivityCompat.checkSelfPermission(context, permission) ==
                PackageManager.PERMISSION_GRANTED;
    }

}
