package com.munin.music.ui.common;

import android.media.MediaPlayer;

import com.munin.library.utils.ResourceUtils;
import com.munin.music.R;

public class VideoMediaNoticeController {
    VideoCoverView mVideoCoverView;

    public VideoMediaNoticeController(VideoCoverView videoCoverView) {
        this.mVideoCoverView = videoCoverView;
    }

    public void handleInfo(int what){
        if(what==MediaPlayer.MEDIA_INFO_BUFFERING_START){
            mVideoCoverView.setNotice(ResourceUtils.getString(R.string.video_buffering));
            return;
        }
        if(what==MediaPlayer.MEDIA_INFO_BUFFERING_END){
            mVideoCoverView.hideNotice();
            return;
        }
        if(what==MediaPlayer.MEDIA_INFO_UNKNOWN){
            mVideoCoverView.setNotice(ResourceUtils.getString(R.string.video_unknown_error));
        }
    }

    public void handleError(int what){
        if(what==MediaPlayer.MEDIA_ERROR_UNKNOWN){
            mVideoCoverView.setNotice(ResourceUtils.getString(R.string.video_unknown_error));
            return;
        }
        if(what==MediaPlayer.MEDIA_ERROR_SERVER_DIED){
            mVideoCoverView.setNotice(ResourceUtils.getString(R.string.video_remote_servlet_error));
        }


    }
}
