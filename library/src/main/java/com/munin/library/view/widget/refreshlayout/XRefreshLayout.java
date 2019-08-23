package com.munin.library.view.widget.refreshlayout;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.Context;
import android.os.Looper;
import android.os.MessageQueue;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.animation.Interpolator;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.view.NestedScrollingChild;
import androidx.core.view.NestedScrollingChildHelper;
import androidx.core.view.NestedScrollingParent;
import androidx.core.view.NestedScrollingParentHelper;
import androidx.core.view.ViewCompat;
import androidx.core.view.ViewConfigurationCompat;

import com.munin.library.log.Logger;
import com.munin.library.view.widget.refreshlayout.interfaces.IRefreshContent;
import com.munin.library.view.widget.refreshlayout.interfaces.IRefreshFooter;
import com.munin.library.view.widget.refreshlayout.interfaces.IRefreshHeader;
import com.munin.library.view.widget.refreshlayout.interfaces.IRefreshLoadLayoutManager;
import com.munin.library.view.widget.refreshlayout.interfaces.OnRefreshLoadMoreListener;
import com.munin.library.view.widget.refreshlayout.interfaces.RefreshLayout;
import com.munin.library.view.widget.refreshlayout.state.RefreshState;
import com.munin.library.view.widget.refreshlayout.utils.XRefreshLayoutUtils;

import static android.view.MotionEvent.obtain;

/**
 * @author M
 */
public class XRefreshLayout extends FrameLayout implements RefreshLayout, NestedScrollingParent, NestedScrollingChild {
    private static final String TAG = "XRefreshLayout";
    IRefreshLoadLayoutManager mManager;
    protected VelocityTracker mVelocityTracker;
    private int mMaximumFlingVelocity = ViewConfiguration.getMaximumFlingVelocity();
    private int mTouchSlop;
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
    protected ValueAnimator mLoadReboundAnimator;
    private OnRefreshLoadMoreListener mListener;
    private float mTempValue = 0;
    private boolean mIsDraging = false;

    public XRefreshLayout(Context context) {
        this(context, null);
    }

    public XRefreshLayout(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        mVelocityTracker = VelocityTracker.obtain();
        mTouchSlop = ViewConfigurationCompat.getScaledPagingTouchSlop(ViewConfiguration.get(context))/10;
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
        Looper.myQueue().addIdleHandler(new MessageQueue.IdleHandler() {
            @Override
            public boolean queueIdle() {
                mViewInit = true;
                if (mHeader != null) {
                    mHeaderHeight = mHeader.getViewHeight();
                    Logger.i(TAG, "initViewTranslationY: view height = " + mHeader.getViewHeight());
                    mHeader.getView().setTranslationY(-mHeaderHeight);
                    mHeader.getView().setAlpha(1);
                }
                if (mFooter != null) {
                    mFooterHeight = mFooter.getViewHeight();
                    Logger.i(TAG, "initViewTranslationY:  footer height = " + mFooter.getViewHeight());
                    mFooter.getView().setTranslationY(mFooterHeight);
                    mFooter.getView().setAlpha(1);
                }
                return false;
            }
        });
    }

    private void addFooter() {
        if (mFooter != null) {
            return;
        }
        mFooter = getFooter();
        if (mFooter == null) {
            return;
        }
        View view = mFooter.getView();
        addView(view);
        FrameLayout.LayoutParams lp = (LayoutParams) view.getLayoutParams();
        lp.gravity = Gravity.BOTTOM;

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
    }

    private float mMoveY = 0, mMoveLastY = 0;
    private int mAction = MotionEvent.ACTION_DOWN;
    private MotionEvent mActionDown = null;
    private boolean mIsMultiPoint = false;

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
        handleMultiPoint(ev);

