package com.munin.library.view.widget.refreshlayout.state;

/**
 * @author M
 */

public enum RefreshState {
    /**
     * 初始状态
     */
    NONE,
    /**
     * 下拉准备刷新
     */
    PULL_DOWN_TO_REFRESH,
    /**
     * 下拉刷新取消
     */
    PULL_DOWN_CANCEL,
    /**
     * 刷新中
     */
    REFRESHING,
    /**
     * 刷新结束
     */
    REFRESH_FINISHED,
    /**
     * 上拉准备加载
     */
    PULL_UP_TO_LOAD,
    /**
     * 上拉取消
     */
    PULL_UP_CANCEL,
    /**
     * 加载中
     */
    LOADING,
    /**
     * 加载结束
     */
    LOAD_FINISHED
}
