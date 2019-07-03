package com.munin.library.view.widget.refreshlayout;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.Context;
import android.os.Looper;
import android.os.MessageQueue;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.animation.Interpolator;
import android.widget.FrameLayout;
import android.widget.Scroller;

import androidx.annotation.Nullable;
import androidx.core.view.NestedScrollingChild;
import androidx.core.view.NestedScrollingChildHelper;
import androidx.core.view.NestedScrollingParent;
import androidx.core.view.NestedScrollingParentHelper;

import com.munin.library.log.Logger;
import com.munin.library.view.widget.refreshlayout.interfaces.IRefreshContent;
import com.munin.library.view.widget.refreshlayout.interfaces.IRefreshFooter;
import com.munin.library.view.widget.refreshlayout.interfaces.IRefreshHeader;
import com.munin.library.view.widget.refreshlayout.interfaces.IRefreshLoadLayoutManager;
import com.munin.library.view.widget.refreshlayout.interfaces.RefreshLayout;
import com.munin.library.view.widget.refreshlayout.state.RefreshState;

public class XRefreshLayout extends FrameLayout implements RefreshLayout, NestedScrollingParent, NestedScrollingChild {
    private static final String TAG = "XRefreshLayout";
    IRefreshLoadLayoutManager mManager;
    protected Scroller mScroller;
    protected VelocityTracker mVelocityTracker;
    private int mMaximumFlingVelocity = ViewConfiguration.getMaximumFlingVelocity();
    private int mMinimumFlingVelocity = ViewConfiguration.getMinimumFlingVelocity();
    protected Interpolator mReboundInterpolator;
    protected NestedScrollingChildHelper mNestedChild = new NestedScrollingChildHelper(this);
    protected NestedScrollingParentHelper mNestedParent = new NestedScrollingParentHelper(this);
    private IRefreshContent mRefreshContent;
    private IRefreshHeader mHeader;
    private IRefreshFooter mFooter;
    private int mHeaderHeight = -1;
    private int mFooterHeight = -1;
    private boolean mViewInit = false;
    private float mChangeY = 0;
    private RefreshState mCurrentState = RefreshState.NONE, mLastState = RefreshState.NONE;
    protected ValueAnimator mReboundAnimator;

    public XRefreshLayout(Context context) {
        this(context, null);
    }

    public XRefreshLayout(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        mVelocityTracker = VelocityTracker.obtain();
        mReboundInterpolator = new ViscousFluidInterpolator();
    }


    @Override
    public IRefreshLoadLayoutManager getRefreshLoadLayoutManager() {
        return mManager;
    }

    @Override
    public void setRefreshLoadLayoutManger(IRefreshLoadLayoutManager manger) {
        mManager = manger;
        addHeader();
        addFooter();
        initViewTranslationY();
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        Logger.i(TAG, "onFinishInflate");
        final int count = super.getChildCount();
        if (count != 1) {
            throw new RuntimeException("只支持1个子View!!!!");
        }
        mRefreshContent = new RefreshContentWrapper(super.getChildAt(0));
    }

    private void addHeader() {
        if (mHeader != null) {
            return;
        }
        mHeader = getHeader();
        if (mHeader == null) {
            return;
        }
        View view = mHeader.getView();
        addView(view);
    }

    private void initViewTranslationY() {
        if (mHeader == null) {
            return;
        }
        Looper.myQueue().addIdleHandler(new MessageQueue.IdleHandler() {
            @Override
            public boolean queueIdle() {
                Logger.i(TAG, "addHeader: view height = " + mHeader.getViewHeight());
                mHeaderHeight = mHeader.getViewHeight();
                mViewInit = true;
                mHeader.getView().setTranslationY(-mHeader.getViewHeight());
                return false;
            }
        });
    }

    private void addFooter() {
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
    }

    private float mMoveY = -1;
    private int mAction = MotionEvent.ACTION_DOWN;

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        boolean consume = false;
        int action = ev.getAction();
        if (!mViewInit) {
            //初始化没成功时，记录动作
            mAction = ev.getAction();
            return true;
        }

        if (action == MotionEvent.ACTION_DOWN && mViewInit) {
            //初始化成功，且第一个动作是按压
            mAction = action;
        }
        if (mAction != MotionEvent.ACTION_DOWN) {
            return true;
        }

