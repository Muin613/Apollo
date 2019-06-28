package com.munin.library.media.audio;

public interface IMusicMedia {
    void prepareAsync();

    void start();

    void pause();

    void release();

    void stop();

    void setLoop(boolean isLoop);

    void play(String url);

    void seekTo(int time);

    int getCurrentPosition();

    int getDuration();

    boolean isLooping();

    void setListener(IMusicMediaListener listener);

    Object getMedia();
}
