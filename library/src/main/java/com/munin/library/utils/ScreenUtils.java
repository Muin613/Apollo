package com.munin.library.utils;

import android.app.Activity;
import android.content.Context;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.WindowManager;

import com.munin.library.log.Logger;

/**
 * @author mingyao
 */
public class ScreenUtils {
    private static final String TAG = "ScreenUtils";

    public static int getScreenWidth(Context context) {
        Activity activity = null;
        if (context instanceof Activity) {
            activity = (Activity) context;
        }

        if (activity == null) {
            Logger.e(TAG, "getScreenWidth: activity is null!");
            return 0;
        }
        WindowManager wm = activity.getWindowManager();
        if (wm == null) {
            Logger.e(TAG, "getScreenWidth: WindowManager is null!");
            return 0;
        }
        Display display = wm.getDefaultDisplay();
        if (display == null) {
            Logger.e(TAG, "getScreenWidth: display is null!");
            return 0;
        }
        DisplayMetrics dm = new DisplayMetrics();
        display.getMetrics(dm);
        Logger.i(TAG, "width = " + dm.widthPixels);
        return dm.widthPixels;
    }

    public static int getScreenHeight(Context context) {
        Activity activity = null;
        if (context instanceof Activity) {
            activity = (Activity) context;
        }

        if (activity == null) {
            Logger.e(TAG, "getScreenWidth: activity is null!");
            return 0;
        }
        WindowManager wm = activity.getWindowManager();
        if (wm == null) {
            Logger.e(TAG, "getScreenWidth: WindowManager is null!");
            return 0;
        }
        Display display = wm.getDefaultDisplay();
        if (display == null) {
            Logger.e(TAG, "getScreenWidth: display is null!");
            return 0;
        }
        DisplayMetrics dm = new DisplayMetrics();
        display.getMetrics(dm);

        return dm.heightPixels;
    }

}