        switch (action) {
            case MotionEvent.ACTION_DOWN:
                mVelocityTracker.addMovement(ev);
                mMoveY = ev.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                consume = handleMove(ev);
                break;
            case MotionEvent.ACTION_UP:
                consume = handleUp(ev);
                int velocity = (int) mVelocityTracker.getYVelocity();
                mMoveY = -1;
                break;
            case MotionEvent.ACTION_CANCEL:
                mVelocityTracker.clear();//清空速度追踪器
                if (mActionDown != null) {
                    mActionDown.recycle();
                    mActionDown = null;
                }
                break;
            default:
                break;
        }
        return consume || super.dispatchTouchEvent(ev);
    }

    float getTouchY(MotionEvent event) {
        final int actionMasked = event.getActionMasked();
        final boolean pointerUp = actionMasked == MotionEvent.ACTION_POINTER_UP;

        float sumY = 0;
        final int count = event.getPointerCount();
        for (int i = 0; i < count; i++) {
            sumY += event.getY(i);
        }
        final int div = pointerUp ? count - 1 : count;
        return sumY / div;
    }

    void handleMultiPoint(MotionEvent ev) {
        final float touchY = getTouchY(ev);
        if (ev.getPointerCount() >= 2) {
            if (!mIsMultiPoint) {
                mMoveY = touchY;
            }
            mIsMultiPoint = true;
        } else {
            if (mIsMultiPoint) {
                //重多点触控退出时，重置移动位置
                mMoveY = ev.getY();
            }
            mIsMultiPoint = false;
        }
    }

    boolean handleMove(MotionEvent e) {
        float currentY = 0;
        if (mIsMultiPoint) {
            currentY = getTouchY(e);
        } else {
            currentY = e.getY();
        }
        float dy = currentY - mMoveY;
        if (Math.abs(dy) > mTouchSlop) {
            mMoveY = currentY;
        } else {
            dy = 0;
        }
        if (mChangeY >= 0 && dy > 0 && !XRefreshLayoutUtils.canScrollVertical(mRefreshContent.getView(), 1)) {
            mIsDraging = false;
            mRefreshContent.getView().setOnTouchListener(null);
            notifyStateChange(RefreshState.NONE);
            notifyHeaderChange();
            adjustNormal();
            return false;
        }
        if (mChangeY <= 0 && dy < 0 && !XRefreshLayoutUtils.canScrollVertical(mRefreshContent.getView(), -1)) {
            mIsDraging = false;
            mRefreshContent.getView().setOnTouchListener(null);
            notifyStateChange(RefreshState.NONE);
            notifyFooterChange();
            adjustNormal();
            return false;
        }
        if (!mIsDraging) {
            mRefreshContent.getView().setOnTouchListener(new OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    return true;
                }
            });
            mIsDraging = true;
        }
        long time = e.getEventTime();
        if (mIsDraging) {
            if (mActionDown == null) {
                mActionDown = obtain(time, time, MotionEvent.ACTION_DOWN, e.getX(), mMoveY, 0);
                super.dispatchTouchEvent(mActionDown);
            }
            MotionEvent me = obtain(time, time, MotionEvent.ACTION_MOVE, e.getX(), mMoveY + dy, 0);
            super.dispatchTouchEvent(me);
            if (mCurrentState == RefreshState.NONE) {
                if (mChangeY >=0) {
                    notifyStateChange(RefreshState.PULL_DOWN_TO_REFRESH);
                    notifyHeaderChange();
                } else {
                    notifyStateChange(RefreshState.PULL_UP_TO_LOAD);
                    notifyFooterChange();
                }
            } else if (mCurrentState == RefreshState.PULL_UP_TO_LOAD) {
                if (mChangeY > 0) {
                    adjustNormal();
                    notifyStateChange(RefreshState.PULL_DOWN_TO_REFRESH);
                    notifyHeaderChange();
                }
            } else if (mCurrentState == RefreshState.PULL_DOWN_TO_REFRESH) {
                if (mChangeY < 0) {
                    adjustNormal();
                    notifyStateChange(RefreshState.PULL_UP_TO_LOAD);
                    notifyFooterChange();
                }
            }
            mChangeY = mChangeY + dy;
            if (mChangeY >= 0 && mHeader != null && XRefreshLayoutUtils.canScrollVertical(mRefreshContent.getView(), 1)) {
                float translationY = 0;
                if (mChangeY < mHeaderHeight * mHeader.getPullMaxRate()) {
                    translationY = mChangeY - mHeaderHeight;
                } else {
                    mChangeY = mHeaderHeight * mHeader.getPullMaxRate();
                    translationY = mChangeY - mHeaderHeight;
                }
                mRefreshContent.move(mChangeY);
                if (translationY <= 0) {
                    mHeader.getView().setTranslationY(translationY);
                } else {
                    mHeader.getView().setTranslationY((mChangeY - mHeaderHeight) / 2);
                }
                float percent = (mHeaderHeight + translationY) / mHeaderHeight;
                mHeader.onMoving(mIsDraging, percent, (int) translationY, mHeaderHeight, (int) (mHeaderHeight * mHeader.getPullMaxRate()));
                //速度追踪
                mVelocityTracker.addMovement(e);
                return true;
            }
            if (mChangeY < 0 && mFooter != null && XRefreshLayoutUtils.canScrollVertical(mRefreshContent.getView(), -1)) {
                float translationY = 0;
                if (Math.abs(mChangeY) < mFooterHeight * mFooter.getPullMaxRate()) {
                    translationY = mChangeY + mFooterHeight;
                } else {
                    mChangeY = -mFooterHeight * mFooter.getPullMaxRate();
                    translationY = mChangeY + mFooterHeight;
                }
                mRefreshContent.move(mChangeY);
                if (translationY >= 0) {
                    mFooter.getView().setTranslationY(translationY);
                } else {
                    mFooter.getView().setTranslationY((mChangeY + mFooterHeight) / 2);
                }
                float percent = (-mFooterHeight + translationY) / mFooterHeight;
                mFooter.onMoving(mIsDraging, percent, (int) translationY, mFooterHeight, (int) (mFooterHeight * mFooter.getPullMaxRate()));
                //速度追踪
                mVelocityTracker.addMovement(e);
                return true;
            }
        }
        return false;
    }

    private void adjustNormal() {
        if (mHeader != null) {
            mHeader.getView().setTranslationY(-mHeaderHeight);
        }
        mChangeY = 0;
        mRefreshContent.move(0);
        if (mFooter != null) {
            mFooter.getView().setTranslationY(mFooterHeight);
        }
    }

    private void notifyHeaderChange() {
        if (mHeader == null) {
            return;
        }
        mHeader.onStateChanged(this, mLastState, mCurrentState);
    }

    private void notifyFooterChange() {
        if (mFooter == null) {
            return;
        }
        mFooter.onStateChanged(this, mLastState, mCurrentState);
    }

    boolean handleUp(MotionEvent e) {
        if (mCurrentState == RefreshState.PULL_DOWN_TO_REFRESH) {
            mVelocityTracker.addMovement(e);
            mVelocityTracker.computeCurrentVelocity(1000, mMaximumFlingVelocity);
            if (mHeader != null && (mHeader.getView().getTranslationY() + mHeaderHeight) >= mHeaderHeight * mHeader.getTriggerPullRate()) {
                notifyStateChange(RefreshState.REFRESHING);
                notifyHeaderChange();
                if (mListener != null) {
                    mListener.onRefresh(this);
                }
                return true;
            } else {
                notifyStateChange(RefreshState.PULL_DOWN_CANCEL);
                notifyHeaderChange();
                finishRefreshAnim(0, 0, mReboundInterpolator);
                return true;
            }
        }
        if (mCurrentState == RefreshState.PULL_UP_TO_LOAD) {
            mVelocityTracker.addMovement(e);
            mVelocityTracker.computeCurrentVelocity(1000, mMaximumFlingVelocity);
            if (mFooter != null && (Math.abs(mFooter.getView().getTranslationY() - mFooterHeight)) >= mFooterHeight * mFooter.getTriggerPullRate()) {
                notifyStateChange(RefreshState.LOADING);
                notifyFooterChange();
                if (mListener != null) {
                    mListener.onLoadMore(this);
                }
                return true;
            } else {
                notifyStateChange(RefreshState.PULL_UP_CANCEL);
                notifyFooterChange();
                finishLoadAnim(0, 0, mReboundInterpolator);
                return true;
            }
        }
        return false;
    }

    private void notifyStateChange(RefreshState state) {
        if (mCurrentState == state) {
            return;
        }
        mLastState = mCurrentState;
        mCurrentState = state;
        Logger.i(TAG, "notifyStateChange: mCurrentState = " + mCurrentState);
    }

    public void setListener(OnRefreshLoadMoreListener listener) {
        this.mListener = listener;
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


    private int getLoadReboundDuration() {
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
    public void finishRefreshAnim(float endSpinner, long startDelay, Interpolator interpolator) {
        if (mReboundAnimator != null) {
            mReboundAnimator.cancel();
        }
        mTempValue = mChangeY;
        mReboundAnimator = ValueAnimator.ofFloat(endSpinner, mChangeY);
        mReboundAnimator.setDuration(getRefreshReboundDuration());
        mReboundAnimator.setInterpolator(interpolator);
        mReboundAnimator.addUpdateListener(reboundUpdateListener);
        mReboundAnimator.addListener(mAnimatorListener);
        mReboundAnimator.setStartDelay(startDelay);
        mReboundAnimator.start();

    }

    public void finishLoadAnim(float endSpinner, long startDelay, Interpolator interpolator) {
        if (mLoadReboundAnimator != null) {
            mLoadReboundAnimator.cancel();
        }
        mTempValue = mChangeY;
        mLoadReboundAnimator = ValueAnimator.ofFloat(endSpinner, mChangeY);
        mLoadReboundAnimator.setDuration(getLoadReboundDuration());
        mLoadReboundAnimator.setInterpolator(interpolator);
        mLoadReboundAnimator.addUpdateListener(mLoadReboundUpdateListener);
        mLoadReboundAnimator.addListener(mAnimatorListener);
        mLoadReboundAnimator.setStartDelay(startDelay);
        mLoadReboundAnimator.start();
    }

    protected Animator.AnimatorListener mAnimatorListener = new AnimatorListenerAdapter() {
        @Override
        public void onAnimationEnd(Animator animation) {
            mReboundAnimator = null;
            mChangeY = 0;
            mTempValue = 0;
            notifyStateChange(RefreshState.NONE);
        }
    };
    private ValueAnimator.AnimatorUpdateListener mLoadReboundUpdateListener = new ValueAnimator.AnimatorUpdateListener() {
        @Override
        public void onAnimationUpdate(ValueAnimator animation) {
            finishLoad((float) animation.getAnimatedValue());
        }
    };

    protected ValueAnimator.AnimatorUpdateListener reboundUpdateListener = new ValueAnimator.AnimatorUpdateListener() {
        @Override
        public void onAnimationUpdate(ValueAnimator animation) {
            finishRefresh((float) animation.getAnimatedValue());
        }
    };

    private void finishRefresh(float moveY) {
        if (mHeader == null) {
            return;
        }
        float changeY = mTempValue - moveY;
        float percent = changeY / mHeaderHeight;
        mChangeY = changeY;
        mRefreshContent.move(changeY);
        if (changeY - mHeaderHeight <= 0) {
            mHeader.getView().setTranslationY(changeY - mHeaderHeight);
        } else {
            mHeader.getView().setTranslationY((changeY - mHeaderHeight) / 2);
        }
        mHeader.onReleased(this, percent, mHeaderHeight, (int) (mHeaderHeight * mHeader.getPullMaxRate()));
    }

    private void finishLoad(float moveY) {
        if (mFooter == null) {
            return;
        }
        float changeY = mTempValue - moveY;
        float percent = changeY / mFooterHeight;
        mChangeY = changeY;
        mRefreshContent.move(changeY);
        if (changeY + mFooterHeight >= 0) {
            mFooter.getView().setTranslationY(changeY + mFooterHeight);
        } else {
            mFooter.getView().setTranslationY((changeY + mFooterHeight) / 2);
        }
        mFooter.onReleased(this, percent, mFooterHeight, (int) (mFooterHeight * mFooter.getPullMaxRate()));
    }

    public void finishRefresh() {
        if (mReboundAnimator != null) {
            mReboundAnimator.cancel();
        }
        notifyStateChange(RefreshState.REFRESH_FINISHED);
        notifyHeaderChange();
        if (mCurrentState == RefreshState.REFRESHING || mCurrentState == RefreshState.REFRESH_FINISHED) {
            mTempValue = mChangeY;
            mReboundAnimator = ValueAnimator.ofFloat(0, mChangeY);
            mReboundAnimator.setDuration(0);
            mReboundAnimator.setInterpolator(mReboundInterpolator);
            mReboundAnimator.addUpdateListener(reboundUpdateListener);
            mReboundAnimator.addListener(mAnimatorListener);
            mReboundAnimator.setStartDelay(0);
            mReboundAnimator.start();
        }
    }

    public void finishLoad() {
        if (mLoadReboundAnimator != null) {
            mLoadReboundAnimator.cancel();
        }
        notifyStateChange(RefreshState.LOAD_FINISHED);
        notifyFooterChange();
        if (mCurrentState == RefreshState.LOADING || mCurrentState == RefreshState.LOAD_FINISHED) {
            mTempValue = mChangeY;
            mLoadReboundAnimator = ValueAnimator.ofFloat(0, mChangeY);
            mLoadReboundAnimator.setDuration(0);
            mLoadReboundAnimator.setInterpolator(mReboundInterpolator);
            mLoadReboundAnimator.addUpdateListener(mLoadReboundUpdateListener);
            mLoadReboundAnimator.addListener(mAnimatorListener);
            mLoadReboundAnimator.setStartDelay(0);
            mLoadReboundAnimator.start();
        }
    }

    private boolean mNestedInProgress = false;

    @Override
    public void onNestedScrollAccepted(View child, View target, int axes) {
        mNestedParent.onNestedScrollAccepted(child, target, axes);
        mNestedChild.startNestedScroll(axes & ViewCompat.SCROLL_AXIS_VERTICAL);
        mNestedInProgress = true;
    }

    @Override
    public void onStopNestedScroll(@NonNull View target) {
        mNestedParent.onStopNestedScroll(target);
        mNestedInProgress = false;
        // Finish the spinner for nested scrolling if we ever consumed any
        // unconsumed nested scroll
        // Dispatch up our nested parent
        mNestedChild.stopNestedScroll();
    }

    public RefreshState getCurrentState() {
        return mCurrentState;
    }
}
