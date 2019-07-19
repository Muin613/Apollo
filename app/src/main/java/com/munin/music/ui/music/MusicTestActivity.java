package com.munin.music.ui.music;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.munin.music.R;
import com.munin.music.media.sound.NativeAudioTrackHelper;


public class MusicTestActivity extends AppCompatActivity {

    NativeAudioTrackHelper helper=new NativeAudioTrackHelper();
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_test_view);
    }

    public void click(View view) {
        helper.play();
    }

    public void click1(View view) {
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
