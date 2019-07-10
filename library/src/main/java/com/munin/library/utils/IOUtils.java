package com.munin.library.utils;

import com.munin.library.log.Logger;

import java.io.Closeable;
import java.io.IOException;

public class IOUtils {
    private static final String TAG = "IOUtils";

    public static void close(Closeable target) {
        if (target == null) {
            return;
        }
        try {
            target.close();
        } catch (IOException e) {
            Logger.e(TAG, "close: " + e.getMessage());
        }
    }
}
