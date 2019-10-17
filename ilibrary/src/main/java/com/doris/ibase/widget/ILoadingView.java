package com.doris.ibase.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.CountDownTimer;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import com.doris.ibase.ilibrary.R;

/**
 * @author Doris
 * @date 2019/10/17
 */
public class ILoadingView extends View {
    /**
     * 倒计时的时间，默认1s
     */
    private int mCountDownTimeTotal = 1000;
    /**
     * 内外层圆弧的颜色，默认都为黑色
     */
    private int mOutsideArcColor = Color.BLACK;
    private int mInsideArcColor = Color.BLACK;
    /**
     * 内外层圆弧转过的角度
     */
    private float mOutsideArcAngle = 300f;
    private float mInsideArcAngle = 60f;
    /**
     * 默认起始角度
     */
    private float mDefaultStartAngle = 105f;
    /**
     * 计算改变之后的起始角度
     */
    private float mStartAngle = mDefaultStartAngle;
    /**
     * view 默认的长度和高度
     */
    private int mDefaultSize = 100;
    /**
     * 定义一个倒计时，
     * 用于执行动画效果，
     * 每隔10毫秒执行一次
     */
    private CountDownTimer mCountDownTimer;
    /**
     * 画笔
     */
    private Paint mPaint;

    public ILoadingView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.ILoadingView);
        mCountDownTimeTotal = array.getInteger(
                R.styleable.ILoadingView_round_once_time, mCountDownTimeTotal);
        mOutsideArcColor = array.getColor(
                R.styleable.ILoadingView_round_outside_color, mOutsideArcColor);
        mInsideArcColor = array.getColor(
                R.styleable.ILoadingView_round_inside_color, mInsideArcColor);
        mOutsideArcAngle = array.getFloat(
                R.styleable.ILoadingView_round_outside_angle, mOutsideArcAngle);
        mInsideArcAngle = array.getFloat(
                R.styleable.ILoadingView_round_inside_angle, mInsideArcAngle);
        mDefaultStartAngle = array.getFloat(
                R.styleable.ILoadingView_round_start_angle, mDefaultStartAngle);
        array.recycle();
        init();
    }

    private void init() {
        mCountDownTimer = new CountDownTimer(mCountDownTimeTotal, 10) {
            @Override
            public void onTick(long millisUntilFinished) {
                // 计算当前剩余时间的比例
                float radio = (float) millisUntilFinished / (float) mCountDownTimeTotal;
                float addAngle = 360 - 360 * radio;
                // 根据比较改变开始位置的角度
                mStartAngle = mDefaultStartAngle + addAngle;
                invalidate();
            }

            @Override
            public void onFinish() {
                if (mCountDownTimer != null) {
                    mCountDownTimer.start();
                }
            }
        };
        mCountDownTimer.start();
        mPaint = new Paint();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        // 内外层圆弧的宽度
        float outsideArcWidth = 8f;
        //首先绘制最外层的圆
        mPaint.setAntiAlias(true);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(outsideArcWidth);
        mPaint.setStrokeJoin(Paint.Join.ROUND);
        mPaint.setStrokeCap(Paint.Cap.ROUND);
        mPaint.setColor(mOutsideArcColor);
        canvas.drawArc(10, 10, mDefaultSize - 10,
                mDefaultSize - 10, mStartAngle, mOutsideArcAngle,
                false, mPaint);
        // 绘制内层的圆
        mPaint.reset();
        mPaint.setAntiAlias(true);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(outsideArcWidth);
        mPaint.setStrokeJoin(Paint.Join.ROUND);
        mPaint.setStrokeCap(Paint.Cap.ROUND);
        mPaint.setColor(mInsideArcColor);
        canvas.drawArc(20 + outsideArcWidth, 20 + outsideArcWidth,
                mDefaultSize - (20 + outsideArcWidth),
                mDefaultSize - (20 + outsideArcWidth), (360 - mStartAngle),
                -mInsideArcAngle, false, mPaint);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        mDefaultSize = dealReadSize(widthMeasureSpec, mDefaultSize);
        setMeasuredDimension(mDefaultSize, mDefaultSize);
    }

    /**
     * 根据不同的model处理不同的尺寸
     */
    private int dealReadSize(int measureSpec, int defaultSize) {
        int result = 0;
        int model = MeasureSpec.getMode(measureSpec);
        int size = MeasureSpec.getSize(measureSpec);
        switch (model) {
            case MeasureSpec.UNSPECIFIED:
                // 不限制，使用默认的尺寸
                result = defaultSize;
                break;
            case MeasureSpec.AT_MOST:
                // 上限
                result = Math.min(defaultSize, size);
                break;
            case MeasureSpec.EXACTLY:
                result = size;
                break;
        }
        return result;
    }

}
