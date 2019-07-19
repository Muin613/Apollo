package com.munin.music.ui.common;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.SurfaceTexture;
import android.os.Build;
import android.util.AttributeSet;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.munin.library.log.Logger;
import com.munin.music.R;
import com.munin.music.manager.VideoControlManager;

/**
 * @author M
 */
public class FullScreenVideoPlayerView extends VideoPlayerView {
    private static final String TAG = "FullScreenVideoView";

    public FullScreenVideoPlayerView(@NonNull Context context) {
        super(context);
    }

    public FullScreenVideoPlayerView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        mScreenControlView.setBackgroundResource(R.drawable.video_no_fullscreen);
        changeVideoView(false);
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onSurfaceTextureAvailable(SurfaceTexture surface, int width, int height) {
        Logger.i(TAG, "onSurfaceTextureAvailable");
        if (VideoControlManager.newInstance().isExistSurface()) {
            mVideoView.setSurfaceTexture(VideoControlManager.newInstance().getSurfaceTexture());
        }
    }
}