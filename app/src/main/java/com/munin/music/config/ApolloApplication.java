package com.munin.music.config;

import android.app.Application;

import com.munin.library.utils.ContextUtils;

/**
 * @author M
 */
public class ApolloApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        ContextUtils.initContext(this);
    }
}
