package com.carlospinan.airhockeywithbettermallets.utilities

import android.util.Log
import com.carlospinan.airhockeywithbettermallets.BuildConfig

private const val LOG_TAG = "OpenGL"

fun log(message: String) {
    if (BuildConfig.DEBUG_LOG) {
        Log.d(LOG_TAG, message)
    }
}