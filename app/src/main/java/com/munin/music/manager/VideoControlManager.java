package com.munin.music.manager;

import android.graphics.SurfaceTexture;
import android.view.Surface;
import android.view.ViewGroup;

import androidx.annotation.NonNull;

import com.munin.library.log.Logger;
import com.munin.library.media.video.IVideoListener;
import com.munin.library.media.video.IVideoPlayer;
import com.munin.library.media.video.IVideoView;
import com.munin.library.media.video.NativeVideoPlayer;
import com.munin.library.utils.ViewUtils;
import com.munin.music.manager.video.VideoState;
import com.munin.music.ui.common.FullScreenVideoPlayerView;
import com.munin.music.utils.ActivityUtils;

/**
 * @author M
 */
public class VideoControlManager implements IVideoListener {
    private static final String TAG = "VideoControlManager";
    private static final int MODE_NORMAL = 0;
    public static final int MODE_FULLSCREEN = 1;
    private IVideoView mVideoView, mFullScreenListener;
    private SurfaceTexture mSurfaceTexture;
    private Surface mSurface;
    public int mCurrentState = VideoState.STATE_INIT;
    private int mMode = MODE_NORMAL;
    private static volatile VideoControlManager mInstance = new VideoControlManager();
    private static boolean mIsClickToPause = false;
    IVideoPlayer mPlayer;
    private FullScreenVideoPlayerView mFullScreenVideoView;

    private VideoControlManager() {
        mPlayer = new NativeVideoPlayer();
        setListener();
    }

    public static VideoControlManager newInstance() {
        return mInstance;
    }

    public SurfaceTexture getSurfaceTexture() {
        return mSurfaceTexture;
    }

    public void attachView(@NonNull IVideoView videoView) {
        if (isFullScreen()) {
            mFullScreenListener = videoView;
            return;
        }
        mFullScreenListener = null;
        changeVideo(videoView);
        detach();
        mVideoView = videoView;
    }

    public void play(String url) {
        setData(url);
    }

    private void changeVideo(@NonNull IVideoView videoView) {
        if (mVideoView == null) {
            videoView.changeVideoView(false);
            return;
        }
        if (mVideoView == videoView) {
            return;
        }
        releaseSurface();
        mVideoView.changeVideoView(true);
        videoView.changeVideoView(false);
    }

    public void changeSurfaceTexture(SurfaceTexture surfaceTexture) {
        if (surfaceTexture == null) {
            return;
        }
        releaseSurface();
        mSurfaceTexture = surfaceTexture;
        mSurface = new Surface(mSurfaceTexture);
        if (mPlayer != null) {
            mPlayer.setSurface(mSurface);
        }
    }

    public boolean isExistSurface() {
        return mSurface != null;
    }

    public boolean isExistSurfaceTexture() {
        return mSurfaceTexture != null;
    }

    private void setData(String url) {
        if (mPlayer == null) {
            return;
        }
        mCurrentState = VideoState.STATE_INIT;
        notifyVideoState();
        mPlayer.play(url);
    }

    private void setListener() {
        if (mPlayer == null) {
            return;
        }
        mPlayer.setListener(this);
    }

    public void enterFullScreen(@NonNull ViewGroup contentView) {
        if (mMode == MODE_NORMAL) {
            ActivityUtils.INSTANCE.changeOrientation(contentView.getContext(),true);
            removeFullScreen();
            if (mVideoView != null) {
                mVideoView.enterFullScreen();
            }
            mFullScreenVideoView = new FullScreenVideoPlayerView(contentView.getContext());
            ViewUtils.addView(contentView, mFullScreenVideoView);
            mMode = MODE_FULLSCREEN;
        }
    }

    public void exitFullScreen() {
        if (mMode == MODE_FULLSCREEN) {
            removeFullScreen();
            if (mVideoView != null) {
                mVideoView.exitFullScreen();
            }
            mMode = MODE_NORMAL;
        }
    }

    private void removeFullScreen() {
        ViewUtils.removeView(mFullScreenVideoView);
        mFullScreenVideoView = null;
    }

    private void releaseSurface() {
        Logger.i(TAG,"releaseSurface");
        if (mSurfaceTexture != null) {
            mSurfaceTexture.release();
            mSurfaceTexture = null;
        }
        if (mSurface != null) {
            mSurface.release();
            mSurface = null;
        }

    }

    public void start() {
        mIsClickToPause = false;
        if (mPlayer == null) {
            return;
        }
        mPlayer.start();
        mCurrentState = VideoState.STATE_PLAYING;
        notifyVideoState();
    }

