package com.munin.music.ui.common;

import android.app.Activity;
import android.content.Context;
import android.graphics.SurfaceTexture;
import android.util.AttributeSet;
import android.view.TextureView;
import android.view.View;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;

import com.munin.library.log.Logger;
import com.munin.library.media.video.IVideoPlayer;
import com.munin.library.media.video.IVideoView;
import com.munin.library.utils.TimeStampUtils;
import com.munin.library.utils.ViewUtils;
import com.munin.music.R;
import com.munin.music.manager.VideoControlManager;
import com.munin.music.manager.video.VideoState;
import com.munin.music.ui.vlog.VideoDetailActivity;

/**
 * @author M
 */
public class VideoPlayerView extends FrameLayout implements IVideoView, View.OnClickListener {
    private static final String TAG = "VideoPlayerView";
    private FrameLayout mContentView;
    protected View mPlayOrPauseView, mCoverView, mScreenControlView;
    private AppCompatTextView mTitle, mTime, mTotalTime;
    protected TextureView mVideoView;
    private AppCompatImageView mThumbImageView;
    protected int mState = VideoState.STATE_INIT;
    private String mUrl = "http://video.maxxipoint.com/Video/Family-60S-0411.mp4";

    public VideoPlayerView(@NonNull Context context) {
        this(context, null);
    }

    public VideoPlayerView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        setId(R.id.id_video_player);
        mContentView = new FrameLayout(context);
        mVideoView = new TextureView(context);
        addView(mContentView);
        inflate(getContext(), R.layout.layout_video_player, this);
    }

    Runnable mRunnable = new Runnable() {
        @Override
        public void run() {
            if (mState == VideoState.STATE_PAUSE) {
                return;
            }
            ViewUtils.setViewVisible(mCoverView, View.GONE);
        }
    };

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        Logger.i(TAG, "onAttachedToWindow");
        mCoverView = findViewById(R.id.video_cover_view);
        mPlayOrPauseView = findViewById(R.id.video_play_control);
        mThumbImageView = findViewById(R.id.video_cover_img);
        mScreenControlView = findViewById(R.id.video_screen_control);
        mTotalTime = findViewById(R.id.video_total_time);
        ViewUtils.setViewClickListener(mPlayOrPauseView, this);
        ViewUtils.setViewClickListener(this, this);
        ViewUtils.setViewClickListener(mScreenControlView, this);
        mVideoView.setSurfaceTextureListener(this);
        ViewUtils.setViewVisible(mCoverView, View.VISIBLE);
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        removeCallbacks(mRunnable);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.video_play_control:
                if (VideoControlManager.newInstance().getVideoView() == this) {
                    if (VideoControlManager.newInstance().canPause()) {
                        VideoControlManager.newInstance().pause();
                        break;
                    }
                    if (VideoControlManager.newInstance().canPlay()) {
                        VideoControlManager.newInstance().start();
                        break;
                    }
                } else {
                    VideoControlManager.newInstance().attachView(this);
                }

                break;
            case R.id.video_screen_control:
                if (!VideoControlManager.newInstance().isFullScreen()) {
                    VideoControlManager.newInstance().enterFullScreen(VideoDetailActivity.mContentView);
                } else {
                    VideoControlManager.newInstance().exitFullScreen();
                }
                break;
            case R.id.id_video_player:
                if (VideoControlManager.newInstance().getVideoView() == this) {
                    ViewUtils.setViewVisible(mCoverView, View.VISIBLE);
                    postDelayed(mRunnable, 3000);
                } else {
                    VideoControlManager.newInstance().attachView(this);
                }
                break;
            default:
                break;
        }
    }


    @Override
    public void onSurfaceTextureAvailable(SurfaceTexture surface, int width, int height) {
        Logger.i(TAG, "onSurfaceTextureAvailable");
        if (!VideoControlManager.newInstance().isExistSurface()) {
            VideoControlManager.newInstance().changeSurfaceTexture(surface);
            play();
        } else {
            mVideoView.setSurfaceTexture(VideoControlManager.newInstance().getSurfaceTexture());
        }
    }

    public void startPlay() {
        ViewUtils.addView(mContentView, mVideoView);
    }

    public void play() {
        VideoControlManager.newInstance().play(mUrl);
    }

    @Override
    public void onSurfaceTextureSizeChanged(SurfaceTexture surface, int width, int height) {

    }

    @Override
    public boolean onSurfaceTextureDestroyed(SurfaceTexture surface) {
        return false;
    }

    @Override
    public void onSurfaceTextureUpdated(SurfaceTexture surface) {

    }

    @Override
    public void onCompletion(IVideoPlayer videoPlayer) {

    }

    @Override
    public boolean onError(IVideoPlayer videoPlayer, int what, int extra) {
        return false;
    }

    @Override
    public boolean onInfo(IVideoPlayer videoPlayer, int what, int extra) {
        return false;
    }

    @Override
    public void onPrepared(IVideoPlayer videoPlayer) {
        VideoControlManager.newInstance().start();
        mTotalTime.setText(TimeStampUtils.stampToData("" + VideoControlManager.newInstance().getDuration()));
    }

    @Override
    public void onBufferingUpdate(IVideoPlayer videoPlayer, int percent) {

    }

    @Override
    public View getView() {
        return this;
    }

    @Override
    public void enterFullScreen() {
        removeCallbacks(mRunnable);
        ViewUtils.setViewVisible(mCoverView, View.GONE);
        ViewUtils.removeView(mVideoView);
        mScreenControlView.setBackgroundResource(R.drawable.video_no_fullscreen);
    }

    @Override
    public void exitFullScreen() {
        removeCallbacks(mRunnable);
        ViewUtils.setViewVisible(mCoverView, View.GONE);
        ViewUtils.addView(mContentView, mVideoView);
        mScreenControlView.setBackgroundResource(R.drawable.video_fullscreen);

    }

    @Override
    public void changeVideoView(boolean isLeave) {
        Logger.i(TAG, "changeVideoView " + isLeave);
        if (isLeave) {
            removeCallbacks(mRunnable);
            ViewUtils.removeView(mVideoView);
            ViewUtils.setViewVisible(mCoverView, View.VISIBLE);
        } else {
            ViewUtils.addView(mContentView, mVideoView);
        }
    }

    @Override
    public void notifyVideoState(int state) {
        mState = state;
        if (mState == VideoState.STATE_PAUSE) {
            ViewUtils.setViewVisible(mCoverView, View.VISIBLE);
        } else {
            postDelayed(mRunnable, 3000);
        }
    }


}
