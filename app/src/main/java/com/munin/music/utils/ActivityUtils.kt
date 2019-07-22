package com.munin.music.utils

import android.app.Activity
import android.content.Context
import android.content.pm.ActivityInfo
import android.view.KeyEvent

object ActivityUtils {
    fun changeOrientation(context: Context, isLandScape: Boolean) {
        var activity: Activity? = null
        if (context is Activity) {
            activity = context
        }
        if (activity == null) {
            return
        }
        if (isLandScape) {
            activity.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
            return
        }
        activity.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
    }
}
