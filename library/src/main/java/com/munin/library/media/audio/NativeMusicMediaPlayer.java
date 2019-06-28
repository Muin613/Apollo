package com.munin.library.media.audio;

import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Build;

import com.munin.library.log.Logger;

/**
 * @author M
 * 原生mediaplayer的音乐播放器
 * @see <a href="https://developer.android.google.cn/guide/topics/media/media-formats.html>developer.com</a>
 */
public class NativeMusicMediaPlayer implements IMusicMedia
        , MediaPlayer.OnBufferingUpdateListener, MediaPlayer.OnCompletionListener
        , MediaPlayer.OnErrorListener, MediaPlayer.OnInfoListener, MediaPlayer.OnPreparedListener {
    private static final String TAG = "NativeMediaPlayer";
    private MediaPlayer mMediaPlayer;
    private IMusicMediaListener mListener;


    @Override
    public void setListener(IMusicMediaListener listener) {
        this.mListener = listener;
    }

    public NativeMusicMediaPlayer() {

    }


    @Override
    public void prepareAsync() {
        Logger.i(TAG, "prepareAsync:");
        if (mMediaPlayer == null) {
            Logger.e(TAG, "prepareAsync: media player is null!");
            return;
        }
        mMediaPlayer.prepareAsync();
    }

    @Override
    public void start() {
        Logger.i(TAG, "start:");
        if (mMediaPlayer == null) {
            Logger.e(TAG, "start: media player is null!");
            return;
        }
        mMediaPlayer.start();
    }

    @Override
    public void pause() {
        Logger.i(TAG, "pause:");
        if (mMediaPlayer == null) {
            Logger.e(TAG, "pause: media player is null!");
            return;
        }
        mMediaPlayer.pause();
    }

    @Override
    public void release() {
        Logger.i(TAG, "release:");
        if (mMediaPlayer == null) {
            Logger.w(TAG, "release: media player is null!");
            return;
        }
        mMediaPlayer.release();
    }

    @Override
    public void stop() {
        Logger.i(TAG, "stop:");
        if (mMediaPlayer == null) {
            Logger.w(TAG, "stop: media player is null!");
            return;
        }
        mMediaPlayer.stop();
    }

    @Override
    public void play(String url) {
        Logger.i(TAG, "play: url = " + url);
        stop();
        release();
        mMediaPlayer = new MediaPlayer();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            AudioAttributes attributes = new AudioAttributes.Builder()
                    .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                    //.setFlags(AudioAttributes.FLAG_LOW_LATENCY)
                    .setUsage(AudioAttributes.USAGE_MEDIA)
                    .setLegacyStreamType(AudioManager.STREAM_MUSIC)
                    .build();
            mMediaPlayer.setAudioAttributes(attributes);
        } else {
            mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        }

        try {
            mMediaPlayer.reset();
            mMediaPlayer.setDataSource(url);
            mMediaPlayer.setOnPreparedListener(this);
            mMediaPlayer.setOnBufferingUpdateListener(this);
            mMediaPlayer.setOnErrorListener(this);
            mMediaPlayer.setOnInfoListener(this);
            mMediaPlayer.setOnCompletionListener(this);
            mMediaPlayer.prepareAsync();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void seekTo(int time) {
        Logger.i(TAG, "seekTo: time = " + time);
        if (mMediaPlayer == null) {
            Logger.e(TAG, "seekTo: media player is null!");
            return;
        }
        mMediaPlayer.seekTo(time);

    }

    @Override
    public void setLoop(boolean isLoop) {
        Logger.i(TAG, "setLoop: isLoop = " + isLoop);
        if (mMediaPlayer == null) {
            Logger.e(TAG, "setLoop: media player is null!");
            return;
        }
        mMediaPlayer.setLooping(isLoop);
    }


    @Override
    public int getCurrentPosition() {
        Logger.i(TAG, "getCurrentPosition:");
        if (mMediaPlayer == null) {
            Logger.e(TAG, "getCurrentPosition: media player is null!");
            return 0;
        }
        return mMediaPlayer.getCurrentPosition();
    }

    @Override
    public int getDuration() {
        Logger.i(TAG, "getDuration:");
        if (mMediaPlayer == null) {
            Logger.e(TAG, "getDuration: media player is null!");
            return 0;
        }
        return mMediaPlayer.getDuration();
    }

    @Override
    public boolean isLooping() {
        Logger.i(TAG, "isLooping:");
        if (mMediaPlayer == null) {
            Logger.e(TAG, "isLooping: media player is null!");
            return false;
        }
        return mMediaPlayer.isLooping();
    }

    @Override
    public void onBufferingUpdate(MediaPlayer mp, int percent) {
        Logger.i(TAG, "onBufferingUpdate: percent = " + percent);
        if (mListener == null) {
            return;
        }
        mListener.onBufferingUpdate(this, percent);
    }

    @Override
    public void onCompletion(MediaPlayer mp) {
        if (mListener == null) {
            return;
        }
        mListener.onCompletion(this);
    }

    @Override
    public boolean onError(MediaPlayer mp, int what, int extra) {
        Logger.i(TAG, "onError: what = " + what + " extra = " + extra);
        if (mListener == null) {
            return false;
        }
        return mListener.onError(this, what, extra);
    }

    @Override
    public boolean onInfo(MediaPlayer mp, int what, int extra) {
        Logger.i(TAG, "onInfo: what = " + what + " extra = " + extra);
        if (mListener == null) {
            return false;
        }
        return mListener.onInfo(this, what, extra);

    }

    @Override
    public void onPrepared(MediaPlayer mp) {
        Logger.i(TAG, "onPrepared:");
        if (mListener == null) {
            return;
        }
        mListener.onPrepared(this);
    }

    @Override
    public Object getMedia() {
        return mMediaPlayer;
    }
}
