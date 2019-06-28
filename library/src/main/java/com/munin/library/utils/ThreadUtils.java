package com.munin.library.utils;

import android.os.Handler;
import android.os.Looper;

public class ThreadUtils {
    private static Handler mHandler;

    public static void runOnUiThread(Runnable task) {
        postDelayed(task, 0);
    }

    public static void post(Runnable task) {
        postDelayed(task, 0);
    }

    public static void postDelayed(Runnable task, long delayTime) {
        if (delayTime == 0) {
            getHandler().post(task);
            return;
        }
        getHandler().postDelayed(task, delayTime);
    }

    public static void remove(Runnable task) {
        getHandler().removeCallbacks(task);
    }

    private static Handler getHandler() {
        if (mHandler == null) {
            mHandler = new Handler(Looper.getMainLooper());
        }
        return mHandler;
    }

}
