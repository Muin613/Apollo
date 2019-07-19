package com.munin.library.media.video;

import android.view.Surface;

public interface IVideoPlayer {

    void start();

    void pause();

    void release();

    void stop();

    void setLoop(boolean isLoop);

    void setSurface(Surface surface);

    void play(String url);

    void seekTo(int time);

    int getCurrentPosition();

    int getDuration();

    boolean isLooping();

    void setListener(IVideoListener listener);

    Object getMedia();
}
