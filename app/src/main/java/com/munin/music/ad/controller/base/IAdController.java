package com.munin.music.ad.controller.base;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import com.munin.library.common.WeakHandler;
import com.munin.music.model.ad.AdModel;

public interface IAdController {
    void bind(AdModel model);

    void show(ViewGroup showView);

    void onAdFinish();

    View inflate(Context context, int resId, ViewGroup parent);

    Runnable getShowRunnable();

    void setContinueTime(int time);

    int getDefaultContinueTime();
    WeakHandler getWeakHandler();

    void destroy();
    void setAdListener(IAdListener listener);
}
