package com.munin.music.config;

import android.app.Application;

import com.munin.library.log.Logger;
import com.munin.library.utils.ContextUtils;
import com.tencent.smtt.sdk.QbSdk;

/**
 * @author M
 */
public class ApolloApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        ContextUtils.initContext(this);
        QbSdk.initX5Environment(getApplicationContext(), new QbSdk.PreInitCallback() {
            @Override
            public void onCoreInitFinished() {
                Logger.e("ApolloApplication", " onCoreInitFinished " );
            }

            @Override
            public void onViewInitFinished(boolean b) {
                Logger.e("ApolloApplication", " onViewInitFinished is " + b);
            }
        });
    }
}
