package com.munin.library.utils;

import android.content.Context;
import android.content.res.Resources;
import android.util.DisplayMetrics;


public class DisplayUtil {

    /**
     * 设备屏幕宽度
     */
    public static int getScreenWidth() {
        Resources resources = getResources();
        if (resources == null) {
            return 0;
        }
        DisplayMetrics dm = resources.getDisplayMetrics();
        if (dm == null) {
            return 0;
        }
        return dm.widthPixels;
    }

    /**
     * 设备屏幕高度
     */
    public static int getScreenHeight() {
        Resources resources = getResources();
        if (resources == null) {
            return 0;
        }
        DisplayMetrics dm = resources.getDisplayMetrics();
        if (dm == null) {
            return 0;
        }
        return dm.heightPixels;
    }

    private static Resources getResources() {
        Context context = ContextUtils.getApplicationContext();
        if (context == null) {
            return null;
        }
        return context.getResources();
    }
}
