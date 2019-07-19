package com.munin.music.ui.vlog;

import android.hardware.Camera;
import android.media.MediaRecorder;
import android.os.Build;
import android.os.Bundle;
import android.view.SurfaceHolder;
import android.view.TextureView;
import android.view.View;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import com.munin.music.R;
import com.munin.music.media.record.NativeMediaRecorder;
import com.munin.music.ui.BaseActivity;
import java.io.File;
import java.io.IOException;

/**
 * @author M
 */
public class RecorderActivity extends BaseActivity {
    TextureView surfaceView;
    NativeMediaRecorder mNativeMediaRecoder;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recorder);
        surfaceView = findViewById(R.id.surfaceView);
        mNativeMediaRecoder=new NativeMediaRecorder(surfaceView);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void record(View view) {
        mNativeMediaRecoder.startCapture();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mNativeMediaRecoder.preShow();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mNativeMediaRecoder.onPause();
    }

    public void stop(View view) {

    }
}