    public void resume() {
        mIsClickToPause = false;
        if (mPlayer == null) {
            return;
        }
        if (mCurrentState == VideoState.STATE_PREPARED || mCurrentState == VideoState.STATE_PAUSE || mCurrentState == VideoState.STATE_COMPLETE) {
            mCurrentState = VideoState.STATE_PLAYING;
            notifyVideoState();
            mPlayer.start();
        }
    }

    public void onResume() {
        if (mIsClickToPause) {
            return;
        }
        resume();
    }

    public void pause() {
        mIsClickToPause = true;
        onPause();
    }

    public void onPause() {
        if (mPlayer == null) {
            return;
        }
        if (mCurrentState == VideoState.STATE_PLAYING) {
            mCurrentState = VideoState.STATE_PAUSE;
            notifyVideoState();
            mPlayer.pause();
        }
    }


    public void seekTo(int time) {
        if (mPlayer == null) {
            return;
        }
        if (mCurrentState != VideoState.STATE_INIT) {
            mPlayer.seekTo(time);
        }
    }

    private void detach() {
        mVideoView = null;
    }

    public void destroy() {
        detach();
        if (mPlayer != null) {
            mPlayer.stop();
            mPlayer.release();
        }
        releaseSurface();
        mCurrentState = VideoState.STATE_INIT;
    }

    @Override
    public void onCompletion(IVideoPlayer videoPlayer) {
        mCurrentState = VideoState.STATE_COMPLETE;
        notifyVideoState();
        if (isFullScreen()) {
            if (mFullScreenListener != null) {
                mFullScreenListener.onCompletion(videoPlayer);
            }
        } else if (mVideoView != null) {
            mVideoView.onCompletion(videoPlayer);
        }
    }


    @Override
    public boolean onError(IVideoPlayer videoPlayer, int what, int extra) {
        mCurrentState = VideoState.STATE_ERROR;
        notifyVideoState();
        if (isFullScreen()) {
            if (mFullScreenListener != null) {
                return mFullScreenListener.onError(videoPlayer, what, extra);
            }
        } else if (mVideoView != null) {
            return mVideoView.onError(videoPlayer, what, extra);
        }
        return false;
    }

    @Override
    public boolean onInfo(IVideoPlayer videoPlayer, int what, int extra) {
        if (isFullScreen()) {
            if (mFullScreenListener != null) {
                return mFullScreenListener.onInfo(videoPlayer, what, extra);
            }
        } else if (mVideoView != null) {
            return mVideoView.onInfo(videoPlayer, what, extra);
        }
        return false;
    }

    @Override
    public void onPrepared(IVideoPlayer videoPlayer) {
        mCurrentState = VideoState.STATE_PREPARED;
        notifyVideoState();
        if (isFullScreen()) {
            if (mFullScreenListener != null) {
                mFullScreenListener.onPrepared(videoPlayer);
            }
        } else if (mVideoView != null) {
            mVideoView.onPrepared(videoPlayer);
        }
    }

    @Override
    public void onBufferingUpdate(IVideoPlayer videoPlayer, int percent) {
        if (isFullScreen()) {
            if (mFullScreenListener != null) {
                mFullScreenListener.onBufferingUpdate(videoPlayer, percent);
            }
        } else if (mVideoView != null) {
            mVideoView.onBufferingUpdate(videoPlayer, percent);
        }
    }

    public int getCurrentPosition() {
        if (mPlayer == null) {
            return 0;
        }
        return mPlayer.getCurrentPosition();
    }

    public int getDuration() {
        if (mPlayer == null) {
            return 0;
        }
        return mPlayer.getDuration();
    }

    public void notifyVideoState() {
        if (isFullScreen()) {
            if (mFullScreenListener != null) {
                mFullScreenListener.notifyVideoState(mCurrentState);
            }
        } else if (mVideoView != null) {
            mVideoView.notifyVideoState(mCurrentState);
        }
    }

    public IVideoView getVideoView() {
        if (isFullScreen()) {
            return mFullScreenListener;
        }
        return mVideoView;
    }

    public boolean isFullScreen() {
        return mMode == MODE_FULLSCREEN;
    }

    public boolean canPause() {
        return mCurrentState == VideoState.STATE_PLAYING;
    }

    public boolean isPlaying() {
        return canPause();
    }

    public boolean canPlay() {
        return mCurrentState == VideoState.STATE_PAUSE || mCurrentState == VideoState.STATE_COMPLETE || mCurrentState == VideoState.STATE_PREPARED;
    }
}
