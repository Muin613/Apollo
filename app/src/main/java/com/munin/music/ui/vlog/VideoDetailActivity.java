package com.munin.music.ui.vlog;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;

import com.munin.music.R;
import com.munin.music.manager.VideoControlManager;
import com.munin.music.ui.BaseActivity;

/**
 * @author M
 */
public class VideoDetailActivity extends BaseActivity {
    private final static String VIDEO_URL = "http://video.maxxipoint.com/Video/Family-60S-0411.mp4";
    public static ViewGroup mContentView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_detail);
        mContentView = findViewById(android.R.id.content);
    }


    public void click(View view) {
        VideoControlManager.newInstance().enterFullScreen(mContentView);
    }

    public void click1(View view) {
        VideoControlManager.newInstance().exitFullScreen();
    }

    @Override
    protected void onResume() {
        super.onResume();
        VideoControlManager.newInstance().onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        VideoControlManager.newInstance().onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        VideoControlManager.newInstance().destroy();
    }
}
