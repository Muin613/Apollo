package com.munin.music.ui.music;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.munin.library.log.Logger;
import com.munin.music.R;
import com.munin.music.media.record.RTAudioRecorderHelper;
import com.munin.music.media.sound.NativeAudioTrackHelper;


public class MusicTestActivity extends AppCompatActivity {

    RTAudioRecorderHelper helper;
    static {
        System.loadLibrary("x264-lib");
    }
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_test_view);
        helper = new RTAudioRecorderHelper(getCacheDir().getAbsolutePath() + "/1111.mp3");
    }

    public void click(View view) {
        helper.startRecord();
    }

    public void click1(View view) {
        helper.stopRecord();
    }

    public void click3(View view) {
        Logger.i("FloatParentLayout", "不是本人");
    }
    public void click4(View view) {
        Logger.i("FloatParentLayout", "不是本人1");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
