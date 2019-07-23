package com.munin.music.ui.common;

import android.content.Context;
import android.graphics.SurfaceTexture;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.TextureView;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.SeekBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.munin.library.common.WeakHandler;
import com.munin.library.log.Logger;
import com.munin.library.media.video.IVideoPlayer;
import com.munin.library.media.video.IVideoView;
import com.munin.library.utils.ResourceUtils;
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
public class VideoPlayerView extends FrameLayout implements IVideoView {
    private static final String TAG = "VideoPlayerView";
    protected TextureView mVideoView;
    protected int mState = VideoState.STATE_DEFAULT;
    private String mUrl = "http://video.maxxipoint.com/Video/Family-60S-0411.mp4";
    protected VideoCoverView mVideoCoverView;
    protected FrameLayout mContentView;
    boolean isShow = false;
    boolean isSeek = false;
    private int mPercent = 0;
    private int mTotalTime = 0;
    WeakHandler mHandler = new WeakHandler();
    Runnable mAction = new Runnable() {
        @Override
        public void run() {
            if (mState == VideoState.STATE_PLAYING && mVideoCoverView.isShowCover()  && !isSeek) {
                int currentTime = VideoControlManager.newInstance().getCurrentPosition();
                mVideoCoverView.changeCurrentTime(TimeStampUtils.stampToData("" + currentTime));
                mVideoCoverView.changeSeekBar(100 * currentTime / mTotalTime);
                mHandler.postDelayed(mAction, 1000);
            } else {
                mHandler.removeCallbacks(mAction);
            }
        }
    };
    Runnable mShowAction = new Runnable() {
        @Override
        public void run() {
            mHandler.removeCallbacks(mAction);
            mVideoCoverView.showCoverControl(false);
        }
    };
    private VideoMediaNoticeController mController;

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
        mController = new VideoMediaNoticeController(mVideoCoverView);
        setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                controlCoverShow(event);
                return true;
            }
        });
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return super.onTouchEvent(event);
    }

    public boolean controlCoverShow(MotionEvent ev) {
        if (VideoControlManager.newInstance().getVideoView() != this) {
            return false;
        }
        if (mState == VideoState.STATE_PLAYING) {
            if (!isShow && ev.getAction() == MotionEvent.ACTION_DOWN) {
                isShow = true;
                mHandler.removeCallbacks(mShowAction);
                mHandler.removeCallbacks(mAction);
                mHandler.post(mAction);
                mVideoCoverView.showCoverControl(true);
                return true;
            }
        }
        if (isShow && (ev.getAction() == MotionEvent.ACTION_UP || ev.getAction() == MotionEvent.ACTION_CANCEL)) {
            isShow = false;
            mHandler.removeCallbacks(mShowAction);
            mHandler.postDelayed(mShowAction, 3000);
            return true;
        }
        return false;
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
                        mVideoCoverView.changePlayIcon(true);
                        return;
                    }
                    if (VideoControlManager.newInstance().canPlay()) {
                        VideoControlManager.newInstance().start();
                        mVideoCoverView.changePlayIcon(false);
                    }
                } else {
                    mVideoCoverView.changePlayIcon(false);
                    mTotalTime = 0;
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
        mVideoCoverView.setSeekBarListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                Logger.i("fuck", "onProgressChanged" + progress + "  " + fromUser);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                Logger.i("fuck", "onStartTrackingTouch");
                if (mState == VideoState.STATE_PLAYING) {
                    if (!isShow) {
                        isSeek = true;
                        isShow = true;
                        mHandler.removeCallbacks(mShowAction);
                        mHandler.removeCallbacks(mAction);
                        mHandler.post(mAction);
                        mVideoCoverView.showCoverControl(true);
                    }
                }
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                Logger.i("fuck", "onStopTrackingTouch");
                if (isShow) {
                    isSeek = false;
                    isShow = false;
                    mHandler.removeCallbacks(mShowAction);
                    mHandler.postDelayed(mShowAction, 3000);
                }
                if (mState == VideoState.STATE_PLAYING && mVideoCoverView.isShowCover()) {
                    VideoControlManager.newInstance().seekTo(mTotalTime * seekBar.getProgress() / 100);
                }
            }
        });
        mVideoView.setSurfaceTextureListener(this);
    }


    @Override
    public void onSurfaceTextureAvailable(SurfaceTexture surface, int width, int height) {
        Logger.i(TAG, "onSurfaceTextureAvailable");
        if (!VideoControlManager.newInstance().isExistSurface()) {
            VideoControlManager.newInstance().changeSurfaceTexture(surface);
            mVideoCoverView.setNotice(ResourceUtils.getString(R.string.video_buffering));
            play();
        } else {
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
        mController.handleError(what);
        return false;
    }

    @Override
    public boolean onInfo(IVideoPlayer videoPlayer, int what, int extra) {
        if (mState != VideoState.STATE_PAUSE) {
            mController.handleInfo(what);
        }
        return false;
    }

    @Override
    public void onPrepared(IVideoPlayer videoPlayer) {
        VideoControlManager.newInstance().start();
        mTotalTime = VideoControlManager.newInstance().getDuration();
        mVideoCoverView.changeTotalTime(TimeStampUtils.stampToData("" + mTotalTime));
    }

    @Override
    public void onBufferingUpdate(IVideoPlayer videoPlayer, int percent) {
        Logger.i(TAG, "onBufferingUpdate: percent = " + percent);
        if (mState == VideoState.STATE_PLAYING || mState == VideoState.STATE_PAUSE) {
            mPercent = percent;
        }
    }

    @Override
    public View getView() {
        return this;
    }

    @Override
    public void enterFullScreen() {
        mVideoCoverView.showCoverControl(false);
        ViewUtils.removeView(mVideoView);
        mVideoCoverView.changeFullScreenIcon(false);
    }

    @Override
    public void exitFullScreen() {
        ActivityUtils.INSTANCE.changeOrientation(getContext(), false);
        mVideoCoverView.showCoverControl(false);
        ViewUtils.addView(mContentView, mVideoView);
        mVideoCoverView.changeFullScreenIcon(true);
    }

    @Override
    public void changeVideoView(boolean isLeave) {
        Logger.i(TAG, "changeVideoView " + isLeave);
        if (isLeave) {
            mHandler.removeCallbacks(mShowAction);
            ViewUtils.removeView(mVideoView);
            mVideoCoverView.reset();
        } else {
            mHandler.removeCallbacks(mShowAction);
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
            mVideoCoverView.showCoverControl(true);
        } else if (mState == VideoState.STATE_PLAYING && mPercent > 0) {
            mHandler.removeCallbacks(mShowAction);
            mHandler.postDelayed(mShowAction, 3000);
        }
    }


}
