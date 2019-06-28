package com.munin.music.ad.controller.base;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.munin.library.common.WeakHandler;
import com.munin.music.model.ad.AdModel;

/**
 * @author M
 * getWeakHandler如果返回为空则按照定时做任务
 */
public abstract class BaseAdController implements IAdController {
    private final static int TIME_SECOND_SHOW = 1000;
    private final static int AD_SHOW_MSG = 101010;
    protected AdModel mModel;
    protected int mContinueTime = 0;
    protected int mHasLastTime = 0;
    private Runnable mRunnable;
    protected Context mContext;
    protected WeakHandler mHandler;
    IAdListener mListener;

    public BaseAdController(Context context) {
        mContext = context;
    }

    @Override
    public void bind(AdModel model) {
        this.mModel = model;
    }

    @Override
    public void show(ViewGroup showView) {
        mHasLastTime = 0;
        if (getWeakHandler() == null) {
            getHandler().sendEmptyMessageDelayed(AD_SHOW_MSG, TIME_SECOND_SHOW);
            return;
        }
        getHandler().postDelayed(getRunnable(), mContinueTime);
    }

    @Override
    public void onAdFinish() {
        if (getWeakHandler() == null) {
            getHandler().removeMessages(AD_SHOW_MSG);
            return;
        }
        getHandler().removeCallbacks(getRunnable());
    }

    @Override
    public void setAdListener(IAdListener listener) {
        mListener = listener;
    }

    @Override
    public View inflate(Context context, int resId, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(resId, parent);
    }

    @Override
    public void setContinueTime(int time) {
        mContinueTime = time;
    }

    private Runnable getRunnable() {
        Runnable runnable = getShowRunnable();
        if (mRunnable == null) {
            mRunnable = runnable;
        }
        if (mRunnable == null) {
            mRunnable = new Runnable() {
                @Override
                public void run() {

                }
            };
        }
        return mRunnable;
    }


    private WeakHandler getHandler() {
        WeakHandler handler = getWeakHandler();
        if (mHandler == null) {
            mHandler = handler;
        }
        if (mHandler == null) {
            mHandler = new WeakHandler(new Handler.Callback() {
                @Override
                public boolean handleMessage(Message msg) {
                    if (getWeakHandler() == null) {
                        mHasLastTime += TIME_SECOND_SHOW;
                        if (mHasLastTime < mContinueTime) {
                            getRunnable().run();
                            mHandler.sendEmptyMessageDelayed(AD_SHOW_MSG, TIME_SECOND_SHOW);
                        } else {
                            onAdFinish();
                            if (mListener != null) {
                                mListener.onAdFinish();
                            }
                        }
                        return false;
                    }
                    return false;
                }
            });
        }
        return mHandler;
    }
}
