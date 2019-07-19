package com.munin.library.media.video;

import android.view.TextureView;
import android.view.View;

/**
 * @author M
 */
public interface IVideoView extends IVideoListener, TextureView.SurfaceTextureListener {
    View getView();

    void enterFullScreen();

    void exitFullScreen();

    void changeVideoView(boolean isLeave);

    void notifyVideoState(int state);
}
