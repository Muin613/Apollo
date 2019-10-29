package com.munin.music.media;

/**
 * Created by clam314 on 2017/3/26
 */

public class LameUtils {
    static {
        System.loadLibrary("lamemp3-lib");
    }

    public native static void close();

    public native static int encode(short[] buffer_l, short[] buffer_r, int samples, byte[] mp3buf);

    public native static int flush(byte[] mp3buf);

    public native static void initialize(int inSampleRate, int outChannel, int outSampleRate, int outBitrate, int quality);

    public static void init(int inSampleRate, int outChannel, int outSampleRate, int outBitrate) {
        initialize(inSampleRate, outChannel, outSampleRate, outBitrate, 7);
    }
}
