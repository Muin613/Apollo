package com.munin.library.view.widget.refreshlayout.interfaces;


import android.view.View;

import androidx.annotation.NonNull;

import com.munin.library.view.widget.refreshlayout.state.OnStateChangeListener;


/**
 * 刷新内部组件
 */
public interface IRefreshInternal extends OnStateChangeListener {

    @NonNull
    View getView();

    void onMoving(boolean isDragging, float percent, float offset, int height, int maxDragHeight);

    void onReleased(@NonNull RefreshLayout refreshLayout, float percent, int height, int maxDragHeight);

    float getPullMaxRate();

    float getTriggerPullRate();

    int getViewHeight();

    boolean isShow();

    void setIsShow(boolean isShow);
}
