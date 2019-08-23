package com.munin.music.ui.vlog;

import android.database.DatabaseUtils;
import android.os.Bundle;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.munin.music.R;
import com.munin.music.dao.User;
import com.munin.music.database.DataBaseUtils;
import com.munin.music.manager.VideoControlManager;
import com.munin.music.ui.BaseActivity;
import com.munin.music.ui.vlog.adapter.VideoAdapter;

/**
 * @author M
 */
public class VideoDetailActivity extends BaseActivity {
    private final static String VIDEO_URL = "http://video.maxxipoint.com/Video/Family-60S-0411.mp4";
    public static ViewGroup mContentView;
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_detail);
        mContentView = findViewById(android.R.id.content);
        recyclerView = findViewById(R.id.rv);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(new VideoAdapter());
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
