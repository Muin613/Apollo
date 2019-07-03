package com.munin.library.view.widget.refreshlayout.interfaces;

import android.view.View;

import androidx.annotation.NonNull;

public interface IRefreshContent {
    boolean canRefresh();

    boolean canLoadMore();

    @NonNull
    View getView();

    void move(float translationY);
}
