package com.munin.library.log;

import android.util.Log;

import com.munin.library.BuildConfig;

/**
 * @author M
 */
public class Logger {
    private static String TAG = "Apollo";

    public static void i(String tag, String msg) {
        if (!BuildConfig.DEBUG) {
            return;
        }
        Log.i(TAG, tag + ":" + msg);
    }

    public static void e(String tag, String msg) {
        Log.e(TAG, tag + ": " + msg);
    }

    public static void d(String tag, String msg) {
        if (!BuildConfig.DEBUG) {
            return;
        }
        Log.d(TAG, tag + ":" + msg);
    }

    public static void w(String tag, String msg) {
        Log.w(TAG, tag + ":" + msg);
    }
}
