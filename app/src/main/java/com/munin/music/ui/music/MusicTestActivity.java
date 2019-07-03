package com.munin.music.ui.music;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.munin.library.view.widget.refreshlayout.XRefreshLayout;
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
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
