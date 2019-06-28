package com.munin.music.ui.player.controller;
import android.animation.ObjectAnimator;
import android.view.View;
import android.view.animation.AccelerateInterpolator;

import androidx.appcompat.widget.AppCompatImageView;

public class MusicStylusController {
    private AppCompatImageView mStylusImgView;
    private static final int ANIM_TIME = 500;
    private ObjectAnimator mAnim;
    private OnAnimListener mListener;
    private boolean mIsDo = true;

    public MusicStylusController(AppCompatImageView stylusImgView) {
        mStylusImgView = stylusImgView;
        initAnim();
    }

    public void setListener(OnAnimListener listener) {
        this.mListener = listener;
    }

    private void initAnim() {
        if (mStylusImgView == null || mAnim != null) {
            return;
        }
        mStylusImgView.setPivotX(0);
        mStylusImgView.setPivotY(0);
        mAnim = ObjectAnimator.ofFloat(mStylusImgView, View.ROTATION, 20, 0);
        mAnim.setDuration(ANIM_TIME);
        mAnim.setInterpolator(new AccelerateInterpolator());
    }

    public void start() {
        if (mIsDo) {
            return;
        }
        if (mAnim == null) {
            return;
        }
        mAnim.start();
        mIsDo = true;
    }

    public void reverse() {
        if (!mIsDo) {
            return;
        }
        if (mAnim == null) {
            return;
        }
        mAnim.reverse();
        mIsDo = false;
    }


    public interface OnAnimListener {
        void onStart();

        void onEnd();
    }
}
