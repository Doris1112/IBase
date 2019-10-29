package com.doris.ibase;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;

import com.doris.ibase.activities.IBaseAppCompatActivity;
import com.doris.ibase.config.INumberConfig;
import com.doris.ibase.helper.IDeviceConfig;
import com.doris.ibase.utils.IToastUtils;
import com.doris.ibase.widget.IAlertDialog;

public class MainActivity extends IBaseAppCompatActivity {

    private TextView mDeviceConfig;

    @Override
    protected int getContentLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    protected void initWidget() {
        super.initWidget();
        mDeviceConfig = findViewById(R.id.tv_device_config);
    }

    @Override
    protected void initData() {
        super.initData();
        ActivityCompat.requestPermissions(this,
                new String[]{
                        Manifest.permission.READ_PHONE_STATE},
                INumberConfig.ZERO);
    }

    private void loadDeviceConfig() {
        mDeviceConfig.setText("手机设备名称：");
        mDeviceConfig.append(IDeviceConfig.getDeviceName());
        mDeviceConfig.append("\nAndroid版本：");
        mDeviceConfig.append(IDeviceConfig.getAndroidVersion());
        mDeviceConfig.append("\nMAC地址：");
        mDeviceConfig.append(IDeviceConfig.getMac());
        mDeviceConfig.append("\n手机唯一标识：");
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE)
                == PackageManager.PERMISSION_GRANTED) {
            mDeviceConfig.append(IDeviceConfig.getOnlyCoding(this));
        }
        mDeviceConfig.append("\nIP地址：");
        mDeviceConfig.append(IDeviceConfig.getIp(this));
        mDeviceConfig.append("\n当前网络：");
        mDeviceConfig.append(IDeviceConfig.getNet(this));
        mDeviceConfig.append("\nUserAgent：");
        mDeviceConfig.append(IDeviceConfig.getUserAgent());
        mDeviceConfig.append("\n当前App的VersionName：");
        mDeviceConfig.append(IDeviceConfig.getAppVersionName(this));
        mDeviceConfig.append("\n当前App的VersionCode：");
        mDeviceConfig.append(IDeviceConfig.getAppVersionCode(this) + "");
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.b_1:
                new IAlertDialog(this).setTitleText("测试标题")
                        .setContentText("测试内容")
                        .show();
                IToastUtils.showToastCenter(this, "测试内容");
                break;
            case R.id.b_2:
                new IAlertDialog(this, IAlertDialog.CONFIRM_TYPE)
                        .setContentText("测试内容")
                        .setOnBackClickCancelable(false)
                        .show();
                IToastUtils.showToastBottom(this, "测试内容");
                break;
            case R.id.b_3:
                new IAlertDialog(this)
                        .setContentText("删除测试内容")
                        .setCancelText("取消")
                        .setConfirmText("删除")
                        .show();
                IToastUtils.showBigToastCenter(this, "测试内容", R.mipmap.ic_launcher);
                break;
            case R.id.b_4:
                new IAlertDialog(this).setTitleText("测试标题")
                        .setContentText("删除测试内容")
                        .setCancelText("取消")
                        .setCancelTextColor(Color.parseColor("#ff4000"))
                        .setConfirmText("删除")
                        .show();
                IToastUtils.showToast(this, Gravity.TOP, "测试内容");
                break;
            case R.id.b_5:
                startActivity(NextActivity.class);
                break;
            case R.id.b_6:
                new IAlertDialog(this, IAlertDialog.CONFIRM_TYPE)
                        .setContentText(R.string.app_name)
                        .setOnBackClickCancelable(false)
                        .show();
                break;
            default:
                break;
        }
    }

    @Override
    public void onRequestPermissionsResult(
            int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        loadDeviceConfig();
    }
}
