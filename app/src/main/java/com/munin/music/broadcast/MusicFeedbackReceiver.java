package com.munin.music.broadcast;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.text.TextUtils;

import com.munin.library.log.Logger;
import com.munin.library.utils.StringUtils;
import com.munin.music.media.music.MusicFeedback;

/**
 * @author M
 */
public class MusicFeedbackReceiver extends MusicReceiver {
    private static final String TAG = "MusicFeedbackReceiver";
    private OnMusicFeedbackListener mFeedbackListener;

    public void setFeedbackListener(OnMusicFeedbackListener feedbackListener) {
        this.mFeedbackListener = feedbackListener;
    }

    public void unRegisterFeedbackListener() {
        mFeedbackListener = null;
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
        if (mFeedbackListener == null) {
            Logger.e(TAG, "onReceive: mFeedbackListener is null!");
            return;
        }
        if (TextUtils.equals(action, MusicFeedback.FEEDBACK_MUSIC_BUFFERING)) {
            int percent = intent.getIntExtra("percent", 0);
            mFeedbackListener.onMusicBuffering(percent);
            return;
        }
        if (TextUtils.equals(action, MusicFeedback.FEEDBACK_MUSIC_PLAYING)) {
            mFeedbackListener.onMusicPlaying();
            return;
        }
        if (TextUtils.equals(action, MusicFeedback.FEEDBACK_MUSIC_DATA_IS_NULL)) {
            mFeedbackListener.onMusicDataIsNull();
            return;
        }
        if (TextUtils.equals(action, MusicFeedback.FEEDBACK_MUSIC_PAUSE)) {
            mFeedbackListener.onMusicPause();
            return;
        }
        if (TextUtils.equals(action, MusicFeedback.FEEDBACK_MUSIC_COMPLETE)) {
            mFeedbackListener.onMusicComplete();
            return;
        }
        if (TextUtils.equals(action, MusicFeedback.FEEDBACK_MUSIC_ERROR)) {
            int what = intent.getIntExtra("what", 0);
            int extra = intent.getIntExtra("extra", 0);
            mFeedbackListener.onMusicError(what, extra);
            return;
        }
        if (TextUtils.equals(action, MusicFeedback.FEEDBACK_MUSIC_INFO)) {
            int what = intent.getIntExtra("what", 0);
            int extra = intent.getIntExtra("extra", 0);
            mFeedbackListener.onMusicInfo(what, extra);
            return;
        }
        if (TextUtils.equals(action, MusicFeedback.FEEDBACK_PARAM_MUSIC_DURATION)) {
            int duration = intent.getIntExtra("duration", 0);
            mFeedbackListener.onMusicFeedbackDuration(duration);
            return;
        }
        if (TextUtils.equals(action, MusicFeedback.FEEDBACK_PARAM_MUSIC_CURRENT_POSITION)) {
            int duration = intent.getIntExtra("current_position", 0);
            mFeedbackListener.onMusicFeedbackCurrentPos(duration);
            return;
        }
        if (TextUtils.equals(action, MusicFeedback.FEEDBACK_PARAM_MUSIC_STATUS)) {
            String status = intent.getStringExtra("status");
            mFeedbackListener.onMusicStatus(status);
            return;
        }
        if (TextUtils.equals(action, MusicFeedback.FEEDBACK_MUSIC_PREPARED)) {
            mFeedbackListener.onMusicPrepared();
        }
    }

    public static IntentFilter getIntentFilter() {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(MusicFeedback.FEEDBACK_MUSIC_BUFFERING);
        intentFilter.addAction(MusicFeedback.FEEDBACK_MUSIC_PLAYING);
        intentFilter.addAction(MusicFeedback.FEEDBACK_MUSIC_DATA_IS_NULL);
        intentFilter.addAction(MusicFeedback.FEEDBACK_MUSIC_PAUSE);
        intentFilter.addAction(MusicFeedback.FEEDBACK_MUSIC_COMPLETE);
        intentFilter.addAction(MusicFeedback.FEEDBACK_MUSIC_ERROR);
        intentFilter.addAction(MusicFeedback.FEEDBACK_MUSIC_INFO);
        intentFilter.addAction(MusicFeedback.FEEDBACK_PARAM_MUSIC_DURATION);
        intentFilter.addAction(MusicFeedback.FEEDBACK_PARAM_MUSIC_CURRENT_POSITION);
        intentFilter.addAction(MusicFeedback.FEEDBACK_PARAM_MUSIC_STATUS);
        intentFilter.addAction(MusicFeedback.FEEDBACK_MUSIC_PREPARED);
        return intentFilter;
    }

    public interface OnMusicFeedbackListener {
        void onMusicPlaying();

        void onMusicDataIsNull();

        void onMusicPause();

        void onMusicComplete();

        void onMusicBuffering(int percent);

        void onMusicError(int what, int extra);

        void onMusicInfo(int what, int extra);

        void onMusicPrepared();

        void onMusicFeedbackDuration(int time);

        void onMusicFeedbackCurrentPos(int time);

        void onMusicStatus(String state);
    }
}
