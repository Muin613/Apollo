package com.munin.music.cache;

import com.munin.library.cache.SpCache;
import com.munin.music.data.ConfigData;

/**
 * @author M
 */
public class GlobalConfigCache extends SpCache<ConfigData> {
    private final static String NAME = "GlobalConfigCache";
    private final static String KEY_NAME = "data";
    private volatile GlobalConfigCache mCache = new GlobalConfigCache();

    private GlobalConfigCache() {
        super();
    }

    public GlobalConfigCache newInstance() {
        return mCache;
    }

    @Override
    public Class setDataClass() {
        return ConfigData.class;
    }

    @Override
    public String getSpName() {
        return NAME;
    }

    @Override
    public String getDataKey() {
        return KEY_NAME;
    }
}
