package com.munin.library.cache;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.munin.library.log.Logger;
import com.munin.library.utils.ContextUtils;

public abstract class SpCache<T> implements ISpCache<T> {
    private static final String TAG = "SpCache";
    protected SharedPreferences mSharedPreferences;
    protected T mData;

    public SpCache() {
        mSharedPreferences = ContextUtils.getApplicationContext().getSharedPreferences(getSpName(), Context.MODE_PRIVATE);
    }

    @Override
    public synchronized T getData() {
        if (isInvalid()) {
            String data = mSharedPreferences.getString(getDataKey(), "");
            try {
                mData = (T) new Gson().fromJson(data, getClass());
            } catch (Exception e) {
                Logger.e(TAG, "getData: e = " + e.getMessage());
            }
        }
        return mData;
    }


    @Override
    public synchronized void update(String data) {
        if (!isInvalid()) {
            Gson gson = new Gson();
            String jsonData = gson.toJson(mData);
            if (jsonData.equals(data)) {
                Logger.i(TAG, "update: data is similar!");
                return;
            }
        }
        try {
            SharedPreferences.Editor editor = mSharedPreferences.edit();
            editor.putString(getDataKey(), data);
            editor.apply();
            mData = (T) new Gson().fromJson(data, getClass());
        } catch (Exception e) {
            Logger.e(TAG, "update: e = " + e.getMessage());
        }

    }

    @Override
    public synchronized boolean isInvalid() {
        return mData == null;
    }
}
