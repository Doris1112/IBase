package com.doris.ibase.utils;

import android.content.Context;
import android.content.pm.PackageManager;
import androidx.core.app.ActivityCompat;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Doris
 * @date 2018/9/1
 */
public class IPermissionUtils {

    /**
     * 检查App是否已授予某些权限
     */
    public static String[] checkPermissions(Context context, String...permissions){
        List<String> mPermissions = new ArrayList<>();
        for (String permission : permissions){
            if (ActivityCompat.checkSelfPermission(context, permission) ==
                    PackageManager.PERMISSION_DENIED){
                // 未授予该权限
                mPermissions.add(permission);
            }
        }
        permissions = new String[mPermissions.size()];
        return mPermissions.toArray(permissions);
    }

    /**
     * 检查App是否已授予指定权限
     */
    public static boolean checkPermissions(Context context, String permission){
        return ActivityCompat.checkSelfPermission(context, permission) ==
                PackageManager.PERMISSION_GRANTED;
    }

}
