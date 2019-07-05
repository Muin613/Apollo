package com.munin.music.ui.common;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.animation.Interpolator;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.munin.library.log.Logger;
import com.munin.library.view.widget.refreshlayout.interfaces.IRefreshFooter;
import com.munin.library.view.widget.refreshlayout.interfaces.RefreshLayout;
import com.munin.library.view.widget.refreshlayout.state.RefreshState;
import com.munin.music.R;

/**
 * @author M
 */
public class LoadFooterView extends FrameLayout implements IRefreshFooter {
    private static final String TAG = "LoadFooter";
    private boolean mIsShow = false;

    public LoadFooterView(@NonNull Context context) {
        this(context, null);
    }

    public LoadFooterView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        setBackgroundResource(R.color.black);
        TextView view = new TextView(context);
        view.setTextColor(Color.GRAY);
        view.setGravity(Gravity.TOP | Gravity.CENTER);
        view.setText("DI   BU");
        setAlpha(0);
        addView(view);
    }

    @NonNull
    @Override
    public View getView() {
        return this;
    }


    @Override
    public void onMoving(boolean isDragging, float percent, float offset, int height, int maxDragHeight) {
        if (percent < -1.001) {
            setScaleX(Math.abs(percent));
            setScaleY(Math.abs(percent));
        } else {
            setScaleY(1);
            setScaleX(1);
        }
        if (Math.abs(percent) < 0.099) {
            mIsShow = false;
        } else {
            mIsShow = true;
        }
        Logger.i(TAG, "onMoving: isDragging = " + isDragging + " percent = " + percent + " offset = " + offset + " height = " + height + " maxDragHeight = " + maxDragHeight);
    }


    @Override
    public void onReleased(@NonNull RefreshLayout refreshLayout, float percent, int height, int maxDragHeight) {
        Logger.i(TAG, "onReleased: height = " + height + " percent = " + percent + " maxDragHeight = " + maxDragHeight);
        if (percent < -1.001) {
            setScaleX(Math.abs(percent));
            setScaleY(Math.abs(percent));
        } else {
            setScaleY(1);
            setScaleX(1);
        }
        if (Math.abs(percent) < 0.099) {
            mIsShow = false;
        } else {
            mIsShow = true;
        }
    }

    @Override
    public float getPullMaxRate() {
        return 3;
    }

    @Override
    public float getTriggerPullRate() {
        return 0.8f;
    }

    @Override
    public int getViewHeight() {
        return getMeasuredHeight();
    }

    @Override
    public boolean isShow() {
        return mIsShow;
    }

    @Override
    public void setIsShow(boolean isShow) {
        mIsShow=isShow;
    }

    @Override
    public void onStateChanged(@NonNull RefreshLayout refreshLayout, @NonNull RefreshState oldState, @NonNull RefreshState newState) {
        Logger.i(TAG, "onStateChanged: oldState = " + oldState + " newState = " + newState);
    }

    @Override
    public void setScaleX(float scaleX) {
        super.setScaleX(scaleX);
        Logger.i(TAG, "setScaleX " + scaleX);
    }
}
