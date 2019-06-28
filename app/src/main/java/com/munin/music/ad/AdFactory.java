package com.munin.music.ad;

import android.content.Context;

import com.munin.music.ad.controller.GifAdController;
import com.munin.music.ad.controller.ImgAdController;
import com.munin.music.ad.controller.VideoAdController;
import com.munin.music.ad.controller.base.AdType;
import com.munin.music.ad.controller.base.IAdController;

public class AdFactory {

    public static IAdController createAdController(Context context, @AdType String type) {
        switch (type) {
            case AdType.GIF:
                return new GifAdController(context);
            case AdType.IMG:
                return new ImgAdController(context);
            case AdType.VIDEO:
                return new VideoAdController(context);
            default:
                return null;
        }
    }
}
