package com.munin.library.utils;

import android.app.Application;
import android.content.Context;

public class ContextUtils {
    private static Application mContext;

    public static void initContext(Application context) {
        mContext = context;
    }

    public static Context getApplicationContext() {
        return mContext;
    }
}
