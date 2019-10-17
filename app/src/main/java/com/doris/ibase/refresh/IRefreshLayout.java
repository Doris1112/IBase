package com.doris.ibase.refresh;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.NestedScrollingChild;
import android.support.v4.view.NestedScrollingChildHelper;
import android.support.v4.view.NestedScrollingParent;
import android.support.v4.view.NestedScrollingParentHelper;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.ListViewCompat;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Transformation;
import android.widget.AbsListView;
import android.widget.ListView;

/**
 * @author Doris
 * @date 2019/10/17
 */
public class IRefreshLayout extends ViewGroup implements NestedScrollingParent, NestedScrollingChild {

    private View mTarget;
    private OnRefreshListener mListener;
    private boolean mRefreshing;
    private int mTouchSlop;
    private float mTotalDragDistance;
    private float mTotalUnconsumed;
    private final NestedScrollingParentHelper mNestedScrollingParentHelper;
    private final NestedScrollingChildHelper mNestedScrollingChildHelper;
    private final int[] mParentScrollConsumed;
    private final int[] mParentOffsetInWindow;
    private boolean mNestedScrollInProgress;
    private int mMediumAnimationDuration;
    private int mCurrentTargetOffsetTop;
    private float mInitialMotionY;
    private float mInitialDownY;
    private boolean mIsBeingDragged;
    private int mActivePointerId;
    private boolean mReturningToStart;
    private final DecelerateInterpolator mDecelerateInterpolator;
    private static final int[] LAYOUT_ATTRS = new int[]{16842766};
    private ICircleImageView mCircleView;
    private ICircularProgressDrawable mProgress;
    private int mCircleViewIndex;
    protected int mFrom;
    private float mStartingScale;
    protected int mOriginalOffsetTop;
    private int mSpinnerOffsetEnd;
    private Animation mScaleAnimation;
    private Animation mScaleDownAnimation;
    private Animation mAlphaStartAnimation;
    private Animation mAlphaMaxAnimation;
    private Animation mScaleDownToStartAnimation;
    private boolean mNotify;
    private int mCircleDiameter;
    private IRefreshLayout.OnChildScrollUpCallback mChildScrollUpCallback;
    private Animation.AnimationListener mRefreshListener;
    private final Animation mAnimateToCorrectPosition;
    private final Animation mAnimateToStartPosition;

