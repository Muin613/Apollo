package com.munin.music.ui.music;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.viewpager.widget.ViewPager;

import com.munin.library.log.Logger;
import com.munin.library.utils.ViewUtils;
import com.munin.library.view.widget.NoHaloViewPager;
import com.munin.music.R;
import com.munin.music.data.MusicData;
import com.munin.music.media.music.MusicStatus;
import com.munin.music.model.music.MusicModel;
import com.munin.music.share.ShareTextController;
import com.munin.music.ui.BaseActivity;
import com.munin.music.ui.music.adapter.MusicItemChangeViewAdapter;
import com.munin.music.ui.music.controller.MusicDiscController;
import com.munin.music.ui.music.controller.MusicPlayPanelController;
import com.munin.music.ui.music.controller.MusicStylusController;
import com.munin.music.ui.music.controller.MusicTitleController;
import com.munin.music.ui.music.view.MusicItemView;

import java.util.ArrayList;
import java.util.List;

/**
 * @author M
 */
public class MusicPlayerActivity extends BaseActivity implements ViewPager.OnPageChangeListener {
    private static final String TAG = "MusicPlayerActivity";
    private NoHaloViewPager mViewPager;
    private MusicItemChangeViewAdapter mViewPagerAdapter;
    private AppCompatImageView mImgView;
    private MusicStylusController controller;
    private MusicPlayPanelController mController;
    private MusicTitleController mTitleController = new MusicTitleController();
    private MusicDiscController mDiscController = new MusicDiscController();
    private ShareTextController mShareTextController = new ShareTextController(this);
    private AppCompatImageView mShareImageView, mBackImageView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_music_player);
        mController = new MusicPlayPanelController() {
            @Override
            public void onMusicStatusChange(String state) {
                super.onMusicStatusChange(state);
                if (!state.equals(MusicStatus.STATUS_MUSIC_INIT)) {
                    mViewPager.setCanScroll(true);
                } else {
                    mViewPager.setCanScroll(false);
                }
                if (state.equals(MusicStatus.STATUS_MUSIC_PLAYING)) {
                    controller.reverse();
                    mDiscController.start();
                    return;
                }
                controller.start();
                mDiscController.stop();
            }
        };
        mShareImageView = findViewById(R.id.music_title_share);
        mBackImageView = findViewById(R.id.music_title_back);
        mController.mMusicBackView = findViewById(R.id.music_control_back);
        mController.mMusicPlayPauseView = findViewById(R.id.music_control_play_or_pause);
        mController.mMusicForwardView = findViewById(R.id.music_control_forward);
        mController.mProgressSeekBar = findViewById(R.id.music_control_seek);
        mController.mMusicTotalTimeView = findViewById(R.id.music_total_time);
        mController.mMusicCurrentTimeView = findViewById(R.id.music_current_time);
        mTitleController.mTitleView = findViewById(R.id.music_title_title);
        mTitleController.mAuthorView = findViewById(R.id.music_title_author);
        mViewPager = findViewById(R.id.music_player_view_pager);
        mImgView = findViewById(R.id.music_stylus_view);
        mViewPagerAdapter = new MusicItemChangeViewAdapter(this);
        mViewPager.setAdapter(mViewPagerAdapter);
        mViewPager.addOnPageChangeListener(this);
        controller = new MusicStylusController(mImgView);
        ViewUtils.setViewClickListener(mController.mMusicBackView, v -> {
            if (mViewPager.getCurrentItem() == 0) {
                return;
            }
            mViewPager.setCurrentItem(mViewPager.getCurrentItem() - 1);
        });
        ViewUtils.setViewClickListener(mController.mMusicForwardView, v -> {
            if ((mViewPager.getCurrentItem() + 1) > mViewPagerAdapter.getCount()) {
                return;
            }
            mViewPager.setCurrentItem(mViewPager.getCurrentItem() + 1);
        });
        ViewUtils.setViewClickListener(mController.mMusicPlayPauseView, v -> {
            mController.playMusic();
        });
        mController.bind(this);
        initData();
        ViewUtils.setViewClickListener(mShareImageView, v -> {
            mShareTextController.share();
        });
        ViewUtils.setViewClickListener(mBackImageView, v -> {
            finish();
        });
    }


    public void initData() {
        List<MusicData> data = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            MusicData temp = new MusicData();
            temp.setmMusicName("歌曲串烧" + i);
            temp.setmMusicAuthor("机器人" + i);
            temp.setMusicUrl("https://m801.music.126.net/20190628172116/5174f43ebb0569aba2d3570cb3b7938a/jdyyaac/045a/0253/065e/303e34d46a59c98f75c41471b3b57a4c.m4a");
            data.add(temp);
        }
        mViewPagerAdapter.initData(data);
        controller.setListener(new MusicStylusController.OnAnimListener() {
            @Override
            public void onStart() {

            }

            @Override
            public void onEnd() {
            }
        });
        mViewPager.setCurrentItem(3);
    }

    private int lastPos = -1;

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        Logger.i(TAG, "onPageScrolled: position = " + position + " positionOffset = " + positionOffset + " " + positionOffsetPixels);
        mViewPager.removeHalo();
        if (lastPos != position && positionOffsetPixels == 0) {
            mViewPager.setCanScroll(false);
            MusicItemView view = mViewPagerAdapter.getViews().get(position);
            mDiscController.setDiscImageView(view.getCoverImage());
            MusicModel model = new MusicModel();
            MusicData data = mViewPagerAdapter.getDataList().get(position);
            mShareTextController.changeShareText(data.getMusicUrl());
            model.setMusicUrl(data.getMusicUrl());
            model.setTitle(data.getMusicName());
            mController.changeMusic(model);
            mTitleController.changeTitle(data.getMusicName(), data.getMusicAuthor());
            lastPos = position;
        }
    }

    @Override
    public void onPageSelected(int position) {

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    @Override
    protected void onResume() {
        super.onResume();
        mController.queryMusicStatus();
        mController.startQueryProgress();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mController.removeQueryProgress();
        mDiscController.stop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mViewPager.removeOnPageChangeListener(this);
        mController.unBind(this);
    }


    public static void startMainAct(Context context) {
        if (context == null) {
            return;
        }
        Intent intent = new Intent(context, MusicPlayerActivity.class);
        context.startActivity(intent);
    }
}
