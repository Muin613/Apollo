package com.munin.library.image;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.munin.library.R;
import com.munin.library.log.Logger;

/**
 * @author M
 */
public class ImageLoadUtils {
    private static final String TAG = "ImageLoadUtils";

    public static void loadImg(Context context, String url, ImageView imageView) {
        if (context == null || imageView == null) {
            Logger.i(TAG, "loadImg: context or imageView is null!");
            return;
        }
        Glide.with(context).load(url).placeholder(R.drawable.palaceholder).into(imageView);
    }

    public static void clearImage(Context context, ImageView imageView) {
        if (context == null || imageView == null) {
            Logger.i(TAG, "clearImage: context or imageView is null!");
            return;
        }
        Glide.with(context).clear(imageView);
    }

    public static void loadGif(Context context, String url, ImageView imageView) {
        if (context == null || imageView == null) {
            Logger.i(TAG, "loadGif: context or imageView is null!");
            return;
        }
        Glide.with(context).asGif().load(url).into(imageView);
    }
}
