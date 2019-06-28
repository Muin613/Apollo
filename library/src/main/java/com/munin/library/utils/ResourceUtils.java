package com.munin.library.utils;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;

import com.munin.library.log.Logger;

/**
 * @author M
 */
public class ResourceUtils {
    private static final String TAG = "ResourceUtils";

    public static int getDimenFromRes(int resId) {
        Resources resources = getResources();
        if (resources == null) {
            Logger.e(TAG, "getDimenByRes: resources is null!");
            return 0;
        }
        return (int) resources.getDimension(resId);
    }

    private static Resources getResources() {
        Context context = ContextUtils.getApplicationContext();
        if (context == null) {
            return null;
        }
        return context.getResources();
    }

    public static Drawable getDrawableByResId(int resId) {
        Resources resources = getResources();
        if (resources == null) {
            return null;
        }
        return resources.getDrawable(resId);
    }

    public static String getString(int resId) {
        Resources resources = getResources();
        if (resources == null) {
            return "";
        }
        return resources.getString(resId);
    }
}
