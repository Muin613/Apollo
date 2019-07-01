package com.munin.music.ui.music.controller;

import android.app.Activity;
import android.content.Intent;
import android.widget.SeekBar;

import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;

import com.munin.library.log.Logger;
import com.munin.library.utils.ThreadUtils;
import com.munin.library.utils.TimeStampUtils;
import com.munin.library.utils.ViewUtils;
import com.munin.music.R;
import com.munin.music.broadcast.MusicFeedbackReceiver;
import com.munin.music.media.music.MusicAction;
import com.munin.music.media.music.MusicQuery;
import com.munin.music.media.music.MusicStatus;
import com.munin.music.model.music.MusicModel;
import com.munin.music.utils.MusicControlUtils;

/**
 * @author M
 */
public class MusicPlayPanelController implements MusicFeedbackReceiver.OnMusicFeedbackListener {
    private static final String TAG = "MusicPlayPanelController";
    public AppCompatImageView mMusicBackView, mMusicPlayPauseView, mMusicForwardView;
    public SeekBar mProgressSeekBar;
    public AppCompatTextView mMusicTotalTimeView, mMusicCurrentTimeView;
    private final static int QUERY_TIME = 1000;
    private String mState = MusicStatus.STATUS_MUSIC_INIT;
    private int mDuration = 0;
    private int mCurrentPos = 0;
    MusicFeedbackReceiver mReceiver = new MusicFeedbackReceiver();
    private Runnable mQueryProgressRunnable = new Runnable() {
        @Override
        public void run() {
            Logger.i(TAG, "query position ");
            if (mState.equals(MusicStatus.STATUS_MUSIC_COMPLETE)) {
                MusicControlUtils.sendBroadCast(new Intent(MusicQuery.PARAM_MUSIC_CURRENT_POSITION));
                return;
            }
            MusicControlUtils.sendBroadCast(new Intent(MusicQuery.PARAM_MUSIC_CURRENT_POSITION));
            ThreadUtils.postDelayed(mQueryProgressRunnable, QUERY_TIME);
        }
    };


    public void bind(Activity activity) {
        if (activity == null) {
            return;
        }
        activity.registerReceiver(mReceiver, MusicFeedbackReceiver.getIntentFilter());
        mReceiver.setFeedbackListener(this);
    }

    public void unBind(Activity activity) {
        if (activity == null) {
            return;
        }
        mReceiver.unRegisterFeedbackListener();
        activity.unregisterReceiver(mReceiver);

        removeQueryProgress();
    }

    public void playMusic() {
        if (mState.equals(MusicStatus.STATUS_MUSIC_PAUSE) || mState.equals(MusicStatus.STATUS_MUSIC_PREPARED) || mState.equals(MusicStatus.STATUS_MUSIC_COMPLETE)) {
            Intent data = new Intent(MusicAction.ACTION_OPT_MUSIC_PLAY);
            MusicControlUtils.sendBroadCast(data);
            return;
        }
        if (mState.equals(MusicStatus.STATUS_MUSIC_PLAYING)) {
            Intent data = new Intent(MusicAction.ACTION_OPT_MUSIC_PAUSE);
            MusicControlUtils.sendBroadCast(data);
        }
    }

    public void changeMusic(MusicModel model) {
        if (model == null) {
            return;
        }
        mCurrentPos = 0;
        mDuration = 0;
        mProgressSeekBar.setProgress(0);
        ViewUtils.setText(mMusicCurrentTimeView, TimeStampUtils.stampToData("" + mCurrentPos));
        ViewUtils.setText(mMusicTotalTimeView, TimeStampUtils.stampToData("" + mDuration));
        Intent data = new Intent(MusicAction.ACTION_OPT_MUSIC_CHANGE);
        data.putExtra(MusicModel.MUSIC_MEDIA_URL_KEY, model.getMusicUrl());
        data.putExtra(MusicModel.MUSIC_MEDIA_TITLE_KEY, model.getTitle());
        data.putExtra(MusicModel.MUSIC_MEDIA_ID_KEY, model.getId());
        data.putExtra(MusicModel.MUSIC_MEDIA_AUTHOR_KEY, model.getAuthor());
        data.putExtra(MusicModel.MUSIC_BG_IMG_URL_KEY, model.getImgUrl());
        MusicControlUtils.sendBroadCast(data);
    }


    @Override
    public void onMusicPlaying() {
        mState = MusicStatus.STATUS_MUSIC_PLAYING;
        Logger.i(TAG, "onMusicPlaying ");
        onMusicStatusChange(mState);
        startQueryProgress();
        changePlayPauseView();
    }

    @Override
    public void onMusicDataIsNull() {
        Logger.i(TAG, "onMusicDataIsNull ");
    }

    @Override
    public void onMusicPause() {
        mState = MusicStatus.STATUS_MUSIC_PAUSE;
        onMusicStatusChange(mState);
        Logger.i(TAG, "onMusicPause ");
        changePlayPauseView();
    }

