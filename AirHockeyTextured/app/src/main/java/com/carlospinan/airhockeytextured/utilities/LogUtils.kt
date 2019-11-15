package com.carlospinan.airhockeytextured.utilities

import android.util.Log
import com.carlospinan.airhockeytextured.BuildConfig

private const val LOG_TAG = "OpenGL"

fun log(message: String) {
    if (BuildConfig.DEBUG_LOG) {
        Log.d(LOG_TAG, message)
    }
}