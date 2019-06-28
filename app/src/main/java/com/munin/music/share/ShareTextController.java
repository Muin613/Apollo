package com.munin.music.share;

import android.app.Activity;

import com.munin.library.log.Logger;
import com.munin.library.share.ShareType;
import com.munin.library.share.SystemShare;
import com.munin.library.utils.StringUtils;

/**
 * @author M
 */
public class ShareTextController {
    private static final String TAG = "ShareTextController";
    private String mShareText = "";
    private Activity mAct;

    public ShareTextController(Activity activity) {
        this.mAct = activity;
    }

    public void changeShareText(String shareText) {
        mShareText = shareText;
    }

    public void share() {
        if (mAct == null) {
            Logger.e(TAG, "share: activity is null!");
            return;
        }
        if (StringUtils.isBlank(mShareText)) {
            mShareText = "";
        }
        new SystemShare.Builder(mAct)
                .setContentType(ShareType.TEXT)
                .setTitle("分享链接")
                .setTextContent(mShareText)
                .build()
                .shareBySystem();
    }
}
