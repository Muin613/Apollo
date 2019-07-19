package com.munin.library.media.video;

public interface IVideoListener {

    void onCompletion(IVideoPlayer videoPlayer);

    boolean onError(IVideoPlayer videoPlayer, int what, int extra);

    boolean onInfo(IVideoPlayer videoPlayer, int what, int extra);

    void onPrepared(IVideoPlayer videoPlayer);

    void onBufferingUpdate(IVideoPlayer videoPlayer, int percent);
}
