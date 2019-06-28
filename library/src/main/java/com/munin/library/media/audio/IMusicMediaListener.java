package com.munin.library.media.audio;

public interface IMusicMediaListener {

    void onCompletion(IMusicMedia musicMedia);

    boolean onError(IMusicMedia musicMedia, int what, int extra);

    boolean onInfo(IMusicMedia musicMedia, int what, int extra);

    void onPrepared(IMusicMedia musicMedia);

    void onBufferingUpdate(IMusicMedia mp, int percent);
}
