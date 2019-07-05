package com.munin.music.ui.music;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.munin.library.log.Logger;
import com.munin.library.view.widget.refreshlayout.XRefreshLayout;
import com.munin.library.view.widget.refreshlayout.interfaces.OnRefreshLoadMoreListener;
import com.munin.library.view.widget.refreshlayout.interfaces.RefreshLayout;
import com.munin.music.R;
import com.munin.music.ui.common.RefreshLayoutManger;


public class MusicTestActivity extends AppCompatActivity {
    XRefreshLayout x;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_test_view);
        x = findViewById(R.id.x);
        x.setRefreshLoadLayoutManger(new RefreshLayoutManger(this));
        x.setListener(new OnRefreshLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                x.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        x.finishLoad();
                    }
                }, 500);
            }

            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                x.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        x.finishRefresh();
                    }
                }, 500);
            }
        });
    }

    public void click(View view) {
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
