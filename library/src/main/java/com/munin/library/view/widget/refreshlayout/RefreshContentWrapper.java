package com.munin.library.view.widget.refreshlayout;

import android.view.View;

import androidx.annotation.NonNull;

import com.munin.library.view.widget.refreshlayout.interfaces.IRefreshContent;

public class RefreshContentWrapper implements IRefreshContent {
    private View mContentView;

    public RefreshContentWrapper(View view) {
        this.mContentView = view;
    }

    @Override
    public boolean canRefresh() {
        return false;
    }

    @Override
    public boolean canLoadMore() {
        return false;
    }

    @NonNull
    @Override
    public View getView() {
        return mContentView;
    }

    @Override
    public void move(float translationY) {
        if (this.mContentView == null) {
            return;
        }
        mContentView.setTranslationY(translationY);
    }

}
