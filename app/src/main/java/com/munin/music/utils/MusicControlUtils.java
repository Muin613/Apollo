package com.munin.music.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import com.munin.library.log.Logger;
import com.munin.library.utils.ContextUtils;
import com.munin.music.service.MusicService;

/**
 * @author M
 */
public class MusicControlUtils {
    private static final String TAG = "MusicControlUtils";

    public static void sendBroadCast(Intent intent) {
        Context context = ContextUtils.getApplicationContext();
        if (context == null) {
            Logger.e(TAG, "sendBroadCast: context is null!");
            return;
        }
        Logger.i(TAG, "sendBroadCast " + intent);
        context.sendBroadcast(intent);
    }

    public static void stopService(Activity activity) {
        if (activity == null) {
            Logger.e(TAG, "unBindService: activity is null!");
            return;
        }
        Intent intent = new Intent(activity, MusicService.class);
        activity.stopService(intent);
    }

    public static void startService(Activity activity) {
        if (activity == null) {
            Logger.e(TAG, "bindService: activity is null!");
            return;
        }
        Intent intent = new Intent(activity, MusicService.class);
        activity.startService(intent);
    }
}
