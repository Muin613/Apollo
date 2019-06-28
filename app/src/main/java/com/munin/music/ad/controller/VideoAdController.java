package com.munin.music.ad.controller;

import android.content.Context;

import com.munin.library.common.WeakHandler;
import com.munin.music.ad.controller.base.BaseAdController;
/**
 * @author M
 * TODO
 */
public class VideoAdController extends BaseAdController {
    public VideoAdController(Context context) {
        super(context);
    }

    @Override
    public Runnable getShowRunnable() {
        return null;
    }

    @Override
    public int getDefaultContinueTime() {
        return 0;
    }

    @Override
    public WeakHandler getWeakHandler() {
        return null;
    }

    @Override
    public void destroy() {

    }
}
