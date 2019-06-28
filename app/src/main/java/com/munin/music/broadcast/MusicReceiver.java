package com.munin.music.broadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;

import com.munin.library.log.Logger;
import com.munin.library.utils.StringUtils;
import com.munin.music.media.music.MusicAction;
import com.munin.music.media.music.MusicQuery;
import com.munin.music.model.music.MusicModel;

/**
 * @author M
 */
public class MusicReceiver extends BroadcastReceiver {
    private static final String TAG = "MusicReceiver";
    private OnMusicListener mListener;


    public MusicReceiver() {

    }

    public void setListener(OnMusicListener listener) {
        this.mListener = listener;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent == null) {
            Logger.e(TAG, "onReceive: intent is null!");
            return;
        }
        Logger.i(TAG, "onReceive! " + intent.getAction());
        String action = intent.getAction();
        if (StringUtils.isBlank(action)) {
            Logger.w(TAG, "onReceive: action is blank!");
            return;
        }
        if (mListener == null) {
            Logger.e(TAG, "onReceive: listener is null!");
            return;
        }
        if (TextUtils.equals(action, MusicAction.ACTION_OPT_MUSIC_CHANGE)) {
            MusicModel model = handleMusicChangeIntent(intent);
            Logger.i(TAG, "数据改变：" + model);
            mListener.onMusicChangeAction(model);
            return;
        }
        if (TextUtils.equals(action, MusicAction.ACTION_OPT_MUSIC_PLAY)) {
            mListener.onMusicPlayAction();
            return;
        }
        if (TextUtils.equals(action, MusicAction.ACTION_OPT_MUSIC_PAUSE)) {
            mListener.onMusicPauseAction();
            return;
        }
        if (TextUtils.equals(action, MusicAction.ACTION_OPT_MUSIC_SEEK_TO)) {
            int time = intent.getIntExtra("seek", 0);
            mListener.onMusicSeekAction(time);
            return;
        }
        if (TextUtils.equals(action, MusicQuery.PARAM_MUSIC_CURRENT_POSITION)) {
            mListener.onMusicQueryCurrentPosition();
            return;
        }
        if (TextUtils.equals(action, MusicQuery.PARAM_MUSIC_DURATION)) {
            mListener.onMusicQueryDuration();
            return;
        }
        if (TextUtils.equals(action, MusicQuery.PARAM_MUSIC_STATUS)) {
            mListener.onMusicQueryStatus();
        }
    }

    public void unRegisterListener() {
        mListener = null;
    }

    private MusicModel handleMusicChangeIntent(Intent intent) {
        if (intent == null) {
            Logger.e(TAG, "handleMusicChangeIntent: intent is null!");
            return null;
        }
        String bgImg = intent.getStringExtra(MusicModel.MUSIC_BG_IMG_URL_KEY);
        String author = intent.getStringExtra(MusicModel.MUSIC_MEDIA_AUTHOR_KEY);
        String id = intent.getStringExtra(MusicModel.MUSIC_MEDIA_ID_KEY);
        String title = intent.getStringExtra(MusicModel.MUSIC_MEDIA_TITLE_KEY);
        String url = intent.getStringExtra(MusicModel.MUSIC_MEDIA_URL_KEY);
        MusicModel model = new MusicModel();
        model.setAuthor(author);
        model.setTitle(title);
        model.setImgUrl(bgImg);
        model.setId(id);
        model.setMusicUrl(url);
        Logger.i(TAG, "handleMusicChangeIntent: " + model);
        return model;
    }

    public interface OnMusicListener extends OnMusicActionListener, OnMusicQueryListener {
    }

    public interface OnMusicActionListener {
        /**
         * 音乐发生切换
         *
         * @param musicModel 音乐数据
         */
        void onMusicChangeAction(MusicModel musicModel);

        /**
         * 音乐播放
         */
        void onMusicPlayAction();

        /**
         * 音乐暂停
         */
        void onMusicPauseAction();

        /**
         * 音乐进度切换
         *
         * @param time 时间
         */
        void onMusicSeekAction(int time);
    }


    public interface OnMusicQueryListener {
        /**
         * 查询音乐当前进度
         */
        void onMusicQueryCurrentPosition();

        /**
         * 查询音乐总时长
         */
        void onMusicQueryDuration();

        /**
         * 查询音乐当前状态
         */
        void onMusicQueryStatus();
    }


}