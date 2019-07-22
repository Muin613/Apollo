package com.munin.music.ui.common;

import android.content.Context;
import android.graphics.SurfaceTexture;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.TextureView;
import android.view.View;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.munin.library.common.WeakHandler;
import com.munin.library.log.Logger;
import com.munin.library.media.video.IVideoPlayer;
import com.munin.library.media.video.IVideoView;
import com.munin.library.utils.TimeStampUtils;
import com.munin.library.utils.ViewUtils;
import com.munin.music.R;
import com.munin.music.manager.VideoControlManager;
import com.munin.music.manager.video.VideoState;
import com.munin.music.ui.vlog.VideoDetailActivity;
import com.munin.music.utils.ActivityUtils;

/**
 * @author M
 */
public class VideoPlayerView extends FrameLayout implements IVideoView, View.OnClickListener {
    private static final String TAG = "VideoPlayerView";
    protected TextureView mVideoView;
    protected int mState = VideoState.STATE_DEFAULT;
    private String mUrl = "http://video.maxxipoint.com/Video/Family-60S-0411.mp4";
    protected VideoCoverView mVideoCoverView;
    protected FrameLayout mContentView;
    boolean isShow = false;
    WeakHandler mHandler = new WeakHandler();
    Runnable mAction = new Runnable() {
        @Override
        public void run() {
            if (mState == VideoState.STATE_PLAYING) {
                mVideoCoverView.changeCurrentTime(TimeStampUtils.stampToData("" + VideoControlManager.newInstance().getCurrentPosition()));
                mHandler.postDelayed(mAction, 1000);
            } else {
                mHandler.removeCallbacks(mAction);
            }
        }
    };
    Runnable mShowAction = new Runnable() {
        @Override
        public void run() {
            mVideoCoverView.changeCoverShow(false);
        }
    };

    public VideoPlayerView(@NonNull Context context) {
        this(context, null);
    }

    public VideoPlayerView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        setId(R.id.id_video_player);
        mVideoCoverView = new VideoCoverView(context);
        mVideoCoverView.setMinimumHeight(300);
        mVideoCoverView.setMinimumWidth(600);
        mVideoView = new TextureView(context);
        mVideoView.setMinimumHeight(300);
        mContentView = new FrameLayout(context);
        addView(mContentView);
        addView(mVideoCoverView);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        Logger.i(TAG, "dispatchTouchEvent " + ev.getAction());
        controlCoverShow(ev);
        return super.dispatchTouchEvent(ev);
    }

    public void controlCoverShow(MotionEvent ev) {
        if (VideoControlManager.newInstance().getVideoView() != this) {
            return;
        }
        if (mState == VideoState.STATE_PLAYING) {
            if (!isShow && ev.getAction() == MotionEvent.ACTION_DOWN) {
                isShow = true;
                mHandler.removeCallbacks(mShowAction);
                mVideoCoverView.changeCoverShow(true);
                return;
            }
            if (isShow && (ev.getAction() == MotionEvent.ACTION_UP || ev.getAction() == MotionEvent.ACTION_CANCEL)) {
                isShow = false;
                mHandler.postDelayed(mShowAction, 3000);
            }
        }
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        Logger.i(TAG, "onAttachedToWindow");
        mVideoCoverView.setControlPlayOrPause(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (VideoControlManager.newInstance().getVideoView() == VideoPlayerView.this) {
                    if (VideoControlManager.newInstance().canPause()) {
                        VideoControlManager.newInstance().pause();
                        return;
                    }
                    if (VideoControlManager.newInstance().canPlay()) {
                        VideoControlManager.newInstance().start();
                    }
                } else {
                    VideoControlManager.newInstance().attachView(VideoPlayerView.this);
                }
            }
        });
        mVideoCoverView.setScreenControl(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!VideoControlManager.newInstance().isFullScreen() && VideoControlManager.newInstance().isPlaying()) {
                    VideoControlManager.newInstance().enterFullScreen(VideoDetailActivity.mContentView);
                } else {
                    VideoControlManager.newInstance().exitFullScreen();
                }
            }
        });
        ViewUtils.setViewClickListener(this, this);
        mVideoView.setSurfaceTextureListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.id_video_player:
                if (VideoControlManager.newInstance().getVideoView() != this) {
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
            Logger.i(TAG, "设置");
            mVideoView.setSurfaceTexture(VideoControlManager.newInstance().getSurfaceTexture());
        }
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
        mVideoCoverView.changeTotalTime(TimeStampUtils.stampToData("" + VideoControlManager.newInstance().getDuration()));
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
        mVideoCoverView.changeCoverShow(false);
        ViewUtils.removeView(mVideoView);
        mVideoCoverView.changeFullScreenIcon(false);
    }

    @Override
    public void exitFullScreen() {
        ActivityUtils.changeOrientation(getContext(),false);
        mVideoCoverView.changeCoverShow(false);
        ViewUtils.addView(mContentView, mVideoView);
        mVideoCoverView.changeFullScreenIcon(true);
    }

    @Override
    public void changeVideoView(boolean isLeave) {
        Logger.i(TAG, "changeVideoView " + isLeave);
        if (isLeave) {
            mHandler.removeCallbacks(mShowAction);
            ViewUtils.removeView(mVideoView);
            mVideoCoverView.changeCoverShow(true);
        } else {
            ViewUtils.addView(mContentView, mVideoView);
        }
    }

    public void setTitle(String title) {
        mVideoCoverView.setTitle(title);
    }

    @Override
    public void notifyVideoState(int state) {
        Logger.i(TAG, "notifyVideoState: state=" + state);
        mState = state;
        if (mState == VideoState.STATE_PAUSE) {
            mHandler.removeCallbacks(mShowAction);
            mVideoCoverView.changeCoverShow(true);
        } else {
            mVideoCoverView.changeCoverShow(false);
        }
    }


}
