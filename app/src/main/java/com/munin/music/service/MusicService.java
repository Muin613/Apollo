package com.munin.music.service;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.BitmapFactory;
import android.os.IBinder;

import com.munin.library.log.Logger;
import com.munin.music.R;
import com.munin.music.broadcast.MusicReceiver;
import com.munin.library.media.audio.IMusicMedia;
import com.munin.library.media.audio.IMusicMediaListener;
import com.munin.music.media.music.MusicAction;
import com.munin.music.media.music.MusicFeedback;
import com.munin.music.media.music.MusicQuery;
import com.munin.music.media.music.MusicStatus;
import com.munin.library.media.audio.NativeMusicMediaPlayer;
import com.munin.music.model.music.MusicModel;
import com.munin.music.ui.player.MusicPlayerActivity;
import com.munin.music.utils.MusicControlUtils;


/**
 * @author M
 */
public class MusicService extends Service implements MusicReceiver.OnMusicListener, IMusicMediaListener {
    private static final String TAG = "MusicService";
    protected MusicModel mMusicModel;
    private int mId = -1;
    private String mState = MusicStatus.STATUS_MUSIC_INIT;
    private IMusicMedia mMusicMedia = new NativeMusicMediaPlayer();

    private MusicReceiver mMusicReceiver = new MusicReceiver();

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        //获取一个Notification构造器
        Notification.Builder builder = new Notification.Builder(this.getApplicationContext());
        Intent nfIntent = new Intent(this, MusicPlayerActivity.class);
        // 设置PendingIntent
        builder.setContentIntent(PendingIntent.getActivity(this, 0, nfIntent, 0))
                // 设置下拉列表中的图标(大图标)
                .setLargeIcon(BitmapFactory.decodeResource(this.getResources(), R.mipmap.apollo))
                // 设置下拉列表里的标题
                .setContentTitle("自己的音乐自己做主")
                // 设置状态栏内的小图标
                .setSmallIcon(R.mipmap.apollo)
                // 设置上下文内容
                .setContentText("free~~~~")
                // 设置该通知发生的时间
                .setWhen(System.currentTimeMillis());
        // 获取构建好的Notification
        Notification notification;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            //修改安卓8.1以上系统报错
            NotificationChannel notificationChannel = new NotificationChannel("101", "MusicService", NotificationManager.IMPORTANCE_MIN);
            //如果使用中的设备支持通知灯，则说明此通知通道是否应显示灯
            notificationChannel.enableLights(false);
            //是否显示角标
            notificationChannel.setShowBadge(false);
            notificationChannel.setLockscreenVisibility(Notification.VISIBILITY_SECRET);
            NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            manager.createNotificationChannel(notificationChannel);
            builder.setChannelId("101");
        }

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN) {
            notification = builder.build();
        } else {
            notification = builder.getNotification();
        }
        //设置为默认的声音
        notification.defaults = Notification.DEFAULT_SOUND;
        startForeground(110, notification);
        return START_STICKY;
    }


    @Override
    public void onCreate() {
        super.onCreate();
        initMusicReceiver();
    }


    private void initMusicReceiver() {
        if (mMusicReceiver == null) {
            return;
        }
        mMusicReceiver.setListener(this);
        registerReceiver(mMusicReceiver, getIntentFilter());
    }

    public IntentFilter getIntentFilter() {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(MusicAction.ACTION_OPT_MUSIC_CHANGE);
        intentFilter.addAction(MusicAction.ACTION_OPT_MUSIC_PAUSE);
        intentFilter.addAction(MusicAction.ACTION_OPT_MUSIC_PLAY);
        intentFilter.addAction(MusicAction.ACTION_OPT_MUSIC_SEEK_TO);
        intentFilter.addAction(MusicQuery.PARAM_MUSIC_CURRENT_POSITION);
        intentFilter.addAction(MusicQuery.PARAM_MUSIC_DURATION);
        intentFilter.addAction(MusicQuery.PARAM_MUSIC_STATUS);
        return intentFilter;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Logger.i(TAG, "销毁？");
        stopForeground(true);
        if (mMusicReceiver != null) {
            mMusicReceiver.unRegisterListener();
            unregisterReceiver(mMusicReceiver);
        }
    }


    @Override
    public void onMusicChangeAction(MusicModel musicModel) {
        Logger.i(TAG, "onMusicChangeAction");
        mMusicModel = musicModel;
        if (mMusicMedia == null) {
            return;
        }
        mMusicMedia.setListener(this);
        prepareMusic();

    }

    @Override
    public void onMusicPlayAction() {
        Logger.i(TAG, "onMusicPlayAction");
        if (mMusicMedia == null) {
            return;
        }
        mState = MusicStatus.STATUS_MUSIC_PLAYING;
        mMusicMedia.start();
        MusicControlUtils.sendBroadCast(new Intent(MusicFeedback.FEEDBACK_MUSIC_PLAYING));
    }

    private void prepareMusic() {
        if (mMusicModel == null) {
            mId = -1;
            mState = MusicStatus.STATUS_MUSIC_INIT;
            mMusicMedia.release();
            Intent intent = new Intent(MusicFeedback.FEEDBACK_PARAM_MUSIC_STATUS);
            intent.putExtra("status", mState);
            MusicControlUtils.sendBroadCast(intent);
            MusicControlUtils.sendBroadCast(new Intent(MusicFeedback.FEEDBACK_MUSIC_DATA_IS_NULL));
            return;
        }
        if (mMusicModel.hashCode() == mId) {
            return;
        }
        mState = MusicStatus.STATUS_MUSIC_INIT;
        Intent intent = new Intent(MusicFeedback.FEEDBACK_PARAM_MUSIC_STATUS);
        intent.putExtra("status", mState);
        MusicControlUtils.sendBroadCast(intent);
        mId = mMusicModel.hashCode();
        mMusicMedia.play(mMusicModel.getMusicUrl());
    }

    @Override
    public void onMusicPauseAction() {
        if (mMusicMedia == null) {
            return;
        }
        mState = MusicStatus.STATUS_MUSIC_PAUSE;
        mMusicMedia.pause();
        MusicControlUtils.sendBroadCast(new Intent(MusicFeedback.FEEDBACK_MUSIC_PAUSE));
    }

    @Override
    public void onMusicSeekAction(int time) {
        Logger.i(TAG, "onMusicSeekAction");
        if (mMusicMedia == null) {
            return;
        }
        mMusicMedia.seekTo(time);
    }

    @Override
    public void onMusicQueryCurrentPosition() {
        Logger.i(TAG, "onMusicQueryCurrentPosition");
        if (mMusicMedia == null) {
            return;
        }
        int time = mMusicMedia.getCurrentPosition();
        Intent intent = new Intent(MusicFeedback.FEEDBACK_PARAM_MUSIC_CURRENT_POSITION);
        intent.putExtra("current_position", time);
        MusicControlUtils.sendBroadCast(intent);
    }

    @Override
    public void onMusicQueryDuration() {
        Logger.i(TAG, "onMusicQueryDuration");
        if (mMusicMedia == null) {
            return;
        }
        int time = mMusicMedia.getDuration();
        Intent intent = new Intent(MusicFeedback.FEEDBACK_PARAM_MUSIC_DURATION);
        intent.putExtra("duration", time);
        MusicControlUtils.sendBroadCast(intent);
    }

    @Override
    public void onMusicQueryStatus() {
        Logger.i(TAG, "onMusicQueryStatus");
        Intent intent = new Intent(MusicFeedback.FEEDBACK_PARAM_MUSIC_STATUS);
        intent.putExtra("status", mState);
        MusicControlUtils.sendBroadCast(intent);
    }

    @Override
    public void onCompletion(IMusicMedia musicMedia) {
        Logger.i(TAG, "onCompletion");
        if (musicMedia == null) {
            return;
        }
        mState = MusicStatus.STATUS_MUSIC_COMPLETE;
        MusicControlUtils.sendBroadCast(new Intent(MusicFeedback.FEEDBACK_MUSIC_COMPLETE));
    }

    @Override
    public boolean onError(IMusicMedia musicMedia, int what, int extra) {
        Logger.i(TAG, "onError");
        mState = MusicStatus.STATUS_MUSIC_ERROR;
        Intent intent = new Intent(MusicFeedback.FEEDBACK_MUSIC_ERROR);
        intent.putExtra("what", what);
        intent.putExtra("extra", extra);
        MusicControlUtils.sendBroadCast(intent);
        return true;
    }

    @Override
    public boolean onInfo(IMusicMedia musicMedia, int what, int extra) {
        Logger.i(TAG, "onInfo");
        Intent intent = new Intent(MusicFeedback.FEEDBACK_MUSIC_INFO);
        intent.putExtra("what", what);
        intent.putExtra("extra", extra);
        MusicControlUtils.sendBroadCast(intent);
        return false;
    }

    @Override
    public void onPrepared(IMusicMedia musicMedia) {
        Logger.i(TAG, "onPrepared");
        mState = MusicStatus.STATUS_MUSIC_PREPARED;
        MusicControlUtils.sendBroadCast(new Intent(MusicFeedback.FEEDBACK_MUSIC_PREPARED));
    }

    @Override
    public void onBufferingUpdate(IMusicMedia mp, int percent) {
        Logger.i(TAG, "onBufferingUpdate");
        Intent intent = new Intent(MusicFeedback.FEEDBACK_MUSIC_BUFFERING);
        intent.putExtra("percent", percent);
        MusicControlUtils.sendBroadCast(intent);
    }
}