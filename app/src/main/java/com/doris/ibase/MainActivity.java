package com.doris.ibase;

import android.graphics.Color;
import android.view.View;
import android.widget.Toast;

import com.doris.ibase.activities.IBaseAppCompatActivity;
import com.doris.ibase.widget.IAlertDialog;

public class MainActivity extends IBaseAppCompatActivity {

    @Override
    protected int getContentLayoutId() {
        return R.layout.activity_main;
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.b_1:
                new IAlertDialog(this).setTitleText("测试标题")
                        .setContentText("测试内容")
                        .show();
                break;
            case R.id.b_2:
                new IAlertDialog(this, IAlertDialog.CONFIRM_TYPE)
                        .setContentText("测试内容")
                        .show();
                break;
            case R.id.b_3:
                new IAlertDialog(this)
                        .setContentText("删除测试内容")
                        .setCancelText("取消")
                        .setConfirmText("删除")
                        .show();
                break;
            case R.id.b_4:
                new IAlertDialog(this).setTitleText("测试标题")
                        .setContentText("删除测试内容")
                        .setCancelText("取消")
                        .setCancelTextColor(Color.parseColor("#ff4000"))
                        .setConfirmText("删除")
                        .show();
                break;
            case R.id.b_5:
                startActivity(NextActivity.class);
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
