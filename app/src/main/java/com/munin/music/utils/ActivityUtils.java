package com.munin.music.utils;

import android.app.Activity;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.view.KeyEvent;

public class ActivityUtils {
    public static void changeOrientation(Context context, boolean isLandScape) {
        Activity activity = null;
        if (context instanceof Activity) {
            activity = (Activity) context;
        }
        if (activity == null) {
            return;
        }
        if (isLandScape) {
            activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
            return;
        }
        activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
    }
}
