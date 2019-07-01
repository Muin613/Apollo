package com.munin.music.ui.music.controller;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.view.View;
import android.view.animation.LinearInterpolator;

import androidx.appcompat.widget.AppCompatImageView;

import com.munin.library.log.Logger;

public class MusicDiscController {
    private static final String TAG = "MusicDiscController";
    private AppCompatImageView mDiscImageView;
    private ObjectAnimator mAnim;



    private static final int ANIM_TIME = 4000;
    private long mCurrentTime = -1;

    public MusicDiscController() {
    }

    public void setDiscImageView(AppCompatImageView discImageView) {
        this.mDiscImageView = discImageView;
        initAnim();
    }

    private void initAnim() {
        if (mDiscImageView == null) {
            return;
        }
        if (mAnim != null) {
            stop();
        }
        mAnim = ObjectAnimator.ofFloat(mDiscImageView, View.ROTATION, 0, 360f);
        mAnim.setDuration(ANIM_TIME);
        mAnim.setRepeatCount(ValueAnimator.INFINITE);
        mAnim.setInterpolator(new LinearInterpolator());
    }

    public void start() {
        if (mAnim == null) {
            return;
        }
        Logger.i(TAG, "start");
        mAnim.start();
        if (mCurrentTime == -1) {
            mCurrentTime = 0;
        }
        mAnim.setCurrentPlayTime(mCurrentTime);
    }

    public void stop() {
        if (mAnim == null) {
            return;
        }
        Logger.i(TAG, "stop");
        mCurrentTime = mAnim.getCurrentPlayTime();
        mAnim.cancel();
    }
}