    @Override
    public void onMusicComplete() {
        mState = MusicStatus.STATUS_MUSIC_COMPLETE;
        onMusicStatusChange(mState);
        changeMusicCurrentTime();
        Logger.i(TAG, "onMusicComplete ");
    }

    @Override
    public void onMusicBuffering(int percent) {
        Logger.i(TAG, "onMusicBuffering percent = " + percent);
    }

    @Override
    public void onMusicError(int what, int extra) {
        mState = MusicStatus.STATUS_MUSIC_ERROR;
        onMusicStatusChange(mState);
        changeMusicCurrentTime();
        Logger.i(TAG, "onMusicError what = " + what + " extra = " + extra);
    }

    @Override
    public void onMusicInfo(int what, int extra) {
        Logger.i(TAG, "onMusicInfo what = " + what + " extra = " + extra);
    }

    @Override
    public void onMusicPrepared() {
        mState = MusicStatus.STATUS_MUSIC_PREPARED;
        onMusicStatusChange(mState);
        Logger.i(TAG, "onMusicPrepared");
        queryMusicDuration();
        changePlayPauseView();
    }

    @Override
    public void onMusicFeedbackDuration(int time) {
        Logger.i(TAG, "onMusicFeedbackDuration time = " + time);
        mDuration = time;
        if (mDuration == -1) {
            mDuration = 0;
        }
        if (mMusicTotalTimeView == null) {
            return;
        }
        mMusicTotalTimeView.setText(TimeStampUtils.stampToData("" + mDuration));
    }

    @Override
    public void onMusicFeedbackCurrentPos(int time) {
        Logger.i(TAG, "onMusicFeedbackCurrentPos time = " + time);
        if (mCurrentPos == time && !mState.equals(MusicStatus.STATUS_MUSIC_PLAYING)) {
            removeQueryProgress();
            return;
        }
        mCurrentPos = time;
        if (mMusicCurrentTimeView != null) {
            if (mCurrentPos == -1) {
                mCurrentPos = 0;
            }
            ViewUtils.setText(mMusicCurrentTimeView, TimeStampUtils.stampToData("" + mCurrentPos));
        }

        if (mCurrentPos == -1 || mDuration == -1 || mDuration == 0) {
            return;
        }
        if (mProgressSeekBar == null) {
            return;
        }
        int percent = mCurrentPos * 100 / mDuration;
        mProgressSeekBar.post(new Runnable() {
            @Override
            public void run() {
                mProgressSeekBar.setProgress(percent);
            }
        });
    }

    @Override
    public void onMusicStatus(String state) {
        mState = state;
        Logger.i(TAG, "onMusicStatus state = " + state);
        boolean flag = (mState.equals(MusicStatus.STATUS_MUSIC_PLAYING) || mState.equals(MusicStatus.STATUS_MUSIC_COMPLETE)) && mDuration == 0 && mCurrentPos == 0;
        if (flag) {
            startQueryProgress();
        }
        onMusicStatusChange(mState);
        changePlayPauseView();
        changeMusicCurrentTime();
    }

    public void onMusicStatusChange(String state) {
    }

    public void startQueryProgress() {
        if (!(mState.equals(MusicStatus.STATUS_MUSIC_PLAYING) || mState.equals(MusicStatus.STATUS_MUSIC_COMPLETE))) {
            return;
        }
        queryMusicDuration();
        removeQueryProgress();
        mQueryProgressRunnable.run();
    }

    private void queryMusicDuration() {
        MusicControlUtils.sendBroadCast(new Intent(MusicQuery.PARAM_MUSIC_DURATION));
    }

    public void queryMusicStatus() {
        MusicControlUtils.sendBroadCast(new Intent(MusicQuery.PARAM_MUSIC_STATUS));
    }

    public void removeQueryProgress() {
        ThreadUtils.remove(mQueryProgressRunnable);
    }

    private void changePlayPauseView() {
        if (mState.equals(MusicStatus.STATUS_MUSIC_PLAYING)) {
            ViewUtils.setImageSource(mMusicPlayPauseView, R.drawable.music_pause);
            return;
        }
        ViewUtils.setImageSource(mMusicPlayPauseView, R.drawable.music_play);
    }

    private void changeMusicCurrentTime() {
        if (mState.equals(MusicStatus.STATUS_MUSIC_COMPLETE)) {
            mCurrentPos = mDuration;
            ViewUtils.setText(mMusicCurrentTimeView, TimeStampUtils.stampToData("" + mDuration));
            mProgressSeekBar.setProgress(100);
            return;
        }
        if (mState.equals(MusicStatus.STATUS_MUSIC_ERROR)) {
            mCurrentPos = 0;
            mProgressSeekBar.setProgress(0);
            ViewUtils.setText(mMusicCurrentTimeView, TimeStampUtils.stampToData("" + mCurrentPos));
        }
    }
}
