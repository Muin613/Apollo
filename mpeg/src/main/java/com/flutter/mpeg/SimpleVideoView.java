package com.flutter.mpeg;

import android.view.SurfaceView;
import android.content.Context;
import android.graphics.PixelFormat;
import android.util.AttributeSet;
import android.view.Surface;
import android.view.SurfaceHolder;

public class SimpleVideoView extends SurfaceView {
    // 这边的加载动态库的顺序也是由规定的，必须将被调用者放在面，没有调用的放在后面
    static {
        System.loadLibrary("video-lib");
    }
    public SimpleVideoView(Context context) {
        this(context,null);
    }

    public SimpleVideoView(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public SimpleVideoView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        SurfaceHolder holder = getHolder();
        holder.setFormat(PixelFormat.RGBA_8888);
    }

    public void play(final String path){
        new Thread(new Runnable() {
            @Override
            public void run() {
                playVideo(path,SimpleVideoView.this.getHolder().getSurface());
            }
        }).start();
    }

    private native void playVideo(String path, Surface surface);
}
