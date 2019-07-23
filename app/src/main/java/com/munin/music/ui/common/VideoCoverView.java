package com.munin.music.ui.common;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.SeekBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;

import com.munin.library.image.ImageLoadUtils;
import com.munin.library.log.Logger;
import com.munin.library.utils.ViewUtils;
import com.munin.music.R;

public class VideoCoverView extends FrameLayout {
    private static final String TAG = "VideoCoverView";
    private AppCompatTextView mNoticeView, mCurrentTimeView, mTotalTimeView, mTitleView;
    public View mPlayOrPauseView, mCoverView, mScreenControlView, mBottomControlView;
    private AppCompatImageView mThumbImageView;
    private SeekBar mSeekBar;
    private boolean mIsShowNotice = false;

    public VideoCoverView(@NonNull Context context) {
        this(context, null);
    }

    public VideoCoverView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        inflate(getContext(), R.layout.layout_video_player, this);
        mThumbImageView = findViewById(R.id.video_cover_img);
        mCoverView = findViewById(R.id.video_cover_view);
        mBottomControlView = findViewById(R.id.video_control_view);
        mPlayOrPauseView = findViewById(R.id.video_play_control);
        mTitleView = findViewById(R.id.video_title);
        mScreenControlView = findViewById(R.id.video_screen_control);
        mTotalTimeView = findViewById(R.id.video_total_time);
        mCurrentTimeView = findViewById(R.id.video_current_time);
        mNoticeView = findViewById(R.id.video_notice);
        mSeekBar = findViewById(R.id.video_control_seek);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();

        ViewUtils.setViewVisible(mCoverView, View.VISIBLE);
    }


    public void setTitle(String title) {
        ViewUtils.setText(mTitleView, title);
    }

    public void setNotice(String notice) {
        changeCoverShow(false);
        mIsShowNotice = true;
        ViewUtils.setViewVisible(mNoticeView, View.VISIBLE);
        ViewUtils.setText(mNoticeView, notice);
    }

    public void hideNotice() {
        changeCoverShow(false);
        mIsShowNotice = false;
        ViewUtils.setText(mNoticeView, "");
        ViewUtils.setViewVisible(mNoticeView, View.GONE);
    }

    public void changeCurrentTime(String currentTime) {
        ViewUtils.setText(mCurrentTimeView, currentTime);
    }

    public void changeTotalTime(String totalTime) {
        ViewUtils.setText(mTotalTimeView, totalTime);
    }

    public void loadThumbImage(String url) {
        ViewUtils.setViewVisible(mThumbImageView, View.VISIBLE);
        ImageLoadUtils.loadImg(getContext(), url, mThumbImageView);
    }

    public void showBottomControl(boolean isShow) {
        if (isShow) {
            ViewUtils.setViewVisible(mBottomControlView, View.VISIBLE);
            return;
        }
        ViewUtils.setViewVisible(mBottomControlView, View.GONE);
    }

    public void changeSeekBar(int percent) {
        mSeekBar.setProgress(percent);
    }

    public void changeCoverShow(boolean isShow) {
        if (isShow) {
            ViewUtils.setViewVisible(mCoverView, View.VISIBLE);
            return;
        }
        ViewUtils.setViewVisible(mCoverView, View.GONE);
    }

    public boolean isShowCover() {
        return ViewUtils.isVisible(mCoverView);
    }

    public void showCoverControl(boolean isShow) {
        if (mIsShowNotice) {
            return;
        }
        changeCoverShow(isShow);
    }

    public void reset() {
        mIsShowNotice = false;
        hideNotice();
        changeCoverShow(true);

    }

    public void changeFullScreenIcon(boolean isFullFlag) {
        if (isFullFlag) {
            mScreenControlView.setBackgroundResource(R.drawable.video_fullscreen);
            return;
        }
        mScreenControlView.setBackgroundResource(R.drawable.video_no_fullscreen);
    }

    public void setControlPlayOrPause(View.OnClickListener clickListener) {
        ViewUtils.setViewClickListener(mPlayOrPauseView, clickListener);
    }

    public void setScreenControl(View.OnClickListener clickListener) {
        ViewUtils.setViewClickListener(mScreenControlView, clickListener);
    }

    public void changePlayIcon(boolean isPause) {
        if (isPause) {
            mPlayOrPauseView.setBackgroundResource(R.drawable.video_pause);
        } else {
            mPlayOrPauseView.setBackgroundResource(R.drawable.video_play);
        }
    }
}
