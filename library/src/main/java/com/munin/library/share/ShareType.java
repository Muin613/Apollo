package com.munin.library.share;

import androidx.annotation.StringDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@StringDef({ShareType.TEXT, ShareType.IMAGE,
        ShareType.AUDIO, ShareType.VIDEO, ShareType.FILE})
@Retention(RetentionPolicy.SOURCE)
public @interface ShareType {
    /**
     * Share Text
     */
    String TEXT = "text/plain";

    /**
     * Share Image
     */
    String IMAGE = "image/*";

    /**
     * Share Audio
     */
    String AUDIO = "audio/*";

    /**
     * Share Video
     */
    String VIDEO = "video/*";

    /**
     * Share File
     */
    String FILE = "*/*";
}
