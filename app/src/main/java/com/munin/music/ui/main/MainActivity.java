package com.munin.music.ui.main;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;

import com.munin.music.R;
import com.munin.music.ui.player.MusicPlayerActivity;
import com.munin.music.utils.MusicControlUtils;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        MusicControlUtils.startService(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        MusicControlUtils.stopService(this);
    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            moveTaskToBack(false);
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
    public void click(View v) {
        MusicPlayerActivity.startMainAct(this);
    }

    public static void startMainAct(Context context) {
        if (context == null) {
            return;
        }
        Intent intent = new Intent(context, MainActivity.class);
        context.startActivity(intent);
    }
}