    void reset() {
        mCircleView.clearAnimation();
        mProgress.stop();
        mCircleView.setVisibility(View.GONE);
        setColorViewAlpha(255);
        setTargetOffsetTopAndBottom(mOriginalOffsetTop - mCurrentTargetOffsetTop);

        mCurrentTargetOffsetTop = mCircleView.getTop();
    }

    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);
        if (!enabled) {
            reset();
        }
    }

    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        reset();
    }

    private void setColorViewAlpha(int targetAlpha) {
        mCircleView.getBackground().setAlpha(targetAlpha);
        mProgress.setAlpha(targetAlpha);
    }

    public IRefreshLayout(@NonNull Context context) {
        this(context, null);
    }

    public IRefreshLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        mRefreshing = false;
        mTotalDragDistance = -1.0F;
        mParentScrollConsumed = new int[2];
        mParentOffsetInWindow = new int[2];
        mActivePointerId = -1;
        mCircleViewIndex = -1;
        mRefreshListener = new Animation.AnimationListener() {
            public void onAnimationStart(Animation animation) {
            }

            public void onAnimationRepeat(Animation animation) {
            }

            public void onAnimationEnd(Animation animation) {
                if (mRefreshing) {
                    mProgress.setAlpha(255);
                    mProgress.start();
                    if (mNotify && mListener != null) {
                        mListener.onRefresh();
                    }
                    mCurrentTargetOffsetTop = mCircleView.getTop();
                } else {
                    reset();
                }

            }
        };
        mAnimateToCorrectPosition = new Animation() {
            public void applyTransformation(float interpolatedTime, Transformation t) {
                int endTarget = mSpinnerOffsetEnd - Math.abs(mOriginalOffsetTop);
                int targetTop = mFrom + (int) ((float) (endTarget - mFrom) * interpolatedTime);
                int offset = targetTop - mCircleView.getTop();
                setTargetOffsetTopAndBottom(offset);
                mProgress.setArrowScale(1.0F - interpolatedTime);
            }
        };
        mAnimateToStartPosition = new Animation() {
            public void applyTransformation(float interpolatedTime, Transformation t) {
                moveToStart(interpolatedTime);
            }
        };
        mTouchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
        mMediumAnimationDuration = 2000;
        setWillNotDraw(false);
        mDecelerateInterpolator = new DecelerateInterpolator(2.0F);
        DisplayMetrics metrics = getResources().getDisplayMetrics();
        mCircleDiameter = (int) (40.0F * metrics.density);
        createProgressView();
        setChildrenDrawingOrderEnabled(true);
        mSpinnerOffsetEnd = (int) (64.0F * metrics.density);
        mTotalDragDistance = (float) mSpinnerOffsetEnd;
        mNestedScrollingParentHelper = new NestedScrollingParentHelper(this);
        mNestedScrollingChildHelper = new NestedScrollingChildHelper(this);
        setNestedScrollingEnabled(true);
        mOriginalOffsetTop = mCurrentTargetOffsetTop = -mCircleDiameter;
        moveToStart(1.0F);
        TypedArray a = context.obtainStyledAttributes(attrs, LAYOUT_ATTRS);
        setEnabled(a.getBoolean(0, true));
        a.recycle();
    }

    protected int getChildDrawingOrder(int childCount, int i) {
        if (mCircleViewIndex < 0) {
            return i;
        } else if (i == childCount - 1) {
            return mCircleViewIndex;
        } else {
            return i >= mCircleViewIndex ? i + 1 : i;
        }
    }

    private void createProgressView() {
        mCircleView = new ICircleImageView(getContext());
        mProgress = new ICircularProgressDrawable(getContext());
        mCircleView.setImageDrawable(mProgress);
        mCircleView.setVisibility(View.GONE);
        addView(mCircleView);
    }

    public void setOnRefreshListener(@Nullable IRefreshLayout.OnRefreshListener listener) {
        mListener = listener;
    }

    public void setRefreshing(boolean refreshing) {
        if (refreshing && !mRefreshing) {
            mRefreshing = true;
            int endTarget = mSpinnerOffsetEnd + mOriginalOffsetTop;
            setTargetOffsetTopAndBottom(endTarget - mCurrentTargetOffsetTop);
            mNotify = false;
            startScaleUpAnimation(mRefreshListener);
        } else {
            setRefreshing(refreshing, false);
        }
    }

    private void startScaleUpAnimation(Animation.AnimationListener listener) {
        mCircleView.setVisibility(View.VISIBLE);
        mProgress.setAlpha(255);
        mScaleAnimation = new Animation() {
            public void applyTransformation(float interpolatedTime, Transformation t) {
                setAnimationProgress(interpolatedTime);
            }
        };
        mScaleAnimation.setDuration((long) mMediumAnimationDuration);
        if (listener != null) {
            mCircleView.setAnimationListener(listener);
        }

        mCircleView.clearAnimation();
        mCircleView.startAnimation(mScaleAnimation);
    }

    void setAnimationProgress(float progress) {
        mCircleView.setScaleX(progress);
        mCircleView.setScaleY(progress);
    }

    private void setRefreshing(boolean refreshing, boolean notify) {
        if (mRefreshing != refreshing) {
            mNotify = notify;
            ensureTarget();
            mRefreshing = refreshing;
            if (mRefreshing) {
                animateOffsetToCorrectPosition(mCurrentTargetOffsetTop, mRefreshListener);
            } else {
                startScaleDownAnimation(mRefreshListener);
            }
        }

    }

    void startScaleDownAnimation(Animation.AnimationListener listener) {
        mScaleDownAnimation = new Animation() {
            public void applyTransformation(float interpolatedTime, Transformation t) {
                setAnimationProgress(1.0F - interpolatedTime);
            }
        };
        mScaleDownAnimation.setDuration(150L);
        mCircleView.setAnimationListener(listener);
        mCircleView.clearAnimation();
        mCircleView.startAnimation(mScaleDownAnimation);
    }

    private void startProgressAlphaStartAnimation() {
        mAlphaStartAnimation = startAlphaAnimation(mProgress.getAlpha(), 76);
    }

    private void startProgressAlphaMaxAnimation() {
        mAlphaMaxAnimation = startAlphaAnimation(mProgress.getAlpha(), 255);
    }

    private Animation startAlphaAnimation(final int startingAlpha, final int endingAlpha) {
        Animation alpha = new Animation() {
            public void applyTransformation(float interpolatedTime, Transformation t) {
                mProgress.setAlpha((int) ((float) startingAlpha + (float) (endingAlpha - startingAlpha) * interpolatedTime));
            }
        };
        alpha.setDuration(300L);
        mCircleView.setAnimationListener(null);
        mCircleView.clearAnimation();
        mCircleView.startAnimation(alpha);
        return alpha;
    }

    public boolean isRefreshing() {
        return mRefreshing;
    }

    private void ensureTarget() {
        if (mTarget == null) {
            for (int i = 0; i < getChildCount(); ++i) {
                View child = getChildAt(i);
                if (!child.equals(mCircleView)) {
                    mTarget = child;
                    break;
                }
            }
        }
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        int width = getMeasuredWidth();
        int height = getMeasuredHeight();
        if (getChildCount() != 0) {
            if (mTarget == null) {
                ensureTarget();
            }

            if (mTarget != null) {
                View child = mTarget;
                int childLeft = getPaddingLeft();
                int childTop = getPaddingTop();
                int childWidth = width - getPaddingLeft() - getPaddingRight();
                int childHeight = height - getPaddingTop() - getPaddingBottom();
                child.layout(childLeft, childTop, childLeft + childWidth, childTop + childHeight);
                int circleWidth = mCircleView.getMeasuredWidth();
                int circleHeight = mCircleView.getMeasuredHeight();
                mCircleView.layout(width / 2 - circleWidth / 2, mCurrentTargetOffsetTop, width / 2 + circleWidth / 2, mCurrentTargetOffsetTop + circleHeight);
            }
        }
    }

    @Override
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        if (mTarget == null) {
            ensureTarget();
        }
        if (mTarget != null) {
            mTarget.measure(MeasureSpec.makeMeasureSpec(getMeasuredWidth() - getPaddingLeft() - getPaddingRight(), MeasureSpec.EXACTLY), MeasureSpec.makeMeasureSpec(getMeasuredHeight() - getPaddingTop() - getPaddingBottom(), MeasureSpec.EXACTLY));
            mCircleView.measure(MeasureSpec.makeMeasureSpec(mCircleDiameter, MeasureSpec.EXACTLY), MeasureSpec.makeMeasureSpec(mCircleDiameter, MeasureSpec.EXACTLY));
            mCircleViewIndex = -1;
            for (int index = 0; index < getChildCount(); ++index) {
                if (getChildAt(index) == mCircleView) {
                    mCircleViewIndex = index;
                    break;
                }
            }
        }
    }

    public boolean canChildScrollUp() {
        if (mChildScrollUpCallback != null) {
            return mChildScrollUpCallback.canChildScrollUp(this, mTarget);
        } else {
            return mTarget instanceof ListView ? ListViewCompat.canScrollList((ListView) mTarget, -1) : mTarget.canScrollVertically(-1);
        }
    }

    public void setOnChildScrollUpCallback(@Nullable IRefreshLayout.OnChildScrollUpCallback callback) {
        mChildScrollUpCallback = callback;
    }

    public boolean onInterceptTouchEvent(MotionEvent ev) {
        ensureTarget();
        int action = ev.getActionMasked();
        if (mReturningToStart && action == 0) {
            mReturningToStart = false;
        }

        if (isEnabled() && !mReturningToStart && !canChildScrollUp() && !mRefreshing && !mNestedScrollInProgress) {
            int pointerIndex;
            switch (action) {
                case 0:
                    setTargetOffsetTopAndBottom(mOriginalOffsetTop - mCircleView.getTop());
                    mActivePointerId = ev.getPointerId(0);
                    mIsBeingDragged = false;
                    pointerIndex = ev.findPointerIndex(mActivePointerId);
                    if (pointerIndex < 0) {
                        return false;
                    }
                    mInitialDownY = ev.getY(pointerIndex);
                    break;
                case 1:
                case 3:
                    mIsBeingDragged = false;
                    mActivePointerId = -1;
                    break;
                case 2:
                    if (mActivePointerId == -1) {
                        return false;
                    }
                    pointerIndex = ev.findPointerIndex(mActivePointerId);
                    if (pointerIndex < 0) {
                        return false;
                    }
                    float y = ev.getY(pointerIndex);
                    startDragging(y);
                case 4:
                case 5:
                default:
                    break;
                case 6:
                    onSecondaryPointerUp(ev);
            }

            return mIsBeingDragged;
        } else {
            return false;
        }
    }

    public void requestDisallowInterceptTouchEvent(boolean b) {
        if ((Build.VERSION.SDK_INT >= 21 || !(mTarget instanceof AbsListView)) && (mTarget == null || ViewCompat.isNestedScrollingEnabled(mTarget))) {
            super.requestDisallowInterceptTouchEvent(b);
        }
    }

    public boolean onStartNestedScroll(View child, View target, int nestedScrollAxes) {
        return isEnabled() && !mReturningToStart && !mRefreshing && (nestedScrollAxes & 2) != 0;
    }

    public void onNestedScrollAccepted(@NonNull View child, @NonNull View target, int axes) {
        mNestedScrollingParentHelper.onNestedScrollAccepted(child, target, axes);
        startNestedScroll(axes & 2);
        mTotalUnconsumed = 0.0F;
        mNestedScrollInProgress = true;
    }

    public void onNestedPreScroll(@NonNull View target, int dx, int dy, @NonNull int[] consumed) {
        if (dy > 0 && mTotalUnconsumed > 0.0F) {
            if ((float) dy > mTotalUnconsumed) {
                consumed[1] = dy - (int) mTotalUnconsumed;
                mTotalUnconsumed = 0.0F;
            } else {
                mTotalUnconsumed -= (float) dy;
                consumed[1] = dy;
            }
            moveSpinner(mTotalUnconsumed);
        }

        int[] parentConsumed = mParentScrollConsumed;
        if (dispatchNestedPreScroll(dx - consumed[0], dy - consumed[1], parentConsumed, (int[]) null)) {
            consumed[0] += parentConsumed[0];
            consumed[1] += parentConsumed[1];
        }
    }

    public int getNestedScrollAxes() {
        return mNestedScrollingParentHelper.getNestedScrollAxes();
    }

    public void onStopNestedScroll(@NonNull View target) {
        mNestedScrollingParentHelper.onStopNestedScroll(target);
        mNestedScrollInProgress = false;
        if (mTotalUnconsumed > 0.0F) {
            finishSpinner(mTotalUnconsumed);
            mTotalUnconsumed = 0.0F;
        }
        stopNestedScroll();
    }

    public void onNestedScroll(@NonNull View target, int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed) {
        dispatchNestedScroll(dxConsumed, dyConsumed, dxUnconsumed, dyUnconsumed, mParentOffsetInWindow);
        int dy = dyUnconsumed + mParentOffsetInWindow[1];
        if (dy < 0 && !canChildScrollUp()) {
            mTotalUnconsumed += (float) Math.abs(dy);
            moveSpinner(mTotalUnconsumed);
        }

    }

    public void setNestedScrollingEnabled(boolean enabled) {
        mNestedScrollingChildHelper.setNestedScrollingEnabled(enabled);
    }

    public boolean isNestedScrollingEnabled() {
        return mNestedScrollingChildHelper.isNestedScrollingEnabled();
    }

    public boolean startNestedScroll(int axes) {
        return mNestedScrollingChildHelper.startNestedScroll(axes);
    }

    public void stopNestedScroll() {
        mNestedScrollingChildHelper.stopNestedScroll();
    }

    public boolean hasNestedScrollingParent() {
        return mNestedScrollingChildHelper.hasNestedScrollingParent();
    }

    public boolean dispatchNestedScroll(int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed, int[] offsetInWindow) {
        return mNestedScrollingChildHelper.dispatchNestedScroll(dxConsumed, dyConsumed, dxUnconsumed, dyUnconsumed, offsetInWindow);
    }

    public boolean dispatchNestedPreScroll(int dx, int dy, int[] consumed, int[] offsetInWindow) {
        return mNestedScrollingChildHelper.dispatchNestedPreScroll(dx, dy, consumed, offsetInWindow);
    }

    public boolean onNestedPreFling(@NonNull View target, float velocityX, float velocityY) {
        return dispatchNestedPreFling(velocityX, velocityY);
    }

    public boolean onNestedFling(@NonNull View target, float velocityX, float velocityY, boolean consumed) {
        return dispatchNestedFling(velocityX, velocityY, consumed);
    }

    public boolean dispatchNestedFling(float velocityX, float velocityY, boolean consumed) {
        return mNestedScrollingChildHelper.dispatchNestedFling(velocityX, velocityY, consumed);
    }

    public boolean dispatchNestedPreFling(float velocityX, float velocityY) {
        return mNestedScrollingChildHelper.dispatchNestedPreFling(velocityX, velocityY);
    }

    private boolean isAnimationRunning(Animation animation) {
        return animation != null && animation.hasStarted() && !animation.hasEnded();
    }

    private void moveSpinner(float oversScrollTop) {
        mProgress.setArrowEnabled(true);
        float originalDragPercent = oversScrollTop / mTotalDragDistance;
        float dragPercent = Math.min(1.0F, Math.abs(originalDragPercent));
        float adjustedPercent = (float) Math.max((double) dragPercent - 0.4D, 0.0D) * 5.0F / 3.0F;
        float extraOS = Math.abs(oversScrollTop) - mTotalDragDistance;
        float slingshotDist = mSpinnerOffsetEnd;
        float tensionSlingshotPercent = Math.max(0.0F, Math.min(extraOS, slingshotDist * 2.0F) / slingshotDist);
        float tensionPercent = (float) ((double) (tensionSlingshotPercent / 4.0F) - Math.pow((double) (tensionSlingshotPercent / 4.0F), 2.0D)) * 2.0F;
        float extraMove = slingshotDist * tensionPercent * 2.0F;
        int targetY = mOriginalOffsetTop + (int) (slingshotDist * dragPercent + extraMove);
        if (mCircleView.getVisibility() != View.VISIBLE) {
            mCircleView.setVisibility(View.VISIBLE);
        }
        mCircleView.setScaleX(1.0F);
        mCircleView.setScaleY(1.0F);

        if (oversScrollTop < mTotalDragDistance) {
            if (mProgress.getAlpha() > 76 && !isAnimationRunning(mAlphaStartAnimation)) {
                startProgressAlphaStartAnimation();
            }
        } else if (mProgress.getAlpha() < 255 && !isAnimationRunning(mAlphaMaxAnimation)) {
            startProgressAlphaMaxAnimation();
        }

        float strokeStart = adjustedPercent * 0.8F;
        mProgress.setStartEndTrim(Math.min(0.8F, strokeStart));
        mProgress.setArrowScale(Math.min(1.0F, adjustedPercent));
        float rotation = (-0.25F + 0.4F * adjustedPercent + tensionPercent * 2.0F) * 0.5F;
        mProgress.setProgressRotation(rotation);
        setTargetOffsetTopAndBottom(targetY - mCurrentTargetOffsetTop);
    }

    private void finishSpinner(float overScrollTop) {
        if (overScrollTop > mTotalDragDistance) {
            setRefreshing(true, true);
        } else {
            mRefreshing = false;
            mProgress.setStartEndTrim(0.0F);
            Animation.AnimationListener listener = new Animation.AnimationListener() {
                public void onAnimationStart(Animation animation) {
                }

                public void onAnimationEnd(Animation animation) {
                    startScaleDownAnimation(null);
                }

                public void onAnimationRepeat(Animation animation) {
                }
            };

            animateOffsetToStartPosition(mCurrentTargetOffsetTop, listener);
            mProgress.setArrowEnabled(false);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        int action = ev.getActionMasked();
        if (mReturningToStart && action == 0) {
            mReturningToStart = false;
        }

        if (isEnabled() && !mReturningToStart && !canChildScrollUp() && !mRefreshing && !mNestedScrollInProgress) {
            float y;
            float overscrollTop;
            int pointerIndex;
            switch (action) {
                case 0:
                    mActivePointerId = ev.getPointerId(0);
                    mIsBeingDragged = false;
                    break;
                case 1:
                    pointerIndex = ev.findPointerIndex(mActivePointerId);
                    if (pointerIndex < 0) {
                        return false;
                    }

                    if (mIsBeingDragged) {
                        y = ev.getY(pointerIndex);
                        overscrollTop = (y - mInitialMotionY) * 0.5F;
                        mIsBeingDragged = false;
                        finishSpinner(overscrollTop);
                    }

                    mActivePointerId = -1;
                    return false;
                case 2:
                    pointerIndex = ev.findPointerIndex(mActivePointerId);
                    if (pointerIndex < 0) {
                        return false;
                    }

                    y = ev.getY(pointerIndex);
                    startDragging(y);
                    if (mIsBeingDragged) {
                        overscrollTop = (y - mInitialMotionY) * 0.5F;
                        if (overscrollTop <= 0.0F) {
                            return false;
                        }
                        moveSpinner(overscrollTop);
                    }
                    break;
                case 3:
                    return false;
                case 4:
                default:
                    break;
                case 5:
                    pointerIndex = ev.getActionIndex();
                    if (pointerIndex < 0) {
                        return false;
                    }
                    mActivePointerId = ev.getPointerId(pointerIndex);
                    break;
                case 6:
                    onSecondaryPointerUp(ev);
            }
            return true;
        } else {
            return false;
        }
    }

    private void startDragging(float y) {
        float yDiff = y - mInitialDownY;
        if (yDiff > (float) mTouchSlop && !mIsBeingDragged) {
            mInitialMotionY = mInitialDownY + (float) mTouchSlop;
            mIsBeingDragged = true;
            mProgress.setAlpha(76);
        }
    }

    private void animateOffsetToCorrectPosition(int from, Animation.AnimationListener listener) {
        mFrom = from;
        mAnimateToCorrectPosition.reset();
        mAnimateToCorrectPosition.setDuration(200L);
        mAnimateToCorrectPosition.setInterpolator(mDecelerateInterpolator);
        if (listener != null) {
            mCircleView.setAnimationListener(listener);
        }

        mCircleView.clearAnimation();
        mCircleView.startAnimation(mAnimateToCorrectPosition);
    }

    private void animateOffsetToStartPosition(int from, Animation.AnimationListener listener) {
        mFrom = from;
        mAnimateToStartPosition.reset();
        mAnimateToStartPosition.setDuration(200L);
        mAnimateToStartPosition.setInterpolator(mDecelerateInterpolator);
        if (listener != null) {
            mCircleView.setAnimationListener(listener);
        }
        mCircleView.clearAnimation();
        mCircleView.startAnimation(mAnimateToStartPosition);
    }

    void moveToStart(float interpolatedTime) {
        int targetTop = mFrom + (int) ((float) (mOriginalOffsetTop - mFrom) * interpolatedTime);
        int offset = targetTop - mCircleView.getTop();
        setTargetOffsetTopAndBottom(offset);
    }

    private void startScaleDownReturnToStartAnimation(int from, Animation.AnimationListener listener) {
        mFrom = from;
        mStartingScale = mCircleView.getScaleX();
        mScaleDownToStartAnimation = new Animation() {
            public void applyTransformation(float interpolatedTime, Transformation t) {
                float targetScale = mStartingScale + -mStartingScale * interpolatedTime;
                setAnimationProgress(targetScale);
                moveToStart(interpolatedTime);
            }
        };
        mScaleDownToStartAnimation.setDuration(150L);
        if (listener != null) {
            mCircleView.setAnimationListener(listener);
        }

        mCircleView.clearAnimation();
        mCircleView.startAnimation(mScaleDownToStartAnimation);
    }

    void setTargetOffsetTopAndBottom(int offset) {
        mCircleView.bringToFront();
        ViewCompat.offsetTopAndBottom(mCircleView, offset);
        mCurrentTargetOffsetTop = mCircleView.getTop();
    }

    private void onSecondaryPointerUp(MotionEvent ev) {
        int pointerIndex = ev.getActionIndex();
        int pointerId = ev.getPointerId(pointerIndex);
        if (pointerId == mActivePointerId) {
            int newPointerIndex = pointerIndex == 0 ? 1 : 0;
            mActivePointerId = ev.getPointerId(newPointerIndex);
        }

    }

    public interface OnChildScrollUpCallback {
        boolean canChildScrollUp(@NonNull IRefreshLayout var1, @Nullable View var2);
    }

    public interface OnRefreshListener {
        void onRefresh();
    }

    private class ICircleImageView extends android.support.v7.widget.AppCompatImageView {

        private Animation.AnimationListener mListener;

        ICircleImageView(Context context) {
            super(context);
            float density = getContext().getResources().getDisplayMetrics().density;
            ShapeDrawable circle = new ShapeDrawable(new OvalShape());
            ViewCompat.setElevation(this, 4.0F * density);
            circle.getPaint().setColor(Color.WHITE);
            ViewCompat.setBackground(this, circle);
        }

        public void setAnimationListener(Animation.AnimationListener listener) {
            mListener = listener;
        }

        public void onAnimationEnd() {
            super.onAnimationEnd();
            if (mListener != null) {
                mListener.onAnimationEnd(getAnimation());
            }
        }
    }
}
