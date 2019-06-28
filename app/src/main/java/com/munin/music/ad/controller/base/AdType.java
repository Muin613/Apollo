package com.munin.music.ad.controller.base;

import androidx.annotation.StringDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@StringDef({AdType.IMG, AdType.GIF, AdType.VIDEO})
@Retention(RetentionPolicy.SOURCE)
public @interface AdType {
    /**
     * IMG
     */
    String IMG = "img";

    /**
     * GIF
     */
    String GIF = "gif";

    /**
     * Video
     */
    String VIDEO = "video";

}
