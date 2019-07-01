package com.munin.music.ui.music;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.munin.music.R;


public class MusicTestActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_test_view);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
