package com.munin.music.ui.player;

import android.os.Bundle;
import android.widget.SeekBar;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import com.munin.library.utils.ViewUtils;
import com.munin.music.R;
import com.munin.music.model.music.MusicModel;
import com.munin.music.ui.player.controller.MusicPlayPanelController;
import java.util.Arrays;
import java.util.List;


public class MusicTestActivity extends AppCompatActivity {
    private AppCompatImageView mMusicBackView, mMusicPlayPauseView, mMusicForwardView;
    private SeekBar mProgressSeekBar;
    private AppCompatTextView mMusicTotalTimeView, mMusicCurrentTimeView;
    private String m = "http://play.baobao88.com/vbaobao88/vid-0dc40aadf5b0bd6497311ee676501415/bbfile/media/000003/%E6%AD%8C2/%E6%96%87%E6%98%8E%E7%A4%BC%E4%BB%AA%E4%B9%8B%E6%AD%8C/5569a960.mp3";
    String m1 = "https://downpwnew.com/12875/Mann%20Mera%20-%20Gajendra%20Verma%20320Kbps.mp3";
    String m2 = "https://m10.music.126.net/20190625152947/b49c71f0fa1a132ee6142d4bf123eafe/yyaac/0e52/0f59/560f/98c7cb948983db16199caeecff889310.m4a";
    private List<String> mData = Arrays.asList(new String[]{m2, m, m1});
    private int pos = 0;
    private MusicPlayPanelController mController = new MusicPlayPanelController();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_test_view);
        mController.mMusicBackView = mMusicBackView = findViewById(R.id.music_control_back);
        mController.mMusicPlayPauseView = mMusicPlayPauseView = findViewById(R.id.music_control_play_or_pause);
        mController.mMusicForwardView = mMusicForwardView = findViewById(R.id.music_control_forward);
        mController.mProgressSeekBar = mProgressSeekBar = findViewById(R.id.music_control_seek);
        mController.mMusicTotalTimeView = mMusicTotalTimeView = findViewById(R.id.music_total_time);
        mController.mMusicCurrentTimeView = mMusicCurrentTimeView = findViewById(R.id.music_current_time);

        mMusicBackView.setOnClickListener(v -> {
            pos--;
            if (pos < 0) {
                pos = 2;
            }
            MusicModel model = new MusicModel();
            model.setMusicUrl(mData.get(pos));
            mController.changeMusic(model);
        });
        mMusicForwardView.setOnClickListener(v -> {
            pos++;
            if (pos > 2) {
                pos = 0;
            }
            MusicModel model = new MusicModel();
            model.setMusicUrl(mData.get(pos));
            mController.changeMusic(model);
        });
        ViewUtils.setViewClickListener(mMusicPlayPauseView, v -> {
            mController.playMusic();
        });
        mController.bind(this);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        mController.unBind(this);

    }
}
