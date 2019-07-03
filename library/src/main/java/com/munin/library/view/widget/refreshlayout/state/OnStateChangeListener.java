package com.munin.library.view.widget.refreshlayout.state;
import androidx.annotation.NonNull;

import com.munin.library.view.widget.refreshlayout.interfaces.RefreshLayout;

/**
 * @author M
 */
public interface OnStateChangeListener {
    /**
     * 【仅限框架内调用】状态改变事件 {@link RefreshState}
     * @param refreshLayout RefreshLayout
     * @param oldState 改变之前的状态
     * @param newState 改变之后的状态
     */
    void onStateChanged(@NonNull RefreshLayout refreshLayout, @NonNull RefreshState oldState, @NonNull RefreshState newState);
}
