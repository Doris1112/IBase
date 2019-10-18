package com.doris.ibase;

import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.widget.Toast;

import com.doris.ibase.activities.IBaseAppCompatActivity;
import com.doris.ibase.utils.IToastUtils;
import com.doris.ibase.widget.IAlertDialog;

public class MainActivity extends IBaseAppCompatActivity {

    @Override
    protected int getContentLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    protected void initData() {
        super.initData();
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
            case R.id.b_7:

                break;
            default:
                break;
        }
    }

    @Override
    public void onBackPressed() {
        Toast.makeText(this, "test", Toast.LENGTH_SHORT).show();
        super.onBackPressed();
    }
}