        switch (action) {
            case MotionEvent.ACTION_DOWN:
                if (mCurrentState == RefreshState.NONE) {
                    mVelocityTracker.addMovement(ev);
                    mMoveY = ev.getY();
                    consume = true;
                }
                break;
            case MotionEvent.ACTION_MOVE:
                consume = handleMove(ev);
                break;
            case MotionEvent.ACTION_UP:
                consume = handleUp(ev);
                int velocity = (int) mVelocityTracker.getYVelocity();
                break;
            case MotionEvent.ACTION_CANCEL:
                mVelocityTracker.clear();//清空速度追踪器
                break;
            default:
                break;
        }
        return consume || super.dispatchTouchEvent(ev);
    }

    boolean handleMove(MotionEvent e) {
        float currentY = e.getY();
        float tempY = currentY - mMoveY;
        mMoveY = currentY;
        if (mCurrentState == RefreshState.NONE) {
            if (tempY > 0) {
                notifyStateChange(RefreshState.PULL_DOWN_TO_REFRESH);
            }
        }
        if (mCurrentState == RefreshState.PULL_DOWN_TO_REFRESH) {
            if (mHeader != null) {
                mChangeY = mChangeY + tempY;
                Logger.i(TAG,"move bian ?"+mChangeY);
                float translationY =0;
                if (mChangeY < mHeaderHeight * mHeader.getPullMaxRate()) {
                    translationY=mChangeY-mHeaderHeight;
                } else {
                    mChangeY= mHeaderHeight * mHeader.getPullMaxRate();
                    translationY=mChangeY-mHeaderHeight;
                }
                Logger.i(TAG,"move "+mChangeY);
                mRefreshContent.move(mChangeY);
                if (translationY <= 0) {
                    mHeader.getView().setTranslationY(translationY);
                } else {
                    mHeader.getView().setTranslationY((mChangeY - mHeaderHeight) / 2);
                }
                float percent = (mHeaderHeight + translationY) / mHeaderHeight;
                mHeader.onMoving(false, percent, (int) translationY, mHeaderHeight, (int) (mHeaderHeight * mHeader.getPullMaxRate()));
            }
            //速度追踪
            mVelocityTracker.addMovement(e);
            return true;
        }
        return false;
    }

    boolean handleUp(MotionEvent e) {
        if (mCurrentState == RefreshState.PULL_DOWN_TO_REFRESH) {
            mVelocityTracker.addMovement(e);
            mVelocityTracker.computeCurrentVelocity(1000, mMaximumFlingVelocity);
            if (mHeader != null && (mHeader.getView().getTranslationY() + mHeaderHeight) >= mHeaderHeight * mHeader.getTriggerPullRate()) {
                notifyStateChange(RefreshState.REFRESHING);
                return true;
            } else {
                notifyStateChange(RefreshState.PULL_DOWN_CANCEL);
                animSpinner(0, 0, mReboundInterpolator);
                return true;
            }
        }
        if (mCurrentState == RefreshState.PULL_UP_TO_LOAD) {
            mVelocityTracker.addMovement(e);
            mVelocityTracker.computeCurrentVelocity(1000, mMaximumFlingVelocity);
            if (mFooter != null && (mFooter.getView().getTranslationY() + mFooterHeight) >= mFooterHeight * mFooter.getTriggerPullRate()) {
                notifyStateChange(RefreshState.LOADING);
                return true;
            } else {
                notifyStateChange(RefreshState.PULL_UP_CANCEL);
                return true;
            }
        }
        return false;
    }

    private void notifyStateChange(RefreshState state) {
        mLastState = mCurrentState;
        mCurrentState = state;
        Logger.i(TAG, "notifyStateChange: mCurrentState = " + mCurrentState);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        Logger.i(TAG, "onAttachedToWindow");
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        Logger.i(TAG, "onDetachedFromWindow");
    }


    private boolean canTouchWhenRefreshOrLoad() {
        return mManager != null && mManager.canTouchWhenRefreshOrLoad();
    }

    private int getLoadReboundDuation() {
        return mManager != null ? mManager.getLoadReboundDuration() : 500;
    }

    private int getRefreshReboundDuration() {
        return mManager != null ? mManager.getRefreshReboundDuration() : 500;
    }

    private @Nullable
    IRefreshHeader getHeader() {
        return mManager != null ? mManager.getHeader() : null;
    }

    private @Nullable
    IRefreshFooter getFooter() {
        return mManager != null ? mManager.getFooter() : null;
    }

    /**
     * 执行回弹动画
     */
    protected ValueAnimator animSpinner(float endSpinner, long startDelay, Interpolator interpolator) {
        if (mChangeY != endSpinner) {
            if (mReboundAnimator != null) {
                mReboundAnimator.cancel();
            }
            mReboundAnimator = ValueAnimator.ofFloat(endSpinner, mChangeY);
            mReboundAnimator.setDuration(getRefreshReboundDuration());
            mReboundAnimator.setInterpolator(interpolator);
            mReboundAnimator.addUpdateListener(reboundUpdateListener);
            mReboundAnimator.addListener(mAnimatorListener);
            mReboundAnimator.setStartDelay(startDelay);
            mReboundAnimator.start();
        }
        return mReboundAnimator;
    }

    protected Animator.AnimatorListener mAnimatorListener = new AnimatorListenerAdapter() {
        @Override
        public void onAnimationEnd(Animator animation) {
            mReboundAnimator = null;
            mChangeY = 0;
            notifyStateChange(RefreshState.NONE);
        }
    };

    protected ValueAnimator.AnimatorUpdateListener reboundUpdateListener = new ValueAnimator.AnimatorUpdateListener() {
        @Override
        public void onAnimationUpdate(ValueAnimator animation) {
            moveSpinner((float) animation.getAnimatedValue());
        }
    };

    private void moveSpinner(float moveY) {
        float changeY = mChangeY - moveY;
        float percent = changeY / mHeaderHeight;
        mRefreshContent.move(changeY);
        if (changeY - mHeaderHeight <= 0) {
            mHeader.getView().setTranslationY(changeY - mHeaderHeight);
        } else {
            mHeader.getView().setTranslationY((changeY - mHeaderHeight) / 2);
        }
        mHeader.onReleased(this, percent, mHeaderHeight, (int) (mHeaderHeight * getHeader().getPullMaxRate()));
    }
}
