package com.doris.ibase.refresh;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.os.CountDownTimer;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.DisplayMetrics;

/**
 * @author Doris
 * @date 2019/10/17
 */
class ICircularProgressDrawable extends Drawable {

    private final RectF mTempBounds = new RectF();
    private final Paint mPaint = new Paint();
    private final Paint mArrowPaint = new Paint();
    private final Paint mCirclePaint = new Paint();
    private float mStartTrim = 0.0F;
    private float mEndTrim = 0.0F;
    private float mRotation = 0.0F;
    private float mStrokeWidth;
    private boolean mShowArrow;
    private Path mArrow;
    private float mArrowScale = 1.0F;
    private float mRingCenterRadius;
    private int mArrowWidth;
    private int mArrowHeight;
    private int mAlpha = 255;
    private int mCurrentColor;
    private int mCountDownTimeTotal = 1000;
    private float mOutsideArcAngle = 300f;
    private float mDefaultStartAngle = 105f;
    private float mStartAngle = mDefaultStartAngle;
    private CountDownTimer mCountDownTimer;

    ICircularProgressDrawable(@NonNull Context context) {
        DisplayMetrics metrics = context.getResources().getDisplayMetrics();
        float screenDensity = metrics.density;
        mStrokeWidth = (float) (2.5 * screenDensity);
        mPaint.setStrokeWidth(mStrokeWidth);
        mRingCenterRadius = (float) (7.5 * screenDensity);
        mArrowWidth = (int) (10.0 * screenDensity);
        mArrowHeight = (int) (5.0 * screenDensity);
        init();
    }

    private void init() {
        mPaint.setAntiAlias(true);
        mPaint.setStyle(Paint.Style.STROKE);
        mArrowPaint.setAntiAlias(true);
        mArrowPaint.setStyle(Paint.Style.FILL);
        mCirclePaint.setColor(Color.TRANSPARENT);
        mCountDownTimer = new CountDownTimer(mCountDownTimeTotal, 10) {
            @Override
            public void onTick(long millisUntilFinished) {
                float radio = (float) millisUntilFinished / (float) mCountDownTimeTotal;
                float addAngle = 360 - 360 * radio;
                mStartAngle = mDefaultStartAngle + addAngle;
                invalidateSelf();
            }

            @Override
            public void onFinish() {
                if (mCountDownTimer != null) {
                    mShowArrow = false;
                    mCountDownTimer.start();
                }
            }
        };
    }

    @Override
    public void draw(@NonNull Canvas canvas) {
        Rect bounds = getBounds();
        canvas.save();
        canvas.rotate(mRotation, bounds.exactCenterX(), bounds.exactCenterY());
        onDraw(canvas, bounds);
        canvas.restore();
    }

    @Override
    public void setAlpha(int alpha) {
        mAlpha = alpha;
        invalidateSelf();
    }

    @Override
    public int getAlpha() {
        return mAlpha;
    }

    @Override
    public void setColorFilter(@Nullable ColorFilter colorFilter) {

    }

    @Override
    public int getOpacity() {
        return PixelFormat.TRANSLUCENT;
    }

    void start() {
        cancel();
        mShowArrow = false;
        mCountDownTimer.start();
    }

    void stop() {
        cancel();
        mRotation = 0.0F;
        mShowArrow = false;
        invalidateSelf();
    }

    void setArrowEnabled(boolean show) {
        mShowArrow = show;
        invalidateSelf();
        if (!show) {
            mCountDownTimer.start();
        }
    }

    void setArrowScale(float scale) {
        mArrowScale = scale;
        invalidateSelf();
    }

    void setStartEndTrim(float end) {
        mStartTrim = 0.0f;
        mEndTrim = end;
        invalidateSelf();
    }

    void setProgressRotation(float rotation) {
        mRotation = rotation;
        invalidateSelf();
    }

    void setColor(int color) {
        mCurrentColor = color;
        invalidateSelf();
    }

    private void cancel() {
        if (mCountDownTimer != null) {
            mCountDownTimer.cancel();
        }
    }

    private void onDraw(Canvas c, Rect bounds) {
        RectF arcBounds = mTempBounds;
        float arcRadius = mRingCenterRadius + mStrokeWidth / 2.0F;
        if (mRingCenterRadius <= 0.0F) {
            arcRadius = (float) Math.min(bounds.width(), bounds.height()) / 2.0F -
                    Math.max((float) mArrowWidth * mArrowScale / 2.0F,
                            mStrokeWidth / 2.0F);
        }
        arcBounds.set((float) bounds.centerX() - arcRadius,
                (float) bounds.centerY() - arcRadius,
                (float) bounds.centerX() + arcRadius,
                (float) bounds.centerY() + arcRadius);
        float startAngle = (mStartTrim + mRotation) * 360.0F;
        float endAngle = (mEndTrim + mRotation) * 360.0F;
        float sweepAngle = endAngle - startAngle;
        mPaint.setColor(mCurrentColor);
        mPaint.setAlpha(mAlpha);
        float inset = mStrokeWidth / 2.0F;
        arcBounds.inset(inset, inset);
        c.drawCircle(arcBounds.centerX(), arcBounds.centerY(),
                arcBounds.width() / 2.0F, mCirclePaint);
        arcBounds.inset(-inset, -inset);
        if (mShowArrow) {
            mPaint.setStrokeCap(Paint.Cap.SQUARE);
            c.drawArc(arcBounds, startAngle, sweepAngle, false, mPaint);
            drawTriangle(c, startAngle, sweepAngle, arcBounds);
            mStartAngle = startAngle;
            mDefaultStartAngle = startAngle;
            mOutsideArcAngle = sweepAngle;
        } else {
            mPaint.setStrokeCap(Paint.Cap.ROUND);
            c.drawArc(arcBounds, mStartAngle, mOutsideArcAngle, false, mPaint);
        }
    }

    private void drawTriangle(Canvas c, float startAngle, float sweepAngle, RectF bounds) {
        if (mArrow == null) {
            mArrow = new Path();
            mArrow.setFillType(Path.FillType.EVEN_ODD);
        } else {
            mArrow.reset();
        }
        float centerRadius = Math.min(bounds.width(), bounds.height()) / 2.0F;
        float inset = (float) mArrowWidth * mArrowScale / 2.0F;
        mArrow.moveTo(0.0F, 0.0F);
        mArrow.lineTo((float) mArrowWidth * mArrowScale, 0.0F);
        mArrow.lineTo((float) mArrowWidth * mArrowScale / 2.0F,
                (float) mArrowHeight * mArrowScale);
        mArrow.offset(centerRadius + bounds.centerX() - inset,
                bounds.centerY() + mStrokeWidth / 2.0F);
        mArrow.close();
        mArrowPaint.setColor(mCurrentColor);
        mArrowPaint.setAlpha(mAlpha);
        c.save();
        c.rotate(startAngle + sweepAngle, bounds.centerX(), bounds.centerY());
        c.drawPath(mArrow, mArrowPaint);
        c.restore();
    }

}
