package com.doris.ibase;

import android.view.MotionEvent;

import com.doris.ibase.activities.IBaseAppCompatActivity;
import com.doris.ibase.helper.ISwipeHelper;

/**
 * Created by Doris on 2018/9/22.
 */
public class NextActivity extends IBaseAppCompatActivity {

    @Override
    protected int getContentLayoutId() {
        return R.layout.activity_next;
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        boolean processTouchEvent = ISwipeHelper.instance().processTouchEvent(ev);
        if (processTouchEvent) {
            return true;
        }
        return super.dispatchTouchEvent(ev);
    }
}
